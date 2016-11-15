package at.floxx.scrumify.coreObjectsProvider.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**The Sprint class represents the Sprint.
 * @author mathias
 *
 */
@Entity
public class Sprint implements Serializable  {
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	
	private String goal;
	private String comments;
	private Calendar startDate;
	private Calendar endDate;
	private int speed;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ProductBacklogItem> storries;
	
	/**The Constructor.
	 * 
	 */
	public Sprint() {
		super();
	}

	/**The Constructor.
	 * @param description
	 * @param comments
	 * @param speed
	 */
	public Sprint(String description, String comments, int speed) {
		super();
		this.goal = description;
		this.comments = comments;
		this.speed = speed;
	}
	
	/**The Constructor.
	 * @param id
	 * @param description
	 * @param comments
	 * @param speed
	 */
	public Sprint(long id, String description, String comments,
			int speed) {
		this(description, comments, speed);
		this.id = id;
	}

	/**Get the Id.
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**Get the Sprintgoal
	 * @return goal
	 */
	public String getGoal() {
		return goal;
	}
	/**Set the Sprintgoal
	 * @param goal
	 */
	public void setGoal(String goal) {
		this.goal = goal;
	}
	/**Get the comment.
	 * @return comments
	 */
	public String getComments() {
		return comments;
	}
	/**Set the comment.
	 * @param comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**Get the starting Date.
	 * @return startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}
	/**Set the starting Date.
	 * @param startDate
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	/**Get the ending Date.
	 * @return endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}
	/**Set the ending Date.
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	/**Get the Speed of this Sprint.
	 * @return speed
	 */
	public int getSpeed() {
		return this.speed;
	}
	/**Set the Speed of this Sprint.
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	/**Get the Storries (List of ProductBacklog Entries)
	 * @return storries
	 */
	public List<ProductBacklogItem> getStorries() {
		return storries;
	}
	/**Set the Storries (List of ProductBacklog Entries)
	 * @param storries
	 */
	public void setStorries(List<ProductBacklogItem> storries) {
		this.storries = storries;
	}
	
	

}
