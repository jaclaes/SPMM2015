package org.cheetahplatform.literatemodeling.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.ITextSelection;

public class CreateNodeLiterateModelingAssociationCommand extends LiterateCommand {
	private final ITextSelection textSelection;
	private final LiterateModel model;
	private final Node node;

	/**
	 * @param model
	 * @param textSelection
	 * @param location
	 * @param activityName
	 */
	public CreateNodeLiterateModelingAssociationCommand(LiterateModel model, ITextSelection textSelection, Node node) {
		super(model, false);
		Assert.isNotNull(model);
		Assert.isNotNull(textSelection);
		Assert.isNotNull(node);
		this.model = model;
		this.textSelection = textSelection;
		this.node = node;
	}

	@Override
	public void execute() {
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(textSelection, node);
		model.addAssociation(association);

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_NODE_ASSOCIATION_CREATED);
		entry.setAttribute(LiterateCommands.ELEMENT_ID, node.getId());
		entry.setAttribute(LiterateCommands.OFFSET, textSelection.getOffset());
		entry.setAttribute(LiterateCommands.LENGTH, textSelection.getLength());

		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return getFullName(node);
	}

}
