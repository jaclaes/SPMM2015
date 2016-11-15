package org.cheetahplatform.modeler.changepattern.model;

import java.util.Date;

import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class UpdateConditionChangePattern extends AbstractChangePattern {

	public static final String UPDATE_CONDITION_CHANGE_PATTERN = "UPDATE_CONDITION";

	/**
	 * @param graph
	 * @param edge
	 * @param condition
	 * @param startTime
	 */
	public UpdateConditionChangePattern(Graph graph, Edge edge, String condition, Date startTime) {
		super(graph);

		RenameCommand command = new RenameCommand(edge, condition);
		command.setStartTime(startTime);
		add(command);
	}

	@Override
	public String getName() {
		return UPDATE_CONDITION_CHANGE_PATTERN;
	}
}
