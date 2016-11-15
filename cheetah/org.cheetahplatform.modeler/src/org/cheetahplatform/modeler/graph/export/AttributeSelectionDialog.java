package org.cheetahplatform.modeler.graph.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.ui.CustomCheckboxTreeViewer;
import org.cheetahplatform.survey.SurveyProcessInformation;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.12.2009
 */
public class AttributeSelectionDialog extends TitleAreaDialog {

	private class DurationFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return !((String) element).contains(SurveyProcessInformation.DURATION_ATTRIBUTE);
		}
	}

	private class SelectAttributeContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}
	}

	private static final String EXPORT_MODELING_PROCESSES = "export_modeling_processes";
	private static final String EXPORT_EXPERIMENTAL_PROCESSES = "export_experimental_processes";
	private static final String SELECTED_ATTRIBUTES = "selected_attributes";
	private AttributeSelectionComposite composite;
	private final List<String> attributes;
	private List<String> selectedAttributes;
	private boolean exportExperimentalProcesses;

	private boolean exportModelingProcesses;
	private DurationFilter filter;

	public AttributeSelectionDialog(Shell parentShell, List<String> attributes) {
		super(parentShell);
		Assert.isNotNull(attributes);
		this.attributes = new ArrayList<String>(attributes);
		selectedAttributes = new ArrayList<String>();
		filter = new DurationFilter();
	}

	private void addListeners() {
		composite.getExportButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeDialog();
			}
		});

		composite.getSelectButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkAllElements();
			}
		});

		composite.getUpButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveUp();
			}
		});

		composite.getDownButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveDown();
			}
		});

		composite.getFilterDurations().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CustomCheckboxTreeViewer viewer = composite.getAttributeViewer();
				ViewerFilter[] filters = viewer.getFilters();
				List<ViewerFilter> filterList = Arrays.asList(filters);
				if (filterList.contains(filter)) {
					viewer.removeFilter(filter);
				} else {
					viewer.addFilter(filter);
				}
			}
		});
	}

	private void checkAllElements() {
		composite.getAttributeViewer().setCheckedElements(attributes.toArray());
		composite.getAttributeViewer().refresh();
	}

	private void closeDialog() {
		Object[] checkedElements = composite.getAttributeViewer().getCheckedElements();
		for (Object object : checkedElements) {
			selectedAttributes.add((String) object);
		}

		exportExperimentalProcesses = composite.getExportExperimentalProcessesButton().getSelection();
		exportModelingProcesses = composite.getExportModelingProcessesButton().getSelection();
		saveSettings();

		okPressed();
	}

	private Object[] computeInitiallyCheckedAttributes() {
		List<Object> initiallyChecked = new ArrayList<Object>();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String selection = store.getString(SELECTED_ATTRIBUTES);
		List<Attribute> toSelect = DatabaseUtil.fromDataBaseRepresentation(selection);

		if (!toSelect.isEmpty()) {
			for (Attribute attribute : toSelect) {
				initiallyChecked.add(attribute.getContent());
			}

			return initiallyChecked.toArray();
		}

		Set<String> toBeChecked = new HashSet<String>();
		toBeChecked.add(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE);

		for (String attribute : attributes) {
			if (toBeChecked.contains(attribute)) {
				initiallyChecked.add(attribute);
			}
		}

		return initiallyChecked.toArray();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Attribute Selection");
		super.configureShell(newShell);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// no buttons
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(CommonConstants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		setTitle("Select Attributes");
		setMessage("Please select all attributes that should be exported.");
		composite = new AttributeSelectionComposite(container, SWT.NONE);

		composite.getAttributeViewer().setLabelProvider(new LabelProvider());
		composite.getAttributeViewer().setContentProvider(new SelectAttributeContentProvider());
		composite.getAttributeViewer().setInput(attributes);
		composite.getAttributeViewer().setCheckedElements(computeInitiallyCheckedAttributes());

		addListeners();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean exportExperimental = store.getBoolean(EXPORT_EXPERIMENTAL_PROCESSES);
		if (store.isDefault(EXPORT_EXPERIMENTAL_PROCESSES)) {
			exportExperimental = true;
		}

		boolean exportModeling = store.getBoolean(EXPORT_MODELING_PROCESSES);
		if (store.isDefault(EXPORT_MODELING_PROCESSES)) {
			exportModeling = true;
		}

		composite.getExportExperimentalProcessesButton().setSelection(exportExperimental);
		composite.getExportModelingProcessesButton().setSelection(exportModeling);

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 700);
	}

	/**
	 * Returns the selectedAttributes.
	 * 
	 * @return the selectedAttributes
	 */
	public List<String> getSelectedAttributes() {
		return selectedAttributes;
	}

	private String getSelectedElement() {
		IStructuredSelection selection = (IStructuredSelection) composite.getAttributeViewer().getSelection();
		if (selection.isEmpty()) {
			return null;
		}

		String element = (String) selection.getFirstElement();
		return element;
	}

	public boolean isExportExperimentalProcesses() {
		return exportExperimentalProcesses;
	}

	public boolean isExportModelingProcesses() {
		return exportModelingProcesses;
	}

	protected void moveDown() {
		String element = getSelectedElement();
		if (element == null) {
			return;
		}

		int oldIndex = attributes.indexOf(element);
		if (oldIndex == attributes.size() - 1) {
			return;
		}
		attributes.remove(element);
		attributes.add(oldIndex + 1, element);
		composite.getAttributeViewer().refresh();
	}

	protected void moveUp() {
		String element = getSelectedElement();
		if (element == null) {
			return;
		}

		int oldIndex = attributes.indexOf(element);
		if (oldIndex == 0) {
			return;
		}
		attributes.remove(element);
		attributes.add(oldIndex - 1, element);
		composite.getAttributeViewer().refresh();
	}

	private void saveSettings() {
		List<Attribute> selection = new ArrayList<Attribute>();
		for (String attribute : selectedAttributes) {
			selection.add(new Attribute("", attribute));
		}

		String toSave = DatabaseUtil.toDatabaseRepresentation(selection);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.putValue(SELECTED_ATTRIBUTES, toSave);
		store.setValue(EXPORT_EXPERIMENTAL_PROCESSES, exportExperimentalProcesses);
		store.setValue(EXPORT_MODELING_PROCESSES, exportModelingProcesses);
	}
}
