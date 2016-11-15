package org.cheetahplatform.tdm.daily.model;

import java.text.MessageFormat;

import org.cheetahplatform.modeler.Activator;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteActivityAction extends Action {
	public static final String ID = "org.cheetahplatform.tdm.daily.model.DeleteActivityAction";

	private final Activity activity;

	public DeleteActivityAction(Activity activity) {
		this.activity = activity;
		setId(ID);
		setText(MessageFormat.format("Delete ''{0}''", activity.getName()));
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete.gif"));
	}

	@Override
	public void run() {
		activity.delete();
	}
}
