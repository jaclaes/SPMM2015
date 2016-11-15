/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.xml.FileByteStorage;
import org.cheetahplatform.common.logging.xml.XMLPromReader;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Simple converter for mxml files to randomize their originators and timestamps.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public class MxmlConverter {
	private static final int MINIMUM_TIME_BETWEEN_INSTANCES = 1;
	private static final int MAXIMUM_TIME_BETWEEN_INSTANCES = 48;
	private static final int MAXIMUM_DURATION_OF_ACTIVITY = 60;
	private static final int MINIMUM_TIME_BETWEEN_ACTIVITIES = 30;

	private static final Map<String, List<String>> ORIGINATORS = new HashMap<String, List<String>>();

	private static final Calendar START = new GregorianCalendar(2008, 0, 1, 8, 0);
	private static final String PATH = "C:\\tmp\\cpnToolsSimulationLogDeviations.mxml";

	@BeforeClass
	public static void initalizeOriginators() {
		List<String> names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("X-Ray", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("Discharge", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("Puncture", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("NonOperativeTherapy", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("OperativeTherapy", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("NonOperativeTherpy1", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("Sonography", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("Anamnesis", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("PatientAdmission", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("MRT", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("FollowUpExamination", names);

		names = new ArrayList<String>();
		names.add("Jakob");
		names.add("Stefan");
		names.add("Barbara");
		ORIGINATORS.put("InitialTreatment", names);
	}

	private Calendar calculateAuditTrailEntryStartTime(Calendar previous) {
		GregorianCalendar calendar = new GregorianCalendar(previous.get(Calendar.YEAR), previous.get(Calendar.MONTH),
				previous.get(Calendar.DAY_OF_MONTH), previous.get(Calendar.HOUR_OF_DAY), previous.get(Calendar.MINUTE));

		int minutestoAdd = (int) (Math.random() * MAXIMUM_DURATION_OF_ACTIVITY + MINIMUM_TIME_BETWEEN_ACTIVITIES);

		calendar.add(Calendar.MINUTE, minutestoAdd);
		return calendar;
	}

	private Calendar calculateProcessInstanceStartTime(Calendar previous) {
		GregorianCalendar calendar = new GregorianCalendar(previous.get(Calendar.YEAR), previous.get(Calendar.MONTH),
				previous.get(Calendar.DAY_OF_MONTH), previous.get(Calendar.HOUR_OF_DAY), previous.get(Calendar.MINUTE));

		int hoursToAdd = (int) (Math.random() * MAXIMUM_TIME_BETWEEN_INSTANCES + MINIMUM_TIME_BETWEEN_INSTANCES);

		calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
		return calendar;
	}

	@Ignore
	@Test
	public void convert() throws Exception {
		File file = new File(PATH);
		XMLPromReader reader = new XMLPromReader(file);
		Process process = reader.getProcess();
		List<ProcessInstance> instances = process.getInstances();
		System.out.println(instances.size());
		XMLPromWriter writer = new XMLPromWriter(new FileByteStorage(new File(PATH + "_out")));

		Calendar previousProcessInstanceStart = START;
		Calendar previousAuditTrailEntryStart = START;

		int count = 0;
		for (ProcessInstance processInstance : instances) {
			writer.append(process, processInstance);
			List<AuditTrailEntry> entries = processInstance.getEntries();

			for (int i = 0; i < entries.size(); i++) {
				AuditTrailEntry auditTrailEntry = entries.get(i);
				if (i == 0) {
					Calendar timeStamp = calculateProcessInstanceStartTime(previousProcessInstanceStart);
					auditTrailEntry.setTimestamp(timeStamp.getTime());
					previousProcessInstanceStart = timeStamp;
					previousAuditTrailEntryStart = timeStamp;
				} else {
					Calendar timeStamp = calculateAuditTrailEntryStartTime(previousAuditTrailEntryStart);
					auditTrailEntry.setTimestamp(timeStamp.getTime());
					previousAuditTrailEntryStart = timeStamp;
				}

				String workflowModelElement = auditTrailEntry.getWorkflowModelElement();
				auditTrailEntry.setOriginator(getRandomOriginator(workflowModelElement));
				writer.append(auditTrailEntry);
			}
			count++;
			if (count % 10 == 0) {
				System.out.println("Number of Converted Instances: " + count);
			}
		}

		writer.close();
	}

	@Ignore
	@Test
	public void findAllWorkflowModelElements() throws Exception {
		Set<String> workflowModelElements = new HashSet<String>();

		File file = new File(PATH);
		XMLPromReader reader = new XMLPromReader(file);
		Process process = reader.getProcess();
		List<ProcessInstance> instances = process.getInstances();
		for (ProcessInstance processInstance : instances) {
			for (AuditTrailEntry auditTrailEntry : processInstance.getEntries()) {
				String workflowModelElement = auditTrailEntry.getWorkflowModelElement();
				workflowModelElements.add(workflowModelElement);
			}
		}

		for (String element : workflowModelElements) {
			System.out.println(element);
		}
	}

	@Ignore
	@Test
	public void findLongestProcessInstance() throws Exception {
		File file = new File(PATH);
		XMLPromReader reader = new XMLPromReader(file);
		Process process = reader.getProcess();
		List<ProcessInstance> instances = process.getInstances();
		int longest = 0;
		for (ProcessInstance processInstance : instances) {
			int size = processInstance.getEntries().size();
			if (size > longest) {
				longest = size;
			}
		}
		System.out.println("Longest Process Instance: " + longest);
	}

	private String getRandomOriginator(String workflowModelElement) {
		List<String> possibleOriginators = ORIGINATORS.get(workflowModelElement);
		assertNotNull("Unknown Activity: " + workflowModelElement + " - Check ORIGINATOR Initialization", possibleOriginators);
		int index = (int) (Math.random() * possibleOriginators.size());
		return possibleOriginators.get(index);
	}
}
