package org.cheetahplatform.modeler.changepattern;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.experiment.IActivityFactory;
import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ChangePatternActivityFactory implements IActivityFactory {

	private static final String TUTORIAL_TYPE = "ExpEditor.TutorialActivity";
	private static final String CHANGE_PATTERN_TYPE = "Change Pattern Tutorial";
	private static final String CHANGE_PATTERN_MODELING = "ExpEditor.ChangePatternModelingActivity";
	
	@Override
	public AbstractExperimentsWorkflowActivity createActivity(String type, String subtype, Node configNode) {
		AbstractExperimentsWorkflowActivity result = null;
		if (type.equals(TUTORIAL_TYPE) && subtype != null && subtype.equals(CHANGE_PATTERN_TYPE)){
			result = new ChangePatternTutorialActivity();
		} else if (type.equals(CHANGE_PATTERN_MODELING)){
			Graph initialGraph = null;
			Process process = null;
			if (configNode != null){
				ChangePatternNode cpNode = (ChangePatternNode) configNode;
				initialGraph = cpNode.getInitialGraph();
				process = new Process(cpNode.getName());
				result = new ConfigurableChangePatternModelingActivity(process, initialGraph, cpNode);
			} else {
				result = new ChangePatternModelingActivity(process, initialGraph);
			}
		}
		return result;
	}

}
