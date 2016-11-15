package org.cheetahplatform.client.ui.action;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.InstantiateSchemaService;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.ui.SelectionSensitiveAction;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredViewer;

import com.swtdesigner.ResourceManager;

/**
 * An action for creating a new instance of a {@link ImperativeProcessSchema}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         09.07.2009
 */
public class InstantiateSchemaAction extends SelectionSensitiveAction<ProcessSchemaHandle> {

	/**
	 * Creates a new action.
	 * 
	 * @param viewer
	 *            the {@link StructuredViewer}
	 */
	public InstantiateSchemaAction(StructuredViewer viewer) {
		super(viewer, ProcessSchemaHandle.class);
		Assert.isNotNull(viewer);
		setText("Instantiate Schema");
		setToolTipText("Creates a new instance of the process schema");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/add_processInstance-16x16.png"));
	}

	@Override
	public void run() {
		ProcessSchemaHandle element = getSelection();

		try {
			InstantiateSchemaService service = new InstantiateSchemaService(element);
			IStatus status = service.synchronousRequest();
			if (status.equals(Status.OK_STATUS)) {
				ModificationService.broadcastChanges(element, ModificationService.SCHEMA_INSTANTIATED);
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Failed to instantiate schema " + element.getName(), e);
		}
	}
}
