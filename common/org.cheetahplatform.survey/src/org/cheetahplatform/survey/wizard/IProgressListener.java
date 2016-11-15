package org.cheetahplatform.survey.wizard;

/**
 * A listener to be informed when progress is made.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.09.2009
 */
public interface IProgressListener {
	/**
	 * This function is called whenever progress the listener should know about is made.
	 */
	public void progressChanged();
}
