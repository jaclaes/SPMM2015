package at.zugal.fitnesse.editor.syntax;

public abstract class AbstractWikiSyntaxVisitor implements IWikiSyntaxVisitor {

	@Override
	public void visitDefineUsage(WikiDefineUsage defineUsage) {
		// do nothing
	}

	@Override
	public void visitDocumentEnd(WikiDocument document) {
		// do nothing
	}

	@Override
	public void visitDocumentStart(WikiDocument document) {
		// do nothing
	}

	@Override
	public void visitHeader1(WikiHeader1 header1) {
		// do nothing
	}

	@Override
	public void visitHeader2(WikiHeader2 header2) {
		// do nothing
	}

	@Override
	public void visitHeader3(WikiHeader3 header3) {
		// do nothing
	}

	@Override
	public void visitLinebreak(WikiLinebreak linebreak) {
		// do nothing
	}

	@Override
	public void visitPlainText(WikiPlainText plainText) {
		// do nothing
	}

	@Override
	public void visitStart(WikiStart start) {
		// do nothing
	}

	@Override
	public void visitTableCellEnd(WikiTableCell cell) {
		// do nothing
	}

	@Override
	public void visitTableCellStart(WikiTableCell cell) {
		// do nothing
	}

	@Override
	public void visitTableColumnSeparator(WikiTableColumnSeparator tableColumnSeparator) {
		// do nothing
	}

	@Override
	public void visitTableEnd(WikiTable table) {
		// do nothing
	}

	@Override
	public void visitTableRowEnd(WikiTableRow row) {
		// do nothing
	}

	@Override
	public void visitTableRowStart(WikiTableRow row) {
		// do nothing
	}

	@Override
	public void visitTableStart(WikiTable table) {
		// do nothing
	}

}
