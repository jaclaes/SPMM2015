package org.cheetahplatform.experiment.editor.views;

import java.util.List;

public interface IValidator {
	/**
	 * 
	 * @return Returns a list of Message Objects that describe the issues found.
	 */
	public List<Message> validate();
}
