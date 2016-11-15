package org.cheetahplatform.testarossa.action;

import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.IInstanceChangedListener;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.eclipse.jface.action.Action;

public abstract class ProcessInstanceSpecificAction extends Action implements IInstanceChangedListener {
	public ProcessInstanceSpecificAction(String id, String image, String disabledImage) {
		this(id, image, disabledImage, AS_PUSH_BUTTON);
	}

	public ProcessInstanceSpecificAction(String id, String image, String disabledImage, int style) {
		super("", style);

		setId(id);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), image));
		if (disabledImage != null) {
			setDisabledImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), disabledImage));
		}

		TestaRossaModel.getInstance().addListener(this);
	}

	public void instanceChanged() {
		firePropertyChange(ENABLED, isEnabled(), !isEnabled());
	}

	@Override
	public boolean isEnabled() {
		return TestaRossaModel.getInstance().isInstanceSet();
	}

}
