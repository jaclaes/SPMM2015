package org.cheetahplatform.conformance.core;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN_ACTIVITY;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_AND_GATEWAY;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_END_EVENT;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_START_EVENT;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_XOR_GATEWAY;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BPMNModelParser {
	private static class ParsedEdge {
		private long source;
		private long target;

		public ParsedEdge(long source, long target) {
			this.source = source;
			this.target = target;
		}

		/**
		 * @return the source
		 */
		public long getSource() {
			return source;
		}

		/**
		 * @return the target
		 */
		public long getTarget() {
			return target;
		}

		public boolean isIncomingOf(long node) {
			return target == node;
		}

	}

	private ImperativeProcessSchema schema;

	private Map<Long, String> gatewayIdToType;
	private List<ParsedEdge> edges;

	/**
	 * The gateways require the edges to be parsed as we need to identify splits/joins.
	 */
	private void createGateways() {
		for (Map.Entry<Long, String> entry : gatewayIdToType.entrySet()) {
			long id = entry.getKey();
			String type = entry.getValue();

			int incoming = 0;
			for (ParsedEdge edge : edges) {
				if (edge.isIncomingOf(id)) {
					incoming++;
				}
			}

			boolean isJoin = incoming > 1;
			INode gateway = null;

			if (isJoin) {
				if (BPMN_AND_GATEWAY.equals(type)) {
					gateway = schema.createAndJoin("");
				} else if (BPMN_XOR_GATEWAY.equals(type)) {
					gateway = schema.createXorJoin("");
				} else {
					throw new IllegalArgumentException("Unkown type: " + type);
				}
			} else {
				if (BPMN_AND_GATEWAY.equals(type)) {
					gateway = schema.createAndSplit("");
				} else if (BPMN_XOR_GATEWAY.equals(type)) {
					gateway = schema.createXorSplit("");
				} else {
					throw new IllegalArgumentException("Unkown type: " + type);
				}
			}

			gateway.setCheetahId(id);
		}
	}

	private void linkNodes() {
		for (ParsedEdge edge : edges) {
			IImperativeNode source = schema.getNode(edge.getSource());
			IImperativeNode target = schema.getNode(edge.getTarget());

			source.addSuccessor(target);
		}
	}

	private void parseEdge(Node edge) {
		NodeList childNodes = edge.getChildNodes();
		long source = 0;
		long target = 0;

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);

			if ("source".equals(child.getNodeName())) {
				source = Long.parseLong(child.getTextContent());
			} else if ("target".equals(child.getNodeName())) {
				target = Long.parseLong(child.getTextContent());
			}
		}

		edges.add(new ParsedEdge(source, target));
	}

	private void parseEdges(Node edges) {
		NodeList childNodes = edges.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node edge = childNodes.item(i);

			if (edge.getNodeName().equals("edge")) {
				parseEdge(edge);
			}
		}
	}

	public ImperativeProcessSchema parseModel(String modelName, InputStream model) throws SAXException, IOException,
			ParserConfigurationException {
		schema = new ImperativeProcessSchema(modelName);
		gatewayIdToType = new HashMap<Long, String>();
		edges = new ArrayList<ParsedEdge>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		Document document = factory.newDocumentBuilder().parse(model);

		Node graphsNode = document.getFirstChild();
		NodeList graphNodeCandidates = graphsNode.getChildNodes();
		for (int i = 0; i < graphNodeCandidates.getLength(); i++) {
			Node graphNode = graphNodeCandidates.item(i);

			if (!graphNode.getNodeName().endsWith("graph")) {
				continue;
			}

			NodeList childNodes = graphNode.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node child = childNodes.item(j);

				if (child.getNodeName().equals("nodes")) {
					parseNodes(child);
				} else if (child.getNodeName().equals("edges")) {
					parseEdges(child);
				}
			}

			break;
		}

		createGateways();
		linkNodes();
		replaceXorWithLoops();

		return schema;
	}

	private void parseNode(Node node) {
		NodeList childNodes = node.getChildNodes();
		long id = 0;
		String name = "";
		String type = "";

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);

			if (child.getNodeName().equals("id")) {
				id = Long.parseLong(child.getTextContent());
			} else if (child.getNodeName().equals("name")) {
				name = child.getTextContent();
			} else if (child.getNodeName().equals("type")) {
				type = child.getTextContent();
			}
		}

		INode createdNode = null;
		if (BPMN_ACTIVITY.equals(type)) {
			createdNode = schema.createActivity(name);
		} else if (BPMN_START_EVENT.equals(type)) {
			createdNode = schema.getStart();
		} else if (BPMN_END_EVENT.equals(type)) {
			createdNode = schema.getEnd();
		} else if (BPMN_AND_GATEWAY.equals(type) || BPMN_XOR_GATEWAY.equals(type)) {
			gatewayIdToType.put(id, type);
		} else {
			throw new IllegalArgumentException("Unkown type: " + type);
		}

		if (createdNode != null) {
			createdNode.setCheetahId(id);
		}
	}

	private void parseNodes(Node nodes) {
		NodeList childNodes = nodes.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);

			if (node.getNodeName().equals("node")) {
				parseNode(node);
			}
		}
	}

	private void replaceXorWithLoops() {
		FindXorLoopsVisitor visitor = new FindXorLoopsVisitor();
		schema.accept(visitor);

		for (XorLoop loop : visitor.getXorLoops()) {
			loop.replaceWithExplicitLoop(schema);
		}
	}
}
