package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.NodeCommand;
import org.cheetahplatform.modeler.graph.model.Node;

public class EditNodeDescriptionCommand extends NodeCommand {

	public static final String ATTRIBUTE_DESCRIPTION = "node_description";
	public static final String COMMAND_EDIT_DESCRIPTION = "EDIT_NODE_DESCRIPTION";

	private final String description;
	private String oldDescription;

	public EditNodeDescriptionCommand(Node node, String description) {
		super(node.getGraph(), node);
		this.description = description;
	}

	@Override
	public void execute() {
		oldDescription = (String) getNode().getProperty(ATTRIBUTE_DESCRIPTION);
		getNode().setProperty(ATTRIBUTE_DESCRIPTION, description);

		AuditTrailEntry entry = createAuditrailEntry(COMMAND_EDIT_DESCRIPTION);
		entry.setAttribute(ATTRIBUTE_DESCRIPTION, description);
		log(entry);
	}

	@Override
	public void undo() {
		new EditNodeDescriptionCommand(getNode(), oldDescription).execute();
	}

}
