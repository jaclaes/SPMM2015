package org.cheetahplatform.modeler.experiment.editor.xml;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class AbstractConverter implements Converter {

	protected abstract void startMarshal(HierarchicalStreamWriter writer);
	
	protected abstract void marshalContents(Object object, HierarchicalStreamWriter writer,
			MarshallingContext context);
	
	protected abstract GraphElement createElement(Graph graph, String type, long id);
}
