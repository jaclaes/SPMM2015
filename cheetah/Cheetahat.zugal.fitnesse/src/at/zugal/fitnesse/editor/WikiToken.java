package at.zugal.fitnesse.editor;

import static org.eclipse.swt.SWT.COLOR_BLACK;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import at.zugal.fitnesse.Activator;
import at.zugal.fitnesse.SWTResourceManager;
import at.zugal.fitnesse.editor.syntax.IWikiSyntax;
import at.zugal.fitnesse.editor.syntax.WikiDefineUsage;
import at.zugal.fitnesse.editor.syntax.WikiHeader1;
import at.zugal.fitnesse.editor.syntax.WikiHeader2;
import at.zugal.fitnesse.editor.syntax.WikiHeader3;
import at.zugal.fitnesse.editor.syntax.WikiLinebreak;
import at.zugal.fitnesse.editor.syntax.WikiPlainText;
import at.zugal.fitnesse.editor.syntax.WikiStart;
import at.zugal.fitnesse.editor.syntax.WikiTableColumnSeparator;

public abstract class WikiToken extends Token {
	public static class DefineUsageToken extends WikiToken {

		public DefineUsageToken() {
			this(null, 0, 0);
		}

		public DefineUsageToken(IDocument document, int offset, int length) {
			super(new TextAttribute(SWTResourceManager.getColor(70, 134, 65)), IWikiSyntax.DEFINE_USAGE, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new DefineUsageToken(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiDefineUsage(parent, this);
		}
	}

	public static class Header1Token extends WikiToken {

		public Header1Token() {
			this(null, 0, 0);
		}

		public Header1Token(IDocument document, int offset, int length) {
			super(new TextAttribute(null, null, SWT.BOLD), IWikiSyntax.HEADER_1, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new Header1Token(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiHeader1(parent, this);
		}

	}

	public static class Header2Token extends WikiToken {

		public Header2Token() {
			this(null, 0, 0);
		}

		public Header2Token(IDocument document, int offset, int length) {
			super(new TextAttribute(null, null, SWT.BOLD), IWikiSyntax.HEADER_2, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new Header2Token(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiHeader2(parent, this);
		}

	}

	public static class Header3Token extends WikiToken {

		public Header3Token() {
			this(null, 0, 0);
		}

		public Header3Token(IDocument document, int offset, int length) {
			super(new TextAttribute(null, null, SWT.BOLD), IWikiSyntax.HEADER_3, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new Header3Token(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiHeader3(parent, this);
		}

	}

	public static class LinebreakToken extends WikiToken {

		public LinebreakToken() {
			this(null, 0, 0);
		}

		public LinebreakToken(IDocument document, int offset, int length) {
			super(new TextAttribute(SWTResourceManager.getColor(SWT.COLOR_BLACK)), IWikiSyntax.LINE_BREAK, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new LinebreakToken(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiLinebreak(parent, this);
		}
	}

	public static class PlainTextToken extends WikiToken {

		public PlainTextToken() {
			this(null, 0, 0);

		}

		public PlainTextToken(IDocument document, int offset, int length) {
			super(new TextAttribute(Display.getDefault().getSystemColor(COLOR_BLACK)), IWikiSyntax.PLAIN_TEXT, document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new PlainTextToken(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiPlainText(parent, this);
		}

	}

	public static class StartToken extends WikiToken {

		public StartToken() {
			this(null, 0, 0);
		}

		public StartToken(IDocument document, int offset, int length) {
			super(new TextAttribute(SWTResourceManager.getColor(SWT.COLOR_BLUE), null, SWT.BOLD), IWikiSyntax.DEFINE_USAGE, document,
					offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new StartToken(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiStart(parent, this);
		}
	}

	public static class TableColumnSeparatorToken extends WikiToken {

		public TableColumnSeparatorToken() {
			this(null, 0, 0);
		}

		public TableColumnSeparatorToken(IDocument document, int offset, int length) {
			super(new TextAttribute(Display.getDefault().getSystemColor(COLOR_BLACK), null, SWT.BOLD), IWikiSyntax.TABLE_COLUMN_SEPARATOR,
					document, offset, length);
		}

		@Override
		public IToken copy(IDocument document, int offset, int length) {
			return new TableColumnSeparatorToken(document, offset, length);
		}

		@Override
		public IWikiSyntax toWikiSyntax(IWikiSyntax parent) {
			return new WikiTableColumnSeparator(parent, this);
		}
	}

	public static String trim(String text) {
		int end = text.length();
		int start = 0;

		while (end > 0 && (text.charAt(end - 1) == ' ' || text.charAt(end - 1) == '\t')) {
			end--;
		}

		while (start < end && start < text.length() && (text.charAt(start) == ' ' || text.charAt(start) == '\t')) {
			start++;
		}

		return text.substring(start, Math.max(0, end));
	}

	/**
	 * Returns the condensed representation of this token, i.e., without leading and trailing whitespaces and trailing line breaks.
	 * 
	 * @return the trimmed version
	 */
	public static String trimLineBreak(String text) {
		int end = text.length();
		int start = 0;

		while (end > 0
				&& end >= start
				&& (text.charAt(end - 1) == ' ' || text.charAt(end - 1) == '\n' || text.charAt(end - 1) == '\r' || text.charAt(end - 1) == '\t')) {
			end--;
		}
		while (start < end && start < text.length() && (text.charAt(start) == ' ' || text.charAt(start) == '\t')) {
			start++;
		}

		return text.substring(start, Math.max(0, end));
	}

	private final String type;
	protected final IDocument document;

	protected final int offset;

	protected final int length;

	public WikiToken(Object data, String type) {
		this(data, type, null, 0, 0);
	}

	public WikiToken(Object data, String type, IDocument document, int offset, int length) {
		super(data);

		this.type = type;
		this.document = document;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Determines whether the token is at the given offset.
	 * 
	 * @param offset
	 *            the offset
	 * @return <code>true</code> if the token is at the given offset, <code>false</code> otherwise
	 */
	public boolean at(int offset) {
		return this.offset <= offset && offset < this.offset + length;
	}

	/**
	 * Create a copy of this token, adding the given offset and length.
	 * 
	 * @param document
	 *            the document for which the token is created
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return a copy
	 */
	public abstract IToken copy(IDocument document, int offset, int length);

	/**
	 * Return the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Return the offset.
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	public String getText() {
		Assert.isNotNull(document, "Document not set");
		Assert.isTrue(offset >= 0);
		Assert.isTrue(length >= 0);
		Assert.isTrue(offset + length <= document.getLength());

		String text = "";
		try {
			text = document.get(offset, length);
		} catch (BadLocationException e) {
			// should not happen, but log anyway
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not trim the content of a token");
			Activator.getDefault().getLog().log(status);
		}

		return text;
	}

	public String getTextUntil(int targetOffset) {
		try {
			return document.get(offset, targetOffset - offset);
		} catch (BadLocationException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not get the content of the token.");
			Activator.getDefault().getLog().log(status);
		}

		return "";
	}

	/**
	 * Return the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}

	/**
	 * Converts the given token into a {@link IWikiSyntax} equivalent.
	 * 
	 * @param parent
	 *            the parent for the object
	 * @return the {@link IWikiSyntax} equivalent
	 */
	public abstract IWikiSyntax toWikiSyntax(IWikiSyntax parent);

	/**
	 * Returns the condensed representation of this token, i.e., without trailing whitespace.
	 * 
	 * @return the trimmed version
	 */
	public String trim() {
		return trim(getText());
	}

	public String trimLineBreak() {
		return trimLineBreak(getText());
	}

}
