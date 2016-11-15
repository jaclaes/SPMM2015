package org.cheetahplatform.modeler.hierarchical;

import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.eclipse.gef.Request;

public class HierarchicalNodeEditPart extends NodeEditPart {

	public HierarchicalNodeEditPart(IGenericModel model) {
		super(model);
	}

	protected void openSubProcess() {
		new OpenSubProcessAction(this).run();
	}

	@Override
	public void performRequest(Request req) {
		if (REQ_OPEN.equals(req.getType())) {
			openSubProcess();
		} else {
			super.performRequest(req);
		}
	}

}
