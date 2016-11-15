package at.component.framework.manager;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.osgi.framework.Bundle;

import at.component.framework.manager.ui.NewProjectDialog;


public class NewProjectDialogController {
	
	private class BundleLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			if (element instanceof Bundle)
				return getBundleName((Bundle) element);
			
			return null;
		}
		
		/**
		 * Returns the name of the given bundle according to my own needs.
		 * 
		 * @param bundle
		 *            The bundle
		 * @return The name
		 *         [bundle.getHeaders().get(org.osgi.framework.Constants.BUNDLE_NAME
		 *         )] or the symbolic name of the bundle
		 */
		public String getBundleName(Bundle bundle) {
			Object bundleName = bundle.getHeaders().get(org.osgi.framework.Constants.BUNDLE_NAME);

			if (bundleName != null && !bundleName.toString().trim().equals(""))
				return bundleName.toString().trim();

			return bundle.getSymbolicName();
		}
	}

	private NewProjectDialog dialog;
	private String projectName;
	private Bundle selectedBundle;
	private int result;

	public NewProjectDialogController(NewProjectDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Saves the inputs and disposes the shell if the projectName isn't already in use.
	 */
	public void createProject() {
		projectName = dialog.getProjectNameText().getText().trim();
	
		if (!Activator.getComponentService().isProjectNameInUse(projectName)) {
			selectedBundle = (Bundle) ((IStructuredSelection) dialog.getTopLevelComponentComboViewer().getSelection()).getFirstElement();;
			result = SWT.OK;
			dialog.getShell().dispose();
		} else {
			MessageDialog.openError(dialog.getShell(), "Projektname existiert bereits",
					"Der angegebene Projektname existiert bereits.\nBitte geben Sie einen anderen Namen an.");
			dialog.getProjectNameText().selectAll();
			dialog.getProjectNameText().setFocus();
		}
	}
	
	public String getProjectName() {
		return projectName;
	}

	public Bundle getSelectedBundle() {
		return selectedBundle;
	}

	public int getResult() {
		return result;
	}

	public void addListeners() {
		dialog.getTopLevelComponentComboViewer().getCombo().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateInputs();
				if (e.character == SWT.CR)
					createProject();
			}
		});
		
		dialog.getCreateProjectButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createProject();
			}
		});
		
		dialog.getCancelButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = SWT.CANCEL;
				dialog.getShell().dispose();
			}
		});
		
		dialog.getProjectNameText().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (validateInputs() && e.character == SWT.CR) {
					createProject();
				}
			}
		});
	}
	
	private boolean validateInputs() {
		if (dialog.getProjectNameText().getText().trim().equals("") || dialog.getTopLevelComponentComboViewer().getSelection().isEmpty()) {
			dialog.getCreateProjectButton().setEnabled(false);
			return false;
		} else {
			dialog.getCreateProjectButton().setEnabled(true);
			return true;
		}
	}

	public void fillUi() {
		dialog.getTopLevelComponentComboViewer().setContentProvider(new ArrayContentProvider());
		dialog.getTopLevelComponentComboViewer().setLabelProvider(new BundleLabelProvider());
		List<Bundle> installedProjectComponentBundles = Activator.getComponentService().getInstalledProjectComponentBundles();
		dialog.getTopLevelComponentComboViewer().setInput(installedProjectComponentBundles);
		
		if (installedProjectComponentBundles.size() > 0)
			dialog.getTopLevelComponentComboViewer().setSelection(new StructuredSelection(installedProjectComponentBundles.get(0)));
	}
}
