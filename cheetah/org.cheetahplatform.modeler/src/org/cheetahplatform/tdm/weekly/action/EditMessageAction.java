/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.action;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.dialog.EditMessageDialog;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

public class EditMessageAction extends Action {

	private final WeeklyActivity activity;

	public EditMessageAction(WeeklyActivity activity) {
		Assert.isNotNull(activity);
		this.activity = activity;
		setText("Edit Note");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/message.gif"));
	}

	@Override
	public void run() {
		EditMessageDialog dialog = new EditMessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), activity
				.getMessage());
		if (dialog.open() == Window.OK) {
			activity.setMessage(dialog.getMessage());
		}
	}
}
