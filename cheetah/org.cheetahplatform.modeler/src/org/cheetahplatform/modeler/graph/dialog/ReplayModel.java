package org.cheetahplatform.modeler.graph.dialog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.ModelingPhaseModel;
import org.cheetahplatform.modeler.graph.model.PpmMetricModel;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

public class ReplayModel implements IAdaptable {
	private class InformListenersCallback implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			Object[] listeners = callbacks.getListeners();
			for (Object object : listeners) {
				((ICommandReplayerCallback) object).processed(command, last);
			}

		}
	}

	private static class ReplayContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object arg0) {
			return ((CommandDelegate) arg0).getChildren().toArray();
		}

		@Override
		public Object getParent(Object arg0) {
			return ((CommandDelegate) arg0).getParent();
		}

		@Override
		public boolean hasChildren(Object arg0) {
			return !((CommandDelegate) arg0).getChildren().isEmpty();
		}

	}

	private static class ReplayLabelProvider extends LabelProvider implements ITableLabelProvider {
		private SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			CommandDelegate command = (CommandDelegate) element;
			if (columnIndex == 0) {
				if (command.getParent() == null) {
					return String.valueOf(command.getIndex());
				}

				return "";
			}

			if (columnIndex == 1) {
				Date timestamp = command.getAuditTrailEntry().getTimestamp();
				return format.format(timestamp);
			}

			if (columnIndex == 2) {
				if (command.getParent() != null) {
					return "    " + command.getLabel();
				}

				return command.getLabel();
			}

			if (columnIndex == 3) {
				if (command.isExecuted()) {
					return "Executed";
				}
				if (command.getParent() != null && command.getParent().isExecuted()) {
					return "Executed";
				}

				return "";
			}

			return "";
		}
	}

	private static final int STEP_SIZE = 5;

	private CommandReplayer replayer;
	private Graph graph;
	private final ProcessInstanceDatabaseHandle graphHandle;
	private Map<AuditTrailEntry, Long> entryToDatabaseId;
	private ListenerList callbacks;

	public ReplayModel(CommandStack stack, ProcessInstanceDatabaseHandle handle, Graph graph) {
		this.graphHandle = handle;
		this.graph = graph;
		this.replayer = new CommandReplayer(stack, this.graph, handle.getInstance());
		this.entryToDatabaseId = new HashMap<AuditTrailEntry, Long>();
		callbacks = new ListenerList();

		initializeAuditTrailEntryIds();
		// ensure that no ids are reused
		Services.getIdGenerator().setMinimalId(replayer.getHighestModelId() + 100);
	}

	public void addCallbackListener(ICommandReplayerCallback callBack) {
		callbacks.add(callBack);
	}

	public ITreeContentProvider createContentProvider() {
		return new ReplayContentProvider();
	}

	public ITableLabelProvider createLabelProvider() {
		return new ReplayLabelProvider();
	}

	public IStructuredSelection executeTo(IStructuredSelection selection) throws Exception {
		CommandDelegate delegate = (CommandDelegate) selection.getFirstElement();
		replayer.executeUntil(delegate, new InformListenersCallback());
		return getCurrentSelection();
	}

	public boolean existsParagraphMapping() throws SQLException {
		PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
				.prepareStatement("select * from paragraph_mapping where audittrail_entry = ?");

		boolean hasMapping = false;
		for (long auditrailEntryId : entryToDatabaseId.values()) {
			statement.setLong(1, auditrailEntryId);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				hasMapping = true;
				break;
			}
		}

		statement.close();
		return hasMapping;
	}

	public CommandDelegate findCommandDelegate(AuditTrailEntry entry) {
		for (CommandDelegate commandDelegate : getCommands()) {
			AuditTrailEntry auditTrailEntry = commandDelegate.getAuditTrailEntry();
			if (auditTrailEntry.equals(entry)) {
				return commandDelegate;
			}
		}
		throw new IllegalStateException("Unknown Audittrail entry");
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ReplayModel.class)) {
			return this;
		}
		if (adapter.equals(ModelingPhaseModel.class)) {
			return new ModelingPhaseModel(this);
		}
		if (adapter.equals(PpmMetricModel.class)) {
			return new PpmMetricModel(this);
		}

		return null;
	}

	public long getAuditTrailEntryDatabaseId(AuditTrailEntry entry) {
		return entryToDatabaseId.get(entry);
	}

	public long getAuditTrailEntryDatabaseId(CommandDelegate command) {
		return entryToDatabaseId.get(command.getAuditTrailEntry());
	}

	public List<CommandDelegate> getCommands() {
		return replayer.getCommands();
	}

	public IStructuredSelection getCurrentCommand() {
		return getCurrentSelection();
	}

	private IStructuredSelection getCurrentSelection() {
		if (replayer.getCurrentCommand() == null) {
			return new StructuredSelection();
		}

		return new StructuredSelection(replayer.getCurrentCommand());
	}

	public int getCurrentStepIndex() {
		CommandDelegate currentCommand = replayer.getCurrentCommand();
		if (currentCommand == null) {
			return -1;
		}
		AuditTrailEntry auditTrailEntry = currentCommand.getAuditTrailEntry();
		List<AuditTrailEntry> entries = graphHandle.getInstance().getEntries();
		return entries.indexOf(auditTrailEntry);
	}

	public long getDurationToNextStep() {
		CommandDelegate currentCommand = replayer.getCurrentCommand();
		if (currentCommand == null) {
			return -1;
		}

		Date timestamp = currentCommand.getAuditTrailEntry().getTimestamp();

		CommandDelegate previousCommand = replayer.getPreviousCommand();
		if (previousCommand == null) {
			long instanceStart = graphHandle.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
			return timestamp.getTime() - instanceStart;
		}

		Date previousTimeStamp = previousCommand.getAuditTrailEntry().getTimestamp();
		return timestamp.getTime() - previousTimeStamp.getTime();
	}

	public Graph getGraph() {
		return graph;
	}

	public IStructuredSelection getLastExecutedCommand() {
		CommandDelegate previousCommand = replayer.getPreviousCommand();
		if (previousCommand == null) {
			return new StructuredSelection();
		}
		return new StructuredSelection(previousCommand);
	}

	public ProcessInstance getProcessInstance() {
		return graphHandle.getInstance();
	}

	public ProcessInstanceDatabaseHandle getProcessInstanceDatabaseHandle() {
		return graphHandle;
	}

	public long getProcessInstanceDatabaseId() {
		return graphHandle.getDatabaseId();
	}

	public CommandReplayer getReplayer() {
		return replayer;
	}

	public long getTimestampOfCurrentCommand() {
		CommandDelegate currentCommand = replayer.getCurrentCommand();
		if (currentCommand == null) {
			return -1;
		}

		Date timestamp = currentCommand.getAuditTrailEntry().getTimestamp();
		return timestamp.getTime();
	}

	public void gotoFirstStep() throws Exception {
		List<CommandDelegate> commands = replayer.getCommands();
		if (commands == null || commands.isEmpty()) {
			return;
		}

		replayer.executeUntil(commands.get(0), new InformListenersCallback());
		// also undo the first command
		previous();
	}

	public void gotoLastStep() throws Exception {
		List<CommandDelegate> commands = replayer.getCommands();
		if (commands == null || commands.isEmpty()) {
			return;
		}

		replayer.executeUntil(commands.get(commands.size() - 1), new InformListenersCallback());
	}

	public boolean hasMoreCommands() {
		return replayer.hasCommands();
	}

	private void initializeAuditTrailEntryIds() {
		try {
			String query = "select database_id from audittrail_entry where process_instance = ? and type != 'GROUP_EVENT_END' order by database_id asc";
			PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement(query);
			statement.setLong(1, graphHandle.getDatabaseId());
			ResultSet resultSet = statement.executeQuery();

			Iterator<CommandDelegate> iterator = replayer.flattenCommands().iterator();
			while (resultSet.next()) {
				long databaseId = resultSet.getLong(1);
				CommandDelegate current = iterator.next();

				entryToDatabaseId.put(current.getAuditTrailEntry(), databaseId);
			}
			if (iterator.hasNext()) {
				System.out.println("problem");
				// iterator.next()
			}

			statement.close();
		} catch (SQLException e) {
			Activator.logError("Could not load the audit trail entries.", e);
		}
	}

	public void jumpBack() throws Exception {
		List<CommandDelegate> commands = replayer.getCommands();
		if (commands == null || commands.isEmpty()) {
			return;
		}

		if (replayer.getCurrentCommand() == null) {
			jumpToIndex(commands, replayer.getCommands().size() - 1 - STEP_SIZE);
			return;
		}

		int currentIndex = commands.indexOf(replayer.getCurrentCommand());
		if (currentIndex < 0) {
			return;
		}
		int index = currentIndex - STEP_SIZE;

		jumpToIndex(commands, index);
	}

	public void jumpForward() throws Exception {
		List<CommandDelegate> commands = replayer.getCommands();
		if (commands == null || commands.isEmpty()) {
			return;
		}
		int currentIndex = commands.indexOf(replayer.getCurrentCommand());
		if (currentIndex < 0) {
			return;
		}
		int index = currentIndex + STEP_SIZE;

		jumpToIndex(commands, index);
	}

	private void jumpToIndex(List<CommandDelegate> commands, int index) throws Exception {
		if (index <= 0) {
			gotoFirstStep();
			return;
		}

		if (index >= replayer.getCommands().size()) {
			gotoLastStep();
			return;
		}

		CommandDelegate command = commands.get(index);
		replayer.executeUntil(command, new InformListenersCallback());
	}

	public void next() {
		CommandDelegate commandToExecute = replayer.getCurrentCommand();
		replayer.executeNextCommand();
		new InformListenersCallback().processed(commandToExecute, true);
	}

	public void previous() {
		CommandDelegate commandToUndo = replayer.getCurrentCommand();
		replayer.undoCommand();
		new InformListenersCallback().processed(commandToUndo, true);
	}

	public void removeCallbackListener(ICommandReplayerCallback callBack) {
		callbacks.remove(callBack);
	}
}
