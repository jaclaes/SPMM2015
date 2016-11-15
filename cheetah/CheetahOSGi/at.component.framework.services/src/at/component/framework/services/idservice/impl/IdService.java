package at.component.framework.services.idservice.impl;

import java.util.HashMap;

import at.component.framework.services.idservice.IIdService;

/**
 * This class represents a service that produces <code>Integer<\code>-ids for projects with the given projectname.
 * 
 * @author Felix Schöpf
 */
public class IdService implements IIdService {
	/**
	 * The map of ids. The key is the projectname, the value the current id.
	 */
	HashMap<String, Integer> ids;

	public IdService() {
		ids = new HashMap<String, Integer>();
	}

	@Override
	public synchronized String getId(String projectName) {
		if (!ids.keySet().contains(projectName))
			ids.put(projectName, 1);

		Integer id = ids.get(projectName);
		String returnValue = String.valueOf(id);

		ids.put(projectName, ++id);

		return returnValue;
	}

	@Override
	public synchronized void setId(String projectName, int id) {
		if (id == -1) {
			ids.remove(projectName);
		} else
			ids.put(projectName, id);
	}
}
