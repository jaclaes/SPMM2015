package org.cheetahplatform.client.ui;

import java.util.List;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectSubProcessDialog extends LocationPersistentTitleAreaDialog {

	private SelectSubProcessDialogComposite composite;
	private SelectSubProcessDialogModel model;
	private GraphicalGraphViewerWithFlyoutPalette graphViewer;

	public SelectSubProcessDialog(Shell parentShell, List<ProcessSchemaHandle> processes) {
		super(parentShell);

		model = new SelectSubProcessDialogModel(processes);
	}

	private void addListener() {
		composite.getSubProcessesTable().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				sequenceSelected();
			}

		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectSubProcessDialogComposite(realParent, SWT.NONE);

		getShell().setText("Select Process");
		setTitle("Select the Process to be Used");
		setMessage("Please select the process to be selected as content of the late binding box.");
		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		IStructuredSelection selection = (IStructuredSelection) composite.getSubProcessesTable().getSelection();
		if (selection.size() != 1) {
			return "Please select one Process.";
		}

		return null;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 400);
	}

	public ProcessSchemaHandle getSelection() {
		return model.getSelection();
	}

	private void initialize() {
		TableViewer sequencesTable = composite.getSubProcessesTable();
		sequencesTable.setContentProvider(new ArrayContentProvider());
		sequencesTable.setLabelProvider(model.createLabelProvider());
		sequencesTable.setInput(model.getInput());
		sequencesTable.setSelection(new StructuredSelection(model.getSelection()));
		addValidationListener(sequencesTable);
		setErrorMessage(null);

		Graph graph = model.computeGraph();
		DefaultGraphicalGraphViewerAdvisor advisor = new SelectSubProcessGraphAdvisor(graph);
		graphViewer = new GraphicalGraphViewerWithFlyoutPalette(composite.getSubProcessComposite(), advisor);
		GraphEditDomain editDomain = (GraphEditDomain) graphViewer.getViewer().getEditDomain();
		editDomain.setEditable(false);
		graphViewer.getPalette().setVisible(false);

		addListener();
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) composite.getSubProcessesTable().getSelection();
		model.setSelection((ProcessSchemaHandle) selection.getFirstElement());

		super.okPressed();
	}

	protected void sequenceSelected() {
		IStructuredSelection selection = (IStructuredSelection) composite.getSubProcessesTable().getSelection();
		model.setSelection((ProcessSchemaHandle) selection.getFirstElement());

		Graph graph = model.computeGraph();
		graphViewer.reset(graph);
	}

}
