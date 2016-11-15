package org.cheetahplatform.j2ee;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

public class PersistentMapConverter extends MapConverter {

	public PersistentMapConverter(Mapper mapper) {
		super(mapper);
	}

	@Override
	public boolean canConvert(Class arg0) {
		return Map.class.isAssignableFrom(arg0);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Map map = (Map) createCollection(HashMap.class);
		populateMap(reader, context, map);
		return map;
	}

}
