package org.cheetahplatform.core.declarative.constraint;

import java.util.List;

import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009
 */
public interface IDeclarativeConstraint extends IIdentifiableObject {
	/**
	 * Checks whether the given process instance can be terminated.
	 * 
	 * @param processInstance
	 *            the process instance
	 * @return <code>true</code> if the process instance can be terminated, <code>false</code> if the termination is prohibited by this
	 *         constraint
	 */
	boolean canTerminate(DeclarativeProcessInstance processInstance);

	/**
	 * Return all activities which are involved in this constraint. The constraints <b>must</b> be returned in the order as it would be
	 * visualized, e.g., in the case of a prerequisite constraint, A should be before B in the list.
	 * 
	 * @return the involved activities
	 */
	List<DeclarativeActivity> getActivities();

	/**
	 * Returns a textual description of constraint.
	 * 
	 * @return a textual description of constraint
	 */
	String getDescription();

	/**
	 * Return all activities which are at the end side of the constraint, e.g., for a precedence constraint A --> B, B is returned.
	 * 
	 * @return all ending activities
	 */
	List<DeclarativeActivity> getEndActivities();

	/**
	 * Return all activities which are at the start side of the constraint, e.g., for a precedence constraint A --> B, A is returned.
	 * 
	 * @return all starting activities
	 */
	List<DeclarativeActivity> getStartActivities();

	/**
	 * Checks whether the given action is executable.
	 * 
	 * @param activity
	 *            the activity
	 * @param processInstance
	 *            the process instance
	 * @return <code>true</code> if the action can be executed, <code>false</code> if the execution is prohibited by this constraint
	 */
	boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance);
}
