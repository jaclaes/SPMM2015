package at.zugal.fitnesse.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.zugal.fitnesse.editor.formatting.WikiTableFormattingStrategy;

public class WikiTableFormattingStrategyTest {
	private WikiTableFormattingStrategy strategy = new WikiTableFormattingStrategy();

	@Test
	public void bug48() throws Exception {
		String content = "|111|\n|222|invalidText\n|3333|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|111 |\n|222 |invalidText\n|3333|", formatted);
	}

	@Test
	public void bug53() throws Exception {
		String content = "|123456|\r\n" + "|1234|123456|\r\n" + "|createAndJoin|Andjoin|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|123456               |\r\n" + "|1234         |123456 |\r\n" + "|createAndJoin|Andjoin|", formatted);
	}

	@Test
	public void bug57() throws Exception {
		String content = "|${IMPERATIVE_SESE_FIXTURE} |	\r\n" + "|start |	${IMPERATIVE_SESE_FIXTURE} 	|\r\n"
				+ "|children |	component |	1 	|\r\n" + "|store |	component[0] 	|polygon 	|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|${IMPERATIVE_SESE_FIXTURE}          |\r\n" + "|start    |${IMPERATIVE_SESE_FIXTURE}|\r\n"
				+ "|children |component    |1           |\r\n" + "|store    |component[0] |polygon     |", formatted);
	}

	@Test
	public void formatEmpty() throws Exception {
		String content = "";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals(content, formatted);
	}

	@Test
	public void formatHeader() throws Exception {
		String content = "|Simple Header    |";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|Simple Header|", formatted);
	}

	@Test
	public void formatHeaderLonger() throws Exception {
		String content = "|${IMPERATIVE_CREATION_FIXTURE}      |\r\n" + "|start|${IMPERATIVE_CREATION_FIXTURE}|\r\n"
				+ "|createAndJoin|Andjoin               |\r\n" + "|link    |Start     |Andsplit        |";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|${IMPERATIVE_CREATION_FIXTURE}                |\r\n" + "|start          |${IMPERATIVE_CREATION_FIXTURE}|\r\n"
				+ "|createAndJoin  |Andjoin                       |\r\n" + "|link           |Start    |Andsplit            |", formatted);
	}

	@Test
	public void formatMultiLine() throws Exception {
		String content = "|123456                   |\r\n" + "|start            |123456 |\r\n" + "|createAn       |Andjoin  |\r\n"
				+ "|createAndblablalb|Andjoin|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|123456                   |\r\n" + "|start            |123456 |\r\n" + "|createAn         |Andjoin|\r\n"
				+ "|createAndblablalb|Andjoin|", formatted);
	}

	@Test
	public void formatMultiLineNullPointer() throws Exception {
		String content = "|123456                   |asd|asdas|asdas\r\n" + "|start            |123456 |\r\n"
				+ "|createAn       |Andjoin  |\r\n" + "|createAndblablalb|Andjoin|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|123456|asd|asdas         |asdas\r\n" + "|start            |123456 |\r\n" + "|createAn         |Andjoin|\r\n"
				+ "|createAndblablalb|Andjoin|", formatted);
	}

	@Test
	public void formatTwoHeaderColumns() throws Exception {
		String content = "|Simple Header               |1234|\n|Second Line  |12345678|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|Simple Header|1234  |\n|Second Line|12345678|", formatted);
	}

	@Test
	public void formatTwoLinesHeader() throws Exception {
		String content = "|Simple Header    |\n|Second Line|";
		String formatted = strategy.format(content, true, "", new int[0]);

		assertEquals("|Simple Header|\n|Second Line  |", formatted);
	}

}
