package at.component.newcomponentwizard.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import at.component.newcomponentwizard.generator.NewProjectCreationOperation;

/**
 * This wizard enables the creation of a component project, which simplifies the implementation of simple and top-level components.
 * 
 * @author "Felix Schöpf"
 */

@SuppressWarnings("restriction")
public class ComponentWizard extends Wizard implements INewWizard {

	private ComponentWizardPage page;

	public ComponentWizard() {
		super();
		setWindowTitle("New Component Project");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ComponentWizardPage();
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard.
	 */
	@Override
	public boolean performFinish() {
		final String projectName = page.getProjectName();
		final boolean isTopLevelComponent = page.isTopLevelComponent();
		final String componentName = page.getComponentName();
		final String osgiFramework = page.getOsgiFramework();
		final String executionEnvironment = page.getExecutionEnvironment();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(projectName, isTopLevelComponent, componentName, osgiFramework, executionEnvironment, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;

	}

	/**
	 * This method finally creates the component project.
	 * 
	 * @param isTopLevelComponent Indicates if a project for a top-level-component should be created or not
	 * @param componentName The name of the component in the OSGi-Framework
	 * @param osgiFramework The selected osgiFramework
	 * @param executionEnvironment The selected ExecutionEnvironment
	 */
	private void doFinish(final String projectName, final boolean isTopLevelComponent, final String componentName, final String osgiFramework, final String executionEnvironment, IProgressMonitor monitor) throws CoreException {
		final IProjectProvider projectProvider = new IProjectProvider() {
			public String getProjectName() {
				return projectName;
			}

			public IProject getProject() {
				return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
			}

			public IPath getLocationPath() {
				return new Path(Platform.getLocation().toOSString());
			}
		};

		BasicNewProjectResourceWizard.updatePerspective(null);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					PluginFieldData data = new PluginFieldData();
					data.setId(projectName);
					data.setName(componentName);
					data.setOSGiFramework(osgiFramework);
					data.setExecutionEnvironment(executionEnvironment);
					data.setOutputFolderName("bin");
					data.setSourceFolderName("src");
					data.setTargetVersion(ICoreConstants.TARGET35);
					data.setClassname(projectName + ".Activator");
					data.setDoGenerateClass(true);
					data.setHasBundleStructure(true);
					data.setUIPlugin(false);
					data.setLegacy(false);
					data.setLibraryName(null);
					data.setProvider("");
					data.setRCPApplicationPlugin(false);
					data.setEnableAPITooling(false);
					data.setSimple(false);
					data.setVersion("1.0.0");
					getContainer().run(false, true, new NewProjectCreationOperation(data, projectProvider, null, isTopLevelComponent));
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		});

	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// do nothing
	}
}