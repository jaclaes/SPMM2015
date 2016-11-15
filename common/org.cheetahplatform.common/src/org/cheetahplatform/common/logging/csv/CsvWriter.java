package org.cheetahplatform.common.logging.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.logging.Attribute;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public class CsvWriter {

	private BufferedWriter writer;
	private final List<IProcessInformation> processInformationList;
	private List<String> attributeNames;

	public CsvWriter(List<IProcessInformation> processInformationList) {
		Assert.isNotNull(processInformationList);
		this.processInformationList = processInformationList;
		attributeNames = extractHeaders(processInformationList);
	}

	/**
	 * Extracts the headers for the csv file by extracting all possible attributes.
	 * 
	 * @param processInformationList
	 *            a list of {@link IProcessInformation}.
	 * @return a list of Strings
	 */
	private List<String> extractHeaders(List<IProcessInformation> processInformationList) {
		List<String> headers = new ArrayList<String>();

		for (IProcessInformation processInformation : processInformationList) {
			List<Attribute> attributes = processInformation.getAttributes();
			for (Attribute attribute : attributes) {
				String attributeName = attribute.getName().trim();
				if (!headers.contains(attributeName)) {
					headers.add(attributeName);
				}
			}
		}

		return headers;
	}

	/**
	 * Returns the attributeNames.
	 * 
	 * @return the attributeNames
	 */
	public List<String> getAttributeNames() {
		return attributeNames;
	}

	/**
	 * Sets the attributeNames.
	 * 
	 * @param attributeNames
	 *            the attributeNames to set
	 */
	public void setAttributeNames(List<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

	public void write(File target, IProgressMonitor monitor) throws IOException {
		write(target, monitor, false);
	}

	public void write(File file, IProgressMonitor monitor, boolean writeLegend) throws IOException {
		if (monitor != null) {
			monitor.beginTask("Exporting to CSV", processInformationList.size() + 1);
		}
		writer = new BufferedWriter(new FileWriter(file));

		List<String> headers = getAttributeNames();
		writeHeaderLine(headers);

		for (IProcessInformation information : processInformationList) {
			writer.write("\n");
			writeValues(information, headers);
			if (monitor != null) {
				monitor.worked(1);
			}
		}
		if (writeLegend) {
			writeLegend();
		}

		writer.close();

		if (monitor != null) {
			monitor.done();
		}
	}

	private void writeHeaderLine(List<String> headers) throws IOException {
		boolean first = true;
		for (String attributeName : headers) {
			attributeName = attributeName.replaceAll("\n", "");
			attributeName = attributeName.replaceAll("\"", "'");

			if (first) {
				first = false;
			} else {
				writer.write(";");
			}
			writer.write("\"" + attributeName + "\"");
		}
	}

	private void writeLegend() throws IOException {
		Set<List<LegendEntry>> legends = new HashSet<List<LegendEntry>>();
		for (IProcessInformation information : processInformationList) {
			for (IValueReplacement replacement : information.getValueReplacements()) {
				List<LegendEntry> legend = replacement.getReplacementLegend();
				if (!legend.isEmpty()) {
					legends.add(legend);
				}
			}
		}

		if (!legends.isEmpty()) {
			writer.write("\n\n\n");
		}
		for (List<LegendEntry> legend : legends) {
			for (LegendEntry entry : legend) {
				writer.write(entry.getSymbol());
				writer.write(";");
				writer.write(entry.getExplanation());
				writer.write("\n");
			}

			writer.write("\n");
		}
	}

	private void writeValues(IProcessInformation information, List<String> headers) throws IOException {
		boolean first = true;
		for (String attributeName : headers) {
			if (first) {
				first = false;
			} else {
				writer.write(";");
			}

			String attributeValue = information.getAttributeValue(attributeName);
			attributeValue = attributeValue.replaceAll("\n", "");
			attributeValue = attributeValue.replaceAll("\r", "");
			attributeValue = attributeValue.replaceAll(";", "");
			writer.write("\"" + attributeValue + "\"");
		}
	}
}
