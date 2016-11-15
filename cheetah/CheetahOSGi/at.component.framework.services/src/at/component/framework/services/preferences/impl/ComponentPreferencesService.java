package at.component.framework.services.preferences.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import at.component.framework.services.Activator;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.preferences.IComponentPreferencesService;

public class ComponentPreferencesService implements
		IComponentPreferencesService {

	@Override
	public Preferences getProjectPreferences(IProject project) {
		if (project == null)
			return null;
		else {
			return Activator.getPreferencesService().getSystemPreferences()
					.node(project.getProjectName());
		}
	}

	@Override
	public List<Preferences> getAllProjectRootNodes() throws BackingStoreException {
		List<Preferences> projectRootNodes = new ArrayList<Preferences>();
		
		for (String name:Activator.getPreferencesService().getSystemPreferences().childrenNames()) {
			projectRootNodes.add(Activator.getPreferencesService().getSystemPreferences().node(name));
		}
		
		return projectRootNodes;
	}
}
