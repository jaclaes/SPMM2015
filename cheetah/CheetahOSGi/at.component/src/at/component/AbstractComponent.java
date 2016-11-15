package at.component;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public abstract class AbstractComponent implements IComponent {
	private ServiceRegistration componentRegistration;
	private String componentId;
	private IComponent parent;
	private List<IComponent> childComponents;
	protected String name;

	public AbstractComponent() {
		childComponents = new LinkedList<IComponent>();
	}

	@Override
	public boolean canHaveChildComponents() {
		return false;
	}

	@Override
	public List<IComponent> getChildComponents() {
		return childComponents;
	}

	@Override
	public String getComponentId() {
		return componentId;
	}

	@Override
	public ServiceReference getComponentReference() {
		if (componentRegistration != null)
			return componentRegistration.getReference();
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNameWithId() {
		return getName() + " (ID = " + getComponentId() + ")";
	}

	@Override
	public IComponent getParent() {
		return parent;
	}

	@Override
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public void setComponentRegistration(ServiceRegistration componentRegistration) {
		this.componentRegistration = componentRegistration;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setParent(IComponent parent) {
		this.parent = parent;
	}

	@Override
	public void unregister() {
		componentRegistration.unregister();
	}
}
