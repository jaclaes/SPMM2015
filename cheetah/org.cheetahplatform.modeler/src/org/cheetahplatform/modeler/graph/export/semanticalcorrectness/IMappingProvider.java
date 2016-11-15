package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public interface IMappingProvider {

	void dispose();

	EdgeCondition getEdgeConditionMapping(Edge edge);

	String getParagraphMapping(Node node);

	void initialize(ProcessInstanceDatabaseHandle handle);

}
