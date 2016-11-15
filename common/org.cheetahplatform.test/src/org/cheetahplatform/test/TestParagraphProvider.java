/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.IMappingProvider;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class TestParagraphProvider implements IMappingProvider {

	private Map<Node, Paragraph> paragraphMapping;
	private Map<Edge, EdgeCondition> edgeConditionMapping;

	public TestParagraphProvider() {
		this.paragraphMapping = new HashMap<Node, Paragraph>();
		this.edgeConditionMapping = new HashMap<Edge, EdgeCondition>();
	}

	public void associate(Edge edge, EdgeCondition edgeCondition) {
		edgeConditionMapping.put(edge, edgeCondition);
	}

	public void associate(Node node, Paragraph paragraph) {
		paragraphMapping.put(node, paragraph);
	}

	@Override
	public void dispose() {
		// nothing to dispose
	}

	@Override
	public EdgeCondition getEdgeConditionMapping(Edge edge) {
		return edgeConditionMapping.get(edge);
	}

	@Override
	public String getParagraphMapping(Node node) {
		Paragraph paragraph = paragraphMapping.get(node);
		if (paragraph == null) {
			return "";
		}

		return paragraph.getDescription();
	}

	@Override
	public void initialize(ProcessInstanceDatabaseHandle handle) {
		// nothing to initialize
	}

}
