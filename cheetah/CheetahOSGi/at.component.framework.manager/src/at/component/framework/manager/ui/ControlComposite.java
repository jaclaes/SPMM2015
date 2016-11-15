package at.component.framework.manager.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;

import at.component.framework.manager.Activator;
import at.component.util.TableColumnResizer;

public class ControlComposite extends Composite {

	private TableViewer installedBundlesTableViewer;
	private TableViewer deployedProjectsTableViewer;
	private TreeViewer activeComponentsTreeViewer;
	private Label projectNameLabel;

	public ControlComposite(Composite parent, int style) {
		super(parent, style);
		
		SashForm tableSashForm = new SashForm(this, SWT.VERTICAL);
		tableSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableSashForm.setLayout(new GridLayout());
		
		createInstalledBundlesTableViewer(tableSashForm);

		createDeployedProjectsTableViewer(tableSashForm);

		createActiveComponentsComposite(tableSashForm);

		tableSashForm.setWeights(new int[] { 31, 31, 31 });
	}
	
	private void createInstalledBundlesTableViewer(SashForm parent) {
		installedBundlesTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);

		Table installedBundlesTable = installedBundlesTableViewer.getTable();
		installedBundlesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		installedBundlesTable.setLinesVisible(true);
		installedBundlesTable.setHeaderVisible(true);
		TableColumnResizer.addToTable(installedBundlesTable, new int[] { 100 }, new int[] { 100 });
		installedBundlesTableViewer.setContentProvider(new ArrayContentProvider());
		installedBundlesTableViewer.setLabelProvider(new InstalledComponentsLabelProvider());
		installedBundlesTableViewer.setInput(Activator.getComponentService().getInstalledComponentBundles());

		final TableColumn installedBundlesTableColumn_1 = new TableColumn(installedBundlesTable, SWT.NONE);
		installedBundlesTableColumn_1.setWidth(150);
		installedBundlesTableColumn_1.setText("Installed Components");
	}
	
	private void createDeployedProjectsTableViewer(SashForm parent) {
		deployedProjectsTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);

		Table deployedProjectsTable = deployedProjectsTableViewer.getTable();
		deployedProjectsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		deployedProjectsTable.setLinesVisible(true);
		deployedProjectsTable.setHeaderVisible(true);
		TableColumnResizer.addToTable(deployedProjectsTable, new int[] { 100 }, new int[] { 100 });
		deployedProjectsTableViewer.setContentProvider(new ArrayContentProvider());
		deployedProjectsTableViewer.setLabelProvider(new LoadedProjectsLabelProvider());
		deployedProjectsTableViewer.setInput(Activator.getComponentService().getDeployedProjects());

		final TableColumn addedProjectsTableColumn_1 = new TableColumn(deployedProjectsTable, SWT.NONE);
		addedProjectsTableColumn_1.setWidth(150);
		addedProjectsTableColumn_1.setText("Deployed Projects");
	}
	
	private void createActiveComponentsComposite(SashForm parent) {
		GridLayout activeProjectCompositeGridLayout = new GridLayout(2, false);
		activeProjectCompositeGridLayout.horizontalSpacing = 0;
		activeProjectCompositeGridLayout.marginWidth = 0;

		Composite activeProjectComposite = new Composite(parent, SWT.NONE);
		activeProjectComposite.setLayout(activeProjectCompositeGridLayout);
		activeProjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		Label projectLabel = new Label(activeProjectComposite, SWT.NONE);
		projectLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
		projectLabel.setText("Project: ");

		projectNameLabel = new Label(activeProjectComposite, SWT.NONE);
		projectNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
		projectNameLabel.setText("");

		activeComponentsTreeViewer = new TreeViewer(activeProjectComposite, SWT.BORDER);
		activeComponentsTreeViewer.setContentProvider(new ActiveComponentsContentProvider());
		activeComponentsTreeViewer.setLabelProvider(new ActiveComponentsLabelProvider());

		Tree activeComponentsTree = activeComponentsTreeViewer.getTree();
		activeComponentsTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}
	
	public Label getProjectNameLabel() {
		return projectNameLabel;
	}

	public TableViewer getInstalledBundlesTableViewer() {
		return installedBundlesTableViewer;
	}

	public TableViewer getDeployedProjectsTableViewer() {
		return deployedProjectsTableViewer;
	}

	public TreeViewer getActiveComponentsTreeViewer() {
		return activeComponentsTreeViewer;
	}
}
