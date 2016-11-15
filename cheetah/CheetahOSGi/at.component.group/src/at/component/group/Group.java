package at.component.group;

import java.util.HashMap;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;

import at.component.AbstractComponent;
import at.component.group.ui.ComponentUiRunnable;

public class Group extends AbstractComponent {

	private ComponentUiController componentUiController;

	@Override
	public void addUI(Composite composite) {
		componentUiController = new ComponentUiController(composite, this);
	}

	@Override
	public boolean canHaveChildComponents() {
		return true;
	}

	@Override
	public HashMap<String, String> getData() {
		if (componentUiController != null) {
			HashMap<String, ComponentUiRunnable> childComponentBoundsMap = componentUiController.getComponentUiRunnables();

			if (childComponentBoundsMap.size() == 0)
				return null;

			HashMap<String, String> mapToSave = new HashMap<String, String>();

			for (String componentId : childComponentBoundsMap.keySet()) {
				ComponentUiRunnable runnable = childComponentBoundsMap.get(componentId);
				mapToSave.put(componentId, runnable.getComponentUi().getBounds().x + "," + runnable.getComponentUi().getBounds().y + ","
						+ runnable.getComponentUi().getBounds().width + "," + runnable.getComponentUi().getBounds().height);
			}

			return mapToSave;
		}

		return null;
	}

	@Override
	public String getName() {
		if (name == null) {
			Object bundleName = Activator.getBundleContext().getBundle().getHeaders().get(Constants.BUNDLE_NAME);

			if (bundleName != null && !bundleName.toString().trim().equals(""))
				name = bundleName.toString().trim();
			else
				name = Activator.getBundleContext().getBundle().getSymbolicName();
		}

		return name;
	}

	@Override
	public String getNameWithId() {
		return getName() + " (ID = " + getComponentId() + ")";
	}

	@Override
	public void initialize(HashMap<String, String> data) {
		if (componentUiController != null && data != null && data.size() > 0) {

			HashMap<String, Rectangle> componentBoundsMap = new HashMap<String, Rectangle>();

			for (String componentId : data.keySet()) {
				String boundsString = data.get(componentId);
				int x = Integer.valueOf(boundsString.substring(0, boundsString.indexOf(",")));
				boundsString = boundsString.substring(boundsString.indexOf(",") + 1);
				int y = Integer.valueOf(boundsString.substring(0, boundsString.indexOf(",")));
				boundsString = boundsString.substring(boundsString.indexOf(",") + 1);
				int width = Integer.valueOf(boundsString.substring(0, boundsString.indexOf(",")));
				boundsString = boundsString.substring(boundsString.indexOf(",") + 1);
				int height = Integer.valueOf(boundsString.substring(0));

				Rectangle bounds = new Rectangle(x, y, width, height);
				componentBoundsMap.put(componentId, bounds);
			}

			componentUiController.setBounds(componentBoundsMap);
		}
	}
}
