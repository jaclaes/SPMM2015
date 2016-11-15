package at.floxx.scrumify.coreObjectsProvider.core;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**The Product Class, representing Products.
 * @author mathias
 *
 */
@Entity
public class Product implements Serializable {
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	private String description;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ProductBacklogItem> productBacklog;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ReleasePlan> releasePlans;
	
	/**The Constructor.
	 * 
	 */
	public Product() {
		super();
	}

	/**The Constructor.
	 * @param name
	 * @param description
	 */
	public Product(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	/**The Constructor.
	 * @param id
	 * @param name
	 * @param description
	 */
	public Product(long id, String name, String description) {
		this(name, description);
		this.setId(id);
	}

	/**Gets the name.
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
	/**Get the List of ProdcutBacklogEntries.
	 * @return productBacklog
	 */
	public List<ProductBacklogItem> getProductBacklog() {
		return productBacklog;
	}
	/**Set the ProdcutBacklog List.
	 * @param productBacklog
	 */
	public void setProductBacklog(List<ProductBacklogItem> productBacklog) {
		this.productBacklog = productBacklog;
	}
	/**Get the List of Releaseplans.
	 * @return releasePlans
	 */
	public List<ReleasePlan> getReleasePlans() {
		return releasePlans;
	}
	/**set the List of Releaseplans.
	 * @param releasePlans
	 */
	public void setReleasePlans(List<ReleasePlan> releasePlans) {
		this.releasePlans = releasePlans;
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
