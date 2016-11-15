package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareConstraintConverter extends AbstractMarshalOnlyConverter {

	public DeclareConstraintConverter() {
		super(DeclareConstraint.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareConstraint constraint = (DeclareConstraint) source;

		writer.startNode("constraint");
		writer.addAttribute("id", String.valueOf(constraint.getId()));
		writer.addAttribute("mandatory", "true");

		writer.startNode("condition");
		writer.endNode();

		writer.startNode("name");
		writer.setValue(constraint.getName());
		writer.endNode();

		writer.startNode("template");
		context.convertAnother(constraint.getTemplate());
		writer.endNode();

		writer.startNode("constraintparameters");
		for (DeclareConstraintParameter parameter : constraint.getParameters()) {
			context.convertAnother(parameter);
		}
		writer.endNode();

		writer.endNode();
	}

}
