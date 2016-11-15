package org.cheetahplatform.j2ee;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class PersistentListConverter extends AbstractCollectionConverter {
	private Mapper mapper;

	public PersistentListConverter(Mapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		return List.class.isAssignableFrom(arg0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext context) {
		List<Object> list = (List<Object>) arg0;
		if (list == null)
			return;
		for (Object obj : list) {
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
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		List<Object> list = new ArrayList<Object>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Object item = readItem(reader, context, list);
			list.add(item);
			reader.moveUp();
		}
		return list;
	}
}
