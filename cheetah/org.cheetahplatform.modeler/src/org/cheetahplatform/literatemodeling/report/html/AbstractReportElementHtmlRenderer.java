package org.cheetahplatform.literatemodeling.report.html;

import org.cheetahplatform.literatemodeling.report.IReportElementRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public abstract class AbstractReportElementHtmlRenderer implements IReportElementRenderer {
	protected String getTemporaryDirectoryPath() {
		String tempDirectory = System.getProperty("java.io.tmpdir");
		String path = tempDirectory + getFileSeparator();
		return path;
	}

	private String getFileSeparator() {
		String separator = System.getProperty("file.separator");
		return separator;
	}

	protected void saveImage(String imageName, Image image) {
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(getTemporaryDirectoryPath() + getFileSeparator() + imageName, SWT.IMAGE_PNG);
	}

	protected void addImageToHtml(StringBuilder builder, String imageName) {
		builder.append("<img src=\"" + imageName + "\">");
	}
}