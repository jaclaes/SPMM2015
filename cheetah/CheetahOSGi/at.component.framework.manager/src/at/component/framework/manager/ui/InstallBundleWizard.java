package at.component.framework.manager.ui;

import org.eclipse.jface.wizard.Wizard;

import at.component.framework.manager.Manager;

public class InstallBundleWizard extends Wizard {

	private InstallBundleWizardPage page;

	public InstallBundleWizard() {
		setWindowTitle("Install Bundle");
	}

	@Override
	public void addPages() {
		page = new InstallBundleWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		if (page.getJarRadioButton().getSelection()) {
			Manager.installComponentBundle(page.getJarText().getText());
		} else {
			Manager.installComponentBundle(page.getFolderText().getText());
		}

		return true;
	}
}
