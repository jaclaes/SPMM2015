package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareActivityConverter extends AbstractMarshalOnlyConverter {

	public DeclareActivityConverter() {
		super(DeclareActivity.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareActivity activity = (DeclareActivity) source;

		writer.startNode("activity");
		writer.addAttribute("id", String.valueOf(activity.getId()));
		writer.addAttribute("name", activity.getName());
		writer.startNode("authorization");
		writer.endNode();
		writer.startNode("datamodel");
		writer.endNode();
		writer.endNode();
	}

}
