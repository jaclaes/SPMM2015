/**
 * 
 */
package org.cheetahplatform.core.declarative.constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.IActivity;

/**
 * A class representing the status returned by a constraint after constraint evaluation.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         24.06.2009 <br>
 */
public class ConstraintStatus {
	private final String description;
	private final List<IActivity> affectedActivites;

	/**
	 * Creates a new {@link ConstraintStatus} object with empty description.
	 */
	public ConstraintStatus() {
		this("");
	}

	/**
	 * Creates a new {@link ConstraintStatus} object.
	 * 
	 * @param description
	 *            a textual description
	 */
	public ConstraintStatus(String description) {
		this.description = description;
		affectedActivites = new ArrayList<IActivity>();
	}

	/**
	 * Creates a new {@link ConstraintStatus} object.
	 * 
	 * @param description
	 *            a textual description
	 * @param activity
	 *            the affected activity
	 */
	public ConstraintStatus(String description, IActivity activity) {
		this(description);
		affectedActivites.add(activity);
	}

	/**
	 * Adds an activity to the list of affected activites.
	 * 
	 * @param activity
	 *            the activity
	 */
	public void addActivity(IActivity activity) {
		affectedActivites.add(activity);
	}

	/**
	 * Returns the affectedActivites.
	 * 
	 * @return the affectedActivites
	 */
	public List<IActivity> getAffectedActivites() {
		return Collections.unmodifiableList(affectedActivites);
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
