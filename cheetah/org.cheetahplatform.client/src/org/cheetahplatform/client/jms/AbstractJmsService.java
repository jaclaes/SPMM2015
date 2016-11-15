package org.cheetahplatform.client.jms;

import static org.cheetahplatform.shared.CheetahConstants.KEY_EXCEPTION;
import static org.cheetahplatform.shared.CheetahConstants.STATUS_OK;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.shared.CheetahConstants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.progress.IProgressConstants;

public abstract class AbstractJmsService implements javax.jms.MessageListener, IJmsService {

	/**
	 * A {@link Job} representing and asynchronous request.
	 * 
	 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
	 * 
	 *         30.06.2009
	 */
	private final class AsynchronousRequestJob extends Job {
		private final WorkbenchPart workbenchPart;
		private final IServiceCallback callback;

		/**
		 * A {@link Job} for an asynchronous request.
		 * 
		 * @param name
		 *            the name
		 * @param workbenchPart
		 *            the {@link WorkbenchPart}
		 * @param callback
		 *            an {@link IServiceCallback}
		 */
		private AsynchronousRequestJob(String name, WorkbenchPart workbenchPart, IServiceCallback callback) {
			super(name);
			this.workbenchPart = workbenchPart;
			this.callback = callback;
		}

		@Override
		public boolean belongsTo(Object family) {
			if (!(family instanceof Job))
				return false;

			return ((Job) family).getName().equals(getName());
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				setProperty(IProgressConstants.KEEPONE_PROPERTY, Boolean.TRUE);

				if (workbenchPart != null) {
					OpenWorkbenchPartAction action = new OpenWorkbenchPartAction("Show calling part.", workbenchPart);
					setProperty(IProgressConstants.ACTION_PROPERTY, action);
					Image titleImage = workbenchPart.getSite().getPart().getTitleImage();
					if (titleImage != null) {
						ImageDescriptor imageDescriptor = ImageDescriptor.createFromImageData(titleImage.getImageData());
						setProperty(IProgressConstants.ICON_PROPERTY, imageDescriptor);
					}
				}
				monitor.beginTask(message, 2);
				monitor.worked(1);
				try {
					AbstractJmsService.this.run();
					Thread.sleep(1000);
					while (!semaphore.tryAcquire(2, TimeUnit.SECONDS)) {
						if (monitor.isCanceled()) {
							throw new InterruptedException("Operation canceled by user");
						}
					}
				} catch (JMSException e) {
					logJmsError(e);
					throw e;
				}
				monitor.worked(1);
				callback.requestCompleted(AbstractJmsService.this);
				monitor.done();
				return Status.OK_STATUS;
			} catch (JMSException e) {
				logJmsError(e);
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error sending request");
			} catch (InterruptedException e) {
				return Status.CANCEL_STATUS;
			}
		}
	}

	/**
	 * Simple action opening the {@link WorkbenchPart} with the given id.
	 * 
	 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
	 * 
	 *         29.06.2009
	 */
	private final class OpenWorkbenchPartAction extends Action {
		private final WorkbenchPart workbenchPart;

		/**
		 * Creates a new action.
		 * 
		 * @param text
		 *            the message
		 * @param workbenchPart
		 *            the {@link WorkbenchPart} to open
		 */
		private OpenWorkbenchPartAction(String text, WorkbenchPart workbenchPart) {
			super(text);
			this.workbenchPart = workbenchPart;
		}

		@Override
		public void run() {
			String id = workbenchPart.getSite().getId();
			Assert.isNotNull(id);
			try {
				IWorkbenchPage activePage = workbenchPart.getSite().getWorkbenchWindow().getActivePage();
				if (workbenchPart instanceof ViewPart) {
					activePage.showView(id);
				} else {
					IEditorInput editorInput = ((EditorPart) workbenchPart).getEditorInput();
					activePage.openEditor(editorInput, id);
				}
			} catch (PartInitException e) {
				UiUtils.showError("Failed to open workbench part with id: " + id, e);
			}
		}
	}

	private class SynchronousRequestJob extends Job {
		private Semaphore jobSemaphore;

		public SynchronousRequestJob(String name) {
			super(name);

			jobSemaphore = new Semaphore(0);
		}

		@Override
		public boolean belongsTo(Object family) {
			if (!(family instanceof Job))
				return false;

			return ((Job) family).getName().equals(getName());
		}

		public IStatus getResultSynchronous() {
			try {
				if (!jobSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
					AbstractJmsService.this.status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"Did not receive an answer within 10 seconds.");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return getStatus();
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			setProperty(IProgressConstants.PROPERTY_IN_DIALOG, false);

			monitor.beginTask(message, 2);
			monitor.worked(1);
			try {
				AbstractJmsService.this.run();
				semaphore.acquire();
			} catch (Exception e) {
				e.printStackTrace();
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
			}

			monitor.worked(1);
			monitor.done();
			jobSemaphore.release();

			return getStatus();
		}

	}

	private Semaphore semaphore;
	private String message;
	protected IStatus status;

	protected AbstractJmsService(String message) {
		semaphore = new Semaphore(0);
		this.message = message;
	}

	public void asynchronousRequest(final IServiceCallback callback, final WorkbenchPart workbenchPart) throws JMSException {
		Assert.isNotNull(callback);
		Job job = new AsynchronousRequestJob(message, workbenchPart, callback);
		job.schedule();
	}

	@SuppressWarnings("unused")
	protected void doOnMessage(Message uncasted) throws JMSException {
		// no actions to take by default
	}

	public IStatus getStatus() {
		return status;
	}

	private void logJmsError(Exception e) {
		Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occured when talking to the server.", e);
		Activator.getDefault().getLog().log(status);
	}

	public void onMessage(Message message) {
		try {
			String statusMessage = message.getStringProperty(CheetahConstants.KEY_STATUS);
			if (STATUS_OK.equals(statusMessage)) {
				status = Status.OK_STATUS;
			} else {
				String stackTrace = message.getStringProperty(KEY_EXCEPTION);
				status = new Status(IStatus.ERROR, statusMessage, stackTrace);
				Activator.getDefault().getLog().log(status);
			}

			doOnMessage(message);
			semaphore.release();
		} catch (JMSException e) {
			logJmsError(e);
		}
	}

	protected abstract void run() throws JMSException;

	public IStatus synchronousRequest() throws JMSException {
		SynchronousRequestJob job = new SynchronousRequestJob(message);
		job.schedule();

		return job.getResultSynchronous();
	}
}
