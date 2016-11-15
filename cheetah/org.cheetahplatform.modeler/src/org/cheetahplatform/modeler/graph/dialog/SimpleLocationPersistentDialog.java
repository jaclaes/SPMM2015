package org.cheetahplatform.modeler.graph.dialog;

import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.ui.composite.MultiLineButton;
import org.cheetahplatform.common.ui.dialog.PersistentLocationSupport;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.ResourceManager;

public abstract class SimpleLocationPersistentDialog extends Dialog {

	private PersistentLocationSupport locationSupport;
	private Composite composite;

	public SimpleLocationPersistentDialog(Shell parentShell) {
		super(parentShell);

		String className = getClass().getName();
		this.locationSupport = new PersistentLocationSupport(className + "_X", className + "Y");
		setBlockOnOpen(false);
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

	/**
	 * Create the actions to be displayed.
	 * 
	 * @return the created actions
	 */
	protected abstract List<IAction> createActions();

	@Override
	protected Control createButtonBar(Composite parent) {
		return null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		realParent.setBackground(CommonConstants.BACKGROUND_COLOR);
		realParent.setBackgroundMode(SWT.INHERIT_FORCE);

		List<IAction> actions = createActions();
		composite = new Composite(realParent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = actions.size();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		initialize(actions);

		return realParent;
	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.TOOL | SWT.ON_TOP;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	private void initialize(List<IAction> actions) {
		for (final IAction action : actions) {
			ImageDescriptor descriptor = action.getImageDescriptor();
			Image image = ResourceManager.getImage(descriptor);
			final MultiLineButton button = new MultiLineButton(composite, SWT.NONE, action.getText(), image);
			if (!action.isEnabled()) {
				button.setEnabled(false);
			}

			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});

			action.addPropertyChangeListener(new IPropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent event) {
					if (event.getProperty().equals(IAction.ENABLED)) {
						button.setEnabled((Boolean) event.getNewValue());
					}
				}
			});
		}
	}
}
