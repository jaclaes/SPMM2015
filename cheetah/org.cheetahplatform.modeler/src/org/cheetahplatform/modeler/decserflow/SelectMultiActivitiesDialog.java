package org.cheetahplatform.modeler.decserflow;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.ResourceManager;

public class SelectMultiActivitiesDialog extends LocationPersistentTitleAreaDialog {

	private SelectMultiActivitiesDialogModel model;
	private SelectMultiActivitiesDialogComposite composite;
	private List<ComboViewer> incomingViewers;
	private List<ComboViewer> outgoingViewers;
	private List<Node> selectedIncoming;
	private List<Node> selectedOutgoing;
	private boolean recursivevCall;

	public SelectMultiActivitiesDialog(Shell parentShell, Graph graph, MultiActivityConstraintDescriptor descriptor) {
		super(parentShell);

		model = new SelectMultiActivitiesDialogModel(graph, descriptor);
		incomingViewers = new ArrayList<ComboViewer>();
		outgoingViewers = new ArrayList<ComboViewer>();
	}

	protected void activitySelected() {
		if (recursivevCall) {
			return;
		}
		recursivevCall = true;

		List<ComboViewer> allViewers = new ArrayList<ComboViewer>();
		allViewers.addAll(incomingViewers);
		allViewers.addAll(outgoingViewers);
		List<Node> selectedActivities = collectActivities(allViewers);

		List<Node> selectableActivities = model.getInput();
		selectableActivities.removeAll(selectedActivities);

		for (ComboViewer currentViewer : allViewers) {
			IStructuredSelection selection = (IStructuredSelection) currentViewer.getSelection();
			List<Node> copy = new ArrayList<Node>(selectableActivities);
			if (!selection.isEmpty()) {
				copy.add((Node) selection.getFirstElement());
			}
			currentViewer.setInput(copy);
			SelectActivityComposite parent = (SelectActivityComposite) currentViewer.getControl().getParent();

			if (!selection.isEmpty()) {
				currentViewer.setSelection(new StructuredSelection(selection.getFirstElement()), true);
				parent.getClearButton().setEnabled(true);
			} else {
				parent.getClearButton().setEnabled(false);
			}
		}

		recursivevCall = false;
	}

	protected void clearSelection(ComboViewer viewer) {
		viewer.setSelection(new StructuredSelection());
		SelectActivityComposite parent = (SelectActivityComposite) viewer.getControl().getParent();
		parent.getClearButton().setEnabled(false);
	}

	public List<Node> collectActivities(List<ComboViewer> viewers) {
		List<Node> activities = new ArrayList<Node>();
		for (ComboViewer viewer : viewers) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			Node selectedNode = (Node) selection.getFirstElement();
			if (selectedNode != null) {
				activities.add(selectedNode);
			}
		}

		return activities;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectMultiActivitiesDialogComposite(realParent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		String message = doValidate(incomingViewers, model.getMinimumIncoming(), "Please select at least {0} incoming activities.");
		if (message != null) {
			return message;
		}

		message = doValidate(outgoingViewers, model.getMinimumOutgoing(), "Please select at least {0} outgoing activities.");
		if (message != null) {
			return message;
		}

		int minimumActivities = model.getMinimumActivities();
		if (collectActivities(incomingViewers).size() + collectActivities(outgoingViewers).size() < minimumActivities) {
			return "Please select at least " + minimumActivities + " activities in sum.";
		}

		return message;
	}

	private String doValidate(List<ComboViewer> viewers, int minimum, String selectionErrorMessage) {
		List<Node> selectedActivities = new ArrayList<Node>();
		for (ComboViewer viewer : viewers) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			Node selectedNode = (Node) selection.getFirstElement();

			if (selectedActivities.contains(selection.getFirstElement())) {
				String message = "''{0}'' is selected twice.";
				return MessageFormat.format(message, selectedNode.getNameNullSafe());
			}
			if (selectedNode != null) {
				selectedActivities.add(selectedNode);
			}
		}

		if (selectedActivities.size() < minimum) {
			return MessageFormat.format(selectionErrorMessage, minimum);
		}

		return null;
	}

	public List<Node> getSelectedIncoming() {
		return selectedIncoming;
	}

	public List<Node> getSelectedOutgoing() {
		return selectedOutgoing;
	}

	private void initialize() {
		getShell().setText("Select Activities");
		setTitle("Select Activities");
		setMessage("Please select below the activities to be used by the constraint.");

		String imagePath = model.getImagePath();
		Image image = ResourceManager.getPluginImage(Activator.getDefault(), imagePath);
		composite.getConstraintFigure().setImage(image);

		initializeComboViewer(composite.getIncomingActivitiesLabel(), composite.getIncomingActivitiesComposite(), incomingViewers,
				model.getMaximumIncoming());
		initializeComboViewer(composite.getOutgoingActivitiesLabel(), composite.getOutgoingActivitiesComposite(), outgoingViewers,
				model.getMaximumOutgoing());
	}

	public void initializeComboViewer(Label label, Composite parent, List<ComboViewer> toAdd, int maximumActivities) {
		if (maximumActivities == 0) {
			parent.setVisible(false);
			GridData compositeData = (GridData) parent.getLayoutData();
			compositeData.exclude = true;

			label.setVisible(false);
			GridData labelData = (GridData) label.getLayoutData();
			labelData.exclude = true;

			return;
		}

		for (int i = 0; i < maximumActivities; i++) {
			SelectActivityComposite selectActivityComposite = new SelectActivityComposite(parent, SWT.NONE);
			selectActivityComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			String activitiyLabel = "Activity " + (i + 1);
			selectActivityComposite.getActivityNameLabel().setText(activitiyLabel);

			final ComboViewer viewer = selectActivityComposite.getActivitySelectionViewer();
			viewer.setLabelProvider(model.createLabelProvider());
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setInput(model.getInput());
			toAdd.add(viewer);
			addValidationListener(viewer);
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					activitySelected();
				}
			});

			selectActivityComposite.getClearButton().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					clearSelection(viewer);
				}
			});
			selectActivityComposite.getClearButton().setEnabled(false);
		}
	}

	@Override
	protected void okPressed() {
		selectedIncoming = collectActivities(incomingViewers);
		selectedOutgoing = collectActivities(outgoingViewers);

		super.okPressed();
	}
}
