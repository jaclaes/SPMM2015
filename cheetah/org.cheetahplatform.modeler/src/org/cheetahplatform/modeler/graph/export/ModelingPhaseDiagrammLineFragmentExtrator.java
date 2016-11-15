package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ModelingPhaseDiagrammLineFragmentExtrator {

	private Map<AuditTrailEntry, Integer> auditTrailEntryToNumberOfElements;

	private Graph createGraph(ProcessInstance modelingInstance) {
		String attribute = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process process = ProcessRepository.getProcess(attribute);
		String type = modelingInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		return graph;
	}

	private CommandReplayer createReplayer(ProcessInstance modelingInstance, Graph graph) {
		GraphCommandStack stack = new GraphCommandStack(graph);
		CommandReplayer replayer = new CommandReplayer(stack, graph, modelingInstance);
		return replayer;
	}

	public List<ModelingPhase> extractLineFragments(Graph graph, CommandReplayer replayer, ProcessInstance modelingInstance,
			List<Chunk> chunks) {
		List<ModelingPhase> points = new ArrayList<ModelingPhase>();
		auditTrailEntryToNumberOfElements = new HashMap<AuditTrailEntry, Integer>();
		int numberOfElements = 0;
		for (Chunk chunk : chunks) {
			long instanceStartTime = modelingInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
			long start = (chunk.getStartTime().getTime() - instanceStartTime);
			long end = (chunk.getEndTime().getTime() - instanceStartTime);

			int startNumberOfElements = graph.getGraphElements().size();
			int toExecute = chunk.getSize();

			Iterator<AuditTrailEntry> iterator = chunk.getEntries().iterator();

			while (toExecute > 0) {
				ignoreScrollEvents(replayer);
				int numberOfExecutedCommands = replayer.executeNextCommand();
				for (int i = 0; i < numberOfExecutedCommands; i++) {
					AuditTrailEntry next = iterator.next();
					auditTrailEntryToNumberOfElements.put(next, graph.getGraphElements().size());
				}

				toExecute -= numberOfExecutedCommands;
			}

			numberOfElements = graph.getGraphElements().size();
			points.add(new ModelingPhase(start, end, startNumberOfElements, numberOfElements, chunk.getSize(), chunk.getType()));
		}
		return points;
	}

	public List<ModelingPhase> extractLineFragments(ProcessInstance modelingInstance, List<Chunk> chunks) {
		Graph graph = createGraph(modelingInstance);
		return extractLineFragments(graph, createReplayer(modelingInstance, graph), modelingInstance, chunks);

	}

	public Map<AuditTrailEntry, Integer> extractNumberOfElements(Graph graph, CommandReplayer replayer, ProcessInstance modelingInstance,
			List<Chunk> chunks) {
		extractLineFragments(graph, replayer, modelingInstance, chunks);
		return auditTrailEntryToNumberOfElements;
	}

	public Map<AuditTrailEntry, Integer> extractNumberOfElements(ProcessInstance modelingInstance, List<Chunk> chunks) {
		Graph graph = createGraph(modelingInstance);
		CommandReplayer replayer = createReplayer(modelingInstance, graph);

		return extractNumberOfElements(graph, replayer, modelingInstance, chunks);
	}

	public void ignoreScrollEvents(CommandReplayer replayer) {
		CommandDelegate currentCommand = replayer.getCurrentCommand();
		while (isCommandToSkip(currentCommand)) {
			replayer.executeNextCommand();
			currentCommand = replayer.getCurrentCommand();
		}
	}

	private boolean isCommandToSkip(CommandDelegate currentCommand) {
		String type = currentCommand.getType();
		return AbstractGraphCommand.VSCROLL.equals(type) || AbstractGraphCommand.HSCROLL.equals(type);
	}
}
