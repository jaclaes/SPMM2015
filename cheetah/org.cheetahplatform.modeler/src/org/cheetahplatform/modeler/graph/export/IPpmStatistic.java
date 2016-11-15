package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.logging.ProcessInstance;

public interface IPpmStatistic {
	String N_A = "N/A";

	String getHeader();

	String getName();

	String getValue(ProcessInstance processInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations);

	boolean isActive();
}
