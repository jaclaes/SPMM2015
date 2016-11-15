package org.cheetahplatform.client.ui.action;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.RecommendationService;
import org.cheetahplatform.common.ui.SelectionSensitiveAction;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class RecommendationAction extends SelectionSensitiveAction<ProcessInstanceHandle> {

	public RecommendationAction(StructuredViewer viewer) {
		super(viewer, ProcessInstanceHandle.class);
		setText("Retrieve Recommendations");
		setToolTipText("Retrieves Recommendations about what to execute next.");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/recommendation-16x16.png"));
		setEnabled(false);
	}

	@Override
	public void run() {
		ProcessInstanceHandle processInstanceHandle = getSelection();
		try {
			RecommendationService service = new RecommendationService(processInstanceHandle);
			IStatus status = service.synchronousRequest();
			if (status.getCode() == IStatus.OK) {
				RecommendationDialog dialog = new RecommendationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						service.getRecommendations());
				dialog.open();
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Error retrieving recommendations", e);
		}
	}
}
