package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.common.ui.CustomCheckboxTreeViewer;
import org.cheetahplatform.common.ui.composite.TimeframeSelectionFieldEditorComposite;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class SelectProcessInstanceComposite extends Composite {
	private TreeViewer instancesViewer;

	private Text idText;
	private Text hostText;
	private Text processText;
	private TimeframeSelectionFieldEditorComposite timeframeComposite;
	private Composite instancesComposite;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectProcessInstanceComposite(Composite parent, int style, int treeStyle) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lblId = new Label(composite_1, SWT.NONE);
		lblId.setText("Id");

		idText = new Text(composite_1, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblHost = new Label(composite_1, SWT.NONE);
		GridData gd_lblHost = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblHost.widthHint = 100;
		lblHost.setLayoutData(gd_lblHost);
		lblHost.setText("Host");

		hostText = new Text(composite_1, SWT.BORDER);
		hostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblProcess = new Label(composite_1, SWT.NONE);
		GridData gd_lblProcess = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblProcess.widthHint = 100;
		lblProcess.setLayoutData(gd_lblProcess);
		lblProcess.setText("Process");

		processText = new Text(composite_1, SWT.BORDER);
		processText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_1, SWT.NONE);
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		timeframeComposite = new TimeframeSelectionFieldEditorComposite(composite_1, SWT.NONE);
		timeframeComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		instancesComposite = new Composite(this, SWT.NONE);
		instancesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Tree tree = new Tree(instancesComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | treeStyle);
		if ((treeStyle & SWT.CHECK) != 0) {
			instancesViewer = new CustomCheckboxTreeViewer(tree);
		} else {
			instancesViewer = new TreeViewer(tree);
		}
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
		treeColumn.setText("Date");

		TreeColumn treeColumn_1 = new TreeColumn(tree, SWT.NONE);
		treeColumn_1.setText("Id");

		TreeColumn treeColumn_2 = new TreeColumn(tree, SWT.NONE);
		treeColumn_2.setText("Host");

		TreeColumn treeColumn_3 = new TreeColumn(tree, SWT.NONE);
		treeColumn_3.setText("Process");

		TreeColumnLayout layout2 = new TreeColumnLayout();
		instancesComposite.setLayout(layout2);
		layout2.setColumnData(treeColumn, new ColumnWeightData(50));
		layout2.setColumnData(treeColumn_1, new ColumnWeightData(15));
		layout2.setColumnData(treeColumn_2, new ColumnWeightData(45));
		layout2.setColumnData(treeColumn_3, new ColumnWeightData(80));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the hostText
	 */
	public Text getHostText() {
		return hostText;
	}

	/**
	 * @return the idText
	 */
	public Text getIdText() {
		return idText;
	}

	/**
	 * @return the instancesComposite
	 */
	public Composite getInstancesComposite() {
		return instancesComposite;
	}

	public TreeViewer getInstancesViewer() {
		return instancesViewer;
	}

	/**
	 * @return the processText
	 */
	public Text getProcessText() {
		return processText;
	}

	/**
	 * @return the timeframeComposite
	 */
	public TimeframeSelectionFieldEditorComposite getTimeframeComposite() {
		return timeframeComposite;
	}
}
