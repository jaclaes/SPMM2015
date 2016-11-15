package at.floxx.scrumify.coreObjectsProvider.core;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;



/**The ProductBacklogItem Class, represents ProductBacklog Entries.
 * @author mathias
 *
 */
@Entity
public class ProductBacklogItem implements Serializable  {
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	private String description;
	private int estimate;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<SprintBacklogItem> tasks;
	
	
	
	/**Get the Estimation of the ProductBacklog Entry.
	 * @return estimate
	 */
	public int getEstimate() {
		return estimate;
	}

	/**Set the Estimation of the ProductBacklog Entry.
	 * @param estimate
	 */
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}

	/**Get the Id.
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**Set the Id.
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**The Constructor.
	 * 
	 */
	public ProductBacklogItem() {
		super();
	}

	/**The Constructor.
	 * @param name
	 * @param description
	 * @param estimate
	 * @param tasks
	 */
	public ProductBacklogItem(String name, String description, int estimate,
			List<SprintBacklogItem> tasks) {
		super();
		this.name = name;
		this.description = description;
		this.tasks = tasks;
		this.estimate = estimate;
	}
	
	/**The Constructor.
	 * @param id
	 * @param name
	 * @param description
	 * @param estimate
	 * @param tasks
	 */
	public ProductBacklogItem(long id, String name,
			String description, int estimate, List<SprintBacklogItem> tasks) {
		this(name, description, estimate, tasks);
		this.id = id;
	}

	/**Get the name.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**Set the name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**Get the SprintBacklog Entries of this ProductBacklog Entry.
	 * @return tasks
	 */
	public List<SprintBacklogItem> getTasks() {
		return tasks;
	}
	/**Set the SprintBacklog Entries of this ProductBacklog Entry.
	 * @param tasks
	 */
	public void setTasks(List<SprintBacklogItem> tasks) {
		this.tasks = tasks;
	}
	/**Get the Description.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**Set the Description.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
	

}
