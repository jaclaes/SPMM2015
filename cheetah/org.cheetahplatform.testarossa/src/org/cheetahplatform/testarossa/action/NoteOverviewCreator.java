package org.cheetahplatform.testarossa.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.tdm.MessageLookup;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * A class creating an overview of all notes attached to Activities.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.12.2009
 */
public class NoteOverviewCreator {

	/**
	 * Simple sorter.
	 * 
	 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
	 * 
	 *         15.12.2009
	 */
	private final class DeclarativeNodeInstanceComparator implements Comparator<IDeclarativeNodeInstance> {
		public int compare(IDeclarativeNodeInstance o1, IDeclarativeNodeInstance o2) {
			return o1.getNode().getName().compareTo(o2.getNode().getName());
		}
	}

	private String getTemporaryDirectoryPath() {
		String tempDirectory = System.getProperty("java.io.tmpdir");
		String separator = System.getProperty("file.separator");
		String path = tempDirectory + separator;
		return path;
	}

	private void logError(Exception e) {
		Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unable to write temporary file to " + getTemporaryDirectoryPath(),
				e);
		Activator.getDefault().getLog().log(status);
	}

	private void printHtmlHeader(StringBuilder builder, DeclarativeProcessInstance instance) {
		builder.append("<html>");
		builder.append("<head>");
		builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"testarossa.css\" />");
		builder.append("<title>");
		builder.append("Testa Rossa - ");
		builder.append(instance.getName());
		builder.append("</title>");
		builder.append("</head>");
		builder.append("</body>");
		builder.append("<table>");
		builder.append("<tr>");
		builder.append("<td width=500px>");
		builder.append("<h1>");
		builder.append(instance.getName());
		builder.append("</h1>");
		builder.append("</td>");
		try {

			String image = "testarossa_128x128.gif";
			writeToFile("/img/" + image, image);
			String cssFile = "testarossa.css";
			writeToFile("/img/" + cssFile, cssFile);

			builder.append("<td>");
			builder.append("<img src=\"" + image + "\">");
			builder.append("</td>");
		} catch (IOException e) {
			logError(e);
		}
		builder.append("</tr>");
		builder.append("</table>");
	}

	private void printNote(StringBuilder builder, IDeclarativeNodeInstance node, String message) {
		builder.append("<h2>");
		builder.append(node.getNode().getName());
		Role role = RoleLookup.getInstance().getRole((DeclarativeActivityInstance) node);
		if (role != null) {
			builder.append(" (");
			builder.append(role.getName());
			builder.append(")");
		}
		builder.append("</h2>");
		// builder.append("<pre>");
		builder.append("<table width=\"800\">");
		builder.append("<tr><td>");
		builder.append(message);
		builder.append("</td></tr>");
		builder.append("</table");
		// builder.append("</pre>");
	}

	/**
	 * Creates an HTML file containing all notes of the current {@link ProcessInstance}.
	 * 
	 * @return the file
	 */
	public File toHtmlFile() {
		StringBuilder builder = new StringBuilder();
		DeclarativeProcessInstance instance = TestaRossaModel.getInstance().getCurrentInstance();

		printHtmlHeader(builder, instance);

		List<IDeclarativeNodeInstance> nodeInstances = instance.getNodeInstances();
		Collections.sort(nodeInstances, new DeclarativeNodeInstanceComparator());

		boolean found = false;
		for (IDeclarativeNodeInstance node : nodeInstances) {
			if (!(node instanceof DeclarativeActivityInstance)) {
				continue;
			}

			String message = MessageLookup.getInstance().getMessage((DeclarativeActivityInstance) node);
			if (message == null) {
				continue;
			}
			printNote(builder, node, message);
			found = true;
		}

		if (!found) {
			builder.append("<h2>No notes have been entered so far.</h2>");
		}

		builder.append("</br>");
		builder.append("</br>");
		builder.append("</body>");
		builder.append("</html>");
		String path = getTemporaryDirectoryPath() + System.currentTimeMillis() + ".html";
		try {
			return writeToFile(path, builder.toString().getBytes());
		} catch (IOException e) {
			logError(e);
			throw new RuntimeException("An error occured when createing the note overview. Please talk to your administrator.", e);
		}
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
		InputStream stream = Activator.class.getResourceAsStream(inputPath);
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);

		writeToFile(outputPath, bytes);
	}
}
