package org.cheetahplatform.modeler.decserflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

public class SelectMultiActivitiesDialogComposite extends Composite {

	private Label constraintFigure;
	private Composite incomingActivitiesComposite;
	private Composite outgoingActivitiesComposite;
	private Label outgoingActivitiesLabel;
	private Label incomingActivitiesLabel;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectMultiActivitiesDialogComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		Label label = new Label(this, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		constraintFigure = new Label(this, SWT.NONE);
		constraintFigure.setAlignment(SWT.CENTER);
		constraintFigure.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		constraintFigure.setBackground(SWTResourceManager.getColor(255, 255, 255));

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		incomingActivitiesLabel = new Label(this, SWT.NONE);
		incomingActivitiesLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		GridData gd_lblIncomingActivities = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblIncomingActivities.horizontalIndent = 10;
		incomingActivitiesLabel.setLayoutData(gd_lblIncomingActivities);
		incomingActivitiesLabel.setText("Incoming Activities");

		incomingActivitiesComposite = new Composite(this, SWT.NONE);
		incomingActivitiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		incomingActivitiesComposite.setLayout(new GridLayout(1, false));

		outgoingActivitiesLabel = new Label(this, SWT.NONE);
		outgoingActivitiesLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		GridData gd_lblOutgoingActivities = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblOutgoingActivities.horizontalIndent = 10;
		outgoingActivitiesLabel.setLayoutData(gd_lblOutgoingActivities);
		outgoingActivitiesLabel.setText("Outgoing Activities");

		outgoingActivitiesComposite = new Composite(this, SWT.NONE);
		outgoingActivitiesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		outgoingActivitiesComposite.setLayout(new GridLayout(1, false));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Label getConstraintFigure() {
		return constraintFigure;
	}

	public Composite getIncomingActivitiesComposite() {
		return incomingActivitiesComposite;
	}

	public Label getIncomingActivitiesLabel() {
		return incomingActivitiesLabel;
	}

	public Composite getOutgoingActivitiesComposite() {
		return outgoingActivitiesComposite;
	}

	public Label getOutgoingActivitiesLabel() {
		return outgoingActivitiesLabel;
	}

}