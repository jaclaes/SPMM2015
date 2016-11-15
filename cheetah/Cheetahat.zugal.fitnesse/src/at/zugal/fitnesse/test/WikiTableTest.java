package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.zugal.fitnesse.editor.syntax.WikiTable;

public class WikiTableTest {
	@Test
	public void countTableColumns1() throws Exception {
		WikiTable table = TestHelper.parseTable("|header|");
		assertEquals(1, table.countTableColumns());
	}

	@Test
	public void countTableColumns2() throws Exception {
		WikiTable table = TestHelper.parseTable("|header|secondColumn|");
		assertEquals(2, table.countTableColumns());
	}

	@Test
	public void countTableColumnsBug48() throws Exception {
		WikiTable table = TestHelper.parseTable("|111|\n|222|invalidText\n|3333|");

		assertEquals(1, table.countTableColumns());
	}

	@Test
	public void countTableColumnsMultiline() throws Exception {
		WikiTable table = TestHelper.parseTable("|header|secondColumn|\n|secondHeaderRow|someText|");
		assertEquals(2, table.countTableColumns());
	}

	@Test
	public void getColumnLengthInvalidCell1() throws Exception {
		WikiTable table = TestHelper.parseTable("|12|\n|12|\n|bla blu|soso");
		int length = table.getMaximumCellLength(0);
		assertEquals(9, length);
	}

	@Test
	public void getColumnLengthInvalidCell2() throws Exception {
		WikiTable table = TestHelper.parseTable("|12|\n|12|\n|bla blu");
		assertEquals(4, table.getMaximumCellLength(0));
	}

	@Test
	public void getColumnLengthInvalidCell3() throws Exception {
		WikiTable table = TestHelper.parseTable("|12|\n|12|\n|1234|\n|1234|");
		assertEquals(6, table.getMaximumCellLength(0));
	}

	@Test
	public void getHeaderLengthInvalidText() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|56");
		assertEquals(6, table.getHeaderLength());
	}

	@Test
	public void getHeaderLengthSingleHeaderRow1() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234  |56|");
		assertEquals(9, table.getHeaderLength());
	}

	@Test
	public void getHeaderLengthSingleHeaderRow2() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|\n|12|");
		assertEquals(6, table.getHeaderLength());
	}

	@Test
	public void getMaximumCellLength() throws Exception {
		String content = "|${IMPERATIVE_CREATION_FIXTURE}      |\r\n" + "|start|${IMPERATIVE_CREATION_FIXTURE}|\r\n"
				+ "|createAndJoin|Andjoin               |\r\n" + "|link    |Start     |Andsplit        |";
		WikiTable table = TestHelper.parseTable(content);

		assertEquals(15, table.getMaximumCellLength(0));
		assertEquals(8, table.getMaximumCellLength(1));
		assertEquals(9, table.getMaximumCellLength(2));
	}

	@Test
	public void getTableColumnSize1() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|\n|sadfsadfsdf|\n|12345|");
		assertEquals(13, table.getMaximumCellLength(0));
	}

	@Test
	public void getTableColumnSize2() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|\n|sadfsadfsdf|\n|1234|123|");
		assertEquals(4, table.getMaximumCellLength(1));
	}

	@Test
	public void getTableColumnSizeIgnoreHeader() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|");
		assertEquals("Header should not influence this computation", 0, table.getMaximumCellLength(0));
	}

	@Test
	public void getTableColumnSizeIgnoreMultiRowHeader() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|\n|12|");
		assertEquals("First header line should not influence this computation", 4, table.getMaximumCellLength(0));
	}

	@Test
	public void getTableColumnSizeLeadingWhiteSpace() throws Exception {
		WikiTable table = TestHelper.parseTable("|12|\n|12|\n|  1234     |");
		assertEquals(6, table.getMaximumCellLength(0));
	}

	@Test
	public void getTableColumnSizeWhiteSpace() throws Exception {
		WikiTable table = TestHelper.parseTable("|1|\n|12|\n|1234     |");
		assertEquals(6, table.getMaximumCellLength(0));
	}

	@Test
	public void tableColumnSizeBug48() throws Exception {
		WikiTable table = TestHelper.parseTable("|111|\n|222|invalidText\n|3333|");
		assertEquals(5, table.getHeaderLength());
	}

	@Test
	public void tableHeaderSize() throws Exception {
		WikiTable table = TestHelper.parseTable("|invalid");
		assertEquals(0, table.getHeaderLength());
	}

	@Test
	public void tableHeaderSize2() throws Exception {
		WikiTable table = TestHelper.parseTable("|1234|\n|123456|");
		assertEquals(8, table.getHeaderLength());
	}

}
