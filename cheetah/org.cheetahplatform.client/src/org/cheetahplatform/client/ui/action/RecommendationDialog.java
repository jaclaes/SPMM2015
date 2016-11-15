package org.cheetahplatform.client.ui.action;

import java.util.List;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.LaunchActivityService;
import org.cheetahplatform.client.model.RecommendationModel;
import org.cheetahplatform.client.ui.editor.ActivityExecutionEditor;
import org.cheetahplatform.client.ui.editor.ActivityExecutionEditorInput;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.Recommendation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;

import com.swtdesigner.SWTResourceManager;

public class RecommendationDialog extends TitleAreaDialog {

	private static final int LAUNCH_ID = 20000;
	private RecommendationModel model;
	private TableViewer tableViewer;

	public RecommendationDialog(Shell parentShell, List<Recommendation> recommendations) {
		super(parentShell);
		Assert.isNotNull(recommendations);
		model = new RecommendationModel(recommendations);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == LAUNCH_ID) {
			launchActivity();
			return;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Recommendations");
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
		Button launchButton = createButton(parent, LAUNCH_ID, "Launch Activity", true);
		launchButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Recommendations");
		setMessage("Find the suggested activies below, ordered by their expected benefit.");
		if (model.getRecommendations().isEmpty()) {
			setErrorMessage("No recommendations available.");
		}
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(composite, SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		TableColumn activityColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		activityColumn.setText("Activity");
		tableColumnLayout.setColumnData(activityColumn, new ColumnWeightData(60));
		TableColumn doColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		doColumn.setText("Do Value");
		tableColumnLayout.setColumnData(doColumn, new ColumnWeightData(20));
		TableColumn dontColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		dontColumn.setText("Don't Value");
		tableColumnLayout.setColumnData(dontColumn, new ColumnWeightData(20));

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(model.createLabelProvider());
		tableViewer.setInput(model.getRecommendations());
		tableViewer.setSorter(model.createSorter());

		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = tableViewer.getSelection();
				getButton(LAUNCH_ID).setEnabled(!selection.isEmpty());
			}
		});

		return container;
	}

	private void launchActivity() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		Recommendation recommendation = (Recommendation) selection.getFirstElement();
		ActivityInstanceHandle activity = recommendation.getActivity();
		ProcessInstanceHandle processInstance = activity.getParent();
		try {
			LaunchActivityService service = new LaunchActivityService(processInstance, activity);
			IStatus status = service.synchronousRequest();
			if (status.equals(Status.OK_STATUS)) {
				ModificationService.broadcastChanges(activity, ModificationService.ACTIVITY_EXECUTED_PROPERTY);

				activity.setId(service.getNewActivityInstanceId());
				ActivityExecutionEditorInput input = new ActivityExecutionEditorInput(processInstance, activity);
				Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.openEditor(input, ActivityExecutionEditor.id);
				okPressed();
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Failed to execute activity " + activity.getName(), e);
		} catch (PartInitException e) {
			UiUtils.showAndLogError("Failed to open editor for activity " + activity.getName(), e);
		}
	}
}
