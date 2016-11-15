package at.zugal.fitnesse.editor.syntax;

import static at.zugal.fitnesse.editor.syntax.IWikiSyntax.TABLE_COLUMN_SEPARATOR;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;

import at.zugal.fitnesse.editor.WikiToken;

public class WikiParser {
	private final ITokenScanner scanner;
	private WikiDocument document;
	private List<WikiToken> tokens;

	public WikiParser(ITokenScanner scanner) {
		this.scanner = scanner;
	}

	/**
	 * Determine whether the coming line does not include a '|'.
	 * 
	 * @param i
	 *            the current index
	 * @return <code>true</code> if the next line does not contain '|', <code>false</code> if it is the last line of the document or
	 *         contains '|'
	 */
	private boolean checkForEmptyLine(int i) {
		Assert.isTrue(tokens.get(i).getType().equals(IWikiSyntax.LINE_BREAK));
		i++;

		for (int index = i; index < tokens.size(); index++) {
			WikiToken token = tokens.get(index);

			if (token.getType().equals(IWikiSyntax.TABLE_COLUMN_SEPARATOR)) {
				return false;
			} else if (token.getType().equals(IWikiSyntax.LINE_BREAK)) {
				return true;
			}
		}

		return true;
	}

	private WikiTableCell createTableRowAndCell(IWikiSyntax context) {
		WikiTableRow row = new WikiTableRow(context);
		context.addChild(row);
		WikiTableCell cell = new WikiTableCell(row);
		row.addChild(cell);
		return cell;
	}

	private List<WikiToken> createTokens() {
		List<WikiToken> tokens = new ArrayList<WikiToken>();

		IToken token = scanner.nextToken();
		while (!token.isEOF()) {
			tokens.add((WikiToken) token);
			token = scanner.nextToken();
		}

		return tokens;
	}

	private IWikiSyntax handleLineBreak(IWikiSyntax context, int i, WikiToken token) {
		String type = context.getType();
		context.addChild(token.toWikiSyntax(context));

		if (type.equals(IWikiSyntax.TABLE) || type.equals(IWikiSyntax.TABLE_ROW) || type.equals(IWikiSyntax.TABLE_CELL)) {
			if (checkForEmptyLine(i)) {
				while (!context.getType().equals(IWikiSyntax.DOCUMENT)) {
					context = context.getParent();
				}
			} else {
				while (!context.getType().equals(IWikiSyntax.TABLE)) {
					context = context.getParent();
				}
			}
		}

		return context;
	}

	private IWikiSyntax handleTableColumnSeparator(IWikiSyntax context, int i, WikiToken token) {
		String type = context.getType();

		if (type.equals(IWikiSyntax.DOCUMENT)) {
			WikiTable table = new WikiTable(context);
			context.addChild(table);
			WikiTableCell cell = createTableRowAndCell(table);

			cell.addChild(token.toWikiSyntax(cell));
			context = cell;
		} else if (type.equals(IWikiSyntax.TABLE)) {
			WikiTableCell cell = createTableRowAndCell(context);

			cell.addChild(token.toWikiSyntax(cell));
			context = cell;
		} else if (type.equals(IWikiSyntax.TABLE_ROW)) {
			WikiTableCell cell = new WikiTableCell(context);
			context.addChild(cell);
			cell.addChild(token.toWikiSyntax(cell));
			context = cell;
		} else if (type.equals(IWikiSyntax.TABLE_CELL)) {
			context.addChild(token.toWikiSyntax(context));

			// add a new cell if we are not at the end of the line
			if (i < tokens.size() - 1 && !tokens.get(i + 1).getType().equals(IWikiSyntax.LINE_BREAK)) {
				IWikiSyntax row = context.getParent();
				WikiTableCell cell = new WikiTableCell(row);
				row.addChild(cell);
				context = cell;
			}
		}
		return context;
	}

	public WikiDocument parse() {
		if (document == null) {
			parseDocument();
		}

		return document;
	}

	private void parseDocument() {
		document = new WikiDocument();
		tokens = createTokens();
		IWikiSyntax context = document;

		for (int i = 0; i < tokens.size(); i++) {
			WikiToken token = tokens.get(i);

			if (token.getType().equals(TABLE_COLUMN_SEPARATOR)) {
				context = handleTableColumnSeparator(context, i, token);
			} else if (token.getType().equals(IWikiSyntax.LINE_BREAK)) {
				context = handleLineBreak(context, i, token);
			} else {
				context.addChild(token.toWikiSyntax(context));
			}
		}
	}
}
