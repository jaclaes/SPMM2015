package org.cheetahplatform.modeler.graph.mapping;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RECONNECT_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ISemanticalCorrectnessEvaluation;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class MapParagraphModel {
	private class MapParagraphLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
		private ITableLabelProvider fallback;

		protected MapParagraphLabelProvider(ITableLabelProvider fallback) {
			this.fallback = fallback;
		}

		@Override
		public Color getBackground(Object element, int columnIndex) {
			if (columnIndex == 4) {
				CommandDelegate command = (CommandDelegate) element;
				if (isIgnored(element)) {
					return SWTResourceManager.getColor(IGNORE);
				}

				Paragraph paragraph = commandToDelegate.get(command);
				if (paragraph == null || paragraph.getColor() == null) {
					return null;
				}

				return SWTResourceManager.getColor(paragraph.getColor());
			}

			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 3) {
				Paragraph paragraph = commandToDelegate.get(element);
				if (isIgnored(element)) {
					return "ignored";
				}

				if (paragraph == null) {
					return "";
				}

				return paragraph.getDescription();
			}

			return fallback.getColumnText(element, columnIndex);
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

		private boolean isIgnored(Object element) {
			CommandDelegate command = (CommandDelegate) element;
			String type = command.getType();
			if (CREATE_EDGE.equals(type) || DELETE_EDGE.equals(type) || RECONNECT_EDGE.equals(type)) {
				return true;
			}

			if (PromLogger.GROUP_EVENT_START.equals(type) || PromLogger.GROUP_EVENT_END.equals(type)) {
				return true;
			}
			if (!command.isAttributeDefined(AbstractGraphCommand.DESCRIPTOR)) {
				return true;
			}

			IGraphElementDescriptor descriptor = command.getDescriptor();
			if (RENAME.equals(type)) {
				if (descriptor instanceof IEdgeDescriptor) {
					return true;
				}
			}
			if (descriptor instanceof ActivityDescriptor) {
				return false;
			}

			return true;
		}

	}

	public static final EdgeCondition REMOVE_EDGE_CONDITION = new EdgeCondition(1000000, "Remove mapping", new RGB(255, 255, 255));
	public static final Paragraph REMOVE_PARAGRAPH = new Paragraph(1000000, "", "Remove mapping", new RGB(255, 255, 255), -1);
	private static final RGB IGNORE = new RGB(200, 200, 200);

	private final ProcessInstanceDatabaseHandle handle;

	private Paragraph currentParagraph;

	private Map<CommandDelegate, Paragraph> commandToDelegate;
	private String process;
	private PreparedStatement insertMappingQuery;
	private PreparedStatement updateMappingQuery;
	private PreparedStatement insertEdgeConditionMappingQuery;
	private PreparedStatement updateEdgeConditionMappingQuery;
	private PreparedStatement deleteEdgeConditionMappingQuery;
	private PreparedStatement deleteParagraphMappingQuery;

	private ReplayModel replayModel;
	private EdgeCondition currentEdgeCondition;

	public MapParagraphModel(ProcessInstanceDatabaseHandle handle) {
		this.handle = handle;
		this.process = handle.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		this.commandToDelegate = new HashMap<CommandDelegate, Paragraph>();

		try {
			Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
			String query = "insert into paragraph_mapping (paragraph, audittrail_entry) values (?, ?)";
			insertMappingQuery = connection.prepareStatement(query);

			query = "update paragraph_mapping set paragraph = ? where audittrail_entry = ?";
			updateMappingQuery = connection.prepareStatement(query);

			query = "delete from paragraph_mapping where audittrail_entry = ?";
			deleteParagraphMappingQuery = connection.prepareStatement(query);

			query = "insert into edge_condition_mapping (process_instance, edge, edge_condition) values (?, ?, ?);";
			insertEdgeConditionMappingQuery = connection.prepareStatement(query);

			query = "update edge_condition_mapping set edge_condition = ? where process_instance = ? and edge = ?;";
			updateEdgeConditionMappingQuery = connection.prepareStatement(query);

			query = "delete from edge_condition_mapping where process_instance = ? and edge = ?";
			deleteEdgeConditionMappingQuery = connection.prepareStatement(query);
		} catch (SQLException e) {
			Activator.logError("Could not create query", e);
		}
	}

	public IBaseLabelProvider createLabelProvider(ITableLabelProvider fallback) {
		return new MapParagraphLabelProvider(fallback);
	}

	public void dispose() {
		try {
			insertMappingQuery.close();
			updateMappingQuery.close();
			deleteParagraphMappingQuery.close();
			insertEdgeConditionMappingQuery.close();
			updateEdgeConditionMappingQuery.close();
			deleteEdgeConditionMappingQuery.close();
		} catch (SQLException e) {
			org.cheetahplatform.common.Activator.logError("Could not close a statement.", e);
		}
	}

	public EdgeCondition getCurrentEdgeCondition() {
		return currentEdgeCondition;
	}

	/**
	 * @return the current
	 */
	public Paragraph getCurrentParagraph() {
		return currentParagraph;
	}

	public List<EdgeCondition> getEdgeConditions() {
		List<EdgeCondition> edgeConditions = new ArrayList<EdgeCondition>(EdgeConditionProvider.getEdgeConditions(process));
		edgeConditions.add(REMOVE_EDGE_CONDITION);

		return edgeConditions;
	}

	public String getGraphType() {
		return handle.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
	}

	/**
	 * @return the handle
	 */
	public ProcessInstanceDatabaseHandle getHandle() {
		return handle;
	}

	public List<Paragraph> getParagraphs() {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>(ParagraphProvider.getParagraphs(process));
		paragraphs.add(REMOVE_PARAGRAPH);

		return paragraphs;
	}

	public Process getProcess() {
		String processId = handle.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		return ProcessRepository.getProcess(processId);
	}

	public String getSemanticalEvaluationScore() {
		Graph graph = replayModel.getGraph();

		Process process2 = ProcessRepository.getProcess(process);

		IExperimentConfiguration experiment = ProcessRepository.getExperimentByModelingProcess(process2);
		List<ISemanticalCorrectnessEvaluation> evaluations = experiment.getSemanticalCorrectnessEvaluations(process);
		if (evaluations.isEmpty()) {
			return "No Semantical Evaluations Available.";
		}

		double sum = 0;
		for (ISemanticalCorrectnessEvaluation evaluation : evaluations) {
			double result = evaluation.evaluate(graph, handle);
			sum += result;
		}

		return "Semantical Evaluation Score: " + sum;
	}

	private void initializeParagraphs() {
		List<CommandDelegate> flattened = replayModel.getReplayer().flattenCommands();
		String query = "select paragraph from paragraph_mapping where audittrail_entry = ?";
		try {
			PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement(query);

			for (CommandDelegate command : flattened) {
				statement.setLong(1, replayModel.getAuditTrailEntryDatabaseId(command));
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					long paragraphId = resultSet.getLong(1);
					Paragraph paragraph = ParagraphProvider.getParagraph(paragraphId);
					commandToDelegate.put(command, paragraph);
				}
			}

			statement.close();
		} catch (SQLException e) {
			Activator.logError("Could not load the mappings.", e);
		}
	}

	public void jumpToNextManualIntervention() throws Exception {
		CommandReplayer replayer = replayModel.getReplayer();
		if (replayer.getCurrentCommand() == null) {
			return;
		}

		CommandDelegate currentCommand = replayer.getCurrentCommand();
		List<CommandDelegate> commands = replayer.getCommands();
		int startIndex = commands.indexOf(currentCommand);
		int index = startIndex;

		for (; index < commands.size(); index++) {
			CommandDelegate command = commands.get(index);
			if (!command.isAttributeDefined(AbstractGraphCommand.DESCRIPTOR)) {
				continue;
			}

			if (!(command.getDescriptor() instanceof ActivityDescriptor)) {
				continue;
			}

			tryToMapElements(command, commands);

			String type = command.getType();
			if (DELETE_NODE.equals(type) || RENAME.equals(type)) {
				if (!command.isExecuted() && index != startIndex && !commandToDelegate.containsKey(command)) {
					if (index >= commands.size() - 1) {
						break;
					}
					tryToMapElements(commands.get(index + 1), commands);
					break;
				}
			}
		}

		if (index >= commands.size()) {
			index = commands.size();
		}

		StructuredSelection selection = new StructuredSelection(commands.get(index - 1));
		replayModel.executeTo(selection);
	}

	public void loadEdgeConditionMapping() {
		String query = "select edge, edge_condition from edge_condition_mapping where process_instance = ?";
		try {
			PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement(query);
			statement.setLong(1, replayModel.getProcessInstanceDatabaseId());
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				long edgeId = resultSet.getLong("edge");
				long edgeConditionId = resultSet.getLong("edge_condition");
				EdgeCondition condition = EdgeConditionProvider.getEdgeCondition(edgeConditionId);
				GraphElement edge = replayModel.getGraph().getGraphElement(edgeId);
				if (edge == null) {
					continue;
				}

				edge.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, condition.getColor());
			}

			statement.close();
		} catch (SQLException e) {
			org.cheetahplatform.common.Activator.logError("Could not load the edge condition mappings.", e);
		}
	}

	private void mapParagraph(long id, Paragraph paragraph, CommandDelegate command) {
		if (!command.isAttributeDefined(AbstractGraphCommand.ID)) {
			return;
		}

		if (command.getGraphElementId() == id) {
			try {
				if (paragraph.equals(REMOVE_PARAGRAPH)) {
					commandToDelegate.remove(command);
					deleteParagraphMappingQuery.setLong(1, replayModel.getAuditTrailEntryDatabaseId(command));
					deleteParagraphMappingQuery.execute();

					return;
				}

				commandToDelegate.put(command, paragraph);

				updateMappingQuery.setLong(1, paragraph.getId());
				updateMappingQuery.setLong(2, replayModel.getAuditTrailEntryDatabaseId(command));
				int affectedRows = updateMappingQuery.executeUpdate();

				if (affectedRows == 0) {
					insertMappingQuery.setLong(1, paragraph.getId());
					insertMappingQuery.setLong(2, replayModel.getAuditTrailEntryDatabaseId(command));
					insertMappingQuery.execute();
				}
			} catch (SQLException e) {
				Activator.logError("Could not save the paragraphs.", e);
			}
		}
	}

	public void paragraphChanged(GraphElement element, CommandDelegate command) {
		Paragraph paragraph = commandToDelegate.get(command);
		RGB color = null;
		if (paragraph != null) {
			color = paragraph.getColor();
		}

		element.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, color);
	}

	public void setCurrentEdgeCondition(EdgeCondition condition) {
		this.currentEdgeCondition = condition;
		this.currentParagraph = null;
	}

	public void setCurrentParagraph(Paragraph paragraph) {
		this.currentParagraph = paragraph;
		this.currentEdgeCondition = null;
	}

	public void setEdgeCondition(Edge edge, EdgeCondition edgeCondition) {
		try {
			if (edgeCondition.equals(REMOVE_EDGE_CONDITION)) {
				deleteEdgeConditionMappingQuery.setLong(1, replayModel.getProcessInstanceDatabaseId());
				deleteEdgeConditionMappingQuery.setLong(2, edge.getId());
				deleteEdgeConditionMappingQuery.execute();

				return;
			}

			updateEdgeConditionMappingQuery.setLong(1, edgeCondition.getId());
			updateEdgeConditionMappingQuery.setLong(2, replayModel.getProcessInstanceDatabaseId());
			updateEdgeConditionMappingQuery.setLong(3, edge.getId());
			int affectedRows = updateEdgeConditionMappingQuery.executeUpdate();

			if (affectedRows == 0) {
				insertEdgeConditionMappingQuery.setLong(1, replayModel.getProcessInstanceDatabaseId());
				insertEdgeConditionMappingQuery.setLong(2, edge.getId());
				insertEdgeConditionMappingQuery.setLong(3, edgeCondition.getId());
				insertEdgeConditionMappingQuery.execute();
			}
		} catch (SQLException e) {
			Activator.logError("Could not save an edge condition mapping.", e);
		}
	}

	public void setParagraph(long id, Paragraph paragraph, CommandDelegate currentCommand, List<CommandDelegate> commands) {
		if (commands.isEmpty()) {
			return;
		}

		// must start at index-1 as the current command always indicates the next command to be executed
		int startIndex = commands.indexOf(currentCommand) - 1;
		if (currentCommand == null) {
			startIndex = commands.size() - 1;
		}
		if (startIndex == -1) {
			return; // first command to be executed --> nothing restored yet --> nothing to map
		}

		currentCommand = commands.get(startIndex);

		for (int i = startIndex; i < commands.size(); i++) {
			if (stop(startIndex, i, commands, id, paragraph)) {
				break;
			}
		}

		for (int i = startIndex; i >= 0; i--) {
			if (stop(startIndex, i, commands, id, paragraph)) {
				break;
			}
		}
	}

	public void setReplayModel(ReplayModel replayModel) {
		this.replayModel = replayModel;

		initializeParagraphs();
		tryToMapParagraphsPerId();
	}

	private boolean stop(int startIndex, int currentIndex, List<CommandDelegate> commands, long id, Paragraph paragraph) {
		CommandDelegate command = commands.get(currentIndex);
		boolean mappingBackwards = startIndex > currentIndex;
		// if we are mapping backwards we are allowed to map the first rename of the mapped element, as its execution gave the element the
		// current name
		if (mappingBackwards) {
			if (command.getType().equals(AbstractGraphCommand.RENAME) && command.getGraphElementId() == id) {
				mapParagraph(id, paragraph, command);
				return false;
			}
		}

		if (currentIndex != startIndex && command.getType().equals(AbstractGraphCommand.RENAME) && command.getGraphElementId() == id) {
			return true;
		}

		mapParagraph(id, paragraph, command);
		return false;
	}

	private void tryToMapElements(CommandDelegate command, List<CommandDelegate> commands) {
		if (!command.isAttributeDefined(AbstractGraphCommand.NAME)) {
			return;
		}

		String name = command.getAttribute(AbstractGraphCommand.NAME);
		List<Paragraph> paragraphs = getParagraphs();
		for (Paragraph paragraph : paragraphs) {
			for (String possibleName : paragraph.getPossibleActivityNames()) {
				if (possibleName.toLowerCase().equals(name.toLowerCase())) {
					setParagraph(command.getGraphElementId(), paragraph, command, commands);
				}
			}
		}
	}

	/**
	 * Try to map existing model elements by their ids.
	 */
	private void tryToMapParagraphsPerId() {
		Graph graph = replayModel.getGraph();

		for (Node node : graph.getNodes()) {
			Paragraph paragraph = ParagraphProvider.getParagraph(process, node.getId());
			if (paragraph == null) {
				continue;
			}

			node.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, paragraph.getColor());
		}
	}
}
