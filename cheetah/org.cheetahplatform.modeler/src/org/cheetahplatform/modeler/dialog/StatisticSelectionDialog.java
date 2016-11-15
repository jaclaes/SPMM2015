package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class StatisticSelectionDialog extends TitleAreaDialog {
	private static final int DESELECT_ALL_ID = 20001;
	private static final int SELECT_ALL_ID = 20000;
	private StatisticsSelectionModel model;
	private CheckboxTableViewer statisticsViewer;
	private List<IPpmStatistic> selectedStatistics;

	public StatisticSelectionDialog(Shell parentShell) {
		super(parentShell);
		model = new StatisticsSelectionModel();
		selectedStatistics = new ArrayList<IPpmStatistic>();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == SELECT_ALL_ID) {
			selectAll();
			return;
		}
		if (buttonId == DESELECT_ALL_ID) {
			deselectAll();
			return;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Statistic Selection");
		super.configureShell(newShell);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		return super.createButtonBar(parent);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, SELECT_ALL_ID, "Select All", false);
		createButton(parent, DESELECT_ALL_ID, "Deselect All", false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		setTitle("Statistic Selection");
		setMessage("Please select the desired statistics below.");

		statisticsViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.MULTI | SWT.HIDE_SELECTION);
		statisticsViewer.setLabelProvider(model.createLabelProvider());
		statisticsViewer.setContentProvider(model.createContentProvider());
		// statisticsViewer.setSorter(model.createSorter());
		statisticsViewer.addFilter(model.createFilter());
		statisticsViewer.setInput(model.getAvailableStatistics());
		statisticsViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return container;
	}

	private void deselectAll() {
		statisticsViewer.setAllChecked(false);
	}

	public List<IPpmStatistic> getSelectedStatistics() {
		return selectedStatistics;
	}

	@Override
	protected void okPressed() {
		for (Object object : statisticsViewer.getCheckedElements()) {
			selectedStatistics.add((IPpmStatistic) object);
		}
		super.okPressed();
	}

	private void selectAll() {
		statisticsViewer.setAllChecked(true);
	}
}
