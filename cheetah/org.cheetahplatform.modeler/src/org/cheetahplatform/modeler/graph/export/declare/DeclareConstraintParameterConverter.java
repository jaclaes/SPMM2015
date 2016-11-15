package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareConstraintParameterConverter extends AbstractMarshalOnlyConverter {

	public DeclareConstraintParameterConverter() {
		super(DeclareConstraintParameter.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareConstraintParameter parameter = (DeclareConstraintParameter) source;

		writer.startNode("parameter");
		writer.addAttribute("templateparameter", parameter.getTemplateparameter());
		writer.startNode("branches");
		writer.startNode("branch");
		writer.addAttribute("name", parameter.getBranch());
		writer.endNode();
		writer.endNode();
		writer.endNode();
	}

}
