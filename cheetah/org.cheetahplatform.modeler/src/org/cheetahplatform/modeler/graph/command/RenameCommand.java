package org.cheetahplatform.modeler.graph.command;

import java.util.Date;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;

public class RenameCommand extends AbstractGraphCommand {

	private final String name;
	private String oldName;
	private Date startTime;

	public RenameCommand(GraphElement element, String name) {
		super(element.getGraph(), element);

		this.name = name;
	}

	@Override
	public void execute() {
		AuditTrailEntry entry = createAuditrailEntry(RENAME);
		String fullName = getAffectedElementName();
		oldName = element.getName();
		element.setName(name);

		entry.setWorkflowModelElement(fullName);
		entry.setAttribute(NEW_NAME, name);

		if (startTime != null) {
			entry.setAttribute(AbstractGraphCommand.RENAME_START_TIME, startTime.getTime());
		}

		log(entry);

		Services.getModificationService().broadcastChange(element, RENAME);
	}

	@Override
	protected String getAffectedElementName() {
		if (element instanceof Node) {
			return getFullName(element);
		}

		return getEdgeName((Edge) element, null);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public void undo() {
		new RenameCommand(element, oldName).execute();
	}
}
