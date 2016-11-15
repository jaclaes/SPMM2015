package org.cheetahplatform.modeler.experiment.editor.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Stack;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.thoughtworks.xstream.XStream;

public class ExperimentEditorMarshaller {

	static {
		ExperimentEditorMarshaller.setNodeDescriptorRegistry(new INodeDescriptorRegistry() {
			@Override
			public NodeDescriptor getNodeDescriptor(String id) {
				// return a generic node descriptor, the real Node Descriptors aren't available and not necessary
				return new NodeDescriptor("", id, id) {

					@Override
					public IGraphElementFigure createFigure(GraphElement element) {
						return null;
					}

					@Override
					public Point getInitialSize() {
						return new Point(0, 0);
					}

					@Override
					public boolean hasCustomName() {
						return false;
					}

					@Override
					public void updateName(IFigure figure, String name) {
						// nothing to do
					}
				};
			}
		});
		ExperimentEditorMarshaller.setEditPartFactory(new EditPartFactory() {

			@Override
			public EditPart createEditPart(EditPart context, Object model) {
				if (Graph.class.isAssignableFrom(model.getClass())) {
					return new GraphEditPart((Graph) model);
				}
				return null;
			}

		});
	}

	private static INodeDescriptorRegistry nodeDescRegistry;
	private static EditPartFactory editPartFactory;

	private static Stack<Graph> GRAPHSTACK;

	public static final String GRAPH = "graph";
	public static final String EXPERIMENTGRAPH = "experimentgraph";
	private static final String UTF8 = "UTF8";
	private static final String XMLDECL = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
	private static final String ERR1 = "Could not read file contents: ";
	private static final String ERR2 = "The file has an unsupported encoding: ";
	private static final String ERR3 = "File not found: ";
	private static final String ERR4 = "Could not write to file : ";

	public static EditPartFactory getEditPartFactory() {
		return editPartFactory;
	}

	public static Stack<Graph> getGraphStack() {
		return GRAPHSTACK;
	}

	public static INodeDescriptorRegistry getNodedescriptorRegistry() {
		return nodeDescRegistry;
	}

	public static void setEditPartFactory(EditPartFactory editPartFactory) {
		ExperimentEditorMarshaller.editPartFactory = editPartFactory;
	}

	public static void setNodeDescriptorRegistry(INodeDescriptorRegistry nodeDescRegistry) {
		ExperimentEditorMarshaller.nodeDescRegistry = nodeDescRegistry;
	}

	private XStream xstream;

	public ExperimentEditorMarshaller() {
		xstream = createXStream();
		ExperimentEditorMarshaller.GRAPHSTACK = new Stack<Graph>();
	}

	protected XStream createXStream() {
		XStream xstream = new XStream();
		xstream.registerConverter(new GraphConverter());
		xstream.registerConverter(new ExperimentGraphConverter());
		xstream.registerConverter(new NodeConverter());
		xstream.registerConverter(new HierarchicalNodeConverter());
		xstream.registerConverter(new TutorialNodeConverter());
		xstream.registerConverter(new BPMNNodeConverter());
		xstream.registerConverter(new ChangePatternNodeConverter());
		xstream.registerConverter(new DecSerFlowNodeConverter());
		xstream.registerConverter(new SurveyNodeConverter());
		xstream.registerConverter(new ModelsNodeConverter());
		xstream.registerConverter(new ModelContainerConverter());
		xstream.registerConverter(new ComprehensionNodeConverter());
		xstream.registerConverter(new FeedbackNodeConverter());
		xstream.registerConverter(new MessageNodeConverter());
		xstream.registerConverter(new EdgeConverter());
		xstream.registerConverter(new SelectionEdgeConverter());
		xstream.alias(GRAPH, Graph.class);
		xstream.alias(EXPERIMENTGRAPH, ExperimentGraph.class);
		xstream.setClassLoader(Graph.class.getClassLoader()); // needed for OSGI/Eclipse class loader
		return xstream;
	}

	public String marshall(Object graph) {
		return xstream.toXML(graph);
	}

	public void marshallToFile(Graph graph, String filePath) throws ExpEditorMarshallerException {
		try {
			String xml = xstream.toXML(graph);
			saveToFile(xml, filePath);
		} catch (Throwable ex) {
			throw new ExpEditorMarshallerException("UnMarshallerException", ex);
		}
	}

	private String readFromFile(String filePath) throws ExpEditorMarshallerException {
		StringBuffer buff = new StringBuffer();

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), UTF8));
			String line = null;
			try {
				while ((line = in.readLine()) != null) {
					buff.append(line).append("\n");
				}
			} catch (IOException ex) {
				throw new ExpEditorMarshallerException(ERR1 + filePath, ex);
			}
		} catch (UnsupportedEncodingException ex) {
			throw new ExpEditorMarshallerException(ERR2 + filePath, ex);
		} catch (FileNotFoundException ex) {
			throw new ExpEditorMarshallerException(ERR3 + filePath, ex);
		}
		return buff.toString();
	}

	private void saveToFile(String content, String filePath) throws ExpEditorMarshallerException {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), UTF8));
			out.write(XMLDECL);
			out.write(content);
			out.close();
		} catch (IOException ex) {
			throw new ExpEditorMarshallerException(ERR4 + filePath, ex);
		}
	}

	public Object unmarshall(String xml) {
		return xstream.fromXML(xml);
	}

	public Graph unmarshallFromFile(String filePath) throws ExpEditorMarshallerException {
		String content = readFromFile(filePath);
		Graph graph = null;
		if (content.length() > 0) {
			graph = (Graph) xstream.fromXML(content);
		}

		return graph;
	}

}
