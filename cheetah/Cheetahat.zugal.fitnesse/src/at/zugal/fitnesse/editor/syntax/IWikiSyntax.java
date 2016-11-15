package at.zugal.fitnesse.editor.syntax;

import java.util.List;

public interface IWikiSyntax {

	// intermediate nodes
	public static final String DOCUMENT = "document";
	public static final String TABLE = "table";
	public static final String TABLE_ROW = "table_row";
	public static final String TABLE_CELL = "table_cell";

	// leafs
	public static final String TABLE_COLUMN_SEPARATOR = "table_column_separator";
	public static final String DEFINE_USAGE = "define_usage";
	public static final String PLAIN_TEXT = "plain_text";
	public static final String ACTION_FIXTURE_START = "action_fixture_start";
	public static final String START = "start";
	public static final String LINE_BREAK = "line_break";
	public static final String HEADER_1 = "header_1";
	public static final String HEADER_2 = "header_2";
	public static final String HEADER_3 = "header_3";

	/**
	 * Accept the given visitor.
	 * 
	 * @param visitor
	 *            the visitor
	 */
	void accept(IWikiSyntaxVisitor visitor);

	/**
	 * Adds a child.
	 * 
	 * @param child
	 *            the child to add
	 */
	void addChild(IWikiSyntax child);

	/**
	 * Returns the object's children.
	 * 
	 * @return the children
	 */
	List<IWikiSyntax> getChildren();

	int getLength();

	int getOffset();

	/**
	 * Return the parent object.
	 * 
	 * @return the parent
	 */
	IWikiSyntax getParent();

	/**
	 * Return the syntax associated with the given offset. Return the lowest level object, e.g., within a table not the table but the
	 * content is returned.
	 * 
	 * @param offset
	 *            the offset
	 * @return the syntax at the given offset
	 */
	IWikiSyntax getSyntaxAt(int offset);

	/**
	 * Returns the text of the object.
	 * 
	 * @return the text
	 */
	String getText();

	/**
	 * Returns the content of the syntax element until the given offset (inclusive).
	 * 
	 * @param offset
	 *            the offset
	 * @return the content of the syntax to this offset
	 */
	String getTextUntil(int offset);

	/**
	 * Returns the type of the object.
	 * 
	 * @return the type
	 */
	String getType();

	/**
	 * Returns the trimmed string content of this object, i.e., remove all trailing whitespaces.
	 * 
	 * @return the trimmed string content
	 */
	String trim();

	/**
	 * Returns the trimmed string content of this object, i.e., remove all trailing whitespaces <b> and line break</b>.
	 * 
	 * @return the trimmed string content
	 */
	String trimLineBreak();

}
