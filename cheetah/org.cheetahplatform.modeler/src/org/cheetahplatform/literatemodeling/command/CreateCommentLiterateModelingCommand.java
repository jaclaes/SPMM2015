package org.cheetahplatform.literatemodeling.command;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.MultiElementLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.text.ITextSelection;

public class CreateCommentLiterateModelingCommand extends LiterateCommand {

	private LiterateModel model;
	private String commentName;
	private ITextSelection textSelection;
	private List<GraphElement> selectedElements;

	public CreateCommentLiterateModelingCommand(LiterateModel model, String commentName, ITextSelection textSelection,
			List<GraphElement> selectedElements) {
		super(model, false);
		this.model = model;
		this.commentName = commentName;
		this.textSelection = textSelection;
		this.selectedElements = selectedElements;
	}

	@Override
	public void execute() {
		MultiElementLiterateModelingAssociation association = new MultiElementLiterateModelingAssociation(commentName, textSelection,
				selectedElements);
		model.addAssociation(association);

		AuditTrailEntry entry = createAuditrailEntry(LiterateCommands.LM_COMMENT_ASSOCIATION_CREATED);
		entry.setAttribute(LiterateCommands.OFFSET, textSelection.getOffset());
		entry.setAttribute(LiterateCommands.LENGTH, textSelection.getLength());
		entry.setAttribute(LiterateCommands.COMMENT_NAME, commentName);
		entry.setAttribute(LiterateCommands.SIZE, selectedElements.size());
		int cnt = 0;
		for (GraphElement element : selectedElements) {
			String name = LiterateCommands.ELEMENT_ID + "_" + cnt++;
			entry.setAttribute(name, element.getId());
		}
		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		StringBuffer buff = new StringBuffer();
		for (GraphElement element : selectedElements) {
			buff.append(getFullName(element)).append(" ");
		}
		return buff.toString();
	}

}
