package org.cheetahplatform.testarossa;

import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.shared.ListenerList;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.testarossa.persistence.PersistentModel;

public class TestaRossaModel {
	private static TestaRossaModel INSTANCE;

	public static TestaRossaModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TestaRossaModel();
		}

		return INSTANCE;
	}

	private ListenerList listeners;
	private DeclarativeProcessInstance currentInstance;

	private TestaRossaModel() {
		listeners = new ListenerList();
	}

	public void addListener(IInstanceChangedListener listener) {
		listeners.add(listener);
	}

	/**
	 * Return the currentInstance.
	 * 
	 * @return the currentInstance
	 */
	public DeclarativeProcessInstance getCurrentInstance() {
		return currentInstance;
	}

	public Workspace getCurrentWorkspace() {
		DeclarativeProcessInstance instance = TestaRossaModel.getInstance().getCurrentInstance();
		return PersistentModel.getInstance().getWorkspace(instance);
	}

	public boolean isInstanceSet() {
		return currentInstance != null;
	}

	public void removeListener(IInstanceChangedListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Set the currentInstance.
	 * 
	 * @param currentInstance
	 *            the currentInstance to set
	 */
	public void setCurrentInstance(DeclarativeProcessInstance currentInstance) {
		this.currentInstance = currentInstance;
		Object[] listeners2 = listeners.getListeners();
		for (Object listener : listeners2) {
			((IInstanceChangedListener) listener).instanceChanged();
		}
	}

}
