package org.cheetahplatform.modeler.graph.export;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.mapping.ParagraphProvider;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CustomNodeConverter implements Converter {

	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String ID = "id";

	private static final String PARAGRAPH = "paragraph";
	private static final String NODE = "node";

	private PreparedStatement selectParagraphQuery;
	private final ReplayModel replayModel;
	private final Process process;

	public CustomNodeConverter(ReplayModel replayModel, Process process) {
		this.replayModel = replayModel;
		this.process = process;

		try {
			selectParagraphQuery = Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("select paragraph from paragraph_mapping where audittrail_entry = ?");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		return Node.class.isAssignableFrom(arg0);
	}

	public void dispose() {
		try {
			selectParagraphQuery.close();
		} catch (SQLException e) {
			org.cheetahplatform.modeler.Activator.logError("Could not close a statement.", e);
		}
	}

	/**
	 * Check if the process has a predefined mapping, e.g., for nodes from an initial model.
	 * 
	 * @param element
	 *            the element
	 * @return a default paragraph mapping, "" if none available
	 */
	private String findDefaultParagraph(GraphElement element) {
		Paragraph paragraph = ParagraphProvider.getParagraph(process.getId(), element.getId());
		if (paragraph != null) {
			return paragraph.getDescription();
		}

		return ""; // if there was an initial model, for some elements there is no paragraph mapping
	}

	public String getParagraph(GraphElement element) {
		long id = element.getId();
		AuditTrailEntry match = null;

		ProcessInstance instance = replayModel.getProcessInstance();
		for (int i = instance.getEntries().size() - 1; i >= 0; i--) {
			AuditTrailEntry current = instance.getEntries().get(i);
			if (current.isAttributeDefined(ID) && current.getLongAttribute(ID) == id) {
				match = current;

				if (RENAME.equals(current.getEventType())) {
					break; // renaming may changed the paragraph
				}
			}
		}

		return getParagraph(element, match);
	}

	private String getParagraph(GraphElement element, AuditTrailEntry match) {
		if (match == null) {
			return findDefaultParagraph(element);
		}

		try {
			long databaseId = replayModel.getAuditTrailEntryDatabaseId(match);
			selectParagraphQuery.setLong(1, databaseId);
			ResultSet resultSet;
			resultSet = selectParagraphQuery.executeQuery();

			if (!resultSet.next()) {
				return findDefaultParagraph(element);
			}

			long paragraphId = resultSet.getLong(1);
			return ParagraphProvider.getParagraph(paragraphId).getDescription();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		Node node = (Node) object;
		writer.startNode(NODE);

		writer.startNode(ID);
		writer.setValue(String.valueOf(node.getId()));
		writer.endNode();
		writer.startNode(NAME);
		writer.setValue(node.getNameNullSafe());
		writer.endNode();
		writer.startNode(TYPE);
		writer.setValue(node.getDescriptor().getId());
		writer.endNode();
		writer.startNode(PARAGRAPH);
		writer.setValue(getParagraph(node));
		writer.endNode();
		writer.startNode("X");
		writer.setValue(String.valueOf(node.getLocation().x));
		writer.endNode();
		writer.startNode("Y");
		writer.setValue(String.valueOf(node.getLocation().y));
		writer.endNode();

		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new AbstractMethodError("Not implemented");
	}

}
