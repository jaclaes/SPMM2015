package at.component.framework.manager.ui;

import org.eclipse.jface.dialogs.IInputValidator;

public class EmptyStringValidator implements IInputValidator {

	@Override
	public String isValid(String newText) {
		if (newText.trim().equals(""))
			return "Der Projektname muss eingegeben werden!";
		return null;
	}

}
