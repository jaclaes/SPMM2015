package org.cheetahplatform.modeler.experiment.editor.xml;

import javax.xml.bind.DatatypeConverter;

import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.model.Graph;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ModelContainerConverter implements Converter {

	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String GRAPH = "graph";
	public static final String IMAGE = "image";
	public static final String ID = "id";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ModelContainer.class);
	}

	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		ModelContainer model = (ModelContainer) object;
		writer.startNode(NAME);
		writer.setValue(model.getName());
		writer.endNode();
		writer.startNode(ID);
		if (model.getId() != null) {
			writer.setValue(String.valueOf(model.getId()));
		}
		writer.endNode();
		writer.startNode(TYPE);
		writer.setValue(model.getType());
		writer.endNode();
		writer.startNode(GRAPH);
		if (model.getGraph() != null) {
			context.convertAnother(model.getGraph());
		}
		writer.endNode();
		writer.startNode(IMAGE);
		if (model.getImage() != null) {
			writer.setValue(DatatypeConverter.printBase64Binary(model.getImage()));
		}
		writer.endNode();

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ModelContainer model = null;

		reader.moveDown();
		String name = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		long id = Long.valueOf(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		String type = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		if (reader.getValue() != null && !reader.getValue().equals("")) {
			Graph graph = (Graph) context.convertAnother(this, Graph.class);
			model = new ModelContainer(type, graph, name, id);
		}
		reader.moveUp();
		reader.moveDown();
		if (reader.getValue() != null && !reader.getValue().equals("")) {
			byte[] image = DatatypeConverter.parseBase64Binary(reader.getValue());
			model = new ModelContainer(image, name, id);
		}
		reader.moveUp();

		return model;
	}

}
