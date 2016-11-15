package at.component.newcomponentwizard.wizards;

import java.util.TreeSet;

import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.util.VMUtil;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * The WizardPage for the ComponentWizard, which enables the creation of a new component project
 * 
 * @author "Felix Schöpf"
 */
@SuppressWarnings("restriction")
public class ComponentWizardPage extends WizardPage {
	private Text projectNameText;
	private Button isTopLevelComponentCheckButton;
	private Text componentNameText;
	private Combo osgiFrameworkCombo;
	private Combo executionEnvironmentCombo;

	private final static String NO_EXECUTION_ENVIRONMENT = PDEUIMessages.PluginContentPage_noEE;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ComponentWizardPage() {
		super("wizardPage");
		setTitle("Component Project");
		setDescription("This wizard creates a project for a component.");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label projectNameLabel = new Label(container, SWT.NONE);
		projectNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		projectNameLabel.setText("Project name:");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		projectNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dialogChanged();
		setControl(container);
		new Label(container, SWT.NONE);

		isTopLevelComponentCheckButton = new Button(container, SWT.CHECK);
		isTopLevelComponentCheckButton.setSelection(false);
		isTopLevelComponentCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		isTopLevelComponentCheckButton.setText("Is Project-Component");
		isTopLevelComponentCheckButton
				.setToolTipText("Check this box if you want to use this component as top-level-component for a project");

		Label componentNameLabel = new Label(container, SWT.NONE);
		componentNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		componentNameLabel.setText("Component Name:");

		componentNameText = new Text(container, SWT.BORDER);
		componentNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		componentNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Label osgiFrameworkLabel = new Label(container, SWT.NONE);
		osgiFrameworkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		osgiFrameworkLabel.setText("OSGi Framework:");

		osgiFrameworkCombo = new Combo(container, SWT.READ_ONLY | SWT.SINGLE);
		osgiFrameworkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		osgiFrameworkCombo.setItems(new String[] { ICoreConstants.EQUINOX, PDEUIMessages.NewProjectCreationPage_standard });
		osgiFrameworkCombo.select(0);
		osgiFrameworkCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		createExecutionEnvironmentControls(container);
	}

	/**
	 * Creates all the ExecutionEnvironment-Widgets
	 * 
	 * @param container
	 *            The container-widget
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createExecutionEnvironmentControls(Composite container) {
		Label executionEnvironmentLabel = new Label(container, SWT.NONE);
		executionEnvironmentLabel.setText("Execution Environment:");

		executionEnvironmentCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		executionEnvironmentCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Gather EEs
		IExecutionEnvironment[] exeEnvs = VMUtil.getExecutionEnvironments();
		TreeSet availableEEs = new TreeSet();
		for (int i = 0; i < exeEnvs.length; i++) {
			availableEEs.add(exeEnvs[i].getId());
		}
		availableEEs.add(NO_EXECUTION_ENVIRONMENT);

		// Set data
		executionEnvironmentCombo.setItems((String[]) availableEEs.toArray(new String[availableEEs.size() - 1]));
		executionEnvironmentCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		// Set default EE based on strict match to default VM
		IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();
		String[] EEChoices = executionEnvironmentCombo.getItems();
		for (int i = 0; i < EEChoices.length; i++) {
			if (!EEChoices[i].equals(NO_EXECUTION_ENVIRONMENT)) {
				if (VMUtil.getExecutionEnvironment(EEChoices[i]).isStrictlyCompatible(defaultVM)) {
					executionEnvironmentCombo.select(i);
					break;
				}
			}
		}

		// Create button
		Button executionEnvironmentButton = new Button(container, SWT.PUSH);
		executionEnvironmentButton.setLayoutData(new GridData());
		executionEnvironmentButton.setText(PDEUIMessages.NewProjectCreationPage_environmentsButton);
		executionEnvironmentButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				PreferencesUtil.createPreferenceDialogOn(getShell(), "org.eclipse.jdt.debug.ui.jreProfiles", //$NON-NLS-1$
						new String[] { "org.eclipse.jdt.debug.ui.jreProfiles" }, null).open(); //$NON-NLS-1$ 
			}
		});
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		if (getProjectName().length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}
		if (getComponentName().length() == 0) {
			updateStatus("Component name must be specified");
			return;
		}
		if (getOsgiFramework() == null) {
			updateStatus("An OSGi Framework has to be selected");
			return;
		}
		if (getExecutionEnvironment() == null) {
			updateStatus("An ExecutionEnvironment has to be selected");
			return;
		}

		updateStatus(null);
	}

	public String getComponentName() {
		return componentNameText.getText();
	}

	public String getExecutionEnvironment() {
		int selectionIndex = executionEnvironmentCombo.getSelectionIndex();

		if (selectionIndex < 0)
			return null;

		return executionEnvironmentCombo.getItem(selectionIndex);
	}

	public String getOsgiFramework() {
		int selectionIndex = osgiFrameworkCombo.getSelectionIndex();

		if (selectionIndex < 0)
			return null;

		return osgiFrameworkCombo.getItem(selectionIndex);
	}

	public String getProjectName() {
		return projectNameText.getText();
	}

	public boolean isTopLevelComponent() {
		return isTopLevelComponentCheckButton.getSelection();
	}

	private void updateStatus(String message) {
		setMessage(message);
		setPageComplete(message == null);
	}
}