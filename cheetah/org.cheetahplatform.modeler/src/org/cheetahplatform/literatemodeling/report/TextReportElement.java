package org.cheetahplatform.literatemodeling.report;

import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         12.05.2010
 */
public class TextReportElement extends AbstractSimpleReportElement {

	private final String text;
	private final String title;

	/**
	 * @param text
	 */
	public TextReportElement(String title, String text) {
		this.title = title;
		Assert.isNotNull(title);
		Assert.isNotNull(text);
		Assert.isLegal(!title.trim().isEmpty());
		this.text = text;
	}

	/**
	 * Returns the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	@Override
	public IReportElementRenderer createRenderer(IReportElementRendererFactory factory) {
		return factory.createTextReportElementRenderer(this);
	}
}
