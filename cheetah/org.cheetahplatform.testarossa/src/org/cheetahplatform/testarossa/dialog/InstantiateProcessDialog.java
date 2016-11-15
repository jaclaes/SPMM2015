package org.cheetahplatform.testarossa.dialog;

import java.util.Calendar;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.composite.InstantiateProcessComposite;
import org.cheetahplatform.testarossa.model.InstanceSelectionDialogModel;
import org.cheetahplatform.testarossa.persistence.PersistentTestaRossaModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class InstantiateProcessDialog extends LocationPersistentTitleAreaDialog {
	private class DateSelectionListener implements Runnable {

		public void run() {
			validate();
		}

	}

	private static final int DIFFERENCE_LOCATION_SELECTION_OPENING = 6;

	private InstantiateProcessComposite composite;
	private InstanceSelectionDialogModel model;
	private DeclarativeProcessSchema process;
	private String name;
	private DateSelectionFieldEditor choiceOfLocationEditor;
	private DateSelectionFieldEditor openingEditor;
	private Calendar choiceOfLocationDate;
	private Calendar openingDate;

	private SelectRoleController frMitterrutznerRoleController;
	private SelectRoleController hrPetzoldRoleController;
	private Role frMitterrutznerRole;
	private Role hrPetzoldRole;

	public InstantiateProcessDialog(Shell parentShell) {
		super(parentShell);

		model = new InstanceSelectionDialogModel();
	}

	private void addListener() {
		composite.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				validate();
			}
		});

		composite.getNameTextWidget().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validate();
			}
		});

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		getButton(OK).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);

		composite = new InstantiateProcessComposite(realParent, SWT.NONE);
		setTitle("Select the Process Below");
		setMessage("Below all available processes are listed. ");
		setTitleImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/testarossa_100x100.gif"));
		getShell().setText("Instantiate Process");

		initialize();

		return realParent;
	}

	/**
	 * Return the choiceOfLocationDate.
	 * 
	 * @return the choiceOfLocationDate
	 */
	public Calendar getChoiceOfLocationDate() {
		return choiceOfLocationDate;
	}

	/**
	 * @return the frMiterrutznerRole
	 */
	public Role getFrMitterrutznerRole() {
		return frMitterrutznerRole;
	}

	/**
	 * @return the hrPetzoldRole
	 */
	public Role getHrPetzoldRole() {
		return hrPetzoldRole;
	}

	/**
	 * Return the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the openingDate.
	 * 
	 * @return the openingDate
	 */
	public Calendar getOpeningDate() {
		return openingDate;
	}

	/**
	 * Return the process.
	 * 
	 * @return the process
	 */
	public DeclarativeProcessSchema getProcess() {
		return process;
	}

	private void initialize() {
		initializeDates();

		TreeViewer viewer = composite.getViewer();
		viewer.setContentProvider(model.createContentProvider(false));
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setSorter(model.createSorter());
		List<DeclarativeProcessSchema> allProcesses = model.getAllProcesses();
		viewer.setInput(allProcesses);

		if (!allProcesses.isEmpty()) {
			viewer.setSelection(new StructuredSelection(allProcesses.get(0)));
		}

		Role frMitterrutzner = RoleLookup.getInstance().getRole(PersistentTestaRossaModel.FR_MITTERRUTZNER);
		frMitterrutznerRoleController = new SelectRoleController(frMitterrutzner, composite.getFrMitterrutznerComposite());

		Role hrPetzold = RoleLookup.getInstance().getRole(PersistentTestaRossaModel.HR_PETZOLD);
		hrPetzoldRoleController = new SelectRoleController(hrPetzold, composite.getHrPetzoldComposite());

		addListener();
	}

	private void initializeDates() {
		Calendar locationSelectionDate = Calendar.getInstance();
		int offset = 1;
		locationSelectionDate.add(Calendar.WEEK_OF_YEAR, offset);
		choiceOfLocationEditor = new DateSelectionFieldEditor(composite.getChoiceOfLocationComposite(), locationSelectionDate,
				new DateSelectionListener());
		Calendar date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, offset + DIFFERENCE_LOCATION_SELECTION_OPENING);
		openingEditor = new DateSelectionFieldEditor(composite.getOpeningComposite(), date, new DateSelectionListener());
	}

	@Override
	protected void okPressed() {
		name = composite.getNameTextWidget().getText();
		process = (DeclarativeProcessSchema) ((IStructuredSelection) composite.getViewer().getSelection()).getFirstElement();
		choiceOfLocationDate = choiceOfLocationEditor.getSelection();
		openingDate = openingEditor.getSelection();
		frMitterrutznerRole = frMitterrutznerRoleController.getSelection();
		hrPetzoldRole = hrPetzoldRoleController.getSelection();

		super.okPressed();
	}

	@Override
	protected void validate() {
		ISelection selection = composite.getViewer().getSelection();
		if (selection.isEmpty()) {
			setErrorMessage("Please select a process.");
			return;
		}
		if (composite.getNameTextWidget().getText().trim().length() == 0) {
			setErrorMessage("Please enter a name.");
			return;
		}
		if (choiceOfLocationEditor.getSelection() == null) {
			setErrorMessage("Please select a date for the choice of location.");
			return;
		}
		if (openingEditor.getSelection() == null) {
			setErrorMessage("Please select a date for the opening.");
			return;
		}

		long differenceInMinutes = (openingEditor.getSelection().getTimeInMillis() - choiceOfLocationEditor.getSelection()
				.getTimeInMillis()) / 60000;
		long differenceInWeeks = differenceInMinutes / (60 * 24 * 7);
		if (differenceInWeeks < DIFFERENCE_LOCATION_SELECTION_OPENING) {
			setErrorMessage("Difference of choice of location and date for opening must be at least "
					+ DIFFERENCE_LOCATION_SELECTION_OPENING + " weeks.");
			return;
		}

		setErrorMessage(null);
		getButton(OK).setEnabled(true);
	}
}
