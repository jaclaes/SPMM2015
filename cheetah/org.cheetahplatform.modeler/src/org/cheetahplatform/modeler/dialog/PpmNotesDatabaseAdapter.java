package org.cheetahplatform.modeler.dialog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cheetahplatform.common.logging.DatabaseAuditTrailEntry;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.model.PpmNote;
import org.eclipse.core.runtime.Assert;
import org.hsqldb.types.Types;

public class PpmNotesDatabaseAdapter {
	private PreparedStatement updateStatement;
	private PreparedStatement insertStatement;
	private PreparedStatement insertAuditTrailEntryStatement;
	private PreparedStatement deleteAudittrailEntriesStatement;

	private final List<CommandDelegate> commands;
	private final long databaseId;
	private List<PpmNote> notes;
	private PreparedStatement deletePpmNoteStatement;

	public PpmNotesDatabaseAdapter(List<CommandDelegate> commands, long databaseId) {
		Assert.isLegal(databaseId > 0);
		this.databaseId = databaseId;
		Assert.isNotNull(commands);
		this.commands = commands;
		createInsertStatements();
		createUpdateStatements();

		try {
			deletePpmNoteStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("DELETE FROM ppmnote WHERE id=?");
		} catch (SQLException e) {
			Activator.logError("Unable to create delete statement", e);
		}

		loadPpmNotes();
	}

	private void createInsertStatements() {
		try {
			insertStatement = org.cheetahplatform.common.Activator
					.getDatabaseConnector()
					.getDatabaseConnection()
					.prepareStatement(
							"INSERT INTO ppmnote (text, startTime, endTime, category, originator, parent, processInstance) VALUES (?,?,?,?,?,?,?)");
		} catch (SQLException e) {
			Activator.logError("Unable to create insert statement", e);
		}
		try {
			insertAuditTrailEntryStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("INSERT INTO audittrailentry_for_note (ppmnote, entryid) VALUES (?,?)");
		} catch (SQLException e) {
			Activator.logError("Unable to create insert audittrail entry statement", e);
		}
	}

	private void createUpdateStatements() {
		try {
			updateStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("UPDATE ppmnote SET startTime=?, endTime=?, originator=?, text=?, category=? WHERE id=?");
		} catch (SQLException e) {
			Activator.logError("Unable to create update statement", e);
		}
		try {
			deleteAudittrailEntriesStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("DELETE FROM audittrailentry_for_note WHERE ppmnote=?");
		} catch (SQLException e) {
			Activator.logError("Unable to create delete statement", e);
		}
	}

	public void deleteNote(PpmNote toDelete) {
		try {
			deletePpmNoteStatement.setLong(1, toDelete.getId());
			deletePpmNoteStatement.execute();
		} catch (SQLException e) {
			Activator.logError("Unable to delete PPM Note", e);
		}
	}

	private CommandDelegate findAuditTrailEntryForId(long auditTrailEntryId) {
		for (CommandDelegate commandDelegate : commands) {
			Collection<? extends CommandDelegate> flatten = commandDelegate.flatten();
			for (CommandDelegate flattened : flatten) {
				DatabaseAuditTrailEntry auditTrailEntry = (DatabaseAuditTrailEntry) flattened.getAuditTrailEntry();
				long id = auditTrailEntry.getId();
				if (auditTrailEntryId == id) {
					return commandDelegate;
				}
			}

		}

		throw new IllegalStateException("Could not find audittrail entry");
	}

	public void insertAuditTrailEntries(PpmNote note) throws SQLException {
		List<CommandDelegate> commands = note.getCommands();
		for (CommandDelegate commandDelegate : commands) {
			DatabaseAuditTrailEntry auditTrailEntry = (DatabaseAuditTrailEntry) commandDelegate.getAuditTrailEntry();
			long id = auditTrailEntry.getId();
			insertAuditTrailEntryStatement.setLong(1, note.getId());
			insertAuditTrailEntryStatement.setLong(2, id);
			insertAuditTrailEntryStatement.execute();
		}
	}

