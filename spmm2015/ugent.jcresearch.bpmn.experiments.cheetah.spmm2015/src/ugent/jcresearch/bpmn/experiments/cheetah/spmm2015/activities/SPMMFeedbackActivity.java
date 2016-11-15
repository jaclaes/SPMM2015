package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import org.cheetahplatform.modeler.engine.FeedbackActivity;

public class SPMMFeedbackActivity extends FeedbackActivity {
	protected void doExecute() {
		org.cheetahplatform.modeler.Messages.FeedbackActivity_2 = 
			"Please use the upcoming editor to let us know if you have any comments regarding this experiment."
			+ "\nYour feedback is highly appreciated!"
			+ "\n\nAlso, if there were any disruptions during the experiment, if you experienced technical "
			+ "problems, if you had difficulties understanding the instructions or working with the tool, please "
			+ "describe these in the upcoming editor.";
		super.doExecute();
	}
}
