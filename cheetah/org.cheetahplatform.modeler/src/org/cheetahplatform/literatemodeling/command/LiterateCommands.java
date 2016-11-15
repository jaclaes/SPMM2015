package org.cheetahplatform.literatemodeling.command;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.report.ReportGenerator;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;

public class LiterateCommands {
	public static final String LM = "LM";
	public static final String LM_DOCUMENT_CHANGED = LM + "_DOCUMENT_CHANGED";
	public static final String OFFSET = "OFFSET";
	public static final String LENGTH = "LENGTH";
	public static final String TEXT = "TEXT";
	public static final String CHANGE_TEXT_TIME = "CHANGE_TEXT_TIME";

	public static final String LM_TEXT_CHANGED = LM + "_TEXT_CHANGED";
	public static final String TEXT_NAME = "TEXT_NAME";
	public static final String TEXT_DESCRIPTION = "TEXT_DESCRIPTION";
	public static final String TEXT_TYPE = "TEXT_TYPE";

	public static final String LM_NODE_ASSOCIATION_CREATED = LM + "NODE__ASSOCIATION_CREATED";
	public static final String ELEMENT_ID = "ELEMENT_ID";

	public static final String LM_EDGE_ASSOCIATION_CREATED = LM + "EDGE_ASSOCIATION_CREATED";
	public static final String CONDITION_NAME = "CONDITION_NAME";
	public static final String LM_COMMENT_ASSOCIATION_CREATED = LM + "COMMENT_ASSOCIATION_CREATED";
	public static final String COMMENT_NAME = "COMMENT_NAME";
	public static final String SIZE = "SIZE";
	public static final String LM_REPORT_CREATED = LM + "REPORT_CREATED";

	public static AbstractGraphCommand createCommand(AuditTrailEntry entry, LiterateModel graph) {
		String type = entry.getEventType();
		if (type.equals(LM_DOCUMENT_CHANGED)) {

			DocumentEvent event = new DocumentEvent();
			event.fDocument = graph.getDocument();
			event.fLength = entry.getIntegerAttribute(LENGTH);
			event.fOffset = entry.getIntegerAttribute(OFFSET);
			event.fText = entry.getAttribute(TEXT);
			event.fModificationStamp = entry.getLongAttribute(CHANGE_TEXT_TIME);

			DocumentChangeCommand command = new DocumentChangeCommand(graph, event);
			return command;
		} else if (type.equals(LM_TEXT_CHANGED)) {
			return new TextChangeCommand(graph, entry.getAttribute(TEXT_TYPE), entry.getAttribute(TEXT));
		} else if (type.equals(LM_NODE_ASSOCIATION_CREATED)) {
			ITextSelection textSelection = new TextSelection(entry.getIntegerAttribute(OFFSET), entry.getIntegerAttribute(LENGTH));
			Node node = (Node) graph.getGraphElement(entry.getIntegerAttribute(ELEMENT_ID));
			return new CreateNodeLiterateModelingAssociationCommand(graph, textSelection, node);
		} else if (type.equals(LM_EDGE_ASSOCIATION_CREATED)) {
			ITextSelection textSelection = new TextSelection(entry.getIntegerAttribute(OFFSET), entry.getIntegerAttribute(LENGTH));
			Edge edge = (Edge) graph.getGraphElement(entry.getIntegerAttribute(ELEMENT_ID));
			String conditionName = entry.getAttribute(CONDITION_NAME);
			return new CreateEdgeLiterateModelingAssociationCommand(graph, conditionName, textSelection, edge);
		} else if (type.equals(LM_COMMENT_ASSOCIATION_CREATED)) {
			ITextSelection textSelection = new TextSelection(entry.getIntegerAttribute(OFFSET), entry.getIntegerAttribute(LENGTH));
			String commentName = entry.getAttribute(COMMENT_NAME);
			int size = entry.getIntegerAttribute(SIZE);

			List<GraphElement> elements = new ArrayList<GraphElement>();
			for (int i = 0; i < size; i++) {
				String attribute = LiterateCommands.ELEMENT_ID + "_" + i;
				elements.add(graph.getGraphElement(entry.getIntegerAttribute(attribute)));
			}
			return new CreateCommentLiterateModelingCommand(graph, commentName, textSelection, elements);
		} else if (type.equals(LM_REPORT_CREATED)) {
			GraphicalGraphViewerWithFlyoutPalette viewer = LiterateModelingEditor.getActiveEditor().getGraphViewer();
			ReportGenerator generator = new ReportGenerator(graph, viewer);
			return new CreateReportLiterateModelingCommand(graph, generator);
		}
		return null;
	}

	public static String getCommandLabel(AuditTrailEntry entry) {
		String type = entry.getEventType();
		if (type.equals(LM_DOCUMENT_CHANGED)) {
			return "Document Change";
		} else if (type.equals(LM_TEXT_CHANGED)) {
			return "Text Change";
		} else if (type.equals(LM_NODE_ASSOCIATION_CREATED)) {
			return "Node Association";
		} else if (type.equals(LM_EDGE_ASSOCIATION_CREATED)) {
			return "Edge Association";
		} else if (type.equals(LM_COMMENT_ASSOCIATION_CREATED)) {
			return "Comment Association";
		} else if (type.equals(LM_REPORT_CREATED)) {
			return "Report Creation";
		}
		return null;
	}
}
