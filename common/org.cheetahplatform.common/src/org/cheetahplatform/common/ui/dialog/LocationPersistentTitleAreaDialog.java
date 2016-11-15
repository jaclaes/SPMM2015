package org.cheetahplatform.common.ui.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Abstract implementation of a {@link TitleAreaDialog}, allows for storing the current location - opens the dialog at the same location the
 * user closed - it. In addition, the dialog provides a simple validation infrastructure.
 * 
 * @author Stefan Zugal
 * 
 */
public abstract class LocationPersistentTitleAreaDialog extends TitleAreaDialog {
	private class ValidationListener implements SelectionListener, ModifyListener, ISelectionChangedListener, IPropertyChangeListener,
			ICheckStateListener {

		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			validate();
		}

		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			validate();
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			validate();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			validate();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			validate();
		}

	}

	private final PersistentLocationSupport locationSupport;
	private final ValidationListener listener;

	public LocationPersistentTitleAreaDialog(Shell parentShell) {
		super(parentShell);

		String className = getClass().getName();
		locationSupport = new PersistentLocationSupport(className + "_X", getClass().getName() + "Y");
		listener = new ValidationListener();
	}

	protected void addValidationListener(Button button) {
		button.addSelectionListener(listener);
	}

	protected void addValidationListener(CheckboxTreeViewer viewer) {
		addValidationListener((StructuredViewer) viewer);
		viewer.addCheckStateListener(listener);
	}

	protected void addValidationListener(DateTime time) {
		time.addSelectionListener(listener);
	}

	protected void addValidationListener(FieldEditor editor) {
		editor.setPropertyChangeListener(listener);
	}

	protected void addValidationListener(StructuredViewer viewer) {
		viewer.addSelectionChangedListener(listener);
	}

	protected void addValidationListener(StyledText text) {
		text.addModifyListener(listener);
	}

	protected void addValidationListener(Text text) {
		text.addModifyListener(listener);
	}

	@Override
	public boolean close() {
		locationSupport.storeLocation(getShell());

		return super.close();
	}

	@Override
	protected void constrainShellSize() {
		super.constrainShellSize();

		locationSupport.restoreLocation(getShell());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		Button button = getButton(OK);
		if (button != null) {
			button.setEnabled(false);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control content = super.createDialogArea(parent);

		getShell().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});

		return content;
	}

	/**
	 * Perform any clean-up operations.
	 */
	protected void dispose() {
		// nothing to dispose
	}

	/**
	 * Perform the validation and return an error message if appropriate. The default implementation always return <code>null</code>.
	 * 
	 * @return an error message, <code>null</code> if the input is correct
	 */
	protected String doValidate() {
		return null;
	}

	protected void validate() {
		String error = doValidate();
		setErrorMessage(error);
		getButton(OK).setEnabled(error == null);
	}

}
