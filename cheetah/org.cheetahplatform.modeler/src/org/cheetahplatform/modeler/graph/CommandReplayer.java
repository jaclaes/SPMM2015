package org.cheetahplatform.modeler.graph;

import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_END;
import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_START;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.widgets.Display;

public class CommandReplayer {

	private class ExecuteCommandBackwardRunnable extends ExecuteCommandRunnable {

		protected ExecuteCommandBackwardRunnable(int endIndex, int startIndex, ICommandReplayerCallback callback) {
			super(startIndex, endIndex, callback);
		}

		@Override
		public void run() {
			for (int currentIndex = endIndex; currentIndex > startIndex; currentIndex--) {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						undoCommand();
					}
				});

				boolean last = currentIndex <= startIndex + 1;
				sleepAndInformCallback(current, last);
			}
		}
	}

	private class ExecuteCommandForwardRunnable extends ExecuteCommandRunnable {

		protected ExecuteCommandForwardRunnable(int startIndex, int endIndex, ICommandReplayerCallback callback) {
			super(startIndex, endIndex, callback);
		}

		@Override
		public void run() {
			for (int currentIndex = startIndex; currentIndex <= endIndex; currentIndex++) {
				CommandDelegate command = current;
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						executeNextCommand();
					}
				});

				boolean last = !(currentIndex < endIndex);
				sleepAndInformCallback(command, last);
			}
		}
	}

	private abstract static class ExecuteCommandRunnable implements Runnable {
		protected int startIndex;
		protected int endIndex;
		protected ICommandReplayerCallback callback;

		protected ExecuteCommandRunnable(int startIndex, int endIndex, ICommandReplayerCallback callback) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.callback = callback;
		}

		protected void sleepAndInformCallback(final CommandDelegate command, final boolean last) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// ignore
			}

			if (callback != null) {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						callback.processed(command, last);
					}
				});
			}
		}
	}

	private final List<CommandDelegate> commands;
	private CommandDelegate current;
	private final CommandStack stack;
	private final Graph graph;

	public CommandReplayer(CommandStack stack, Graph graph, ProcessInstance instance) {
		this.stack = stack;
		this.graph = graph;
		this.commands = new ArrayList<CommandDelegate>();

		initializeCommands(instance);

		if (!commands.isEmpty()) {
			current = commands.get(0);
		}
	}

	/**
	 * Executes the next command if there is one - check before calling whether {@link #hasCommands()} returns <code>true</code>.
	 * 
	 * @return the number of executed commands.
	 */
	public int executeNextCommand() {
		if (current == null) {
			return 0;
		}

		int numberOfAuditTrailEntries = current.getNumberOfAuditTrailEntries();
		current.execute();
		int index = commands.indexOf(current) + 1;
		if (index < commands.size()) {
			current = commands.get(index);
		} else {
			current = null;
		}
		return numberOfAuditTrailEntries;
	}

	public void executeUntil(CommandDelegate until, ICommandReplayerCallback callback) throws Exception {
		if (until != null && until.getParent() != null) {
			until = until.getParent();
		}

		int currentIndex = commands.indexOf(current);
		if (currentIndex == -1 && !commands.isEmpty() && until != null) {
			currentIndex = commands.size() - 1;
		}
		final int newIndex = commands.indexOf(until);
		Runnable runnable = null;

		if (currentIndex > newIndex) {
			runnable = new ExecuteCommandBackwardRunnable(currentIndex, newIndex, callback);
		} else {
			runnable = new ExecuteCommandForwardRunnable(currentIndex, newIndex, callback);
		}

		new Thread(runnable).start();
	}

	public List<CommandDelegate> flattenCommands() {
		List<CommandDelegate> flattened = new ArrayList<CommandDelegate>();
		for (CommandDelegate command : commands) {
			flattened.addAll(command.flatten());
		}

		return flattened;
	}

	public List<CommandDelegate> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public CommandDelegate getCurrentCommand() {
		return current;
	}

	public Graph getGraph() {
		return graph;
	}

	public long getHighestModelId() {
		long maximumId = 0;
		for (CommandDelegate command : commands) {
			long id = command.getGraphElementId();
			if (id > maximumId) {
				maximumId = id;
			}
		}

		return maximumId;
	}

	public CommandDelegate getPreviousCommand() {
		CommandDelegate currentCommand = getCurrentCommand();
		int index = commands.indexOf(currentCommand);
		if (!commands.isEmpty() && index == -1) {
			return commands.get(commands.size() - 1);
		}

		if (index == 0) {
			return null;
		}
		return commands.get(index - 1);
	}

	public CommandStack getStack() {
		return stack;
	}

	/**
	 * Determine whether there are still commands which can be executed.
	 * 
	 * @return <code>true</code> if there are more commands, <code>false</code> otherwise
	 */
	public boolean hasCommands() {
		return current != null;
	}

	private void initializeCommands(ProcessInstance instance) {
		List<AuditTrailEntry> entries = instance.getEntries();
		CommandDelegate context = null;

		int commandIndex = 1;
		for (int i = 0; i < entries.size(); i++) {
			AuditTrailEntry entry = entries.get(i);
			if (GROUP_EVENT_END.equals(entry.getEventType())) {
				context = null;
				continue;
			}

			CommandDelegate command = new CommandDelegate(this, entry, commandIndex, context);
			if (context == null) {
				commands.add(command);
			} else {
				context.addChildCommand(command);
				commandIndex--;
			}

			if (GROUP_EVENT_START.equals(command.getType())) {
				context = command;
			}
			commandIndex++;
		}
	}

	public void undoCommand() {
		int index = commands.indexOf(current) - 1;
		if (current == null) {
			index = commands.size() - 1;
		}
		if (index == -1) {
			return; // no or first command
		}

		current = commands.get(index);
		current.undo();
	}
}
