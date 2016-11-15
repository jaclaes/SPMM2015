package at.component.framework.services.idservice;

public interface IIdService {
	/**
	 * This method returns a new unique id for the current project --> the combination of projectname and id is unique in the system.
	 * 
	 * @param projectName
	 *            The project for which the id is needed
	 * @return The id for the given projectname
	 */
	public String getId(String projectName);

	/**
	 * Sets the id for the given projectname to the value of the given id accept for a value of "-1" - then the given project and therefore
	 * the stored id is removed.
	 * 
	 * @param projectName
	 *            The project for which the id is set
	 * @param id
	 *            The id that is set
	 */
	public void setId(String projectName, int id);
}
