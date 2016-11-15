package at.component.framework.services.preferences;

import java.util.List;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import at.component.framework.services.componentservice.IProject;

public interface IComponentPreferencesService {
	public Preferences getProjectPreferences(IProject project);
	public List<Preferences> getAllProjectRootNodes() throws BackingStoreException;
}
