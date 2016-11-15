package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExperimentGraphConverter extends GraphConverter {

	private static final String PROCESS = "process";
	private static final String LOG_EMAIL = "logemail";
	private static final String LOG_URL = "logurl";
	private static final String LOG_USER = "loguser";
	private static final String LOG_PASSWD = "logpasswd";
	private static final String SHOWSTARTMODELINGDIALOG = "showstartmodelingdialog";

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ExperimentGraph.class);
	}

	@Override
	protected Graph createGraph() {
		return new ExperimentGraph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		super.marshal(object, writer, context);
		ExperimentGraph graph = (ExperimentGraph) object;
		writer.startNode(PROCESS);
		writer.setValue(graph.getProcess());
		writer.endNode();
		writer.startNode(LOG_EMAIL);
		writer.setValue(graph.getEmail());
		writer.endNode();
		writer.startNode(LOG_URL);
		writer.setValue(graph.getUrl());
		writer.endNode();
		writer.startNode(LOG_USER);
		writer.setValue(graph.getUser());
		writer.endNode();
		writer.startNode(LOG_PASSWD);
		writer.setValue(graph.getPassword());
		writer.endNode();
		writer.startNode(SHOWSTARTMODELINGDIALOG);
		writer.setValue(new Boolean(graph.isStartModelingDialogShown()).toString());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ExperimentGraph graph = (ExperimentGraph) super.unmarshal(reader, context);
		reader.moveDown();
		graph.setProcess(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		graph.setEmail(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		graph.setUrl(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		graph.setUser(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		graph.setPassword(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		graph.setStartModelingDialogShown(new Boolean(reader.getValue()));
		reader.moveUp();
		return graph;
	}

}
