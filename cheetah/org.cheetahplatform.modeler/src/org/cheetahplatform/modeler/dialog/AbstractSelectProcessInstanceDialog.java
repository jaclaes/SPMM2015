package org.cheetahplatform.modeler.dialog;

import java.util.Date;
import java.util.Map;

import org.cheetahplatform.common.ui.TimeframeSelectionController;
import org.cheetahplatform.common.ui.TreeViewerColumnSorter;
import org.cheetahplatform.common.ui.dialog.DefaultExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public abstract class AbstractSelectProcessInstanceDialog extends LocationPersistentTitleAreaDialog {

	private class FilterListener implements ModifyListener, Runnable {

		@Override
		public void modifyText(ModifyEvent e) {
			updateFilter();
		}

		@Override
		public void run() {
			updateFilter();
		}
	}

	public static final int EXPAND_ALL = 1002;

	protected SelectProcessInstanceComposite composite;
	private TimeframeSelectionController timeframeController;
	protected SelectProcessInstanceModel model;

	public AbstractSelectProcessInstanceDialog(Shell parentShell) {
		super(parentShell);

		this.model = new SelectProcessInstanceModel(new DefaultExtraColumnProvider());
	}

	public AbstractSelectProcessInstanceDialog(Shell parentShell, IExtraColumnProvider extraColumnProvider) {
		super(parentShell);

		this.model = new SelectProcessInstanceModel(extraColumnProvider);
	}

	private void addExtraTableColumns() {
		Tree tree = composite.getInstancesViewer().getTree();

		for (Map.Entry<String, Integer> entry : model.getExtraColumns().entrySet()) {
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setText(entry.getKey());

			TreeColumnLayout layout = (TreeColumnLayout) composite.getInstancesComposite().getLayout();
			Integer weight = entry.getValue();
			layout.setColumnData(column, new ColumnWeightData(weight));
		}
	}

	/**
	 * Instances from the given process are not displayed in the dialog.
	 * 
	 * @param processId
	 *            the id
	 */
	public void addIgnoredProcess(String processId) {
		model.addIgnoredProcess(processId);
	}

	/**
	 * @see SelectProcessInstanceModel#addIncludedProcess(String)
	 */
	public void addIncludedProcess(String processId) {
		model.addIncludedProcess(processId);
	}

	private void addListener() {
		addValidationListener(composite.getInstancesViewer());

		FilterListener listener = new FilterListener();
		composite.getIdText().addModifyListener(listener);
		composite.getHostText().addModifyListener(listener);
		composite.getProcessText().addModifyListener(listener);
		timeframeController.addListener(listener);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == EXPAND_ALL) {
			expandAll();
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button expandAllButton = createButton(parent, EXPAND_ALL, "Expand All", false);
		expandAllButton.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, true, false));
		super.createButtonsForButtonBar(parent);
	}

	protected void createComposite(Composite realParent) {
		composite = new SelectProcessInstanceComposite(realParent, SWT.NONE, getTreeStyle());
		initialize();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		createComposite(realParent);

		return realParent;
	}

	@Override
	protected void dispose() {
		model.saveFilters();
		model.dispose();
	}

	private void expandAll() {
		TreeViewer instancesViewer = composite.getInstancesViewer();
		instancesViewer.expandAll();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 800);
	}

	/**
	 * Return the style of the tree to be displayed.
	 * 
	 * @return the style
	 */
	protected abstract int getTreeStyle();

	protected void initialize() {
		addExtraTableColumns();

		TreeViewer viewer = composite.getInstancesViewer();
		viewer.setContentProvider(model.createContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setInput(model.loadAllProcessInstances());
		viewer.setFilters(model.createFilter());

		TreeViewerColumnSorter.addColumnSorter(viewer, model.createSorters());

		Date from = model.getFrom();
		Date to = model.getTo();
		timeframeController = new TimeframeSelectionController(composite.getTimeframeComposite(), from, to);

		initializeFilters();
		addListener();
	}

	private void initializeFilters() {
		setText(composite.getIdText(), model.getId());
		setText(composite.getHostText(), model.getHost());
		setText(composite.getProcessText(), model.getProcess());

		timeframeController.setFrom(model.getFrom());
		timeframeController.setTo(model.getTo());
	}

	private void setText(Text text, String value) {
		if (value == null) {
			return;
		}

		text.setText(value);
	}

	protected void updateFilter() {
		String host = composite.getHostText().getText();
		model.setHost(host);
		String id = composite.getIdText().getText();
		model.setId(id);
		String process = composite.getProcessText().getText();
		model.setProcess(process);

		Date from = timeframeController.getFrom();
		model.setFrom(from);
		Date to = timeframeController.getTo();
		model.setTo(to);

		composite.getInstancesViewer().refresh();
	}

}
