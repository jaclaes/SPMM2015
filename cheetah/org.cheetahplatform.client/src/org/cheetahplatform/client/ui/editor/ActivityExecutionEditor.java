package org.cheetahplatform.client.ui.editor;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.CancelActivityService;
import org.cheetahplatform.client.jms.CompleteActivityService;
import org.cheetahplatform.client.ui.ActivityExecutionComposite;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.swtdesigner.ResourceManager;

public class ActivityExecutionEditor extends EditorPart {
	private static final int DILBERT_AMOUNT = 10;
	public static final String id = "org.cheetahplatform.client.ActivityExecutionEditor";

	private ActivityExecutionComposite composite;
	private ActivityEditorModel model;

	public ActivityExecutionEditor() {
		model = new ActivityEditorModel();
	}

	protected void cancelActivity() {
		CancelActivityService service = new CancelActivityService(model.getProcessInstance(), model.getActivityInstance());
		try {
			IStatus status = service.synchronousRequest();
			if (status.equals(Status.OK_STATUS)) {
				ModificationService.broadcastChanges(model.getActivityInstance(), ModificationService.ACTIVITY_CANCELED_PROPERTY);
				getSite().getWorkbenchWindow().getActivePage().closeEditor(this, false);
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Could not cancel Activity " + model.getActivityInstance().getName(), e);
		}
	}

	protected void completeActivity() {
		ActivityExecutionEditorInput input = (ActivityExecutionEditorInput) getEditorInput();

		CompleteActivityService service = new CompleteActivityService(input.getProcessInstance(), input.getActivity());
		try {
			IStatus status = service.synchronousRequest();
			if (status.equals(Status.OK_STATUS)) {
				ModificationService.broadcastChanges(input.getActivity(), ModificationService.ACTIVITY_COMPLETED_PROPERTY);
				getSite().getWorkbenchWindow().getActivePage().closeEditor(this, false);
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Could not complete Activity " + input.getActivity().getName(), e);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new ActivityExecutionComposite(parent, SWT.NONE);
		int comicIndex = ((int) (Math.random() * DILBERT_AMOUNT)) + 1;
		Image comic = ResourceManager.getPluginImage(Activator.getDefault(), "dilbert/dilbert" + comicIndex + ".png");
		composite.getDilbertLabel().setImage(comic);

		composite.getCompleteButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				completeActivity();
			}
		});

		composite.getCancelButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancelActivity();
			}
		});
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// not supported
	}

	@Override
	public void doSaveAs() {
		// not supported
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());

		ActivityExecutionEditorInput editorInput = (ActivityExecutionEditorInput) getEditorInput();
		model.setProcessInstance(editorInput.getProcessInstance());
		model.setActivityInstance(editorInput.getActivity());
	}

	@Override
	public boolean isDirty() {
		// temporary implementation to avoid saving
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		composite.setFocus();
	}

}
