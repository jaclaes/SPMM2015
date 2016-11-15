package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareConstraintTemplateConverter extends AbstractMarshalOnlyConverter {

	public DeclareConstraintTemplateConverter() {
		super(DeclareConstraintTemplate.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareConstraintTemplate template = (DeclareConstraintTemplate) source;

		writer.startNode("description");
		writer.setValue(template.getDescription());
		writer.endNode();

		writer.startNode("display");
		writer.setValue(template.getDisplay());
		writer.endNode();

		writer.startNode("name");
		writer.setValue(template.getName());
		writer.endNode();

		writer.startNode("text");
		writer.setValue(template.getText());
		writer.endNode();

		writer.startNode("parameters");
		for (DeclareConstraintTemplateParameter parameter : template.getParameters()) {
			writer.startNode("parameter");
			context.convertAnother(parameter);
			writer.endNode();
		}

		writer.startNode("statemessages");
		for (DeclareStateMessage message : template.getStateMessages()) {
			writer.startNode("message");
			context.convertAnother(message);
			writer.endNode();
		}
		writer.endNode();

		writer.endNode();
	}

}
