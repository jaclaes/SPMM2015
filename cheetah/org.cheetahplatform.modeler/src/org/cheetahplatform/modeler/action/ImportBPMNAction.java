package org.cheetahplatform.modeler.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImportBPMNAction extends AbstractModelingEditorAction<GraphicalGraphViewerWithFlyoutPalette> {

	private static class BPMNParser {
		private Map<String, Node> idToNode;
		private final Graph graph;
		private CommandStack commandStack;

		public BPMNParser(GraphicalGraphViewerWithFlyoutPalette viewer) {
			this.graph = viewer.getGraph();
			this.commandStack = viewer.getViewer().getEditDomain().getCommandStack();
			this.idToNode = new HashMap<String, Node>();
		}

		public void parse(String file) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				Document document = factory.newDocumentBuilder().parse(new File(file));

				NodeList processElement = document.getElementsByTagName("process");
				if (processElement.getLength() != 1) {
					throw new Exception("Expected one process as root tag, but was: " + processElement.getLength());
				}

				NodeList children = processElement.item(0).getChildNodes();
				processNodes(children);
				processEdges(children);
			} catch (Exception e) {
				Activator.logError("Could not read an xml file.", e);
			}
		}

		private void processEdges(NodeList children) {
			for (int i = 0; i < children.getLength(); i++) {
				org.w3c.dom.Node child = children.item(i);

				if (!(child instanceof Element)) {
					continue;// process elements only
				}

				String type = child.getNodeName();

				if (type.equals("sequenceFlow")) {
					String sourceId = child.getAttributes().getNamedItem("sourceRef").getTextContent();
					String targetId = child.getAttributes().getNamedItem("targetRef").getTextContent();

					Node source = idToNode.get(sourceId);
					Node target = idToNode.get(targetId);

					Edge edge = new Edge(graph, EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW));
					CreateEdgeCommand createEdgeCommand = new CreateEdgeCommand(graph, edge, source, target, "");
					commandStack.execute(createEdgeCommand);
				} else {
					continue;
				}
			}
		}

		private void processNodes(NodeList children) {
			for (int i = 0; i < children.getLength(); i++) {
				org.w3c.dom.Node child = children.item(i);

				if (!(child instanceof Element)) {
					continue;// process elements only
				}

				String type = child.getNodeName();
				String id = child.getAttributes().getNamedItem("id").getTextContent();
				INodeDescriptor descriptor = null;

				if (type.equals("startEvent")) {
					descriptor = (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT);
				} else if (type.equals("endEvent")) {
					descriptor = (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_END_EVENT);
				} else if (type.equals("task")) {
					descriptor = (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY);
				} else if (type.equals("exclusiveGateway")) {
					descriptor = (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_XOR_GATEWAY);
				} else if (type.equals("parallelGateway")) {
					descriptor = (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_AND_GATEWAY);
				} else {
					continue;
				}

				Node node = new Node(graph, descriptor);
				idToNode.put(id, node);
				CreateNodeCommand createCommand = new CreateNodeCommand(graph, node, new Point(42, 42), id);
				commandStack.execute(createCommand);
			}
		}
	}

	public static final String ID = "org.cheetahplatform.modeler.action.ImportBPMNAction";

	public ImportBPMNAction() {
		super(GraphicalGraphViewerWithFlyoutPalette.class);

		setId(ID);
		setText("Import BPMN");
	}

	@Override
	protected void run(GraphicalGraphViewerWithFlyoutPalette viewer) {
		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		String result = dialog.open();
		if (result == null) {
			return;
		}

		new BPMNParser(viewer).parse(result);
	}
}
