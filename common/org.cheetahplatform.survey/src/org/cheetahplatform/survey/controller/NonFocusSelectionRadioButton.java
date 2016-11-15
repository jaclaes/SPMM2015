package org.cheetahplatform.survey.controller;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A radio button with some slightly changed behavior when it comes to selection and focus. <br>
 * Ordinary radio buttons get selected whenever they get focus. This does not.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.10.2009
 */
class NonFocusSelectionRadioButton extends Button {
	private boolean focusLost = true;

	/**
	 * @param parent
	 * @param style
	 */
	public NonFocusSelectionRadioButton(Composite parent, int style) {
		super(parent, style);
		addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				focusLost = false;
			}

			@Override
			public void focusLost(FocusEvent e) {
				focusLost = true;
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// override check
	}

	@Override
	public boolean forceFocus() {
		boolean selection = getSelection();
		boolean forceFocus = super.forceFocus();
		if (!selection)
			setSelectionWithoutCheckingFocus(false);

		return forceFocus;
	}

	@Override
	public void setSelection(boolean selected) {
		if (focusLost) {
			return;
		}

		super.setSelection(selected);
		Control[] children = getParent().getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (!(child instanceof NonFocusSelectionRadioButton))
				continue;

			if (this != child) {
				NonFocusSelectionRadioButton nonFocusSelectionRadioButton = (NonFocusSelectionRadioButton) child;
				if (nonFocusSelectionRadioButton.getSelection()) {
					if (selected)
						nonFocusSelectionRadioButton.setSelectionWithoutCheckingFocus(false);
				}
			}
		}
	}

	public void setSelectionWithoutCheckingFocus(boolean selected) {
		super.setSelection(selected);
	}
}