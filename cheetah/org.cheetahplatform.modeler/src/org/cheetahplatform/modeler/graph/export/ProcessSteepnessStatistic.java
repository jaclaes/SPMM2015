package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.model.Graph;

public class ProcessSteepnessStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Steepness";
	}

	@Override
	public String getName() {
		return "Steepness";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		String attribute = processInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process process = ProcessRepository.getProcess(attribute);
		String type = processInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);

		GraphCommandStack stack = new GraphCommandStack(graph);
		CommandReplayer replayer = new CommandReplayer(stack, graph, processInstance);

		List<ModelingPhase> lineFragments = new ModelingPhaseDiagrammLineFragmentExtrator().extractLineFragments(stack.getGraph(),
				replayer, processInstance, chunks);

		if (lineFragments.isEmpty()) {
			return N_A;
		}

		ModelingPhase lastFragment = lineFragments.get(lineFragments.size() - 1);
		long endTimeStamp = lastFragment.getEnd();
		int endNumberOfElements = lastFragment.getEndNumberOfElements();

		AuditTrailEntry entry = processInstance.getEntries().get(0);
		long startTimeStamp = (entry.getTimestamp().getTime() - processInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP));

		long duration = endTimeStamp - startTimeStamp;
		double steepness = (double) endNumberOfElements / (duration / 10000);

		DecimalFormat decimalFormat = new DecimalFormat("##.0000");
		return decimalFormat.format(steepness);
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_PPM_STATISTICS);
	}
}
