package org.cheetahplatform.modeler.graph.export.declare;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareAssignmentConverter extends AbstractMarshalOnlyConverter {

	private static final String ASSIGNMENT = "assignment";

	public DeclareAssignmentConverter() {
		super(DeclareAssignment.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareAssignment assignment = (DeclareAssignment) source;

		writer.startNode(ASSIGNMENT);
		writer.addAttribute("language", "ConDec");
		writer.addAttribute("name", "Cheetah Model");
		writer.startNode("activitydefinitions");
		for (DeclareActivity activity : assignment.getActivityDefinitions()) {
			context.convertAnother(activity);
		}
		writer.endNode();

		writer.startNode("constraintdefinitions");
		for (DeclareConstraint constrain : assignment.getConstraintDefinitions()) {
			context.convertAnother(constrain);
		}
		writer.endNode();

		writer.startNode("graphical");
		context.convertAnother(assignment.getGraphical());
		writer.endNode();
		writer.endNode();
	}

}
