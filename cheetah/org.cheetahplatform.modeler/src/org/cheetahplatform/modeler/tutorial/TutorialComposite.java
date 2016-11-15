package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.IGraphicalGraphViewerAdvisor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.10.2009
 */
public class TutorialComposite extends Composite {

	private Label instructionsLabel;
	private Browser screencastBrowser;
	private GraphicalGraphViewerWithFlyoutPalette viewer;

	public TutorialComposite(Composite parent, int style, IGraphicalGraphViewerAdvisor advisor, Point screencastSize) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite instructionsComposite = new Composite(this, SWT.NONE);
		Composite labelWrapper = new Composite(instructionsComposite, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		instructionsComposite.setLayout(layout);
		instructionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		GridLayout labelWrapperLayout = new GridLayout();
		labelWrapperLayout.marginHeight = 10;
		labelWrapperLayout.marginWidth = 15;
		labelWrapper.setLayout(labelWrapperLayout);
		labelWrapper.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		instructionsLabel = new Label(labelWrapper, SWT.WRAP);
		instructionsLabel.setFont(SWTResourceManager.getFont("Arial", 16, SWT.BOLD));
		GridData instructionsLabelLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		instructionsLabelLayoutData.heightHint = 160;
		instructionsLabelLayoutData.widthHint = screencastSize.x;
		instructionsLabel.setLayoutData(instructionsLabelLayoutData);

		Label horizontalSeparator = new Label(instructionsComposite, SWT.SEPARATOR);
		GridData horizontalSeparatorLayoutdata = new GridData(SWT.FILL, SWT.FILL, false, true);
		horizontalSeparatorLayoutdata.verticalSpan = 3;
		horizontalSeparator.setLayoutData(horizontalSeparatorLayoutdata);

		Label separatorLabel = new Label(instructionsComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separatorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		screencastBrowser = new Browser(instructionsComposite, SWT.NONE);
		screencastBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite graphViewerComposite = new Composite(this, SWT.NONE);
		GridLayout graphViewerCompositeLayout = new GridLayout();
		graphViewerCompositeLayout.marginHeight = 0;
		graphViewerCompositeLayout.marginWidth = 0;
		graphViewerComposite.setLayout(graphViewerCompositeLayout);
		graphViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new GraphicalGraphViewerWithFlyoutPalette(graphViewerComposite, advisor);

		Label bottomSeparator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separatorLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		separatorLayoutData.horizontalSpan = 2;
		bottomSeparator.setLayoutData(separatorLayoutData);
	}

	/**
	 * Returns the instructionsLabel.
	 * 
	 * @return the instructionsLabel
	 */
	public Label getInstructionsLabel() {
		return instructionsLabel;
	}

	/**
	 * Returns the screencastBrowser.
	 * 
	 * @return the screencastBrowser
	 */
	public Browser getScreencastBrowser() {
		return screencastBrowser;
	}

	/**
	 * Returns the viewer.
	 * 
	 * @return the viewer
	 */
	public GraphicalGraphViewerWithFlyoutPalette getViewer() {
		return viewer;
	}

}
