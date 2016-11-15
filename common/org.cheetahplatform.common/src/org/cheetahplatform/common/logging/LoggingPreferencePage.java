/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging;

import java.text.MessageFormat;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.Messages;
import org.cheetahplatform.common.logging.db.ConnectionSetting;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Preference page for configuring the logging of Alaska.
 * 
 * @author Jakob Pinggera <br>
 *         Michael Schier<br>
 *         Stefan Zugal <br>
 * 
 */
public class LoggingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String Id = "org.alaskasimulator.ui.LoggingPreferencePage"; //$NON-NLS-1$

	private StringFieldEditor uriFieldEditor;
	private StringFieldEditor userFieldEditor;
	private StringFieldEditor passwordFieldEditor;
	private Button testConnectionButton;

	private LoggingType loggingType;

	private StringFieldEditor portFieldEditor;
	private StringFieldEditor schemaFieldEditor;

	private RadioGroupFieldEditor loggingTypeFieldEditor;
	private IDatabaseConnector databaseConnector;

	private Button selectPredefinedConnectionButton;

	/**
	 * Creates a new preference page.
	 */
	public LoggingPreferencePage() {
		super(FieldEditorPreferencePage.GRID);

		databaseConnector = Activator.getDatabaseConnector();
	}

	/**
	 * Sets the enabled state of the element, which are responsible for gathering data for the database url.
	 * 
	 * @param enabled
	 *            <code>true</code> if the elements should be enabled, <code>false</code> otherwise
	 */
	private void activateDatabaseElement(boolean enabled) {
		uriFieldEditor.getTextControl(getFieldEditorParent()).setEnabled(enabled);
		portFieldEditor.getTextControl(getFieldEditorParent()).setEnabled(enabled);
		userFieldEditor.getTextControl(getFieldEditorParent()).setEnabled(enabled);
		passwordFieldEditor.getTextControl(getFieldEditorParent()).setEnabled(enabled);
		schemaFieldEditor.getTextControl(getFieldEditorParent()).setEnabled(enabled);
		testConnectionButton.setEnabled(enabled);
		selectPredefinedConnectionButton.setEnabled(enabled);
	}

	@Override
	protected void createFieldEditors() {
		loggingTypeFieldEditor = new RadioGroupFieldEditor(CommonConstants.PREFERENCE_LOGGING_TYPE, Messages.LoggingPreferencePage_7, 2,
				new String[][] { { Messages.LoggingPreferencePage_8, CommonConstants.PREFERENCE_XML_LOGGING_TYPE },
						{ Messages.LoggingPreferencePage_9, CommonConstants.PREFERENCE_DB_LOGGING_TYPE } }, getFieldEditorParent());
		addField(loggingTypeFieldEditor);

		uriFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_HOST, Messages.LoggingPreferencePage_10, getFieldEditorParent());
		addField(uriFieldEditor);
		portFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_PORT, Messages.LoggingPreferencePage_0, getFieldEditorParent());
		addField(portFieldEditor);
		userFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_USER_DB_NAME, Messages.LoggingPreferencePage_11,
				getFieldEditorParent());
		addField(userFieldEditor);
		passwordFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_PASSWORD, Messages.LoggingPreferencePage_12,
				getFieldEditorParent());
		passwordFieldEditor.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwordFieldEditor);
		schemaFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_SCHEMA, Messages.LoggingPreferencePage_1,
				getFieldEditorParent());
		addField(schemaFieldEditor);

		selectPredefinedConnectionButton = new Button(getFieldEditorParent(), SWT.NONE);
		selectPredefinedConnectionButton.setText("Select Predefined Connection...");
		selectPredefinedConnectionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectPredefinedConnection();
			}
		});

		testConnectionButton = new Button(getFieldEditorParent(), SWT.NONE);
		testConnectionButton.setText(Messages.LoggingPreferencePage_2);
		testConnectionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testConnection();
			}
		});

		boolean dbLogging = CommonConstants.PREFERENCE_DB_LOGGING_TYPE.equals(getPreferenceStore().getString(
				CommonConstants.PREFERENCE_LOGGING_TYPE));
		if (dbLogging)
			loggingType = LoggingType.DATABASE;
		else
			loggingType = LoggingType.XML;
		activateDatabaseElement(dbLogging);
	}

	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	@Override
	public boolean performOk() {
		boolean result = super.performOk();

		if (LoggingType.DATABASE.equals(loggingType)) {
			databaseConnector.setDatabaseURL(DatabaseUtil.createDatabaseUrl(uriFieldEditor.getStringValue(),
					portFieldEditor.getStringValue(), schemaFieldEditor.getStringValue()));
			databaseConnector.setDefaultCredentials(userFieldEditor.getStringValue(), passwordFieldEditor.getStringValue());
			databaseConnector.setAdminCredentials(userFieldEditor.getStringValue(), passwordFieldEditor.getStringValue());
		}

		Activator.getDefault().setLoggingType(loggingType);

		return result;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (CommonConstants.PREFERENCE_DB_LOGGING_TYPE.equals(event.getNewValue())) {
			loggingType = LoggingType.DATABASE;
			activateDatabaseElement(true);
		} else if (CommonConstants.PREFERENCE_XML_LOGGING_TYPE.equals(event.getNewValue())) {
			loggingType = LoggingType.XML;
			activateDatabaseElement(false);
		}

		super.propertyChange(event);
	}

	protected void selectPredefinedConnection() {
		SelectPredefinedConnectionDialog dialog = new SelectPredefinedConnectionDialog(getShell());
		if (dialog.open() != Window.OK) {
			return;
		}

		ConnectionSetting connection = dialog.getConnection();
		uriFieldEditor.setStringValue(connection.getHost());
		portFieldEditor.setStringValue(connection.getPort());
		userFieldEditor.setStringValue(connection.getAdminUsername());
		passwordFieldEditor.setStringValue(connection.getAdminPassword());
		schemaFieldEditor.setStringValue(connection.getSchema());
		loggingTypeFieldEditor.setPreferenceName(CommonConstants.PREFERENCE_DB_LOGGING_TYPE);
	}

	/**
	 * Tests the database connection.
	 */
	protected void testConnection() {
		performApply();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		try {
			databaseConnector.getDatabaseConnection();
			databaseConnector.closeConnection();
			MessageDialog.openInformation(shell, Messages.LoggingPreferencePage_3, Messages.LoggingPreferencePage_4);
		} catch (Exception e) {
			MessageDialog.openError(shell, Messages.LoggingPreferencePage_5,
					MessageFormat.format(Messages.LoggingPreferencePage_6, e.getMessage()));
		}
	}
}
