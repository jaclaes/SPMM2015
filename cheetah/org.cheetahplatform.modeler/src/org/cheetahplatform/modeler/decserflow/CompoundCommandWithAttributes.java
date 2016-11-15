package org.cheetahplatform.modeler.decserflow;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.DataContainer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

public class CompoundCommandWithAttributes extends CompoundCommand {
	private DataContainer attributes;

	public CompoundCommandWithAttributes() {
		this.attributes = new DataContainer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canExecute() {
		List<Command> commands = getCommands();
		for (Command command : commands) {
			if (command == null || !command.canExecute()) {
				return false;
			}
		}

		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean canUndo() {
		List<Command> commands = getCommands();
		for (Command command : commands) {
			if (command == null || !command.canUndo()) {
				return false;
			}
		}

		return true;
	}

	public List<Attribute> getAttributes() {
		return attributes.getAttributes();
	}

	public String getAttributeSafely(String key) {
		return attributes.getAttributeSafely(key);
	}

	public void setAttribute(String name, String content) {
		attributes.setAttribute(name, content);
	}

}
