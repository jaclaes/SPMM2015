package org.cheetahplatform.test.modeler;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.commands.Command;

public class TestLogCommand extends Command {
	public static final String TEST = "test";
	private Graph graph;
	private final String type;

	public TestLogCommand(Graph graph) {
		this(graph, TEST);
	}

	public TestLogCommand(Graph graph, String type) {
		this.graph = graph;
		this.type = type;
	}

	@Override
	public void execute() {
		graph.log(new AuditTrailEntry(type));
	}
}