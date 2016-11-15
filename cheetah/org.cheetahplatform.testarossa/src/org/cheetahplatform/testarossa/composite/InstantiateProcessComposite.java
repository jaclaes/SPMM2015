package org.cheetahplatform.testarossa.composite;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class InstantiateProcessComposite extends Composite {

	private Text nameText;
	private TreeViewer viewer;
	private Tree tree;
	private Text choiceOfLocationText;
	private DateSelectionComposite choiceOfLocationComposite;
	private DateSelectionComposite openingComposite;
	private Label lblRolefrMitterrutzner;
	private Label lblRolehrPetzold;
	private SelectRoleComposite frMitterrutznerComposite;
	private SelectRoleComposite hrPetzoldComposite;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public InstantiateProcessComposite(Composite parent, int style) {
		super(parent, style);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		final Label nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText("Name:");

		nameText = new Text(this, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label choiceOfLocationLabel = new Label(this, SWT.NONE);
		choiceOfLocationLabel.setText("Choice of Location:");

		choiceOfLocationComposite = new DateSelectionComposite(this, SWT.NONE);
		choiceOfLocationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label openingLabel = new Label(this, SWT.NONE);
		openingLabel.setText("Opening:");

		openingComposite = new DateSelectionComposite(this, SWT.NONE);
		openingComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		lblRolefrMitterrutzner = new Label(this, SWT.NONE);
		lblRolefrMitterrutzner.setText("Role \"Fr Mitterrutzner\"");

		frMitterrutznerComposite = new SelectRoleComposite(this, SWT.NONE);
		frMitterrutznerComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblRolehrPetzold = new Label(this, SWT.NONE);
		lblRolehrPetzold.setText("Role \"Hr Petzold\"");

		hrPetzoldComposite = new SelectRoleComposite(this, SWT.NONE);
		hrPetzoldComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		viewer = new TreeViewer(this, SWT.BORDER);
		tree = viewer.getTree();
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		layoutData.minimumHeight = 100;
		tree.setLayoutData(layoutData);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Return the choiceOfLocationComposite.
	 * 
	 * @return the choiceOfLocationComposite
	 */
	public DateSelectionComposite getChoiceOfLocationComposite() {
		return choiceOfLocationComposite;
	}

	/**
	 * Return the choiceOfLocationDate.
	 * 
	 * @return the choiceOfLocationDate
	 */
	public Text getChoiceOfLocationText() {
		return choiceOfLocationText;
	}

	/**
	 * @return the frMitterrutznerComposite
	 */
	public SelectRoleComposite getFrMitterrutznerComposite() {
		return frMitterrutznerComposite;
	}

	/**
	 * @return the hrPetzoldComposite
	 */
	public SelectRoleComposite getHrPetzoldComposite() {
		return hrPetzoldComposite;
	}

	/**
	 * Returns the nameText.
	 * 
	 * @return the nameText
	 */
	public Text getNameTextWidget() {
		return nameText;
	}

	/**
	 * Return the openingComposite.
	 * 
	 * @return the openingComposite
	 */
	public DateSelectionComposite getOpeningComposite() {
		return openingComposite;
	}

	/**
	 * Returns the viewer.
	 * 
	 * @return the viewer
	 */
	public TreeViewer getViewer() {
		return viewer;
	}

}
