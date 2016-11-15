package org.cheetahplatform.common.tutorial;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public abstract class AbstractTutorialStep implements ITutorialStep {
	private String description;

	public AbstractTutorialStep(String description) {
		this.description = description;
	}

	@Override
	public void aboutToShow() {
		// ignore
	}

	@Override
	public void completed() {
		// ignore
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}
}
