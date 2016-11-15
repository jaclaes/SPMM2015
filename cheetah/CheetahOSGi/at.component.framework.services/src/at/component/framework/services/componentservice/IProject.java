package at.component.framework.services.componentservice;

import java.util.List;

import at.component.IComponent;

public interface IProject {
	public List<IComponent> getComponents();

	public IComponent getProjectComponent();

	public String getProjectName();

	public String getSaveDestination();

	public void setSaveDestination(String saveDestination);
	
	public void setDirty(boolean dirty);
	
	public boolean isDirty();
	
	public void addProjectListener(ProjectListener listener);
}
