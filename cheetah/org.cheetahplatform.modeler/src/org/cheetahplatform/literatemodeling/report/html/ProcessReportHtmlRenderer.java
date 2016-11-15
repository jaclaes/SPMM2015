package org.cheetahplatform.literatemodeling.report.html;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import org.cheetahplatform.literatemodeling.report.IReportElement;
import org.cheetahplatform.literatemodeling.report.IReportElementRenderer;
import org.cheetahplatform.literatemodeling.report.ProcessReport;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         11.05.2010
 */
public class ProcessReportHtmlRenderer implements IRunnableWithProgress {
	private final ProcessReport report;
	private File htmlFile;

	/**
	 * @param report2
	 */
	public ProcessReportHtmlRenderer(ProcessReport report) {
		Assert.isNotNull(report);
		this.report = report;
	}

	/**
	 * @return
	 */
	public String getPathToHtmlFile() {
		return htmlFile.getAbsolutePath();
	}

	private String getTemporaryDirectoryPath() {
		String tempDirectory = System.getProperty("java.io.tmpdir");
		String separator = System.getProperty("file.separator");
		String path = tempDirectory + separator;
		return path;
	}

	private void printHtmlHeader(StringBuilder builder) {
		builder.append("<html>");
		builder.append("<head>");
		builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"literatemodeling.css\" />");
		builder.append("<title>");
		builder.append("Literate Modeling - ");
		builder.append(report.getName());
		builder.append("</title>");
		builder.append("</head>");
		builder.append("</body>");
		builder.append("<table>");
		builder.append("<tr>");
		builder.append("<td width=500px>");
		builder.append("<h1>");
		builder.append(report.getName());
		builder.append("</h1>");
		builder.append("</td>");
		try {
			String image = "unilogo.png";
			writeToFile("img/" + image, image);
			String cssFile = "literatemodeling.css";
			writeToFile("img/" + cssFile, cssFile);

			builder.append("<td>");
			builder.append("<a href=\"http://bpm.q-e.at\" target=\"_blank\">");
			builder.append("<img src=\"" + image + "\">");
			builder.append("</a>");
			builder.append("</td>");
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error writing file when creating report."));
		}
		builder.append("</tr>");
		builder.append("<tr>");
		builder.append("<td COLSPAN=2>");
		builder.append(report.getShortDescription());
		builder.append("</td>");
		builder.append("</tr>");
		builder.append("</table>");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.subTask("Rendering Report to HTML Page");
		StringBuilder builder = new StringBuilder();
		printHtmlHeader(builder);

		List<IReportElement> reportElements = report.getReportElements();
		for (IReportElement reportElement : reportElements) {
			IReportElementRenderer renderer = reportElement.createRenderer(new HtmlReportElementRenderFactory());
			renderer.render(builder);
		}

		builder.append("</br>");
		builder.append("</br>");
		builder.append("</body>");
		builder.append("</html>");
		String path = getTemporaryDirectoryPath() + System.currentTimeMillis() + ".html";
		try {
			htmlFile = writeToFile(path, builder.toString().getBytes());

		} catch (IOException e) {
			throw new RuntimeException("An error occured when creating the note overview. Please talk to your administrator.", e);
		}
		monitor.worked(1);
	}

	private File writeToFile(String path, byte[] bytes) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}

		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		outputStream.write(bytes);
		outputStream.flush();
		outputStream.close();
		return file;
	}

	private void writeToFile(String inputPath, String outputFileName) throws IOException {
		String outputPath = getTemporaryDirectoryPath() + outputFileName;
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path(inputPath), null);
		InputStream stream = input.openStream();
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		writeToFile(outputPath, bytes);
	}

}
