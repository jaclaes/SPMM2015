package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.experiment.editor.model.SurveyNode;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.survey.core.Survey;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SurveyNodeConverter extends NodeConverter {
	public static final String SURVEYNODE = "surveynode";
	private static final String SURVEY = "survey";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(SurveyNode.class);
	}

	@Override
	protected Node createElement(Graph graph, String type, long id) {
		NodeDescriptor descriptor = ExperimentEditorMarshaller.getNodedescriptorRegistry().getNodeDescriptor(type);
		return new SurveyNode(graph, descriptor, id);
	}

	@Override
	protected void marshalContents(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshalContents(object, writer, context);
		SurveyNode node = (SurveyNode) object;
		writer.startNode(SURVEY);
		if (node.getSurvey() != null) {
			context.convertAnother(node.getSurvey());
		}
		writer.endNode();
	}

	@Override
	protected void startMarshal(HierarchicalStreamWriter writer) {
		writer.startNode(SURVEYNODE);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		SurveyNode node = (SurveyNode) super.unmarshal(reader, context);
		reader.moveDown();
		if (reader.getNodeName().equals(SURVEY) && reader.hasMoreChildren()) {
			Survey survey = (Survey) context.convertAnother(node, Survey.class);
			node.setSurvey(survey);
		}
		reader.moveUp();
		return node;
	}
}
