package org.cheetahplatform.j2ee;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class PersistentSetConverter extends AbstractCollectionConverter {
	private Mapper mapper;

	public PersistentSetConverter(Mapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public boolean canConvert(Class arg0) {
		return Set.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context) {
		Set set = (Set) arg0;
		if (set == null)
			return;
		for (Object obj : set) {
			if (obj == null) {
				writer.startNode("null"); //$NON-NLS-1$
				writer.endNode();
			} else {
				writer.startNode(mapper.serializedClass(obj.getClass()));
				context.convertAnother(obj);
				writer.endNode();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Set set = new HashSet();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Object item = readItem(reader, context, set);
			set.add(item);
			reader.moveUp();
		}
		return set;
	}
}
