package org.cheetahplatform.modeler.dialog;

import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.graph.SyntaxCheckResponse;
import org.cheetahplatform.modeler.graph.SyntaxChecker;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.ProcessOfProcessModelingIteration;
import org.cheetahplatform.modeler.graph.model.Graph;

public class SyntaxStatistic implements IPpmStatistic {

	@Override
	public String getHeader() {
		return "Syntax Errors";
	}

	@Override
	public String getName() {
		return "Syntax Errors";
	}

	@Override
	public String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		SyntaxChecker syntaxChecker = new SyntaxChecker();
		String type = processInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Process process = ProcessRepository.getProcess(processInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS));
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		AbstractModelingActivity.restoreGraph(graph, processInstance);

		try {
			SyntaxCheckResponse response = syntaxChecker.invoke(graph);
			if (response.getErrorMessage() != null) {
				return response.getErrorMessage();
			}

			return String.valueOf(response.getSyntaxErrors().size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return N_A;
	}

	@Override
	public boolean isActive() {
		return CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_SYNTAX_STATISTIC);
	}

}
