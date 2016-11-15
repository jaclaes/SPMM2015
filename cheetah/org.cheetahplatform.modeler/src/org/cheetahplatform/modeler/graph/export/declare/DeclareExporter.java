package org.cheetahplatform.modeler.graph.export.declare;

import java.io.File;
import java.io.FileOutputStream;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.XStream;

public class DeclareExporter extends AbstractExporter {

	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		ProcessInstance instance = modelingInstance.getInstance();
		String type = instance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		String processId = instance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process process = ProcessRepository.getProcess(processId);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		AbstractModelingActivity.restoreGraph(graph, modelingInstance.getInstance());

		DeclareModel model = new GraphToDeclareModelConverter(graph).convert();
		File output = new File(target, modelingInstance.getId() + ".xml");
		XStream xStream = new XStream();
		xStream.registerConverter(new DeclareModelConverter());
		xStream.registerConverter(new DeclareAssignmentConverter());
		xStream.registerConverter(new DeclareActivityConverter());
		xStream.registerConverter(new DeclareConstraintConverter());
		xStream.registerConverter(new DeclareConstraintParameterConverter());
		xStream.registerConverter(new DeclareConstraintTemplateConverter());
		xStream.registerConverter(new DeclareConstraintTemplateParameterConverter());
		xStream.registerConverter(new DeclareStateMessageConverter());
		xStream.registerConverter(new DeclareGraphicalConverter());
		xStream.registerConverter(new DeclareCellConverter());

		try {
			FileOutputStream out = new FileOutputStream(output);
			String xml = xStream.toXML(model);
			xml = xml.replaceAll("<org.cheetahplatform.modeler.graph.export.declare.DeclareModel>",
					"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<model>");
			xml = xml.replaceAll("</org.cheetahplatform.modeler.graph.export.declare.DeclareModel>", "</model>");

			out.write(xml.getBytes());
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("Could not write the declare model.", e);
		}
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

}
