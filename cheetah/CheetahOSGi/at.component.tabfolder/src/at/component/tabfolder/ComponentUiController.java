package at.component.tabfolder;

import java.util.Hashtable;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.wireadmin.Wire;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.IComponentUiController;
import at.component.framework.services.componentservice.Constants;
import at.component.framework.services.componentservice.ProjectConfiguration;
import at.component.framework.services.componentservice.UninstallableBundlesException;
import at.component.util.Connector;

public class ComponentUiController implements IComponentUiController {

	private final IComponent component;
	private final Composite parent;
	private TabFolderUi componentUi;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ComponentUiController(Composite parent, IComponent component) {
		this.parent = parent;
		this.component = component;

		Hashtable eventHandlerProperties = new Hashtable();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, new String[] { Constants.TOPIC_COMPONENT_STARTED });

		Activator.getBundleContext().registerService(EventHandler.class.getName(), this, eventHandlerProperties);

		componentUi = new TabFolderUi(parent);
		addListeners();
		addDropSupport();
	}

	private void addDropSupport() {
		DropTarget dropTarget = new DropTarget(componentUi.getTabFolder(), DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				startComponentOrProject((String) event.data);
			}

		});
	}

	private void addListeners() {
		componentUi.getRenameItem().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				renameSelectedTabItemComponent();
			}
		});
		
		componentUi.getConnectItem().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Connector.getInstance().showConnectorDialog(parent.getShell(), SWT.NONE, (IComponent) componentUi.getTabFolder().getSelection().getData());
			}
		});
		
		componentUi.getTabFolder().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (componentUi.getTabFolder().getSelection() == null) {
					componentUi.getRenameItem().setEnabled(false);
					componentUi.getConnectItem().setEnabled(false);
				} else {
					componentUi.getRenameItem().setEnabled(true);
					componentUi.getConnectItem().setEnabled(true);
				}
			}
		});
		
		componentUi.getTabFolder().addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override
			public void close(CTabFolderEvent event) {
				Widget item = event.item;
				IComponent component = (IComponent) item.getData();
				try {
					Activator.getComponentService().stopComponent(component);
				} catch (BundleException e) {
					// do nothing
				}
			}
		});
		
		componentUi.getTabFolder().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				renameSelectedTabItemComponent();
			}
		});
	}
	
	private void renameSelectedTabItemComponent() {
		CTabItem selectedTabItem = componentUi.getTabFolder().getSelection();
		IComponent component = (IComponent) selectedTabItem.getData();
		
		InputDialog dialog = new InputDialog(componentUi.getShell(), "Neuer Name",
				"Geben Sie einen neunen Namen für die Komponente \"" + component.getNameWithId() + "\" an!", "", new IInputValidator() {

					@Override
					public String isValid(String newText) {
						if (newText.trim().length() == 0)
							return "Der Name muss mindestens ein Zeichen enthalten!";

						return null;
					}
				});

		if (dialog.open() == Window.OK) {
			String newName = dialog.getValue();
			Activator.getComponentService().renameComponent(component, newName);
			selectedTabItem.setText(component.getNameWithId());
			selectedTabItem.getParent().layout();
		}
	}

	@Override
	public void consumersConnected(Wire[] wires) {
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getTopic().equals(Constants.TOPIC_COMPONENT_STARTED)) {
			String projectName = (String) event.getProperty(ComponentConstants.PROJECT_NAME);
			String componentId = (String) event.getProperty(ComponentConstants.COMPONENT_ID);
			String startingComponentId = (String) event.getProperty(ComponentConstants.CALLING_COMPONENT_ID);

			if (projectName != null && componentId != null && !projectName.trim().equals("") && !componentId.trim().equals("")) {
				IComponent startedComponent = Activator.getComponentService().getComponent(projectName, componentId);

				if (component != null) {
					if (startingComponentId == null || projectName == null)
						return;

					if (startingComponentId.equals(component.getComponentId()))
						if (Activator.getComponentService().getProject(projectName).getComponents().contains(component)) {
							componentUi.addNewComponentUI(startedComponent);
						}
				}
			}
		}
	}

	@Override
	public Object polled(Wire wire) {
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
	}

	public void startComponent(String bundleSymbolicName) {
		for (Bundle bundle : Activator.getComponentService().getInstalledComponentBundles()) {
			if (bundle.getSymbolicName().equals(bundleSymbolicName)) {
				try {
					Activator.getComponentService().startComponent(bundle, component,
							Activator.getComponentService().getProjectName(component));
				} catch (BundleException e) {
					MessageDialog.openError(parent.getShell(), "Component not started", "Die Komponente konnte nicht gestartet werden.");
				}
			}
		}
	}

	public void startComponentOrProject(String eventData) {
		String bundleSymbolicName = eventData.substring(0, eventData.indexOf("@"));
		try {
			ProjectConfiguration addedProjectConfiguration = Activator.getComponentService().getDeployedProject(bundleSymbolicName);

			if (addedProjectConfiguration == null) {
				startComponent(bundleSymbolicName);
			} else {
				Activator.getComponentService().startDeployedProject(component, addedProjectConfiguration);
			}
		} catch (UninstallableBundlesException e) {
			MessageDialog.openError(componentUi.getShell(), "Bundleinstallationsfehler",
					"Folgende Bundles konnten nicht installiert werden.\n\n" + e.getMessage()
							+ "\nBitte installieren Sie die erforderlichen Bundles\nund versuchen Sie es erneut!");
			e.printStackTrace();
		} catch (BundleException e) {
			MessageDialog.openError(componentUi.getShell(), "Component Start Error",
					"An error occurred while starting the components of the project\n\n\"" + e.getMessage() + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public void updated(Wire wire, Object value) {
	}
}
