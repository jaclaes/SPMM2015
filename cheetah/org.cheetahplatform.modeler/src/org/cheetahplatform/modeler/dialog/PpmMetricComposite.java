package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class PpmMetricComposite extends Composite {
	private Text modelIdText;
	private Text workflowIdText;
	private Text processText;
	private Table table;
	private TableViewer metricTableViewer;

	public PpmMetricComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setBackground(SWTResourceManager.getColor(255, 255, 255));
		setBackgroundMode(SWT.INHERIT_FORCE);

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Model Id");

		modelIdText = new Text(this, SWT.BORDER);
		modelIdText.setEditable(false);
		modelIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Workflow Id");

		workflowIdText = new Text(this, SWT.BORDER);
		workflowIdText.setEditable(false);
		workflowIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Process");

		processText = new Text(this, SWT.BORDER);
		processText.setEditable(false);
		processText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPpmMetrics = new Label(this, SWT.NONE);
		lblPpmMetrics.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPpmMetrics.setText("PPM Metrics");

		Composite grpPpmMetrics = new Composite(this, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		grpPpmMetrics.setLayout(tableColumnLayout);
		grpPpmMetrics.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		metricTableViewer = new TableViewer(grpPpmMetrics, SWT.BORDER | SWT.FULL_SELECTION);
		table = metricTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn metricColumn = new TableColumn(table, SWT.NONE);
		metricColumn.setWidth(100);
		metricColumn.setText("Metric");
		tableColumnLayout.setColumnData(metricColumn, new ColumnWeightData(80));

		TableColumn valueColumn = new TableColumn(table, SWT.NONE);
		valueColumn.setWidth(100);
		valueColumn.setText("Value");
		tableColumnLayout.setColumnData(valueColumn, new ColumnWeightData(20));
	}

	public TableViewer getMetricTableViewer() {
		return metricTableViewer;
	}

	public Text getModelIdText() {
		return modelIdText;
	}

	public Text getProcessText() {
		return processText;
	}

	public Text getWorkflowIdText() {
		return workflowIdText;
	}
}
