package org.cheetahplatform.modeler.graph;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.gef.tools.CreationTool;

public class GraphElementCreationTool extends CreationTool {
	@Override
	protected void executeCurrentCommand() {
		AbstractGraphCommand command = (AbstractGraphCommand) getCurrentCommand();
		if (command != null) {
			GraphElement element = command.getGraphElement();
			if (!element.getDescriptor().canExecuteCreationCommand(command, element)) {
				return;
			}
		}

		// needed for collaborative editing, since the currentCommand gets
		// replaced by something else in case something changes while the user
		// enters the name of the component
		setCurrentCommand(command);

		super.executeCurrentCommand();
	}
}
