package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareModelConverter extends AbstractMarshalOnlyConverter {

	public DeclareModelConverter() {
		super(DeclareModel.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareModel model = (DeclareModel) source;

		context.convertAnother(model.getAssignment());
	}

}
