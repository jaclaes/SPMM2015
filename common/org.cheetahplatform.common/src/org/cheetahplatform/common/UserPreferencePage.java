package org.cheetahplatform.common;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class UserPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private StringFieldEditor userNameFieldEditor;

	@Override
	protected void createFieldEditors() {
		userNameFieldEditor = new StringFieldEditor(CommonConstants.PREFERENCE_USER, "User", getFieldEditorParent());
		addField(userNameFieldEditor);
	}

	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

}
