package at.component.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import at.component.IComponent;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.ConnectionInformation;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;

public class Connector {

	private static Connector INSTANCE = null;

	public static Connector getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Connector();
		return INSTANCE;
	}

	private IComponentWireAdmin componentWireAdmin;

	private IComponentService componentService;

	private Connector() {
		componentService = Activator.getComponentService();
		componentWireAdmin = Activator.getComponentWireAdmin();
	}

	private void removeUnconnectableTargetComponents(List<IComponent> potentialTargetComponents) {
		List<IComponent> componentsToRemove = new ArrayList<IComponent>();

		for (IComponent component : potentialTargetComponents) {
			if (!componentWireAdmin.isComponentProducerAndConsumer(component))
				componentsToRemove.add(component);
		}

		potentialTargetComponents.removeAll(componentsToRemove);
	}

	/**
	 * This method shows the SWT-Dialog that allows to edit the connections between the given and all other components.
	 * 
	 * @param shell
	 *            The parent shell
	 * @param style
	 *            The style of the <code>Dialog</code>
	 * @param component
	 *            The component for which the connections can be edited
	 */
	public void showConnectorDialog(Shell shell, int style, IComponent component) {
		if (componentWireAdmin.isComponentProducerAndConsumer(component)) {
			List<IComponent> connectedTargetComponents = componentWireAdmin.getConnectedComponents(component);

			String projectName = componentService.getProjectName(component);

			List<IComponent> potentialTargetComponents = componentService.getComponents(projectName);
			potentialTargetComponents.remove(component);
			potentialTargetComponents.remove(componentService.getProject(projectName).getProjectComponent());
			potentialTargetComponents.removeAll(connectedTargetComponents);
			removeUnconnectableTargetComponents(potentialTargetComponents);

			List<IComponent> potentialTargetComponentsBefore = new ArrayList<IComponent>();
			potentialTargetComponentsBefore.addAll(potentialTargetComponents);
			List<IComponent> connectedTargetComponentsBefore = new ArrayList<IComponent>();
			connectedTargetComponentsBefore.addAll(connectedTargetComponents);

			ConnectorDialog dialog = new ConnectorDialog(shell, style, component, connectedTargetComponents, potentialTargetComponents);
			dialog.open();

			updateWires(component, potentialTargetComponentsBefore, connectedTargetComponentsBefore, potentialTargetComponents,
					connectedTargetComponents);
		} else {
			MessageDialog.openError(shell, "Keine Verbindung möglich",
					"Da die Komponente weder Daten verschickt noch empfängt,\nwird auch keine Verbindung benötigt!");
		}
	}

	private void updateWires(IComponent sourceComponent, List<IComponent> potentialTargetComponentsBefore,
			List<IComponent> connectedTargetComponentsBefore, List<IComponent> potentialTargetComponentsAfter,
			List<IComponent> connectedTargetComponentsAfter) {
		String sourceComponentPid = componentWireAdmin.getPersistentIdOfComponent(sourceComponent);

		List<ConnectionInformation> wiresToDelete = new ArrayList<ConnectionInformation>();
		for (IComponent component : potentialTargetComponentsAfter) {
			if (!potentialTargetComponentsBefore.contains(component)) {
				wiresToDelete.add(new ConnectionInformation(componentWireAdmin.getPersistentIdOfComponent(component), sourceComponentPid));
				wiresToDelete.add(new ConnectionInformation(sourceComponentPid, componentWireAdmin.getPersistentIdOfComponent(component)));
			}
		}

		List<ConnectionInformation> wiresToCreate = new ArrayList<ConnectionInformation>();
		for (IComponent component : connectedTargetComponentsAfter) {
			if (!connectedTargetComponentsBefore.contains(component)) {
				wiresToCreate.add(new ConnectionInformation(componentWireAdmin.getPersistentIdOfComponent(component), sourceComponentPid));
				wiresToCreate.add(new ConnectionInformation(sourceComponentPid, componentWireAdmin.getPersistentIdOfComponent(component)));
			}
		}

		for (ConnectionInformation connectionInformation : wiresToDelete) {
			componentWireAdmin.deleteWireWithPids(connectionInformation.getProducerPid(), connectionInformation.getConsumerPid());
		}

		for (ConnectionInformation connectionInformation : wiresToCreate) {
			componentWireAdmin.createWireWithPids(connectionInformation.getProducerPid(), connectionInformation.getConsumerPid(), null);
		}
	}
}
