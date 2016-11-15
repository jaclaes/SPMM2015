package org.cheetahplatform.testarossa.dialog;

import java.util.List;

import org.cheetahplatform.common.IDeferredObjectProvider;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.model.ReminderLabelProvider;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.08.2009
 */
public class ReminderAggregationDialog extends TitleAreaDialog {

	private static final int CLOSE = 20003;
	private static final int DISMISS = 20001;
	private static final int SELECT_ALL = 20004;
	private static final int UNSELECT_ALL = 20005;

	private final List<IReminderInstance> reminders;
	private CheckboxTableViewer tableViewer;
	private final IDeferredObjectProvider<DeclarativeProcessInstance> provider;

	public ReminderAggregationDialog(Shell parentShell, IDeferredObjectProvider<DeclarativeProcessInstance> provider,
			List<IReminderInstance> reminders) {
		super(parentShell);

		Assert.isNotNull(reminders);
		this.provider = provider;
		this.reminders = reminders;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == DISMISS) {
			dismissSelectedReminder();
			return;
		} else if (buttonId == CLOSE) {
			setReturnCode(Window.OK);
			close();
			return;
		} else if (buttonId == SELECT_ALL) {
			tableViewer.setAllChecked(true);
			return;
		} else if (buttonId == UNSELECT_ALL) {
			tableViewer.setAllChecked(false);
			return;
		}
		throw new IllegalArgumentException("Unknwon button pressed: " + buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Reminders");
		newShell.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/reminder.png"));
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, SELECT_ALL, "Select All", false);
		createButton(parent, UNSELECT_ALL, "Deselect All", false);
		createButton(parent, DISMISS, "Dismiss/Activate", false);
		createButton(parent, CLOSE, "Close", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Reminders");
		setMessage("The following reminders were triggered.");

		Composite wrapper = new Composite(container, SWT.NONE);
		wrapper.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Table table = new Table(wrapper, SWT.CHECK);
		tableViewer = new CheckboxTableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ReminderLabelProvider(provider));
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText("Name");
		TableColumn descriptionColumn = new TableColumn(table, SWT.NONE);
		descriptionColumn.setText("Role");
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableColumnLayout.setColumnData(nameColumn, new ColumnWeightData(70));
		tableColumnLayout.setColumnData(descriptionColumn, new ColumnWeightData(30));
		wrapper.setLayout(tableColumnLayout);
		tableViewer.setInput(reminders);
		return container;
	}

	private void dismissSelectedReminder() {
		Object[] checkedElements = tableViewer.getCheckedElements();
		for (Object object : checkedElements) {
			IReminderInstance reminder = (IReminderInstance) object;
			reminder.setDismissed(!reminder.isDismissed());
		}

		tableViewer.refresh();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(650, 400);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
