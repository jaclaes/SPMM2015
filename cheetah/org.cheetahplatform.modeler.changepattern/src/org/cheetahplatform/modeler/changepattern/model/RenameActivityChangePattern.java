package org.cheetahplatform.modeler.changepattern.model;

import java.util.Date;

import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class RenameActivityChangePattern extends AbstractChangePattern {
	public static final String RENAME_ACTIVITY_CHANGE_PATTERN = "RENAME_ACTIVITY";

	/**
	 * @param graph
	 * @param node
	 * @param activityName
	 */
	public RenameActivityChangePattern(Graph graph, Node node, String activityName, Date startTime) {
		super(graph);
		RenameCommand command = new RenameCommand(node, activityName);
		command.setStartTime(startTime);
		add(command);
	}

	@Override
	public String getName() {
		return RENAME_ACTIVITY_CHANGE_PATTERN;
	}

}
