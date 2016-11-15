package org.cheetahplatform.modeler.bpmn;

import java.io.InputStream;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;

public class BPMNModelingActivityWithLayout extends BPMNModelingActivity {

	private boolean layoutAvailable;

	public BPMNModelingActivityWithLayout(Graph initialGraph, Process process) {
		this(initialGraph, process, false);
	}

	public BPMNModelingActivityWithLayout(Graph initialGraph, Process process, boolean optional) {
		super(initialGraph, process, optional);
		this.layoutAvailable = CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_LAYOUT_DIALOG);
	}

	public BPMNModelingActivityWithLayout(InputStream initialProcess, Graph initialGraph, Process process, boolean optional)
			throws Exception {
		this(restoreGraph(initialProcess, initialGraph), process, optional);
	}

	@Override
	protected void doExecute() {
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.SHOW_LAYOUT_DIALOG, true);
		super.doExecute();
	}

	@Override
	protected void postExecute() {
		super.postExecute();
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.SHOW_LAYOUT_DIALOG, layoutAvailable);
	}
}
