package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.ui.CustomCheckboxTreeViewer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class SelectXMLLogConvertDialog extends TitleAreaDialog {
	private List<XMLLog> xmlLogsToConvert;
	private CustomCheckboxTreeViewer treeViewer;
	private XMLLogModel model;

	public SelectXMLLogConvertDialog(Shell parentShell) {
		super(parentShell);
		xmlLogsToConvert = new ArrayList<XMLLog>();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Select XML Logs to Convert");

		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Select XML Logs to Convert");
		setMessage("Are you sure that you want to convert the logged XML files to database-based ones?\nIf some of the instance have already been converted, this may mess up your database with lots of duplicates...");

		try {
			model = new XMLLogModel();
		} catch (Exception e) {
			Activator.logError("Could not load xml logs", e);
			MessageDialog.openError(getShell(), "Error", "Could not load xml logs.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer = new CustomCheckboxTreeViewer(composite, SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		Tree table = treeViewer.getTree();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TreeColumn idColumn = new TreeColumn(table, SWT.NONE);
		idColumn.setText("id");
		idColumn.setWidth(100);
		TreeColumn timestampColumn = new TreeColumn(table, SWT.NONE);
		timestampColumn.setText("Server Timestamp");
		timestampColumn.setWidth(200);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setContentProvider(model.getContentProvider());
		treeViewer.setLabelProvider(model.getLabelProvider());
		treeViewer.setInput(model.getLogs());
		treeViewer.setCheckedElements(model.getCheckedElements());

		return container;
	}

	public List<XMLLog> getXmlLogsToConvert() {
		return xmlLogsToConvert;
	}

	@Override
	protected void okPressed() {
		if (MessageDialog
				.openQuestion(
						Display.getDefault().getActiveShell(),
						"Convert XML Files?",
						"Are you sure that you want to convert the logged XML files to database-based ones?\nIf some of the instance have already been converted, this may mess up your database with lots of duplicates...")) {

			Object[] checkedElements = treeViewer.getCheckedElements();
			for (Object object : checkedElements) {
				xmlLogsToConvert.add((XMLLog) object);
			}
		}

		super.okPressed();
	}
}
