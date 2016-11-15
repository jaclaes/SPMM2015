package org.cheetahplatform.modeler.graph.export;

import org.cheetahplatform.common.ui.CustomCheckboxTreeViewer;
import org.cheetahplatform.common.ui.composite.MultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.12.2009
 */
public class AttributeSelectionComposite extends Composite {

	private MultiLineButton upButton;
	private MultiLineButton downButton;
	private MultiLineButton selectButton;
	private MultiLineButton exportButton;
	private CustomCheckboxTreeViewer attributeViewer;
	private Group group;
	private Button exportExperimentalProcessesButton;
	private Button exportModelingProcessesButton;
	private MultiLineButton filterDurations;

	public AttributeSelectionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group.setLayout(new GridLayout(1, false));

		exportExperimentalProcessesButton = new Button(group, SWT.CHECK);
		exportExperimentalProcessesButton.setSelection(true);
		exportExperimentalProcessesButton.setText("Export Experimental Processes");

		exportModelingProcessesButton = new Button(group, SWT.CHECK);
		exportModelingProcessesButton.setSelection(true);
		exportModelingProcessesButton.setText("Export Modeling Processes");
		new Label(this, SWT.NONE);
		Tree table = new Tree(this, SWT.CHECK | SWT.BORDER | SWT.MULTI);
		attributeViewer = new CustomCheckboxTreeViewer(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));

		upButton = new MultiLineButton(this, SWT.NONE, "up", ResourceManager.getPluginImage(Activator.getDefault(),
				"img/lc_arrowshapes_up.png"));
		downButton = new MultiLineButton(this, SWT.NONE, "down", ResourceManager.getPluginImage(Activator.getDefault(),
				"img/lc_arrowshapes_down.png"));
		selectButton = new MultiLineButton(this, SWT.NONE, "select all", ResourceManager.getPluginImage(Activator.getDefault(),
				"img/24-em-check.png"));
		filterDurations = new MultiLineButton(this, SWT.CHECK, "filter durations", ResourceManager.getPluginImage(Activator.getDefault(),
				"img/duration_16x16.png"));
		exportButton = new MultiLineButton(this, SWT.NONE, "export", ResourceManager.getPluginImage(Activator.getDefault(),
				"img/data_export24.png"));
		Label placeholder = new Label(this, SWT.NONE);
		placeholder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
	}

	/**
	 * Returns the attributeViewer.
	 * 
	 * @return the attributeViewer
	 */
	public CustomCheckboxTreeViewer getAttributeViewer() {
		return attributeViewer;
	}

	/**
	 * Returns the downButton.
	 * 
	 * @return the downButton
	 */
	public MultiLineButton getDownButton() {
		return downButton;
	}

	/**
	 * Returns the exportButton.
	 * 
	 * @return the exportButton
	 */
	public MultiLineButton getExportButton() {
		return exportButton;
	}

	public Button getExportExperimentalProcessesButton() {
		return exportExperimentalProcessesButton;
	}

	public Button getExportModelingProcessesButton() {
		return exportModelingProcessesButton;
	}

	/**
	 * @return the filterDurations
	 */
	public MultiLineButton getFilterDurations() {
		return filterDurations;
	}

	/**
	 * Returns the selectButton.
	 * 
	 * @return the selectButton
	 */
	public MultiLineButton getSelectButton() {
		return selectButton;
	}

	/**
	 * Returns the upButton.
	 * 
	 * @return the upButton
	 */
	public MultiLineButton getUpButton() {
		return upButton;
	}
}
