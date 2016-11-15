package org.cheetahplatform.modeler.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CompoundGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;

public class CommandDelegate {
	private List<CommandDelegate> children;

	private AbstractGraphCommand command;
	private final AuditTrailEntry entry;
	private final int index;
	private boolean executed;
	private final CommandDelegate parent;
	private final CommandReplayer replayer;

	public CommandDelegate(CommandReplayer replayer, AuditTrailEntry entry, int index, CommandDelegate parent) {
		this.replayer = replayer;
		this.entry = entry;
		this.index = index;
		this.parent = parent;
		this.children = new ArrayList<CommandDelegate>();
	}

	public void addChildCommand(CommandDelegate delegate) {
		children.add(delegate);
	}

	public void execute() {
		getCommand();

		if (command instanceof CompoundGraphCommand) {
			for (CommandDelegate child : children) {
				CompoundGraphCommand compoundCommand = (CompoundGraphCommand) command;
				if (!compoundCommand.contains(child)) {
					compoundCommand.add(child);
				}
			}
		}

		replayer.getStack().execute(command);
		executed = true;
	}

	public Collection<? extends CommandDelegate> flatten() {
		List<CommandDelegate> flattened = new ArrayList<CommandDelegate>();
		flattened.add(this);
		for (CommandDelegate child : children) {
			flattened.addAll(child.flatten());
		}

		return flattened;
	}

	public String getAttribute(String key) {
		return entry.getAttribute(key);

	}

	public AuditTrailEntry getAuditTrailEntry() {
		return entry;
	}

	public List<CommandDelegate> getChildren() {
		return children;
	}

	public AbstractGraphCommand getCommand() {
		if (command == null) {
			command = AbstractGraphCommand.createCommand(entry, replayer.getGraph());
		}

		return command;
	}

	public IGraphElementDescriptor getDescriptor() {
		String descriptorId = entry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
		return EditorRegistry.getDescriptor(descriptorId);
	}

	public long getGraphElementId() {
		if (!entry.isAttributeDefined(AbstractGraphCommand.ID)) {
			return -1;
		}
		return entry.getLongAttribute(AbstractGraphCommand.ID);
	}

	public long getHighestModelId() {
		long highestId = getGraphElementId();
		for (CommandDelegate childCommand : children) {
			long childId = childCommand.getHighestModelId();

			if (childId > highestId) {
				highestId = childId;
			}
		}

		return highestId;
	}

	public int getIndex() {
		return index;
	}

	public String getLabel() {
		return AbstractGraphCommand.getCommandLabel(entry);
	}

	public int getNumberOfAuditTrailEntries() {
		if (children.isEmpty()) {
			return 1;
		}

		int total = 0;
		for (CommandDelegate delegate : children) {
			total += delegate.getNumberOfAuditTrailEntries();
		}
		return total;
	}

	public CommandDelegate getParent() {
		return parent;
	}

	public String getType() {
		return entry.getEventType();
	}

	public boolean isAttributeDefined(String attribute) {
		return entry.isAttributeDefined(attribute);
	}

	public boolean isExecuted() {
		return executed;
	}

	public void undo() {
		command.undo();
		executed = false;
	}
}