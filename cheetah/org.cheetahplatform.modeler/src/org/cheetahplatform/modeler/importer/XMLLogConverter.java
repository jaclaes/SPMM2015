package org.cheetahplatform.modeler.importer;

import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_PROCESS_INSTANCE;
import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_TIMESTAMP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseIdGenerator;
import org.cheetahplatform.common.logging.db.DatabasePromWriter;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.dialog.XMLLog;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class XMLLogConverter implements IRunnableWithProgress {

	private final List<XMLLog> xmlLogsToConvert;

	public XMLLogConverter(List<XMLLog> xmlLogsToConvert) {
		Assert.isNotNull(xmlLogsToConvert);
		this.xmlLogsToConvert = xmlLogsToConvert;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		Map<String, String> oldToNewProcessInstanceId;
		List<Process> allProcesses;
		try {
			Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
			oldToNewProcessInstanceId = new HashMap<String, String>();
			DatabaseIdGenerator idGenerator = new DatabaseIdGenerator();
			allProcesses = new ArrayList<Process>();

			PreparedStatement statement = connection.prepareStatement("select log from xml_log where database_id=?");

			for (XMLLog log : xmlLogsToConvert) {
				System.out.print("starting " + log.getId() + " ..");
				statement.setInt(1, log.getId());
				ResultSet resultSet = statement.executeQuery();
				int experimentalWorkflowCount = 0;

				while (resultSet.next()) {
					Clob logClob = resultSet.getClob("log");
					BufferedReader br = new BufferedReader(new InputStreamReader(logClob.getAsciiStream(), "UTF8"));
					String line, totalString = "";
					while ((line = br.readLine()) != null) {
						totalString += line;
					}
					br.close();
					String[] logs = totalString.split("<\\?xml");
					for (String logString : logs) {
						if (logString.equals("")) {
							continue;
						}
						List<Process> fromXML = XMLLogHandler.readLogFrom(log.getId(), new ByteArrayInputStream(("<?xml" + logString)
								.trim().getBytes()));
						allProcesses.addAll(fromXML);

						// process ids first
						for (Process process : fromXML) {
							if (ProcessRepository.isExperimentalWorkflow(process)) {
								experimentalWorkflowCount++;
							}

							for (ProcessInstance instance : process.getInstances()) {
								String newId = idGenerator.generateId();
								oldToNewProcessInstanceId.put(instance.getId(), newId);
								instance.setId(newId);
							}
						}
					}

					if (experimentalWorkflowCount != 1) {
						throw new Exception("There should be exactly one experimental process, but was: " + experimentalWorkflowCount
								+ ", id: " + log.getId());
					}
				}
				System.out.println(".. " + log.getId() + " finished");
			}

			statement.close();
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

		// then convert the process instances and adapt ids accordingly
		for (Process process : allProcesses) {
			String processId = process.getId();
			if (processId.equals("BPMNTestCase")) {
				continue;
			}
			Process processToAppend = ProcessRepository.getProcess(processId);

			for (ProcessInstance instance : process.getInstances()) {
				if (!instance.isAttributeDefined(ATTRIBUTE_TIMESTAMP)) {
					if (instance.hasAuditTrailEntries()) {
						instance.setAttribute(ATTRIBUTE_TIMESTAMP, instance.getEntries().get(0).getTimestamp().getTime());
					}
				}

				DatabasePromWriter writer = new DatabasePromWriter();
				writer.append(processToAppend, instance);

				for (AuditTrailEntry auditTrailEntry : instance.getEntries()) {
					if (auditTrailEntry.isAttributeDefined(ATTRIBUTE_PROCESS_INSTANCE)) {
						String oldId = auditTrailEntry.getAttribute(ATTRIBUTE_PROCESS_INSTANCE);
						String substitutedId = oldToNewProcessInstanceId.get(oldId);
						Assert.isNotNull(substitutedId);
						auditTrailEntry.setAttribute(ATTRIBUTE_PROCESS_INSTANCE, substitutedId);
					}

					writer.append(auditTrailEntry);
				}
			}
		}
	}

}
