/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alaskasimulator.core.logging.xml.XMLPromParser;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.StaticPromWriteProvider;
import org.cheetahplatform.common.logging.xml.FileByteStorage;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.cheetahplatform.experiment.thinkaloud.ThinkAloudExperiment;
import org.eclipse.core.runtime.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AtlasToMxmlConverter {
	private class Quotation {
		private String id;
		private String startDate;
		private String code;

		public Quotation(String id, String startDate) {
			this.id = id;
			this.startDate = startDate;
		}

		protected String getCode() {
			return code;
		}

		protected Date getStartDate() throws ParseException {
			return simpleDateFormat.parse(startDate);
		}

		protected void setCode(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return id + ":" + code;
		}
	}

	private class QuotationProcessInstance {
		private String id;
		private List<Quotation> quotations;

		public QuotationProcessInstance(String id) {
			super();
			this.id = id;
			quotations = new ArrayList<AtlasToMxmlConverter.Quotation>();
		}

		public void addQuotation(Quotation quotation) {
			quotations.add(quotation);
		}

		protected String getId() {
			return id;
		}

		protected List<Quotation> getQuotations() {
			return quotations;
		}
	}

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d'T'H:m:s");

	private static final String PATH = "C:\\tmp\\";
	private static final String FILE = PATH + "hu_mortgageCP05quotations.xml";
	private static final String TARGET = PATH + "hu_mortgageCP05quotations.mxml";
	private static Process process = ThinkAloudExperiment.MODELING_TASK_2_CHANGE_PATTERN;

	// private static final String FILE = PATH + "TDM.xml";
	// private static final String TARGET = PATH + "TDM.mxml";
	// private static Process process = TdmCaseStudyExperiment.TDM_MODELING_PROCESS;

	public static void main(String[] args) throws Exception {
		XMLPromParser parser = new XMLPromParser(new File("C:\\tmp\\out.mxml"));
		Process process = parser.getProcess();

		String[] codes = new String[] { "ask (domain)", "ask (experiment)", "ask (notation)", "ask (process model)", "ask (session)",
				"ask (testcase)", "challenge", "clarify (domain)", "clarify (experiment)", "clarify (notation)", "clarify (process model)",
				"clarify (session)", "clarify (testcase)", "model (process model)", "model (testcase)", "propose",
				"propose (process model)", "propose (testcase)", "support" };

		Set<String> ids = new HashSet<String>();
		ids.add("10.txt");

		for (ProcessInstance instance : process.getInstances()) {
			if (!ids.contains(instance.getId())) {
				continue;
			}

			Map<String, Integer> count = new TreeMap<String, Integer>();
			for (String code : codes) {
				count.put(code, 0);
			}

			for (AuditTrailEntry entry : instance.getEntries()) {
				String type = entry.getWorkflowModelElement();
				Integer current = count.get(type);
				if (current == null) {
					current = 0;
				}

				current = current + 1;
				count.put(type, current);
			}

			System.out.println(instance.getId() + "\n");
			for (Map.Entry<String, Integer> entry : count.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}

			System.out.println("\n\n");
		}

	}

	private Map<String, Quotation> quotationMap;
	private Map<String, QuotationProcessInstance> processInstancesMap;

	private Map<String, String> codeMap;

	@Ignore
	@Test
	public void convert() throws Exception {
		quotationMap = new HashMap<String, AtlasToMxmlConverter.Quotation>();
		codeMap = new HashMap<String, String>();
		processInstancesMap = new HashMap<String, AtlasToMxmlConverter.QuotationProcessInstance>();

		parse();

		PromLogger promLogger = new PromLogger(new StaticPromWriteProvider(new XMLPromWriter(new FileByteStorage(new File(TARGET)))));

		Collection<QuotationProcessInstance> values = processInstancesMap.values();
		for (QuotationProcessInstance processInstance : values) {
			ProcessInstance instanceToLog = new ProcessInstance(processInstance.getId());
			promLogger.append(process, instanceToLog);
			List<Quotation> quotations = processInstance.getQuotations();
			for (Quotation quotation : quotations) {
				AuditTrailEntry entry = new AuditTrailEntry("running", quotation.getCode());
				entry.setTimestamp(quotation.getStartDate());
				promLogger.append(entry);
			}
		}
		promLogger.close();
	}

	private List<Node> filterNodes(NodeList root, String type) {
		ArrayList<Node> arrayList = new ArrayList<Node>();
		for (int i = 0; i < root.getLength(); i++) {
			Node item = root.item(i);
			if (item == null) {
				continue;
			}
			if (type.equals(item.getNodeName())) {
				arrayList.add(item);
			}
		}
		return arrayList;
	}

	private String getAttribute(Node node, String attribute) {
		NamedNodeMap attributes = node.getAttributes();
		Node namedItem = attributes.getNamedItem(attribute);
		String textContent = namedItem.getTextContent();
		return textContent;
	}

	private Node getChild(Node node, String name) {
		List<Node> filterNodes = filterNodes(node.getChildNodes(), name);
		Assert.isLegal(filterNodes.size() == 1);

		return filterNodes.get(0);
	}

	private void parse() throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
		FileInputStream input = new FileInputStream(new File(FILE));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		Document document = factory.newDocumentBuilder().parse(input);
		Node root = document.getChildNodes().item(0);
		parsePrimDocs(root);
		parseCodes(root);
		parseLinks(root);
	}

	private void parseCodes(Node root) {
		Node codes = getChild(root, "codes");
		List<Node> codeList = filterNodes(codes.getChildNodes(), "code");
		for (Node code : codeList) {
			String id = getAttribute(code, "id");
			String name = getAttribute(code, "name");
			codeMap.put(id, name);
		}
	}

	private void parseLinks(Node root) {
		Node links = getChild(root, "links");
		Node objectSegmentLinks = getChild(links, "objectSegmentLinks");
		Node codings = getChild(objectSegmentLinks, "codings");
		List<Node> iLinks = filterNodes(codings.getChildNodes(), "iLink");
		for (Node node : iLinks) {
			String codeReference = getAttribute(node, "obj");
			String quotationReference = getAttribute(node, "qRef");

			Quotation quotation = quotationMap.get(quotationReference);
			String code = codeMap.get(codeReference);
			quotation.setCode(code);
		}
	}

	private void parsePrimDoc(Node primDoc) {
		String processInstanceId = getAttribute(primDoc, "name");
		QuotationProcessInstance processInstance = new QuotationProcessInstance(processInstanceId);

		Node quotations = getChild(primDoc, "quotations");
		List<Node> qs = filterNodes(quotations.getChildNodes(), "q");
		for (Node node : qs) {
			String id = getAttribute(node, "id");
			String startDate = getAttribute(node, "cDate");
			// String endDate = getAttribute(node, "mDate");
			Quotation quotation = new Quotation(id, startDate);
			quotationMap.put(id, quotation);
			processInstance.addQuotation(quotation);
		}
		processInstancesMap.put(processInstanceId, processInstance);
	}

	private void parsePrimDocs(Node root) {
		List<Node> primDocs = filterNodes(root.getChildNodes(), "primDocs");
		for (Node node : primDocs) {
			List<Node> primDocList = filterNodes(node.getChildNodes(), "primDoc");
			for (Node primDoc : primDocList) {
				parsePrimDoc(primDoc);
			}
		}
	}
}
