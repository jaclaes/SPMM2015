package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

public abstract class AbstractMarshalOnlyConverter implements Converter {

	private Class<?> target;

	protected AbstractMarshalOnlyConverter(Class<?> target) {
		this.target = target;
	}

	@Override
	public boolean canConvert(Class type) {
		return target.isAssignableFrom(type);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		throw new AbstractMethodError("Not implemented");
	}

}
