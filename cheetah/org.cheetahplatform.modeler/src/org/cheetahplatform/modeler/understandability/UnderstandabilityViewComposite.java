package org.cheetahplatform.modeler.understandability;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.PageBook;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class UnderstandabilityViewComposite extends Composite {

	private Composite questionComposite;
	private PlainMultiLineButton dismissButton;
	private PlainMultiLineButton hintButton;
	private Label headerLabel;
	private Composite composite;
	private Composite composite_1;
	private ProgressBar progressBar;
	private Label lblOverallProgress;
	private Composite composite_2;
	private Composite composite_3;
	private Label lblExplanation;
	private Text explanationText;
	private PageBook explanationPageBook;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public UnderstandabilityViewComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setBackground(SWTResourceManager.getColor(255, 255, 255));
		setBackgroundMode(SWT.INHERIT_FORCE);

		Image hintImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/hint.gif"); //$NON-NLS-1$
		Image image = ResourceManager.getPluginImage(Activator.getDefault(), "img/nextquestion.png"); //$NON-NLS-1$

		headerLabel = new Label(this, SWT.WRAP);
		headerLabel.setFont(SWTResourceManager.getFont("", 12, SWT.NORMAL)); //$NON-NLS-1$
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		layoutData.widthHint = 900;
		headerLabel.setLayoutData(layoutData);
		headerLabel.setText(Messages.UnderstandabilityViewComposite_3);

		composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);

		explanationPageBook = new PageBook(composite_2, SWT.NONE);
		explanationPageBook.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		composite_3 = new Composite(explanationPageBook, SWT.NONE);
		composite_3.setLayout(new GridLayout(1, false));

		lblExplanation = new Label(composite_3, SWT.NONE);
		lblExplanation.setText("Short Explanation");

		explanationText = new Text(composite_3, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		explanationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		composite_1 = new Composite(explanationPageBook, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));

		lblOverallProgress = new Label(composite_1, SWT.NONE);
		lblOverallProgress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblOverallProgress.setText(Messages.UnderstandabilityViewComposite_4);

		progressBar = new ProgressBar(composite_1, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		composite = new Composite(composite_2, SWT.NONE);
		composite.setSize(281, 40);
		composite.setLayout(new GridLayout(2, true));
		hintButton = new PlainMultiLineButton(composite, SWT.HORIZONTAL, Messages.UnderstandabilityViewComposite_5, hintImage, null);
		hintButton.setFont(SWTResourceManager.getFont("", 12, SWT.NORMAL)); //$NON-NLS-1$
		dismissButton = new PlainMultiLineButton(composite, SWT.HORIZONTAL, Messages.UnderstandabilityViewComposite_7, image, null);
		dismissButton.setFont(SWTResourceManager.getFont("", 12, SWT.NORMAL)); //$NON-NLS-1$

		questionComposite = new Composite(this, SWT.NONE);
		GridData gd_questionComposite = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_questionComposite.verticalIndent = 5;
		questionComposite.setLayoutData(gd_questionComposite);
		questionComposite.setLayout(new GridLayout(1, false));

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ASK_FOR_UNDERSTANDABILITY_EXPLANATION)) {
			explanationPageBook.showPage(composite_3);
		} else {
			explanationPageBook.showPage(composite_1);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the dismissButton
	 */
	public PlainMultiLineButton getDismissButton() {
		return dismissButton;
	}

	public PageBook getExplanationPageBook() {
		return explanationPageBook;
	}

	public Text getExplanationText() {
		return explanationText;
	}

	/**
	 * @return the headerLabel
	 */
	public Label getHeaderLabel() {
		return headerLabel;
	}

	/**
	 * Returns the hintButton.
	 * 
	 * @return the hintButton
	 */
	public PlainMultiLineButton getHintButton() {
		return hintButton;
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public Control getProgressLabel() {
		return lblOverallProgress;
	}

	/**
	 * @return the questionComposite
	 */
	public Composite getQuestionComposite() {
		return questionComposite;
	}
}
