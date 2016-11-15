package org.cheetahplatform.common.logging.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;

public class DatabaseUtil {

	static final String NULL = "null";
	static final char SEPARATOR = ',';

	/**
	 * Creates the database connection url from the given arguments.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param schema
	 *            the schema to use
	 * @return the database connection url
	 */
	public static String createDatabaseUrl(String host, String port, String schema) {
		StringBuilder databaseURI = new StringBuilder();
		databaseURI.append("jdbc:mysql://"); //$NON-NLS-1$
		databaseURI.append(host);
		databaseURI.append(":"); //$NON-NLS-1$
		databaseURI.append(port);
		databaseURI.append("/"); //$NON-NLS-1$
		databaseURI.append(schema);
		return databaseURI.toString();
	}

	/**
	 * Converts the representation which is stored in the database to attributes.
	 * 
	 * @param data
	 *            The database representation of Attributes
	 * @return a list of attributes
	 */
	public static List<Attribute> fromDataBaseRepresentation(String data) {
		if (data == null) {
			return Collections.emptyList();
		}

		List<Attribute> attributes = new ArrayList<Attribute>();
		int currentIndex = 0;

		while (true) {
			int firstSeparator = data.indexOf(SEPARATOR, currentIndex);
			int secondSeparator = data.indexOf(SEPARATOR, firstSeparator + 1);
			if (firstSeparator == -1) {
				break;
			}

			int nameLength = Integer.parseInt(data.substring(currentIndex, firstSeparator));
			String name = data.substring(secondSeparator + 1, secondSeparator + 1 + nameLength);

			String content = null;
			String contentLengthString = data.substring(firstSeparator + 1, secondSeparator);
			int contentLength = 0;
			if (!NULL.equals(contentLengthString)) {
				contentLength = Integer.parseInt(contentLengthString);
				content = data.substring(secondSeparator + 1 + nameLength, secondSeparator + 1 + nameLength + contentLength);
			}

			attributes.add(new Attribute(name, content));
			currentIndex = secondSeparator + 1 + nameLength + contentLength;
		}

		return attributes;
	}

	static Object nullSafeLength(String string) {
		if (string == null) {
			return NULL;
		}

		return string.length();
	}

	static Object nullSafeString(String string) {
		if (string == null) {
			return "";
		}

		return string;
	}

	public static String toDatabaseRepresentation(Attribute attribute) {
		ArrayList<Attribute> list = new ArrayList<Attribute>(1);
		list.add(attribute);
		return toDatabaseRepresentation(list);
	}

	/**
	 * Converts the attributes to the representation which is stored in the database.
	 * 
	 * @param attributes
	 *            the attributes
	 * @return the database representation
	 */
	public static String toDatabaseRepresentation(List<Attribute> attributes) {
		StringBuilder representation = new StringBuilder();
		for (Attribute attribute : attributes) {
			String name = attribute.getName();
			String content = attribute.getContent();
			representation.append(name.length());
			representation.append(SEPARATOR);
			representation.append(nullSafeLength(content));
			representation.append(SEPARATOR);
			representation.append(name);
			representation.append(nullSafeString(content));
		}

		return representation.toString();
	}

	/**
	 * Converts the attributes to the representation which is stored in the database.
	 * 
	 * @param attributes
	 *            the attributes
	 * @return the database representation
	 */
	public static String toHumanReadableRepresentation(List<Attribute> attributes) {
		StringBuilder representation = new StringBuilder();
		int counter = 0;
		for (Attribute attribute : attributes) {
			String name = attribute.getName();
			String content = attribute.getContent();
			representation.append(name);
			representation.append("='");
			representation.append(nullSafeString(content));
			if (counter < attributes.size())
				representation.append("', ");
			else
				representation.append("';");
			counter++;
		}

		return representation.toString();
	}

}
