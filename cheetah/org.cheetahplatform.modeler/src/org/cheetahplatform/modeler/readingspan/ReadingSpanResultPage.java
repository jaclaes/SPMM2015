package org.cheetahplatform.modeler.readingspan;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ReadingSpanResultPage extends WizardPage {
	public static final String ID = "READING_SPAN_RESULT";
	private final int level;
	private final int iteration;
	protected Composite container;
	private List<Text> endwords;
	private List<Text> content;

	protected ReadingSpanResultPage(int level, int iteration) {
		this(ID, level, iteration);
	}

	protected ReadingSpanResultPage(String id, int level, int iteration) {
		super(id);

		Assert.isLegal(level > 0);
		this.level = level;
		Assert.isLegal(iteration > 0);
		this.iteration = iteration;
	}

	public List<Attribute> collectResults() {
		List<Attribute> attributes = new ArrayList<Attribute>();
		String attributeprefix = ReadingSpanWizard.TEST_PREFIX + "-" + level + "." + iteration + ".";

		Assert.isLegal(endwords.size() == content.size());
		for (int i = 0; i < endwords.size(); i++) {
			String endwordValue = endwords.get(i).getText();
			String endwordId = attributeprefix + (i + 1) + "-endword";
			attributes.add(new Attribute(endwordId, endwordValue));

			String contentValue = content.get(i).getText();
			String contentId = attributeprefix + (i + 1) + "-content";
			attributes.add(new Attribute(contentId, contentValue));
		}

		return attributes;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		setTitle("Ergebnisse");
		setDescription("Bitte tragen Sie das letzte Worte ein und deuten Sie den Inhalt des Satzes mit zwei bis drei Stichworten an.");

		Label lblSatzebene = new Label(container, SWT.NONE);
		lblSatzebene.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblSatzebene.setText("Satz");

		Label lblEndwort = new Label(container, SWT.NONE);
		GridData gd_lblEndwort = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblEndwort.heightHint = 18;
		lblEndwort.setLayoutData(gd_lblEndwort);
		lblEndwort.setText("Endwort");

		Label lblInhalt = new Label(container, SWT.NONE);
		lblInhalt.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblInhalt.setText("Inhalt");

		endwords = new ArrayList<Text>();
		content = new ArrayList<Text>();

		for (int i = 0; i < level; i++) {
			Label label = new Label(container, SWT.NONE);
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label.setText(String.valueOf(i + 1));

			Text lastWordText = new Text(container, SWT.BORDER);
			lastWordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			endwords.add(lastWordText);

			Text contentText = new Text(container, SWT.BORDER);
			contentText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			content.add(contentText);
		}

		Assert.isLegal(endwords.size() == content.size());
	}
}
