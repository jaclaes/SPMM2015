package commonServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class StringTools {

	public static String fillStringIntoStringBefore(String wholeString, String delemiterString, String mySubString) {
		StringBuilder contents = new StringBuilder();
		contents.append(wholeString.substring(0, wholeString.indexOf(delemiterString)));
		contents.append(System.getProperty("line.separator"));
		contents.append(mySubString);
		contents.append(System.getProperty("line.separator"));
		contents.append(wholeString.substring(wholeString.indexOf(delemiterString), wholeString.length()));

		return contents.toString();
	}

	public static String fillStringIntoStringAfter(String wholeString, String delemiterString, String mySubString) {
		StringBuilder contents = new StringBuilder();
		contents.append(wholeString.substring(0, wholeString.indexOf(delemiterString) + delemiterString.length()));
		contents.append(System.getProperty("line.separator"));
		contents.append(mySubString);
		contents.append(System.getProperty("line.separator"));
		contents.append(wholeString.substring(wholeString.indexOf(delemiterString) + delemiterString.length(), wholeString.length()));
		return contents.toString();
	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This style of implementation does not throw Exceptions to the
	 * caller.
	 * 
	 * @param aFile
	 *            is a file which already exists and can be read.
	 */
	static public String getContents(File aFile) {
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

	/**
	 * Change the contents of text file in its entirety, overwriting any existing text.
	 * 
	 * This style of implementation throws all exceptions to the caller.
	 * 
	 * @param aFile
	 *            is an existing file which can be written to.
	 * @throws IllegalArgumentException
	 *             if param does not comply.
	 * @throws FileNotFoundException
	 *             if the file does not exist.
	 * @throws IOException
	 *             if problem encountered during write.
	 */
	static public void setContents(File aFile, String aContents) throws FileNotFoundException, IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: " + aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + aFile);
		}

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(aFile));
		try {
			// FileWriter always assumes default encoding is OK!
			output.write(aContents);
		} finally {
			output.close();
		}
	}

	/**
	 * Read the ProjectName which is the same as the Filename of the acmodel File
	 * 
	 * @param pathToXmlFile
	 *            - the absolute path to the xml File which will be parsed
	 */
	public static String readProjectName(String pathToXmlFile) {
		// if (!(pathToXmlFile.contains("<") && pathToXmlFile.contains(">"))){
		// if (pathToXmlFile.contains("\\")){
		// pathToXmlFile = pathToXmlFile.replace('\\','/');
		// String [] slashSplits = pathToXmlFile.split( "/" );
		// String [] pointSplits = slashSplits[ slashSplits.length -1 ].split(".acm");
		// return pointSplits[0];
		// }
		// else
		// return "TestProject";
		// }
		// else
		return "TestProject";
	}
}
