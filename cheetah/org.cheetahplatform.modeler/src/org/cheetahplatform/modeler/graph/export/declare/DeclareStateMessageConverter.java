package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareStateMessageConverter extends AbstractMarshalOnlyConverter {

	public DeclareStateMessageConverter() {
		super(DeclareStateMessage.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareStateMessage message = (DeclareStateMessage) source;

		writer.addAttribute("state", message.getState());
		writer.setValue(message.getMessage());
	}

}
