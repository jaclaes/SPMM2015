package org.cheetahplatform.survey.core.validator;

/**
 * Allows all input.
 * 
 * @author Michael Schier<br>
 *         Stefan Zugal
 * 
 */
public class DummyValidator implements IAttributeValidator {

	@Override
	public Object parseInput(String input) {
		return input;
	}

	@Override
	public String validate(Object input) {
		return null;
	}

}