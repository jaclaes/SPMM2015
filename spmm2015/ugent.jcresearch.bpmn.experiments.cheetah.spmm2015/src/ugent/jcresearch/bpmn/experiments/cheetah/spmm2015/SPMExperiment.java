package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.AskStudentIDActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.engine.configurations.AbstractExperimentalConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.tutorial.UGent.tool.TutorialActivity;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.BPMNModelingDualTaskActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.CountingSpanActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.HiddenFigures1Activity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.HiddenFigures2Activity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.OperationSpanNLActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.ReadingSpanNLActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.SPMMFeedbackActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.SPMMSurveyActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.ToolClosingMessageActivity;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities.SPMMSurveyActivity.Part;
import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment.TreatmentActivity;


public class SPMExperiment extends AbstractExperimentalConfiguration {
	private static final Process EXPERIMENTAL_PROCESS = new Process("spmm2015");
	private static final Process OSPAN = new Process("OSpan");
	private static final Process CSPAN = new Process("CSpan");
	private static final Process RSPAN = new Process("RSpan");
	private static final Process HFT1 = new Process("HFT1");
	private static final Process HFT2 = new Process("HFT2");
	private static final Process BPMN_CASE_1 = new Process("BPMNCase1");
	private static final Process BPMN_CASE_2 = new Process("BPMNCase2");
	private static final Process BPMN_CASE_3 = new Process("BPMNCase3");
	
	private static final String VERSION = "1.0";


	public SPMExperiment() {
		super();

		//Activator.getDatabaseConnector().setDatabaseURL("jdbc:mysql://mysqlha2.ugent.be/CHEETAH");
		//Activator.getDatabaseConnector().setDefaultCredentials("cheetahtool", "GJjq7ReLSL2QDBAE");
		//Activator.getDatabaseConnector().setAdminCredentials("cheetahtool", "GJjq7ReLSL2QDBAE");

		// Activator.getDatabaseConnector().setDatabaseURL("jdbc:mysql://138.232.65.123:13306/novices_versus_experts");
		// Activator.getDatabaseConnector().setAdminCredentials("nve_user", "3q5469d06ctu");
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}


	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		
		String versionString = "-v" + VERSION; 
		if (!XMLLogHandler.getFilenameBase().endsWith(versionString))
			XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + versionString);
		
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		
		org.cheetahplatform.common.Messages.XMLLogHandler_8b = 
				"\n\nPlease send an email to bpmugent@gmail.com with the file in attachment."
				+ "\n - Subject: 'Submission'"
				+ "\n - Message: Message can be left empty, because this will be ignored. "
				+ "\n            Instead, contact me at jan.claes@ugent.be for questions or remarks."
				+ "\n - Attachment: Put one or more test result zip-files in the attachment"
				+ "\n               (don't change the name of the generated zip files!)"
				+ "\n\nClose this box only AFTER sending the email to make sure you have all the necessary "
				+ "details for composing your email.";
		
		//spmt2014_cf62@sendtodropbox.com

		WorkflowConfiguration configuration = new WorkflowConfiguration(1501);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new HiddenFigures1Activity());
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);

		configuration = new WorkflowConfiguration(1502);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new HiddenFigures2Activity());
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);

		configuration = new WorkflowConfiguration(1503);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new OperationSpanNLActivity());
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(1504);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new CountingSpanActivity());
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(1505);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new ReadingSpanNLActivity());
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(1506);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new SPMMSurveyActivity(Part.PRE, null));
		configuration.add(new SPMMFeedbackActivity());
		configuration.add(new ToolClosingMessageActivity());
		configurations.add(configuration);
		
		// BENCHMARK SESSION
		configuration = new WorkflowConfiguration(1511);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingDualTaskActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_CASE_1, "1"));
		configuration.add(new SPMMSurveyActivity(Part.POST, "visa control"));
		configuration.add(new BPMNModelingDualTaskActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_CASE_2, "2"));
		configuration.add(new SPMMSurveyActivity(Part.POST, "defaulter handling"));
		configuration.add(new SPMMFeedbackActivity());
		configurations.add(configuration);
		
		//TREATMENT GROUP
		configuration = new WorkflowConfiguration(152001);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new TreatmentActivity(false));
		configuration.add(new BPMNModelingDualTaskActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_CASE_3, "3"));
		configuration.add(new SPMMSurveyActivity(Part.POST, "mortgage request"));
		configuration.add(new SPMMFeedbackActivity());
		configurations.add(configuration);
		
		//CONTROL GROUP
		configuration = new WorkflowConfiguration(152002);
		configuration.add(new AskStudentIDActivity());
		configuration.add(new BPMNModelingDualTaskActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_CASE_3, "3"));
		configuration.add(new SPMMSurveyActivity(Part.POST, "mortgage request"));
		configuration.add(new TreatmentActivity(false));
		configuration.add(new SPMMFeedbackActivity());
		configurations.add(configuration);
		
		
		//TEST CONFIGURATIONS
		configuration = new WorkflowConfiguration(201500001);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new HiddenFigures1Activity());
		configurations.add(configuration);

		configuration = new WorkflowConfiguration(201500002);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new HiddenFigures2Activity());
		configurations.add(configuration);

		configuration = new WorkflowConfiguration(201500003);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new OperationSpanNLActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(201500004);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new CountingSpanActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(201500005);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new ReadingSpanNLActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(201500006);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new SPMMSurveyActivity(Part.PRE, null));
		configurations.add(configuration);
		configuration = new WorkflowConfiguration(201500007);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new SPMMSurveyActivity(Part.POST, "<PROCESS_NAME>"));
		configurations.add(configuration);
/*
		configuration = new WorkflowConfiguration(201500008);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new SPMMSurveyActivity(Part.TWO));
		configurations.add(configuration);
*/

		configuration = new WorkflowConfiguration(201500009);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new TutorialActivity());
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(201500010);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new BPMNModelingDualTaskActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_CASE_1, "1"));
		configurations.add(configuration);
		
		configuration = new WorkflowConfiguration(201500011);
		configuration.add(new ShowMessageActivity("Warning", "this is a test configuration!"));
		configuration.add(new TreatmentActivity(true));
		configurations.add(configuration);
		
		return configurations;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		return Collections.emptyMap();
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(OSPAN);
		processes.add(CSPAN);
		processes.add(RSPAN);
		processes.add(HFT1);
		processes.add(HFT2);
		processes.add(BPMN_CASE_1);
		processes.add(BPMN_CASE_2);
		processes.add(BPMN_CASE_3);
		return processes;
	}
}