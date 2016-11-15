/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.logging.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

public class XMLPromWriter implements IPromWriter {
	public static final String XML_ATTRIBUTE_TAG = "Attribute"; //$NON-NLS-1$
	public static final String XML_WORKFLOW_LOG_TAG = "WorkflowLog"; //$NON-NLS-1$
	public static final String XML_DATA_TAG = "Data"; //$NON-NLS-1$
	public static final String XML_PROCESS_TAG = "Process"; //$NON-NLS-1$
	public static final String XML_PROCESS_INSTANCE_TAG = "ProcessInstance"; //$NON-NLS-1$
	public static final String XML_ID_ATTRIBUTE = "id"; //$NON-NLS-1$
	public static final String XML_AUDIT_TRAIL_ENTRY_TAG = "AuditTrailEntry"; //$NON-NLS-1$
	public static final String XML_WORKFLOW_MODEL_ELEMENT_TAG = "WorkflowModelElement"; //$NON-NLS-1$
	public static final String XML_EVENT_TYPE = "EventType"; //$NON-NLS-1$
	public static final String XML_TIME_STAMP_TAG = "Timestamp"; //$NON-NLS-1$
	public static final String XML_TYPE_ATTRIBUTE = "type"; //$NON-NLS-1$

	private static final String PROCESS_TRAILER = "\t</Process>\n</WorkflowLog>";
	private static final String PROCESS_INSTANCE_TRAILER = "\t\t</" + XML_PROCESS_INSTANCE_TAG + ">\n\t</Process>\n</WorkflowLog>"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<" //$NON-NLS-1$
			+ XML_WORKFLOW_LOG_TAG
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\n"; //$NON-NLS-1$
	private static final String INDENT = "\t\t\t\t"; //$NON-NLS-1$

	public static final DateFormat TIME_STAMP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssSSSZ");

	/**
	 * Escapes the given xml string (i.e. convert < to &lt; etc.).
	 * 
	 * @param xml
	 *            the xml to be converted
	 * @return the escaped xml
	 */
	public static String escapeXml(String xml) {
		if (xml == null) {
			return "";
		}

		StringWriter stringWriter = new StringWriter(xml.length());
		PrettyPrintWriter writer = new PrettyPrintWriter(stringWriter);
		writer.setValue(xml);
		writer.flush();
		writer.close();
		return stringWriter.toString();
	}

	private final IByteStorage storage;
	private Process process;
	private String processInstanceId;

	public XMLPromWriter(IByteStorage storage) {
		this.storage = storage;
	}

	@Override
	public IStatus append(AuditTrailEntry entry) {
		try {
			long position = storage.getPointer();
			storage.seek(position - PROCESS_INSTANCE_TRAILER.getBytes().length);

			log("\t\t\t<" + XML_AUDIT_TRAIL_ENTRY_TAG + ">\n"); //$NON-NLS-1$ //$NON-NLS-2$
			logAttributeList(entry.getAttributes());
			log(INDENT);
			log("<WorkflowModelElement>"); //$NON-NLS-1$
			log(escapeXml(entry.getWorkflowModelElement()));
			log("</WorkflowModelElement>\n"); //$NON-NLS-1$
			log(INDENT);
			log("<EventType>"); //$NON-NLS-1$
			log(escapeXml(entry.getEventType()));
			log("</EventType>\n"); //$NON-NLS-1$
			log(INDENT);
			log("<Timestamp>"); //$NON-NLS-1$
			log(escapeXml(createTimeStamp(entry)));
			log("</Timestamp>\n"); //$NON-NLS-1$
			if (entry.getOriginator() != null) {
				log(INDENT);
				log("<Originator>");
				log(escapeXml(entry.getOriginator()));
				log("</Originator>\n");
			}
			log("\t\t\t</" + XML_AUDIT_TRAIL_ENTRY_TAG + "> \n"); //$NON-NLS-1$ //$NON-NLS-2$

			log(PROCESS_INSTANCE_TRAILER);
		} catch (IOException e) {
			Activator.logError("Couldn't write log entry", e); //$NON-NLS-1$
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Couldn't write log entry", e);
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus append(Process process, ProcessInstance instance) {
		Assert.isTrue(this.process == null || this.process.equals(process));

		try {
			if (this.process == null) {
				log(HEADER);
				log("\t<" + XML_PROCESS_TAG + " id=\"" + process.getId() + "\">\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				log(PROCESS_TRAILER);
			}

			this.process = process;
			processInstanceId = instance.getId();
			List<Attribute> attributes = instance.getAttributes();

			long position = storage.getPointer();
			storage.seek(position - PROCESS_TRAILER.getBytes().length);

			log("\t\t<" + XML_PROCESS_INSTANCE_TAG + " id=\"" + processInstanceId + "\">\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			logAttributeList(attributes);
			log(PROCESS_INSTANCE_TRAILER);
		} catch (IOException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Couldn't write first log entry.", e);
		}

		return Status.OK_STATUS;
	}

	@Override
	public void close() {
		try {
			if (storage != null) {
				storage.close();
			}
		} catch (IOException e) {
			Activator.logError("Couldn't close logfile " + storage, e); //$NON-NLS-1$
		}
	}

	private String createTimeStamp(AuditTrailEntry entry) {
		return TIME_STAMP.format(entry.getTimestamp());
	}

	@Override
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	private void log(String string) throws IOException {
		storage.write(string.getBytes("utf-8")); //$NON-NLS-1$
	}

	private void logAttribute(Attribute currentAttribute) throws IOException {
		log(INDENT);
		log("\t<" + XML_ATTRIBUTE_TAG + " name=\""); //$NON-NLS-1$ //$NON-NLS-2$
		log(escapeXml(currentAttribute.getName()));
		log("\">"); //$NON-NLS-1$
		log(escapeXml(currentAttribute.getContent()));
		log("</" + XML_ATTRIBUTE_TAG + ">\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Logs the given attributes. The content of the attribute will be escaped (e.g. < becomes &lt;).
	 * 
	 * @param Attribute
	 *            the attributes to be logged
	 * @throws IOException
	 *             if the attributes could not be logged
	 */
	private void logAttributeList(List<Attribute> Attribute) throws IOException {
		if (Attribute.isEmpty())
			return;

		log(INDENT);
		log("<" + XML_DATA_TAG + ">\n"); //$NON-NLS-1$ //$NON-NLS-2$

		for (Attribute currentAttribute : Attribute) {
			logAttribute(currentAttribute);
		}
		log(INDENT);
		log("</" + XML_DATA_TAG + ">\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
