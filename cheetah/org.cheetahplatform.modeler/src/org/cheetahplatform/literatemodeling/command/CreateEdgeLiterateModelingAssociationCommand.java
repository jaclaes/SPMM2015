package org.cheetahplatform.literatemodeling.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.report.EdgeLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.text.ITextSelection;

public class CreateEdgeLiterateModelingAssociationCommand extends LiterateCommand {

	private Edge edge;
	private ITextSelection textSelection;
	private LiterateModel model;
	private String conditionName;

	public CreateEdgeLiterateModelingAssociationCommand(LiterateModel model, String conditionName, ITextSelection textSelection, Edge edge) {
		super(model, false);
		this.edge = edge;
		this.textSelection = textSelection;
		this.model = model;
		this.conditionName = conditionName;
	}

	@Override
	public void execute() {
		EdgeLiterateModelingAssociation association = new EdgeLiterateModelingAssociation(textSelection, edge);
		model.addAssociation(association);

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_EDGE_ASSOCIATION_CREATED);
		entry.setAttribute(LiterateCommands.ELEMENT_ID, edge.getId());
		entry.setAttribute(LiterateCommands.CONDITION_NAME, conditionName);
		entry.setAttribute(LiterateCommands.OFFSET, textSelection.getOffset());
		entry.setAttribute(LiterateCommands.LENGTH, textSelection.getLength());
		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return getFullName(edge);
	}

}
