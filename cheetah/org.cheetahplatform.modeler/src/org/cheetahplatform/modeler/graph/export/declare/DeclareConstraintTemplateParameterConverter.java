package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareConstraintTemplateParameterConverter extends AbstractMarshalOnlyConverter {

	public DeclareConstraintTemplateParameterConverter() {
		super(DeclareConstraintTemplateParameter.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareConstraintTemplateParameter parameter = (DeclareConstraintTemplateParameter) source;

		writer.addAttribute("branchable", parameter.getBranchable());
		writer.addAttribute("id", parameter.getId());
		writer.addAttribute("name", parameter.getName());

		writer.startNode("graphical");
		writer.startNode("style");
		writer.addAttribute("number", parameter.getStyle());
		writer.endNode();

		writer.startNode("begin");
		writer.addAttribute("fill", parameter.getFillBegin());
		writer.addAttribute("style", parameter.getStyleBegin());
		writer.endNode();

		writer.startNode("middle");
		writer.addAttribute("fill", parameter.getFillMiddle());
		writer.addAttribute("style", parameter.getStyleMiddle());
		writer.endNode();

		writer.startNode("end");
		writer.addAttribute("fill", parameter.getFillEnd());
		writer.addAttribute("style", parameter.getStyleEnd());
		writer.endNode();

		writer.endNode();
	}

}
