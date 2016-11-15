package org.cheetahplatform.modeler.graph;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.bit.bpservices.components.responses.BPServiceResponse;
import com.thoughtworks.xstream.XStream;

public abstract class AbstractIBMService {
	private static final String PROCESS_ATTRIBUTE = "process";
	protected static final String DEFAULT_PROCESS_ID = "-1";
	private static final String DEFAULT_DEFINITIONS_ID = "-2";
	private static final String TARGET_REF_ATTRIBUTE = "targetRef";
	private static final String SOURCE_REF_ATTRIBUTE = "sourceRef";
	private static final String SEQUENCE_FLOW_ELEMENT = "sequenceFlow";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String ID_ATTRIBUTE = "id";
	private static final String PROCESS_ELEMENT = PROCESS_ATTRIBUTE;
	private static final String DEFINITIONS_ELEMENT = "definitions";

	protected String copyErrorToClipBoard(Graph graph, BPServiceResponse response) throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException, UnknownHostException {
		Document toFormat = toIBMFormat(graph);
		DOMSource domSource = new DOMSource(toFormat);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.transform(domSource, streamResult);

		String xml = new String(out.toByteArray());
		XStream xStream = new XStream();
		String responseXml = xStream.toXML(response);
		xml = xml + "\n\n\n\n---------------------------\nResponse\n---------------------------\n";
		xml = xml + "host: " + InetAddress.getLocalHost() + "\n---------------------------\n";
		xml = xml + responseXml;

		Clipboard clipboard = new Clipboard(Display.getDefault());
		clipboard.setContents(new Object[] { xml }, new Transfer[] { TextTransfer.getInstance() });

		return xml;
	}

	private String getId(Node node) {
		if (node == null) {
			return "";
		}

		return String.valueOf(node.getId());
	}

	private boolean idEquals(Node node, String id) {
		if (node == null) {
			return false;
		}

		return node.getDescriptor().getId().equals(id);
	}

	public Document toIBMFormat(Graph graph) throws ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		document.setXmlStandalone(true);

		Element defintionsElement = (Element) document.appendChild(document.createElement(DEFINITIONS_ELEMENT));
		defintionsElement.setAttribute(ID_ATTRIBUTE, DEFAULT_DEFINITIONS_ID);
		defintionsElement.setAttribute("typeLanguage", "http://www.w3.org/2001/XMLSchema");
		defintionsElement.setAttribute("expressionLanguage", "http://www.w3.org/1999/XPath");
		defintionsElement.setAttribute("targetNamespace", "http://www.omg.org/bpmn20");
		defintionsElement.setAttribute("xmlns", "http://schema.omg.org/spec/BPMN/2.0");
		defintionsElement.setAttribute("xmlns:bpmndi", "http://bpmndi.org");

		Element processElement = (Element) defintionsElement.appendChild(document.createElement(PROCESS_ELEMENT));
		processElement.setAttribute(ID_ATTRIBUTE, DEFAULT_PROCESS_ID);

		for (Node node : graph.getNodes()) {
			String type = null;
			String id = node.getDescriptor().getId();
			if (id.equals(EditorRegistry.BPMN_ACTIVITY) || id.equals(EditorRegistry.BPMN_ACTIVITY_HIERARCHICAL)) {
				type = "task";
			} else if (id.equals(EditorRegistry.BPMN_AND_GATEWAY)) {
				type = "parallelGateway";
			} else if (id.equals(EditorRegistry.BPMN_XOR_GATEWAY)) {
				type = "exclusiveGateway";
			} else if (id.equals(EditorRegistry.BPMN_START_EVENT)) {
				type = "startEvent";
			} else if (id.equals(EditorRegistry.BPMN_END_EVENT)) {
				type = "endEvent";
			} else {
				throw new IllegalArgumentException("Unknown element type: " + id);
			}

			Element element = document.createElement(type);
			processElement.appendChild(element);
			element.setAttribute(NAME_ATTRIBUTE, node.getNameNullSafe());
			element.setAttribute(ID_ATTRIBUTE, String.valueOf(node.getId()));
			element.setAttribute(PROCESS_ATTRIBUTE, DEFAULT_PROCESS_ID);
		}

		for (Edge edge : graph.getEdges()) {
			if (idEquals(edge.getSource(), EditorRegistry.BPMN_END_EVENT) || idEquals(edge.getTarget(), EditorRegistry.BPMN_START_EVENT)) {
				continue; // the layout cannot cope with incoming edges to start events or outgoing edges from end events
			}

			Element element = (Element) processElement.appendChild(document.createElement(SEQUENCE_FLOW_ELEMENT));
			element.setAttribute(ID_ATTRIBUTE, String.valueOf(edge.getId()));
			element.setAttribute(SOURCE_REF_ATTRIBUTE, getId(edge.getSource()));
			element.setAttribute(TARGET_REF_ATTRIBUTE, getId(edge.getTarget()));
			element.setAttribute(PROCESS_ATTRIBUTE, DEFAULT_PROCESS_ID);
			element.setAttribute(NAME_ATTRIBUTE, edge.getNameNullSafe());
		}

		return document;
	}

}