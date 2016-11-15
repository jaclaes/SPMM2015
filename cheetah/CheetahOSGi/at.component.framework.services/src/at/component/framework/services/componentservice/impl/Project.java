package at.component.framework.services.componentservice.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import at.component.IComponent;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectListener;

public class Project implements IProject {
	private String projectName;
	private IComponent projectComponent;
	private String saveDestination;
	private boolean dirty;
	private List<ProjectListener> listeners;

	public Project(String projectName, IComponent projectComponent) {
		this.projectName = projectName;
		this.projectComponent = projectComponent;
		listeners = new ArrayList<ProjectListener>();
	}

	private void addChildComponents(IComponent component, List<IComponent> components) {
		for (IComponent childComponent : component.getChildComponents()) {
			components.add(childComponent);
			addChildComponents(childComponent, components);
		}
	}

	@Override
	public List<IComponent> getComponents() {
		List<IComponent> components = new LinkedList<IComponent>();

		if (projectComponent != null) {
			components.add(projectComponent);
			addChildComponents(projectComponent, components);
		}

		return components;
	}

	@Override
	public IComponent getProjectComponent() {
		return projectComponent;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public String getSaveDestination() {
		return saveDestination;
	}

	public void setProjectComponent(IComponent projectComponent) {
		this.projectComponent = projectComponent;
	}

	@Override
	public void setSaveDestination(String saveDestination) {
		this.saveDestination = saveDestination;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			informListener();
		}
	}

	private void informListener() {
		for (ProjectListener listener : listeners)
			listener.dirtyChanged(this);
	}

	@Override
	public void addProjectListener(ProjectListener listener) {
		listeners.add(listener);
	}
}
