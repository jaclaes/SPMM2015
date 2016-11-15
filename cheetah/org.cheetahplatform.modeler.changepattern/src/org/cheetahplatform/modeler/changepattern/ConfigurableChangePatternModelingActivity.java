package org.cheetahplatform.modeler.changepattern;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.experiment.ChangePatternRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ChangePatternNode;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ConfigurableChangePatternModelingActivity extends ChangePatternModelingActivity {

	private ChangePatternNode cpNode;
	
	public ConfigurableChangePatternModelingActivity(Process process, Graph initialGraph, ChangePatternNode cpNode) {
		super(process, initialGraph);
		this.cpNode = cpNode;
	}

	@Override
	protected void doExecute() {
		if (cpNode != null){
			CheetahPlatformConfigurator.getInstance().set(IConfiguration.CHANGE_PATTERN_LAYOUT, cpNode.isLayoutAvailable());
			CheetahPlatformConfigurator.getInstance().set(IConfiguration.CHANGE_PATTERN_UNDO, cpNode.isUndoAvailable());
			
			for(String pattern : ChangePatternRegistry.getAll().keySet()){
				CheetahPlatformConfigurator.getInstance().set(pattern, cpNode.getChangePatterns().contains(pattern));
			}			
		}
		super.doExecute();
	}
	
	

}
