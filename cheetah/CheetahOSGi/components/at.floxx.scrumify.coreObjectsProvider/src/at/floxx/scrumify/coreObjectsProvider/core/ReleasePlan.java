package at.floxx.scrumify.coreObjectsProvider.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**The ReleasePlan class represents the a ReleasePlan.
 * @author mathias
 *
 */
@Entity
public class ReleasePlan implements Serializable {
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;

	private String projectName;
	private String releaseGoalDescription;
	private Calendar startDate;
	private Calendar endDate;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Sprint> sprints;

	/**The Constructor.
	 * 
	 */
	public ReleasePlan() {
		super();
	}

	/**The Constructor.
	 * @param projectName
	 * @param releaseGoalDescription
	 */
	public ReleasePlan(String projectName, String releaseGoalDescription) {
		super();
		this.projectName = projectName;
		this.releaseGoalDescription = releaseGoalDescription;
	}

	/**The Constructor.
	 * @param id
	 * @param name
	 * @param goalDescription
	 */
	public ReleasePlan(long id, String name, String goalDescription) {
		this(name, goalDescription);
		this.setId(id);
	}

	/**Get the Projectname.
	 * @return projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**Set the Projectname.
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**Get the Releasegoal Description.
	 * @return releaseGoalDescription
	 */
	public String getReleaseGoalDescription() {
		return releaseGoalDescription;
	}

	/**Set the Releasegoal Description.
	 * @param releaseGoalDescription
	 */
	public void setReleaseGoalDescription(String releaseGoalDescription) {
		this.releaseGoalDescription = releaseGoalDescription;
	}

	/**Get the Starting Date of this Releaseplan.
	 * @return startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**Set the Starting Date of this Releaseplan.
	 * @param startDate
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**Get the Ending Date of this Releaseplan.
	 * @return endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**Set the Ending Date of this Releaseplan.
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**Get the Sprint List part of this Releaseplan.
	 * @return sprints
	 */
	public List<Sprint> getSprints() {
		return sprints;
	}

	/**Set the Sprint List part of this Releaseplan.
	 * @param sprints
	 */
	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	/**Set the id.
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**Get the id.
	 * @return id
	 */
	public long getId() {
		return id;
	}

}
