package org.cheetahplatform.modeler.changepattern.tutorial;

import org.cheetahplatform.modeler.changepattern.model.SerialInsertChangePattern;
import org.cheetahplatform.modeler.tutorial.AbstractChangePatternTutorialStep;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class SerialInsertTutorialStep extends AbstractChangePatternTutorialStep {

	public SerialInsertTutorialStep() {
		super("Use the \"Serial Insert\" change pattern to add an activity to the process model.",
				SerialInsertChangePattern.SERIAL_INSERT_CHANGE_PATTERN);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/SerialInsert/SerialInsert.htm";
	}
}
