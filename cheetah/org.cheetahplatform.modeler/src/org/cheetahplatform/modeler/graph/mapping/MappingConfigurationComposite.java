package org.cheetahplatform.modeler.graph.mapping;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         20.10.2009
 */
public class MappingConfigurationComposite extends Composite {

	private ComboViewer processComboViewer;
	private TableViewer paragraphTableViewer;
	private TableViewer possibleNamesViewer;

	public MappingConfigurationComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite processSelectionComposite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		processSelectionComposite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.horizontalSpan = 2;
		processSelectionComposite.setLayoutData(layoutData);
		Label label = new Label(processSelectionComposite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		label.setText("Process");
		processComboViewer = new ComboViewer(processSelectionComposite, SWT.READ_ONLY);
		Combo combo = processComboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		createParagraphComposite();
		createPossibleNamesComposite();

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	private void createParagraphComposite() {
		Composite composite = new Composite(this, SWT.NONE);
		TableColumnLayout columnLayout = new TableColumnLayout();
		composite.setLayout(columnLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		paragraphTableViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.BORDER);
		Table table = paragraphTableViewer.getTable();
		table.setLinesVisible(true);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Paragraph");
		ColumnWeightData columnWeightData = new ColumnWeightData(100);
		columnLayout.setColumnData(column, columnWeightData);
	}

	private void createPossibleNamesComposite() {
		Composite composite = new Composite(this, SWT.NONE);
		TableColumnLayout columnLayout = new TableColumnLayout();
		composite.setLayout(columnLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		possibleNamesViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.BORDER);
		Table table = possibleNamesViewer.getTable();
		table.setLinesVisible(true);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Possible Activity Names");
		ColumnWeightData columnWeightData = new ColumnWeightData(100);
		columnLayout.setColumnData(column, columnWeightData);
	}

	/**
	 * Returns the paragraphTableViewer.
	 * 
	 * @return the paragraphTableViewer
	 */
	public TableViewer getParagraphTableViewer() {
		return paragraphTableViewer;
	}

	/**
	 * Returns the possibleNamesViewer.
	 * 
	 * @return the possibleNamesViewer
	 */
	public TableViewer getPossibleNamesViewer() {
		return possibleNamesViewer;
	}

	/**
	 * Returns the processComboViewer.
	 * 
	 * @return the processComboViewer
	 */
	public ComboViewer getProcessComboViewer() {
		return processComboViewer;
	}
}
