/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.DataContainer;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This parser reads an XML file conforming to the MXML format and extracts all relevant information - some part of the data is omitted,
 * since it does not fit our model.
 */
public class XMLPromReader {
	private final Process process;

	/**
	 * Creates a new LogmodelParser for given file.
	 * 
	 * @param file
	 *            The file to parse.
	 * @throws Exception
	 *             if the content could not be parsed
	 */
	public XMLPromReader(File file) throws Exception {
		this(new FileInputStream(file));

	}

	public XMLPromReader(InputStream input) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			Document document = factory.newDocumentBuilder().parse(input);

			Node log = document.getChildNodes().item(0);
			if (!log.getNodeName().equals(XMLPromWriter.XML_WORKFLOW_LOG_TAG))
				throw new IllegalXMLFormatException("The root node must be labeled " + XMLPromWriter.XML_WORKFLOW_LOG_TAG); //$NON-NLS-1$

			List<Process> processList = parse(log);
			Assert.isLegal(processList.size() == 1, "There should be exactly one process in the log!"); //$NON-NLS-1$
			process = processList.get(0);
		} catch (Exception e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error reading log.", e));
			throw e;
		}
	}

	public Process getProcess() {
		return process;
	}

	/**
	 * Parses the given node containing all processes.
	 * 
	 * @param workflowLog
	 *            The node containing all processes.
	 * @return The parsed processes.
	 */
	private List<Process> parse(Node workflowLog) {
		List<Process> result = new ArrayList<Process>();

		NodeList content = workflowLog.getChildNodes();
		for (int i = 0; i < content.getLength(); i++) {
			Node node = content.item(i);
			String nodeName = node.getNodeName();
			if (nodeName.equals(XMLPromWriter.XML_PROCESS_TAG)) {
				result.add(parseProcess(node));
				break;
			}

			// ignore all other nodes
		}

		return result;
	}

	/**
	 * Parses the data of a processInstance.
	 * 
	 * @param dataNode
	 *            The Node with the data.
	 * @param container
	 *            the container to which the data will be added.
	 */
	private void parseData(Node dataNode, DataContainer container) {
		NodeList childNodes = dataNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item.getNodeName().equals(XMLPromWriter.XML_ATTRIBUTE_TAG)) {
				String content = item.getTextContent();
				String name = item.getAttributes().item(0).getTextContent();
				container.setAttribute(name, content);
			}
		}
	}

	/**
	 * Parses the audit trail entry containing the details about the log entry.
	 * 
	 * @param node
	 *            The node containing the information about the entry (as children).
	 * @param instance
	 *            The process instance to which the log entry belongs.
	 */
	private void parseEntry(Node node, ProcessInstance instance) {
		NodeList content = node.getChildNodes();
		AuditTrailEntry entry = new AuditTrailEntry();
		instance.addEntry(entry);

		for (int i = 0; i < content.getLength(); i++) {
			Node childNode = content.item(i);
			String name = childNode.getNodeName();
			if (name.equals(XMLPromWriter.XML_WORKFLOW_MODEL_ELEMENT_TAG)) {
				entry.setWorkflowModelElement(childNode.getTextContent());
			} else if (name.equals(XMLPromWriter.XML_EVENT_TYPE)) {
				entry.setEventType(childNode.getTextContent());
			} else if (name.equals(XMLPromWriter.XML_TIME_STAMP_TAG)) {
				entry.setTimestamp(parseTimeStamp(childNode));
			} else if (name.equals(XMLPromWriter.XML_DATA_TAG)) {
				parseData(childNode, entry);
			}
		}
	}

	/**
	 * Parses the process instance containing the logs.
	 * 
	 * @param node
	 *            The node containing the process instance.
	 * @param process
	 *            The process containing the process instance to parse.
	 */
	private void parseInstance(Node node, Process process) {
		ProcessInstance instance = new ProcessInstance();
		process.add(instance);

		NamedNodeMap attributes = node.getAttributes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node item = attributes.item(j);
			if (item.getNodeName().equals(XMLPromWriter.XML_ID_ATTRIBUTE))
				instance.setId(item.getTextContent());
		}

		NodeList content = node.getChildNodes();

		for (int i = 0; i < content.getLength(); i++) {
			Node childNode = content.item(i);

			// omit all other nodes
			if (childNode.getNodeName().equals(XMLPromWriter.XML_AUDIT_TRAIL_ENTRY_TAG)) {
				parseEntry(childNode, instance);
			} else if (childNode.getNodeName().equals(XMLPromWriter.XML_DATA_TAG)) {
				parseData(childNode, instance);
			}
		}
	}

	/**
	 * Parses the node containing all processes.
	 * 
	 * @param node
	 *            The node.
	 * @return The parsed {@link Process}.
	 */
	private Process parseProcess(Node node) {
		String id = null;

		NamedNodeMap attributes = node.getAttributes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node item = attributes.item(j);
			if (item.getNodeName().equals(XMLPromWriter.XML_ID_ATTRIBUTE)) {
				id = item.getTextContent();
			}
		}

		Assert.isNotNull(id, "The process to be parsed has no id."); //$NON-NLS-1$
		Process process = new Process(id);

		NodeList content = node.getChildNodes();
		for (int i = 0; i < content.getLength(); i++) {
			Node childNode = content.item(i);

			if (childNode.getNodeName().equals(XMLPromWriter.XML_PROCESS_INSTANCE_TAG))
				parseInstance(childNode, process);
		}

		return process;
	}

	private Date parseTimeStamp(Node childNode) {
		String text = childNode.getTextContent();

		try {
			return XMLPromWriter.TIME_STAMP.parse(text);
		} catch (ParseException e) {
			try {
				long timeStamp = Long.parseLong(text);
				return new Date(timeStamp);
			} catch (NumberFormatException e1) {
				// fall-back for legacy data
				DateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				try {
					return timeStamp.parse(text);
				} catch (ParseException e2) {
					return null;
				}
			}
		}
	}
}
