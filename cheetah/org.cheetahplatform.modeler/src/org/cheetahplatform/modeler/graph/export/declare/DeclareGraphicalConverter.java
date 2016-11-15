package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareGraphicalConverter extends AbstractMarshalOnlyConverter {

	public DeclareGraphicalConverter() {
		super(DeclareGraphical.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareGraphical graphical = (DeclareGraphical) source;

		writer.startNode("activities");
		for (DeclareCell cell : graphical.getActivities()) {
			writer.startNode("cell");
			context.convertAnother(cell);
			writer.endNode();
		}
		writer.endNode();

		writer.startNode("constraints");
		for (DeclareCell cell : graphical.getConstraints()) {
			writer.startNode("cell");
			context.convertAnother(cell);
			writer.endNode();
		}
		writer.endNode();
	}

}
