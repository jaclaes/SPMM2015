package org.cheetahplatform.modeler.generic.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.shared.ListenerList;
import org.eclipse.gef.EditPart;

public abstract class GenericModel implements IGenericModel {

	protected IGenericModel parent;
	private final PropertyChangeSupport changeSupport;
	private ListenerList dirtyListener;

	protected GenericModel(IGenericModel parent) {
		this.parent = parent;
		this.changeSupport = new PropertyChangeSupport(this);
		this.dirtyListener = new ListenerList();
	}

	public void addDirtyListener(IDirtyListener listener) {
		dirtyListener.add(listener);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void dirty() {
		for (Object listener : dirtyListener.getListeners()) {
			((IDirtyListener) listener).dirty();
		}
	}

	protected void firePropertyChanged(Class<? extends GenericModel> target, String property) {
		IGenericModel parentByType = getParentType(target);
		parentByType.firePropertyChanged(property);
	}

	protected void firePropertyChanged(PropertyChangeEvent evt) {
		changeSupport.firePropertyChange(evt);
	}

	@Override
	public void firePropertyChanged(String property) {
		changeSupport.firePropertyChange(property, true, false);
	}

	public void firePropertyChanged(String property, EditPart exclude, boolean value) {
		PropertyChangeListener[] listeners = changeSupport.getPropertyChangeListeners();
		for (PropertyChangeListener listener : listeners) {
			if (listener.equals(exclude)) {
				continue;
			}

			listener.propertyChange(new PropertyChangeEvent(parent, property, null, value));
		}
	}

	@Override
	public List<? extends Object> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public IGenericModel getParent() {
		return parent;
	}

	public IGenericModel getParentType(Class<?> target) {
		IGenericModel model = this;
		while (!target.isAssignableFrom(model.getClass())) {
			model = model.getParent();
		}

		return model;
	}

	public GenericModel getRoot() {
		IGenericModel model = this;
		while (model.getParent() != null) {
			model = model.getParent();
		}

		return (GenericModel) model;
	}

	@Override
	public List<? extends Object> getSourceConnections() {
		return Collections.emptyList();
	}

	@Override
	public List<? extends Object> getTargetConnections() {
		return Collections.emptyList();
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void setParent(IGenericModel parent) {
		Assert.isNotNull(parent);
		this.parent = parent;
	}

}
