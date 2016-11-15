package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;

public interface INodeDescriptorRegistry {

	public static final String EXPEDITOR = "ExpEditor";

	public static final String SURVEY = EXPEDITOR + ".SurveyActivity";
	public static final String TUTORIAL = EXPEDITOR + ".TutorialActivity";
	public static final String DECSERFLOW = EXPEDITOR + ".DecSerFlowModelingActivity";
	public static final String CHANGEPATTERN = EXPEDITOR + ".ChangePatternModelingActivity";
	public static final String BPMN = EXPEDITOR + ".BPMNModelingActivity";
	public static final String FEEDBACK = EXPEDITOR + ".FeedbackActivity";
	public static final String COMPREHENSION = EXPEDITOR + ".ComprehensionActivity";
	public static final String QUESTIONLIST = EXPEDITOR + ".QuestionList";
	public static final String MODELLIST = EXPEDITOR + ".ModelList";
	public static final String MESSAGE = EXPEDITOR + ".MessageActivity";

	public NodeDescriptor getNodeDescriptor(String id);
}
