package org.cheetahplatform.modeler.graph.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.model.Graph;

public class CompoundGraphCommand extends AbstractGraphCommand {
	private List<CommandDelegate> commands;

	public CompoundGraphCommand(Graph graph) {
		super(graph, null);

		this.commands = new ArrayList<CommandDelegate>();
	}

	public void add(CommandDelegate child) {
		commands.add(child);
	}

	/**
	 * @param command
	 * @return
	 */
	public boolean contains(CommandDelegate command) {
		return commands.contains(command);
	}

	@Override
	public void execute() {
		for (CommandDelegate command : commands) {
			command.execute();
		}
	}

	@Override
	protected String getAffectedElementName() {
		return "";
	}

	public boolean hasChildren() {
		return !commands.isEmpty();
	}

	@Override
	public void undo() {
		List<CommandDelegate> reversed = new ArrayList<CommandDelegate>(commands);
		Collections.reverse(reversed);
		for (CommandDelegate command : reversed) {
			command.undo();
		}
	}

}
