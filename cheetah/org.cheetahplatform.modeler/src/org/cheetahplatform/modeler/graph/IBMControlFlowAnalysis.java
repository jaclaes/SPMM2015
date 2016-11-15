package org.cheetahplatform.modeler.graph;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.w3c.dom.Document;

import com.ibm.bit.bpservices.ControlFlowAnalysisServiceStub;
import com.ibm.bit.bpservices.components.responses.ControlFlowAnalysisServiceResponse;

public class IBMControlFlowAnalysis extends AbstractIBMService {
	public ControlFlowAnalysisServiceResponse invoke(Graph graph) throws Exception {
		Document document = toIBMFormat(graph);

		ControlFlowAnalysisServiceResponse response = new ControlFlowAnalysisServiceStub().doCFA(document);
		if (response.getError() != null) {
			copyErrorToClipBoard(graph, response);
		}

		return response;
	}
}
