package org.cheetahplatform.client.model;

import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.jms.IJmsService;
import org.cheetahplatform.client.jms.IServiceCallback;
import org.cheetahplatform.client.jms.TaskRetrievalService;
import org.cheetahplatform.client.ui.Worklist;
import org.cheetahplatform.common.INamed;
import org.cheetahplatform.j2ee.XStreamProvider;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.DeclarativeProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.ResourceManager;
import com.thoughtworks.xstream.XStream;

/**
 * The model for the work list responsible for retrieving the active tasks.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public class WorklistModel implements IServiceCallback {
	/**
	 * Simple content provider for the work list.
	 * 
	 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
	 * 
	 *         24.06.2009
	 */
	private static class WorklistContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ProcessSchemaHandle) {
				return ((ProcessSchemaHandle) parentElement).getInstancesWithActiveActivities().toArray();
			}
			if (parentElement instanceof ProcessInstanceHandle) {
				return ((ProcessInstanceHandle) parentElement).getActiveActivities().toArray();
			}

			return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof ProcessSchemaHandle) {
				ProcessSchemaHandle handle = (ProcessSchemaHandle) element;
				return handle.hasActiveActivities();
			}

			if (element instanceof ProcessInstanceHandle) {
				return ((ProcessInstanceHandle) element).hasActiveActivities();
			}

			return false;
		}
	}

	/**
	 * {@link LabelProvider} for {@link INamed} objects.
	 * 
	 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
	 * 
	 *         24.06.2009
	 */
	private static class WorklistLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof ProcessSchemaHandle)
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/process-16x16.png");
			if (element instanceof ActivityInstanceHandle)
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/activity-16x16.gif");
			if (element instanceof DeclarativeProcessInstanceHandle) {
				if (((DeclarativeProcessInstanceHandle) element).canTerminate()) {
					return ResourceManager.getPluginImage(Activator.getDefault(), "icons/processInstance_can_terminate-16x16.png");
				}

				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/processInstance_cannot_terminate-16x16.png");
			}
			if (element instanceof ProcessInstanceHandle)
				return ResourceManager.getPluginImage(Activator.getDefault(), "icons/processInstance-16x16.png");

			throw new IllegalArgumentException("Unknown type: " + element);
		}

		@Override
		public String getText(Object element) {
			return ((INamed) element).getName();
		}
	}

	private final Worklist worklist;
	private XStream xStream;
	private List<ProcessSchemaHandle> schemas;

	/**
	 * Creates a new model.
	 * 
	 * @param worklist
	 *            the {@link Worklist} to inform on changes
	 */
	public WorklistModel(Worklist worklist) {
		this.worklist = worklist;

		xStream = XStreamProvider.createConfiguredXStream();
	}

	/**
	 * Creates a new content provider.
	 * 
	 * @return a new content provider
	 */
	public ITreeContentProvider createContentProvider() {
		return new WorklistContentProvider();
	}

	/**
	 * Creates a new label provider.
	 * 
	 * @return a new label provider
	 */
	public LabelProvider createLabelProvider() {
		return new WorklistLabelProvider();
	}

	/**
	 * Returns the schemas.
	 * 
	 * @return the schemas
	 */
	public List<ProcessSchemaHandle> getSchemas() {
		return Collections.unmodifiableList(schemas);
	}

	/**
	 * Queries all active tasks from the server. <br>
	 * This request is performed asynchronously.
	 * 
	 * @throws JMSException
	 *             if something goes wrong.
	 */
	public void queryActiveTasks() throws JMSException {
		TaskRetrievalService retrievalService = new TaskRetrievalService();
		retrievalService.asynchronousRequest(this, worklist);
	}

	@SuppressWarnings("unchecked")
	public void requestCompleted(IJmsService service) {
		String activeTasks = ((TaskRetrievalService) service).getActiveTasks();
		schemas = (List<ProcessSchemaHandle>) xStream.fromXML(activeTasks);

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				worklist.refresh();
			}
		});
	}
}
