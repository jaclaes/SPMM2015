package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.XStream;

/**
 * Exporter for exporting graphs to a custom format - implemented for Matthias Weidlich.
 * 
 * @author Stefan Zugal
 * 
 */
public class CustomGraphNotationExporter extends AbstractExporter {

	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private static final String START_GRAPHS = "<graphs>\n";
	private static final String END_GRAPHS = "</graphs>";

	private List<String> toWrite;
	private File target;
	private CustomNodeConverter nodeConverter;

	public CustomGraphNotationExporter() {
		toWrite = new ArrayList<String>();
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance modelingInstance = handle.getInstance();

		String type = modelingInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Process process = ProcessRepository.getProcess(modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS));
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);
		AbstractModelingActivity.restoreGraph(graph, modelingInstance);
		ProcessInstanceDatabaseHandle graphHandle = new ProcessInstanceDatabaseHandle(handle.getDatabaseId(), modelingInstance.getId(), "",
				"");
		graphHandle.setInstance(modelingInstance);
		ReplayModel replayModel = new ReplayModel(new GraphCommandStack(graph), graphHandle, graph);

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(modelingInstance.getAttributeObject(CommonConstants.ATTRIBUTE_PROCESS));
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, modelingInstance.getId()));
		attributes.add(new Attribute(ModelerConstants.ATTRIBUTE_TYPE, type));

		XStream xStream = new XStream();
		xStream.registerConverter(new CustomGraphConverter(attributes));
		nodeConverter = new CustomNodeConverter(replayModel, process);
		xStream.registerConverter(nodeConverter);
		xStream.registerConverter(new CustomEdgeConverter());
		String xml = xStream.toXML(graph);
		xml = xml.replaceAll("<org.cheetahplatform.modeler.graph.model.Graph>\n", "");
		xml = xml.replaceAll("\n</org.cheetahplatform.modeler.graph.model.Graph>", "");
		toWrite.add(xml);
	}

	@Override
	public void exportFinished() {
		nodeConverter.dispose();

		if (target == null) {
			return;
		}
		Charset charset = Charset.forName("UTF-8");

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(target);
			out.write(HEADER.getBytes(charset));
			out.write(START_GRAPHS.getBytes(charset));

			for (String graph : toWrite) {
				out.write(graph.getBytes(charset));
				out.write("\n".getBytes(charset));
			}

			out.write(END_GRAPHS.getBytes(charset));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

}
