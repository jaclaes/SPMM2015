package at.floxx.scrumify.sprintBacklogItemList.model;

/**Represents the state of a task.
 * @author Mathias Breuss
 *
 */
public enum SprintItemState {
	/**
	 * Task is open.
	 */
	OPEN, /**
	 * Task is being worked on.
	 */
	WIP, /**
	 * Task is ready to be verified.
	 */
	TBV, /**
	 * Task is finished.
	 */
	DONE

}
