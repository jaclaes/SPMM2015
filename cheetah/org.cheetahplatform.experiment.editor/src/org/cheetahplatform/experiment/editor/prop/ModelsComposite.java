package org.cheetahplatform.experiment.editor.prop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class ModelsComposite extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Table table;
	private Button addBPMNButton;
	private Button addDecSerFlowButton;
	private Button addImageButton;
	public Table getTable() {
		return table;
	}

	public Button getAddBPMNButton() {
		return addBPMNButton;
	}

	public Button getAddDecSerFlowButton() {
		return addDecSerFlowButton;
	}

	public Button getAddImageButton() {
		return addImageButton;
	}

	public Button getEditButton() {
		return editButton;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public Button getUpButton() {
		return upButton;
	}

	public Button getDownButton() {
		return downButton;
	}

	private Button editButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;
	
	public ModelsComposite(IManagedForm managedForm) {
		super(managedForm.getForm().getBody(), SWT.NONE);
		init();
	}
	
	protected void init() {
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);

		setLayout(new GridLayout());

		GridData gridData = new GridData(660, SWT.DEFAULT);
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		setLayoutData(gridData);

		final Section attributesSection = toolkit.createSection(this, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		attributesSection.setDescription("Proces models for comprehension tasks");
		final GridData gridData_2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		attributesSection.setLayoutData(gridData_2);
		attributesSection.setText("Manage process models");

		final Composite mainComposite = toolkit.createComposite(attributesSection, SWT.NONE);
		attributesSection.setClient(mainComposite);
		final GridLayout gridLayout_1 = new GridLayout();
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		gridLayout_1.numColumns = 2;
		gridLayout_1.verticalSpacing = 25;
		mainComposite.setLayout(gridLayout_1);
		toolkit.paintBordersFor(mainComposite);

		Composite tableComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		GridLayout questionsGridLayout = new GridLayout(2, false);
		questionsGridLayout.horizontalSpacing = 15;
		tableComposite.setLayout(questionsGridLayout);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		table = toolkit.createTable(tableComposite, SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final GridData gridData_1 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData_1.minimumHeight = 200;
		table.setLayoutData(gridData_1);

		final Composite buttonComposite = toolkit.createComposite(tableComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true));
		buttonComposite.setLayout(new GridLayout());
		toolkit.paintBordersFor(buttonComposite);

		addBPMNButton = toolkit.createButton(buttonComposite, "Add BPMN", SWT.NONE);
		addBPMNButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		addDecSerFlowButton = toolkit.createButton(buttonComposite, "Add DecSerFlow", SWT.NONE);
		addDecSerFlowButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		addImageButton = toolkit.createButton(buttonComposite, "Add Image", SWT.NONE);
		addImageButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		editButton = toolkit.createButton(buttonComposite, "Edit", SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		removeButton = toolkit.createButton(buttonComposite, "Remove", SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		upButton = new Button(buttonComposite, SWT.NONE);
		upButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		toolkit.adapt(upButton, true, true);
		upButton.setText("Up");

		downButton = new Button(buttonComposite, SWT.NONE);
		downButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		toolkit.adapt(downButton, true, true);
		downButton.setText("Down");		
	}

}
