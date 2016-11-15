package org.cheetahplatform.modeler.graph.mapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.graphics.RGB;

public class ParagraphProvider {
	private static List<Paragraph> PARAGRAPHS;
	private static final String COLOR_SEPARATOR = ",";

	static {
		PARAGRAPHS = new ArrayList<Paragraph>();

		try {
			Statement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection().createStatement();
			ResultSet resultSet = statement
					.executeQuery("select database_id, process, description, possible_activity_names, color, default_model_element from paragraph;");

			while (resultSet.next()) {
				long id = resultSet.getLong(1);
				String process = resultSet.getString(2);
				String description = resultSet.getString(3);
				Set<String> possibleActivityNames = activityNamesFromString(resultSet.getString(4));
				RGB color = colorFromString(resultSet.getString(5));
				long defaultModelElement = resultSet.getLong(6);

				Paragraph paragraph = new Paragraph(id, process, description, color, defaultModelElement);
				PARAGRAPHS.add(paragraph);
				for (String activityName : possibleActivityNames) {
					paragraph.addPossibleActivityName(activityName);
				}
			}

			statement.close();
		} catch (SQLException e) {
			Activator.logError("Could not load the paragraphs.", e);
		}
	}

	private static Set<String> activityNamesFromString(String string) {
		List<Attribute> possibleActivityNames = DatabaseUtil.fromDataBaseRepresentation(string);
		Set<String> activityNames = new HashSet<String>();
		for (Attribute attribute : possibleActivityNames) {
			activityNames.add(attribute.getContent());
		}

		return activityNames;
	}

	public static void addParagraph(Paragraph paragraph) {
		PARAGRAPHS.add(paragraph);
	}

	private static RGB colorFromString(String string) {
		String[] rgb = string.split(COLOR_SEPARATOR);
		int red = Integer.parseInt(rgb[0]);
		int green = Integer.parseInt(rgb[1]);
		int blue = Integer.parseInt(rgb[2]);

		return new RGB(red, green, blue);
	}

	private static String colorToString(Paragraph paragraph) {
		RGB color = paragraph.getColor();
		String colorString = color.red + COLOR_SEPARATOR + color.green + COLOR_SEPARATOR + color.blue;
		return colorString;
	}

	public static Paragraph getParagraph(long paragraphId) {
		for (Paragraph paragraph : PARAGRAPHS) {
			if (paragraph.getId() == paragraphId) {
				return paragraph;
			}
		}

		throw new AssertionFailedException("Could not find a paragraph for the id: " + paragraphId);
	}

	public static Paragraph getParagraph(String processId, long modelElementId) {
		List<Paragraph> processParagraphs = getParagraphs(processId);
		for (Paragraph paragraph : processParagraphs) {
			if (paragraph.getModelElementId() == modelElementId) {
				return paragraph;
			}
		}

		return null;
	}

	public static Paragraph getParagraph(String process, String description) {
		for (Paragraph paragraph : getParagraphs(process)) {
			if (paragraph.getDescription().equals(description)) {
				return paragraph;
			}
		}

		return null;
	}

	public static List<Paragraph> getParagraphs(String process) {
		List<Paragraph> matching = new ArrayList<Paragraph>();
		for (Paragraph paragraph : PARAGRAPHS) {
			if (paragraph.getProcess().equals(process)) {
				matching.add(paragraph);
			}
		}

		return matching;
	}

	public static Set<String> getProcesses() {
		Set<String> processes = new HashSet<String>();
		for (Paragraph paragraph : PARAGRAPHS) {
			processes.add(paragraph.getProcess());
		}

		return processes;
	}

	protected static String namesToString(Paragraph paragraph) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (String name : paragraph.getPossibleActivityNames()) {
			attributes.add(new Attribute("", name));
		}

		return DatabaseUtil.toDatabaseRepresentation(attributes);
	}

	public static void save() throws SQLException {
		Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
		for (Paragraph paragraph : PARAGRAPHS) {
			if (!update(connection, paragraph)) {
				save(connection, paragraph);
			}
		}
	}

	public static void save(Connection connection, Paragraph paragraph) throws SQLException {
		String sql = "insert into paragraph (database_id, process, description, possible_activity_names, color, default_model_element) values (?, ?, ?, ?, ?, ?);";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, paragraph.getId());
		statement.setString(2, paragraph.getProcess());
		statement.setString(3, paragraph.getDescription());
		statement.setString(4, namesToString(paragraph));
		statement.setString(5, colorToString(paragraph));
		statement.setLong(6, paragraph.getModelElementId());
		statement.execute();

		statement.close();
	}

	private static boolean update(Connection connection, Paragraph paragraph) throws SQLException {
		String sql = "update paragraph set process = ?, description = ?, possible_activity_names = ?, color = ?, default_model_element = ? where database_id = ?;";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, paragraph.getProcess());
		statement.setString(2, paragraph.getDescription());
		statement.setString(3, namesToString(paragraph));
		statement.setString(4, colorToString(paragraph));
		statement.setLong(5, paragraph.getModelElementId());
		statement.setLong(6, paragraph.getId());
		int updateCount = statement.executeUpdate();
		statement.close();

		return updateCount > 0;
	}
}
