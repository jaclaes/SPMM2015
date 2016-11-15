package org.cheetahplatform.tdm.modeler.test;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.swtdesigner.ResourceManager;

public class TDMTestEditorInput implements IEditorInput {

	private final TDMTest test;
	private final IPromLogger logger;

	public TDMTestEditorInput(TDMTest test, IPromLogger logger) {
		Assert.isNotNull(test);
		Assert.isNotNull(logger);

		this.test = test;
		this.logger = logger;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TDMTestEditorInput other = (TDMTestEditorInput) obj;
		if (test == null) {
			if (other.test != null) {
				return false;
			}
		} else if (!test.equals(other.test)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null; // no adapters provided
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/test_passed.gif");
	}

	/**
	 * @return the logger
	 */
	public IPromLogger getLogger() {
		return logger;
	}

	@Override
	public String getName() {
		return test.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * @return the test
	 */
	public TDMTest getTest() {
		return test;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logger == null) ? 0 : logger.hashCode());
		result = prime * result + ((test == null) ? 0 : test.hashCode());
		return result;
	}

}
