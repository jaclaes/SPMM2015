package org.cheetahplatform.common.logging;

public interface IPromLogger extends IPromWriter, ILogListener {
	/**
	 * Enable/disable logging.
	 * 
	 * @param enabled
	 *            <code>true</code> to enable logging, <code>false</code> to disable
	 */
	void setEnabled(boolean enabled);
}
