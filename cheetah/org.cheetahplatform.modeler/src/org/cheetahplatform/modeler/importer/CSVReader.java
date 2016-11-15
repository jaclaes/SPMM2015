package org.cheetahplatform.modeler.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

	private final InputStream input;
	private List<CSVLine> lines;

	public CSVReader(InputStream input) {
		this.input = input;
	}

	public List<String> getColumn(int columnNumber) throws IOException {
		if (lines == null) {
			read();
		}

		List<String> column = new ArrayList<String>();
		for (CSVLine line : lines) {
			column.add(line.get(columnNumber));
		}

		return column;
	}

	public int getColumnCount() throws IOException {
		if (lines == null) {
			read();
		}

		if (lines.isEmpty()) {
			return 0;
		}

		return lines.get(0).size();
	}

	public List<CSVLine> read() throws IOException {
		if (lines != null) {
			return lines;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = reader.readLine();
		lines = new ArrayList<CSVLine>();

		while (line != null) {
			CSVLine currentLine = new CSVLine();
			lines.add(currentLine);

			int currentIndex = 0;
			while (currentIndex < line.length()) {
				int nextIndex = line.indexOf(";", currentIndex);
				if (nextIndex == -1) {
					nextIndex = line.length();
				}

				String token = line.substring(currentIndex, nextIndex);
				currentLine.add(token);
				currentIndex = nextIndex + 1; // skip ";"
			}

			line = reader.readLine();
		}

		input.close();
		return lines;
	}

}
