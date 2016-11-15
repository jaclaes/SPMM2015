package org.cheetahplatform.testarossa.action;

import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.persistence.PersistentModel;
import org.eclipse.jface.action.Action;

public class SaveAction extends Action {
	public static final String ID = "org.cheetahplatform.testarossa.actions.Save";

	public SaveAction() {
		setToolTipText("Save");
		setId(ID);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/save.gif"));
	}

	@Override
	public void run() {
		PersistentModel.save();
	}
}
