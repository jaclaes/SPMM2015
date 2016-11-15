package at.floxx.scrumify.coreObjectsProvider.core;

/**Represents the State of SprintBacklog Entries.
 * Where OPEN means the task has not been started, WIP means Work
 * in Progress, TBV To be verified and DONE means that the Task has been
 * finished.
 * @author mathias
 *
 */
public enum SprintItemState  {
	/**
	 * Task has not been started yet.
	 */
	OPEN, /**
	 * Someone is working on the task.
	 */
	WIP, /**
	 * Result of this Task can be verified.
	 */
	TBV, /**
	 * Task is finished.
	 */
	DONE

}
