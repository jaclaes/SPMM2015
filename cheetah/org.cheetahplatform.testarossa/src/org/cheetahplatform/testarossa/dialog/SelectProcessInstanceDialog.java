package org.cheetahplatform.testarossa.dialog;

import java.util.Calendar;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.composite.TreeSelectionComposite;
import org.cheetahplatform.testarossa.model.InstanceSelectionDialogModel;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectProcessInstanceDialog extends LocationPersistentTitleAreaDialog {

	private static final int INSTANTIATE = 1000;

	private TreeSelectionComposite composite;
	private InstanceSelectionDialogModel model;
	private DeclarativeProcessInstance selection;

	public SelectProcessInstanceDialog(Shell parentShell) {
		super(parentShell);

		model = new InstanceSelectionDialogModel();
	}

	private void addListener() {
		composite.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				validateInput();
			}
		});

		composite.getViewer().addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				if (validateInput()) {
					okPressed();
				}
			}
		});
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);

		if (buttonId == INSTANTIATE) {
			InstantiateProcessDialog dialog = new InstantiateProcessDialog(getShell());

			if (dialog.open() == Window.OK) {
				DeclarativeProcessSchema process = dialog.getProcess();
				String name = dialog.getName();
				Calendar choiceOfLocationDate = dialog.getChoiceOfLocationDate();
				Calendar openingDate = dialog.getOpeningDate();
				Role hrPetzoldRole = dialog.getHrPetzoldRole();
				Role frMitterrutznerRole = dialog.getFrMitterrutznerRole();
				DeclarativeProcessInstance newInstance = model.instantiateProcess(process, name, choiceOfLocationDate, openingDate,
						hrPetzoldRole, frMitterrutznerRole);

				refresh();

				composite.getViewer().setSelection(new StructuredSelection(newInstance));
				getButton(OK).setFocus();
			}
		}
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = ((Composite) super.createButtonBar(parent)).getChildren()[0];
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		((GridLayout) ((Composite) control).getLayout()).makeColumnsEqualWidth = false;
		validateInput();
		return control;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button newInstance = createButton(parent, INSTANTIATE, "New Instance", false);
		newInstance.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, true, false));

		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new TreeSelectionComposite(realParent, SWT.NONE);
		setTitle("Select the Process Instance Below");
		setMessage("Below all available process instances are listed. ");
		setTitleImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/testarossa_100x100.gif"));
		getShell().setText("Process Instance Selection");

		initialize();

		return realParent;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}

	/**
	 * Return the selection.
	 * 
	 * @return the selection
	 */
	public DeclarativeProcessInstance getSelection() {
		return selection;
	}

	private void initialize() {
		TreeViewer viewer = composite.getViewer();
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setContentProvider(model.createContentProvider(true));
		viewer.setSorter(model.createSorter());
		refresh();

		addListener();
	}

	@Override
	protected void okPressed() {
		selection = (DeclarativeProcessInstance) ((IStructuredSelection) composite.getViewer().getSelection()).getFirstElement();
		model.storeLastSelectedInstance(selection);

		super.okPressed();
	}

	private void refresh() {
		TreeViewer viewer = composite.getViewer();
		viewer.setInput(model.getAllProcesses());
		viewer.expandAll();

		viewer.setSelection(model.getLastSelectedInstance());
	}

	protected boolean validateInput() {
		IStructuredSelection selection = (IStructuredSelection) composite.getViewer().getSelection();
		boolean valid = selection.getFirstElement() instanceof DeclarativeProcessInstance;
		getButton(OK).setEnabled(valid);

		return valid;
	}
}
