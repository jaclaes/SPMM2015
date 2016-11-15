package org.cheetahplatform.experiment.editor.xml;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.experiment.editor.desc.BPMNDescriptor;
import org.cheetahplatform.experiment.editor.desc.ChangePatternDescriptor;
import org.cheetahplatform.experiment.editor.desc.ComprehensionDescriptor;
import org.cheetahplatform.experiment.editor.desc.DecSerFlowDescriptor;
import org.cheetahplatform.experiment.editor.desc.FeedbackDescriptor;
import org.cheetahplatform.experiment.editor.desc.MessageDescriptor;
import org.cheetahplatform.experiment.editor.desc.ModelListDescriptor;
import org.cheetahplatform.experiment.editor.desc.QuestionListDescriptor;
import org.cheetahplatform.experiment.editor.desc.SurveyDescriptor;
import org.cheetahplatform.experiment.editor.desc.TutorialDescriptor;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;

public class NodeDescriptorRegistry implements INodeDescriptorRegistry {

	private static Map<String, NodeDescriptor> descriptors;
	
	static {
		descriptors = new HashMap<String, NodeDescriptor>();
		descriptors.put(BPMN, new BPMNDescriptor());
		descriptors.put(CHANGEPATTERN, new ChangePatternDescriptor());
		descriptors.put(DECSERFLOW, new DecSerFlowDescriptor());
		descriptors.put(SURVEY, new SurveyDescriptor());
		descriptors.put(QUESTIONLIST, new QuestionListDescriptor());
		descriptors.put(MODELLIST, new ModelListDescriptor());
		descriptors.put(COMPREHENSION, new ComprehensionDescriptor());
		descriptors.put(TUTORIAL, new TutorialDescriptor());
		descriptors.put(FEEDBACK, new FeedbackDescriptor());
		descriptors.put(MESSAGE, new MessageDescriptor());
	}

	@Override
	public NodeDescriptor getNodeDescriptor(String id) {
		return descriptors.get(id);
	}
	
	

}
