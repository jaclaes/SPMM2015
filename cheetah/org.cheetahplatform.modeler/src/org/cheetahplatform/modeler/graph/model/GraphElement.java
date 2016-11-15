package org.cheetahplatform.modeler.graph.model;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;

public abstract class GraphElement extends GenericModel implements INamed {

	protected String name;
	private long id;
	protected IGraphElementDescriptor descriptor;
	/**
	 * Allows for storing additional properties for clients.
	 */
	protected Map<String, Object> properties;

	protected GraphElement(IGenericModel parent, IGraphElementDescriptor descriptor, long id) {
		super(parent);
		this.descriptor = descriptor;
		this.id = id;
		this.properties = new HashMap<String, Object>();
	}

	public IGraphElementDescriptor getDescriptor() {
		return descriptor;
	}

	public Graph getGraph() {
		return (Graph) getParentType(Graph.class);
	}

	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the name, the empty string if no name is given.
	 * 
	 * @return the name
	 */
	public String getNameNullSafe() {
		if (name == null) {
			return "";
		}

		return name;
	}

	public Object getProperty(String property) {
		return properties.get(property);
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;

		firePropertyChanged(ModelerConstants.PROPERTY_NAME);
	}

	public void setProperty(String property, Object value) {
		properties.put(property, value);
		firePropertyChanged(property);
	}

}
