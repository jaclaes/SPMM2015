package org.cheetahplatform.conformance.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.declarative.constraint.ChainedResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedSuccessionConstraint;
import org.cheetahplatform.core.declarative.constraint.ExclusiveChoiceConstraint;
import org.cheetahplatform.core.declarative.constraint.InitConstraint;
import org.cheetahplatform.core.declarative.constraint.MOutOfNConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiPrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.MutualExclusionConstraint;
import org.cheetahplatform.core.declarative.constraint.NegationResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.RespondedExistenceConstraint;
import org.cheetahplatform.core.declarative.constraint.SelectionConstraint;
import org.cheetahplatform.core.declarative.constraint.SuccessionConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DeclarativeModelParser {

	private DeclarativeProcessSchema schema;

	private void checkForConstraintUsage(Node constraintNode, String name) {
		NodeList childNodes = constraintNode.getChildNodes();
		boolean used = true;

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);

			if (child.getNodeName().equals("constraintparameters")) {
				used = child.getFirstChild().getFirstChild().hasChildNodes();
				break;
			}
		}

		Assert.isTrue(!used, "Unknown constraint: " + name);
	}

	private DeclarativeActivity findActivity(Node node) {
		Node branchesNode = node.getChildNodes().item(0);
		Node branchNode = branchesNode.getFirstChild();
		if (branchNode == null) {
			return null;// invalid model
		}

		String name = branchNode.getAttributes().getNamedItem("name").getTextContent();

		for (INode activity : schema.getNodes()) {
			if (activity.getName().equals(name)) {
				return (DeclarativeActivity) activity;
			}
		}

		throw new IllegalArgumentException("There is no activity for name: " + name);
	}

	private NodeList getConstraintParameters(Node constraintNode) {
		NodeList childNodes = constraintNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);

			if (child.getNodeName().equals("constraintparameters")) {
				return (child).getChildNodes();
			}
		}

		throw new IllegalStateException("Could not find the constraint parameters.");
	}

	public DeclarativeProcessSchema parse(String modelName, InputStream model) throws SAXException, IOException,
			ParserConfigurationException {
		this.schema = new DeclarativeProcessSchema(modelName);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		Document document = factory.newDocumentBuilder().parse(model);

		Node modelNode = document.getFirstChild();
		Node assignmentNode = modelNode.getFirstChild();

		NodeList childNodes = assignmentNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeName().equals("activitydefinitions")) {
				parseActivityDefinitions(node);
			} else if (node.getNodeName().equals("constraintdefinitions")) {
				parseConstraints(node);
			}
		}

		model.close();

		return schema;
	}

	private void parseActivityDefinitions(Node activities) {
		NodeList childNodes = activities.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node activity = childNodes.item(i);
			String name = activity.getAttributes().getNamedItem("name").getTextContent();
			long id = Long.parseLong(activity.getAttributes().getNamedItem("id").getTextContent());

			DeclarativeActivity declarativeActivity = schema.createActivity(name);
			declarativeActivity.setCheetahId(id);
		}
	}

	private void parseAlternativePrecedenceConstraint(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));
		DeclarativeActivity activity3 = findActivity(parameters.item(2));
		List<DeclarativeActivity> precedence = new ArrayList<DeclarativeActivity>();
		precedence.add(activity1);
		precedence.add(activity2);

		schema.addConstraint(new MultiPrecedenceConstraint(precedence, activity3));
	}

	private void parseChainedResponse(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new ChainedResponseConstraint(activity1, activity2));
	}

	private void parseChainedSuccession(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new ChainedSuccessionConstraint(activity1, activity2));
	}

	private void parseConstraint(Node constraintNode) {
		NodeList childNodes = constraintNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);

			if (child.getNodeName().equals("template")) {
				NodeList templateChildrenList = child.getChildNodes();

				for (int j = 0; j < templateChildrenList.getLength(); j++) {
					Node templateChild = templateChildrenList.item(j);
					if (!templateChild.getNodeName().equals("name")) {
						continue;
					}

					String name = templateChild.getTextContent();

					if (name.equals("not co-existence")) {
						parseMutualExclusion(constraintNode);
					} else if (name.equals("precedence")) {
						parsePrecedence(constraintNode);
					} else if (name.equals("negation response") || name.equals("not succession")) {
						parseNegationResponse(constraintNode);
					} else if (name.equals("existence")) {
						parseSelectionConstraint(constraintNode, 1, Integer.MAX_VALUE);
					} else if (name.equals("absence2")) {
						parseSelectionConstraint(constraintNode, 0, 1);
					} else if (name.equals("chain response")) {
						parseChainedResponse(constraintNode);
					} else if (name.equals("exactly1")) {
						parseSelectionConstraint(constraintNode, 1, 1);
					} else if (name.equals("strong init") || name.equals("init")) {
						parseInit(constraintNode);
					} else if (name.equals("succession")) {
						parseSuccession(constraintNode);
					} else if (name.equals("chain succession")) {
						parseChainedSuccession(constraintNode);
					} else if (name.equals("exclusive choice")) {
						parseExlusiveChoiceConstraint(constraintNode);
					} else if (name.equals("responded existence")) {
						parseRespondedExistenceConstraint(constraintNode);
					} else if (name.equals("choice 1 of 3")) {
						parseMOutOfNConstraint(constraintNode, 1, 3);
					} else if (name.equals("choice 1of 4")) {
						parseMOutOfNConstraint(constraintNode, 1, 4);
					} else if (name.equals("alternative precedence")) {
						parseAlternativePrecedenceConstraint(constraintNode);
					} else {
						checkForConstraintUsage(constraintNode, name);
					}
				}
			}
		}
	}

	private void parseConstraints(Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			parseConstraint(childNodes.item(i));
		}
	}

	private void parseExlusiveChoiceConstraint(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		if (activity1 == null || activity2 == null) {
			return; // invalid model
		}

		schema.addConstraint(new ExclusiveChoiceConstraint(activity1, activity2));
	}

	private void parseInit(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity = findActivity(parameters.item(0));

		schema.addConstraint(new InitConstraint(activity));
	}

	private void parseMOutOfNConstraint(Node constraintNode, int minimum, int activitiesCount) {
		NodeList parameters = getConstraintParameters(constraintNode);
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
		for (int i = 0; i < activitiesCount; i++) {
			activities.add(findActivity(parameters.item(i)));
		}

		schema.addConstraint(new MOutOfNConstraint(activities, minimum));
	}

	private void parseMutualExclusion(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new MutualExclusionConstraint(activity1, activity2));
	}

	private void parseNegationResponse(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new NegationResponseConstraint(activity1, activity2));
	}

	private void parsePrecedence(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new PrecedenceConstraint(activity1, activity2));
	}

	private void parseRespondedExistenceConstraint(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new RespondedExistenceConstraint(activity1, activity2));
	}

	private void parseSelectionConstraint(Node constraintNode, int minimum, int maximum) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity = findActivity(parameters.item(0));

		schema.addConstraint(new SelectionConstraint(activity, minimum, maximum));
	}

	private void parseSuccession(Node constraintNode) {
		NodeList parameters = getConstraintParameters(constraintNode);
		DeclarativeActivity activity1 = findActivity(parameters.item(0));
		DeclarativeActivity activity2 = findActivity(parameters.item(1));

		schema.addConstraint(new SuccessionConstraint(activity1, activity2));
	}
}
