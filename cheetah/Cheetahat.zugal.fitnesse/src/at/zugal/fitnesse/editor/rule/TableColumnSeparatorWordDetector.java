package at.zugal.fitnesse.editor.rule;

import org.eclipse.jface.text.rules.IWordDetector;

import at.zugal.fitnesse.Constants;

public class TableColumnSeparatorWordDetector implements IWordDetector {

	@Override
	public boolean isWordPart(char c) {
		return false;
	}

	@Override
	public boolean isWordStart(char c) {
		return c == Constants.TABLE_COLUMN_SEPARATOR_CHAR;
	}

}
