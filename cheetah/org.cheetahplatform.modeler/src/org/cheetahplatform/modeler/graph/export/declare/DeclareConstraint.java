package org.cheetahplatform.modeler.graph.export.declare;

import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;

public class DeclareConstraint {
	public static DeclareConstraint createConstraint(Edge edge) {
		String id = edge.getDescriptor().getId();

		if (id.equals(EditorRegistry.DECSERFLOW_SELECTION)) {
			return createSingleActivityConstraint(edge, "existence");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_INIT)) {
			return createSingleActivityConstraint(edge, "init");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_PRECEDENCE)) {
			return createConstraint(edge, "precedence", "2", "1");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_RESPONSE)) {
			return createConstraint(edge, "response", "2", "1");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_SUCCESSION)) {
			return createConstraint(edge, "succession", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE)) {
			return createConstraint(edge, "responded existence", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_COEXISTENCE)) {
			return createConstraint(edge, "co-existence", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION)) {
			return createConstraint(edge, "not co-existence", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_NEGATION_RESPONSE)) {
			return createConstraint(edge, "negation response", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_PRECEDENCE)) {
			return createConstraint(edge, "chain precedence", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_RESPONSE)) {
			return createConstraint(edge, "chain response", "1", "2");
		}
		if (id.equals(EditorRegistry.DECSERFLOW_CHAINED_SUCCESSION)) {
			return createConstraint(edge, "chain succession", "1", "2");
		}

		throw new RuntimeException("Unsupported constraint type: " + id);
	}

	private static DeclareConstraint createConstraint(Edge edge, String name, String source, String target) {
		DeclareConstraintTemplate template = DeclareConstraintTemplate.getTemplate(edge);
		DeclareConstraintParameter parameter1 = new DeclareConstraintParameter(source, edge.getSource().getName());
		DeclareConstraintParameter parameter2 = new DeclareConstraintParameter(target, edge.getTarget().getName());

		return new DeclareConstraint(edge.getId(), name, template, parameter1, parameter2);
	}

	private static DeclareConstraint createSingleActivityConstraint(Edge edge, String name) {
		DeclareConstraintTemplate template = DeclareConstraintTemplate.getTemplate(edge);
		DeclareConstraintParameter parameter = new DeclareConstraintParameter("1", edge.getSource().getName());
		return new DeclareConstraint(edge.getId(), name, template, parameter);
	}

	private long id;
	private String name;
	private DeclareConstraintTemplate template;
	private List<DeclareConstraintParameter> parameters;

	public DeclareConstraint(long id, String name, DeclareConstraintTemplate template, DeclareConstraintParameter... parameters) {
		this.id = id;
		this.name = name;
		this.template = template;
		this.parameters = Arrays.asList(parameters);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<DeclareConstraintParameter> getParameters() {
		return parameters;
	}

	public DeclareConstraintTemplate getTemplate() {
		return template;
	}

}
