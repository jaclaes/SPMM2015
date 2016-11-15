/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.dialog;

import java.util.List;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ConstraintViolationDialog extends TitleAreaDialog {

	private static class ConstraintLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			IDeclarativeConstraint constraint = (IDeclarativeConstraint) element;
			return constraint.getDescription();
		}
	}

	public static final int EXECUTE_ANYWAY = 20000;

	private final List<IDeclarativeConstraint> violatedConstraints;

	public ConstraintViolationDialog(Shell parentShell, List<IDeclarativeConstraint> violatedConstraints) {
		super(parentShell);
		Assert.isNotNull(violatedConstraints);
		this.violatedConstraints = violatedConstraints;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == EXECUTE_ANYWAY)
			setReturnCode(EXECUTE_ANYWAY);
		else
			setReturnCode(CANCEL);

		close();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Constraint Violation");
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, EXECUTE_ANYWAY, "Execute", false);
		createButton(parent, CANCEL, "Cancel", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		setTitle("Constraint Violation");
		setMessage("Some constraints are violated by executing this activity.\nDo you want to execute anyway?");

		TableViewer constraintViewer = new TableViewer(container);
		constraintViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		constraintViewer.setContentProvider(new ArrayContentProvider());
		constraintViewer.setLabelProvider(new ConstraintLabelProvider());
		constraintViewer.setInput(violatedConstraints);

		return container;
	}
}
