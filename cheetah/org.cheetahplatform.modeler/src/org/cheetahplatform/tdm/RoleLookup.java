/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class RoleLookup {
	private static RoleLookup INSTANCE;

	public static RoleLookup getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RoleLookup();
		}

		return INSTANCE;
	}

	private Map<DeclarativeActivity, Role> activityToRole;
	private Map<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>> overrides;
	private Set<Role> roles;

	private RoleLookup() {
		activityToRole = new HashMap<DeclarativeActivity, Role>();
		roles = new HashSet<Role>();
		overrides = new HashMap<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>>();
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public void assignRole(DeclarativeActivity activity, Role role) {
		activityToRole.put(activity, role);
	}

	public void assignRole(DeclarativeActivityInstance activity, Role role) {
		DeclarativeProcessInstance instance = activity.getProcessInstance();
		assignRole(instance, (DeclarativeActivity) activity.getNode(), role);
	}

	public void assignRole(DeclarativeProcessInstance instance, DeclarativeActivity activity, Role role) {
		Map<DeclarativeActivity, Role> mapping = overrides.get(instance);
		if (mapping == null) {
			mapping = new HashMap<DeclarativeActivity, Role>();
			overrides.put(instance, mapping);
		}

		mapping.put(activity, role);
	}

	public Map<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>> getAllOverrides() {
		return Collections.unmodifiableMap(overrides);
	}

	public List<Role> getAllRoles() {
		return new ArrayList<Role>(roles);
	}

	/**
	 * Determine the role for the given activity, <code>null</code> if there is no such assignment.
	 * 
	 * @param activity
	 *            the activity
	 * @return the assigned role, <code>null</code> if there is no such
	 */
	public Role getRole(DeclarativeActivity activity) {
		return activityToRole.get(activity);
	}

	/**
	 * Determine the role for the given activity instance.
	 * 
	 * @param activity
	 *            the instance
	 * @return the assigned role, taking into account defined overrides, <code>null</code> if neither override nor default role is defined
	 */
	public Role getRole(DeclarativeActivityInstance activity) {
		return getRole(activity.getProcessInstance(), (DeclarativeActivity) activity.getNode());
	}

	/**
	 * Determine the role for the given activity and instance.
	 * 
	 * @param instance
	 *            the process instance
	 * @param activity
	 *            the activity
	 * @return the assigned role, taking into account defined overrides, <code>null</code> if neither override nor default role is defined
	 */
	public Role getRole(DeclarativeProcessInstance instance, DeclarativeActivity activity) {
		Map<DeclarativeActivity, Role> roles = overrides.get(instance);

		if (roles != null) {
			Role overriddenRole = roles.get(activity);
			if (overriddenRole != null) {
				return overriddenRole;
			}
		}

		return getRole(activity);
	}

	public Role getRole(String name) {
		for (Role role : roles) {
			if (role.getName().equals(name)) {
				return role;
			}
		}

		return null;
	}
}
