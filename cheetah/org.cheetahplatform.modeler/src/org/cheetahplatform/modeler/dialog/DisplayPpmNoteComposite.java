package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class DisplayPpmNoteComposite extends Composite {
	private Text noteText;
	private DateTime startTime;
	private DateTime endTime;
	private Text originatorText;
	private Text categoryText;
	private Button selectCategoryButton;
	private TreeViewer auditTrailEntryTreeViewer;

	public DisplayPpmNoteComposite(Composite parent, int style) {
		this(parent, style, 0);
	}

	public DisplayPpmNoteComposite(Composite parent, int style, int depth) {
		super(parent, style);

		Group group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if (depth == 0) {
			group.setText("PPM Note");
		} else {
			group.setText("Comment");
		}

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 2;
		gridLayout.marginLeft = 5 + depth * 20;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Composite composite = new Composite(group, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, true);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 55, 15);
		lblNewLabel.setText("Starttime");

		startTime = new DateTime(composite, SWT.BORDER | SWT.TIME | SWT.LONG);
		startTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		startTime.setBounds(0, 0, 76, 24);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setBounds(0, 0, 55, 15);
		lblNewLabel_1.setText("Endtime");

		endTime = new DateTime(composite, SWT.BORDER | SWT.TIME);
		endTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endTime.setBounds(0, 0, 76, 24);

		Group grpAudittrailEntries = new Group(group, SWT.NONE);
		grpAudittrailEntries.setText("Audittrail Entries");
		GridData gd_grpAudittrailEntries = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		gd_grpAudittrailEntries.heightHint = 130;
		grpAudittrailEntries.setLayoutData(gd_grpAudittrailEntries);
		grpAudittrailEntries.setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(grpAudittrailEntries, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		TreeColumnLayout tcl_composite_2 = new TreeColumnLayout();
		composite_2.setLayout(tcl_composite_2);

		auditTrailEntryTreeViewer = new TreeViewer(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = auditTrailEntryTreeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(auditTrailEntryTreeViewer, SWT.NONE);
		TreeColumn treeColumn = treeViewerColumn.getColumn();
		tcl_composite_2.setColumnData(treeColumn, new ColumnWeightData(10, true));
		treeColumn.setText("#");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(auditTrailEntryTreeViewer, SWT.NONE);
		TreeColumn trclmnNewColumn = treeViewerColumn_1.getColumn();
		tcl_composite_2.setColumnData(trclmnNewColumn, new ColumnWeightData(20, true));
		trclmnNewColumn.setText("Timestamp");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(auditTrailEntryTreeViewer, SWT.NONE);
		TreeColumn trclmnEvent = treeViewerColumn_2.getColumn();
		tcl_composite_2.setColumnData(trclmnEvent, new ColumnWeightData(70, true));
		trclmnEvent.setText("Event");

		Composite composite_1 = new Composite(group, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_1.widthHint = 250;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setBounds(0, 0, 64, 64);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		Label lblCategory = new Label(composite_1, SWT.NONE);
		lblCategory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCategory.setText("Category");

		categoryText = new Text(composite_1, SWT.BORDER);
		categoryText.setEditable(false);
		categoryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectCategoryButton = new Button(composite_1, SWT.NONE);
		selectCategoryButton.setText("...");

		Label lblOriginator = new Label(composite_1, SWT.NONE);
		lblOriginator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblOriginator.setText("Originator");

		originatorText = new Text(composite_1, SWT.BORDER);
		originatorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		noteText = new Text(group, SWT.BORDER | SWT.WRAP);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_text.heightHint = 100;
		gd_text.widthHint = 500;
		noteText.setLayoutData(gd_text);
	}

	@Override
	protected void checkSubclass() {
		// i dont mind ;)
	}

	/**
	 * @return the auditTrailEntryTreeViewer
	 */
	public TreeViewer getAuditTrailEntryTreeViewer() {
		return auditTrailEntryTreeViewer;
	}

	/**
	 * @return the categoryText
	 */
	public Text getCategoryText() {
		return categoryText;
	}

	/**
	 * @return the endTime
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * @return the originatorText
	 */
	public Text getOriginatorText() {
		return originatorText;
	}

	/**
	 * @return the text
	 */
	public Text getPpmNoteText() {
		return noteText;
	}

	/**
	 * @return the selectCategoryButton
	 */
	public Button getSelectCategoryButton() {
		return selectCategoryButton;
	}

	/**
	 * @return the startTime
	 */
	public DateTime getStartTime() {
		return startTime;
	}
}
