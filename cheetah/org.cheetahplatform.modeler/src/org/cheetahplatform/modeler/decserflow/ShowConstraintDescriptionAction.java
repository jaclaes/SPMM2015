package org.cheetahplatform.modeler.decserflow;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_CONSTRAINTS;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.ResourceManager;

public class ShowConstraintDescriptionAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.decserflow.ShowConstraintDescriptionAction";
	private final Edge edge;

	public ShowConstraintDescriptionAction(Edge edge) {
		setId(ID);
		setText("Show Constraint Description");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/help.gif"));
		this.edge = edge;
	}

	@Override
	public void run() {
		IDeclarativeConstraint constraint = (IDeclarativeConstraint) Services.getCheetahObjectLookup().getObject(NAMESPACE_CONSTRAINTS,
				edge.getId());
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Constraint Description", constraint.getDescription());
	}
}