	public void insertNote(PpmNote note, PpmNote parentPpmNote, long processInstanceDatabaseId) {
		try {
			insertStatement.setString(1, note.getText());
			insertStatement.setString(2, String.valueOf(note.getStartTime().getTime()));
			if (note.getEndTime() != null) {
				insertStatement.setString(3, String.valueOf(note.getEndTime().getTime()));
			} else {
				insertStatement.setNull(3, Types.VARCHAR);
			}

			IPpmNoteCategory category = note.getCategory();
			if (category != null) {
				insertStatement.setLong(4, category.getId());
			} else {
				insertStatement.setNull(4, Types.BIGINT);
			}
			insertStatement.setString(5, note.getOriginator());
			if (parentPpmNote != null) {
				insertStatement.setLong(6, parentPpmNote.getId());
			} else {
				insertStatement.setNull(6, Types.BIGINT);
			}

			insertStatement.setLong(7, processInstanceDatabaseId);

			insertStatement.execute();
			ResultSet generatedKeys = insertStatement.getGeneratedKeys();
			while (generatedKeys.next()) {
				long id = generatedKeys.getLong(1);
				Assert.isLegal(id > 0);
				Assert.isLegal(note.getId() == 0);
				note.setId(id);
			}
			generatedKeys.close();

			insertAuditTrailEntries(note);
		} catch (SQLException e) {
			Activator.logError("Unable to store PPM Note", e);
		}
	}

	private void loadAudittrailEntries(PpmNote ppmNote, long id) throws SQLException {
		PreparedStatement loadAuditTrailEntries = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
				.prepareStatement("select entryid from audittrailentry_for_note where ppmnote=?");

		loadAuditTrailEntries.setLong(1, id);

		ResultSet auditTrailEntryResultSet = loadAuditTrailEntries.executeQuery();
		while (auditTrailEntryResultSet.next()) {
			long auditTrailEntryId = auditTrailEntryResultSet.getLong(1);
			CommandDelegate command = findAuditTrailEntryForId(auditTrailEntryId);
			ppmNote.addCommand(command);
		}
		auditTrailEntryResultSet.close();
	}

	public List<PpmNote> loadPpmNotes() {
		if (notes == null) {
			notes = new ArrayList<PpmNote>();
			try {

				PreparedStatement statement = org.cheetahplatform.common.Activator
						.getDatabaseConnector()
						.getDatabaseConnection()
						.prepareStatement(
								"select id, text, startTime, endTime, category, originator, parent from ppmnote where processInstance=?");

				statement.setLong(1, databaseId);

				ResultSet resultSet = statement.executeQuery();

				Map<Long, PpmNote> noteCache = new HashMap<Long, PpmNote>();
				HashMap<PpmNote, Long> parentMap = new HashMap<PpmNote, Long>();

				while (resultSet.next()) {
					long id = resultSet.getLong(1);
					String text = resultSet.getString(2);
					String startTime = resultSet.getString(3);
					String endTime = resultSet.getString(4);
					long categoryId = resultSet.getLong(5);
					String originator = resultSet.getString(6);
					long parent = resultSet.getLong(7);

					IPpmNoteCategory category = PpmNoteCategoryProvider.getInstance().getCategory(categoryId);

					PpmNote ppmNote = new PpmNote();
					ppmNote.setId(id);
					ppmNote.setText(text);
					ppmNote.setOriginator(originator);
					ppmNote.setStartTime(new Date(Long.parseLong(startTime)));
					if (endTime != null) {
						ppmNote.setEndTime(new Date(Long.parseLong(endTime)));
					}
					ppmNote.setCategory(category);

					noteCache.put(id, ppmNote);

					loadAudittrailEntries(ppmNote, id);

					if (parent == 0) {
						notes.add(ppmNote);
						continue;
					}

					parentMap.put(ppmNote, parent);
				}

				for (Entry<PpmNote, Long> entry : parentMap.entrySet()) {
					long parent = entry.getValue();
					PpmNote parentNote = noteCache.get(parent);

					parentNote.addComment(entry.getKey());
				}

				resultSet.close();
			} catch (SQLException e) {
				Activator.logError("Unable to load PPM Notes", e);
			}
		}

		return notes;
	}

	public void updateNote(PpmNote note) {
		try {
			updateStatement.setString(1, String.valueOf(note.getStartTime().getTime()));
			if (note.getEndTime() != null) {
				updateStatement.setString(2, String.valueOf(note.getEndTime().getTime()));
			} else {
				updateStatement.setNull(2, Types.VARCHAR);
			}

			updateStatement.setString(3, note.getOriginator());
			updateStatement.setString(4, note.getText());
			IPpmNoteCategory category = note.getCategory();
			if (category != null) {
				updateStatement.setLong(5, category.getId());
			} else {
				updateStatement.setNull(5, Types.BIGINT);
			}

			updateStatement.setLong(6, note.getId());
			updateStatement.execute();

			deleteAudittrailEntriesStatement.setLong(1, note.getId());
			deleteAudittrailEntriesStatement.execute();
			insertAuditTrailEntries(note);

		} catch (SQLException e) {
			Activator.logError("Unable to update PPM Note", e);
		}
	}
}
