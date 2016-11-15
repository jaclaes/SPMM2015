package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015;

import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.configuration.IConfigurator;

public class SPMConfigurator implements IConfigurator {


	@Override
	public void configure(IConfiguration configuration) {
		Messages.ExperimentalWorkflowEngine_22 = "Please enter the access code printed on your instructions sheet";
		configuration.enableModelerMode();
		configuration.set(IConfiguration.EXPERIMENT, new SPMExperiment());
		configuration.set(IConfiguration.DELETE_EDGES_WHEN_DELETING_NODE, true);
		configuration.set(IConfiguration.SHOW_TOOL_BAR, true);
		configuration.set(IConfiguration.SHOW_TUTORIAL_SKIP_STEPS, true);
	}
}
