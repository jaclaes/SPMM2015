package at.zugal.fitnesse.editor.syntax;

/**
 * Interface for clients visiting {@link IWikiSyntax} syntax trees. The following visiting ordering is obeyed: visitXXXStart - visit all
 * Children - visitXXXEnd.
 * 
 * @author user
 * 
 */
public interface IWikiSyntaxVisitor {
	void visitDefineUsage(WikiDefineUsage defineUsage);

	void visitDocumentEnd(WikiDocument document);

	void visitDocumentStart(WikiDocument document);

	void visitHeader1(WikiHeader1 header1);

	void visitHeader2(WikiHeader2 header2);

	void visitHeader3(WikiHeader3 header3);

	void visitLinebreak(WikiLinebreak linebreak);

	void visitPlainText(WikiPlainText plainText);

	void visitStart(WikiStart start);

	void visitTableCellEnd(WikiTableCell cell);

	void visitTableCellStart(WikiTableCell cell);

	void visitTableColumnSeparator(WikiTableColumnSeparator tableColumnSeparator);

	void visitTableEnd(WikiTable table);

	void visitTableRowEnd(WikiTableRow row);

	void visitTableRowStart(WikiTableRow row);

	void visitTableStart(WikiTable table);
}
