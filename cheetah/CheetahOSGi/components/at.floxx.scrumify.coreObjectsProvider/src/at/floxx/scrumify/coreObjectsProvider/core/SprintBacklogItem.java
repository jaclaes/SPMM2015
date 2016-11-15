package at.floxx.scrumify.coreObjectsProvider.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**The SprintBacklogItem class representing SprintBacklog Entries.
 * @author mathias
 *
 */
@Entity
public class SprintBacklogItem implements Serializable  {
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	
	private String description;
	private Integer openEstimate;
	private SprintItemState state;
	private String responsibility;
	private Map<String, Integer> burnDown;

		
	/**The Constructor.
	 * 
	 */
	public SprintBacklogItem() {
		super();
	}

	/**The Constructor.
	 * @param description
	 * @param estimation
	 * @param responsibility
	 */
	public SprintBacklogItem(String description, int estimation,
			String responsibility) {
		super();
		this.description = description;
		this.state = SprintItemState.OPEN;
		this.responsibility = responsibility;
		this.openEstimate = estimation;
		this.burnDown = new HashMap<String, Integer>();
	}
	
	/**The Constructor.
	 * @param id
	 * @param description
	 * @param estimation
	 * @param responsibility
	 */
	public SprintBacklogItem(long id, String description, int estimation,
			String responsibility) {
		this(description, estimation, responsibility);
		this.id = id;
	}

	/**Get the Id.
	 * @return id
	 */
	public long getId() {
		return this.id;
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
	/**Get the open Estimate for this SprintBacklog Item.
	 * @return openEstimate
	 */
	public int getOpenEstimate() {
		return openEstimate;
	}
	/**Set the open Estimate for this SprintBacklog Item.
	 * @param newEstimate
	 */
	public void setOpenEstimate(int newEstimate) {
		this.openEstimate = newEstimate;
	}
	/**Get the State.
	 * @return state
	 */
	public SprintItemState getState() {
		return state;
	}
	/**Set the State.
	 * @param state
	 */
	public void setState(SprintItemState state) {
		this.state = state;
	}
	/**Get the Responsible person of this Item.
	 * @return responsibility
	 */
	public String getResponsibility() {
		return responsibility;
	}
	/**Get the Responsible person of this Item.
	 * @param responsibility
	 */
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	
	/**Get the history of estimate for this sprint
	 * @return burnDown Hashmap, where the Key is a date and the value the open estimate.
	 */
	public Map<String, Integer> getBurnDown() {
		if(this.burnDown == null)
			this.burnDown = new HashMap<String, Integer>();
		
		return this.burnDown;
	}
	
	/**Adds an entry to the burndown Hashmap.
	 * @param calendar
	 * @param newEstimate
	 */
	public void updateOpenEstimate(Calendar calendar, int newEstimate) {
		if(this.burnDown == null)
			this.burnDown = new HashMap<String, Integer>();
		String date = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		this.burnDown.put(date, newEstimate);
		this.openEstimate = newEstimate;
	}
	

}
