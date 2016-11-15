package org.cheetahplatform.modeler;

import org.cheetahplatform.modeler.graph.export.DurationModelingPhaseDetectionStrategy;
import org.cheetahplatform.modeler.graph.export.IModelingPhaseDetectionStrategy;
import org.eclipse.swt.graphics.RGB;

public class ModelerConstants {
	public static final RGB COLOR_ACTIVITY_LABEL_FONT = new RGB(75, 75, 75);
	public static final RGB COLOR_ACTIVITY_BORDER = new RGB(91, 129, 178);
	public static final RGB COLOR_ACTIVITY_SELECTION = new RGB(0, 0, 0);
	public static final RGB COLOR_SELECTION = new RGB(220, 220, 220);

	public static final String PROPERTY_NODES = "PROPERTY_NODES";
	public static final String PROPERTY_EDGES = "PROPERTY_EDGES";
	public static final String PROPERTY_LAYOUT = "PROPERTY_LAYOUT";
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_DELETED = "PROPERTY_DELETED";
	public static final String PROPERTY_BACKGROUND_COLOR = "PROPERTY_BACKGROUND_COLOR";
	public static final String PROPERTY_BENDPOINT = "PROPERTY_BENDPOINT";

	public static final String ATTRIBUTE_TYPE = "type";
	public static final String ATTRIBUTE_ACTIVITY_WIDTH = "activity_size_width";
	public static final String ATTRIBUTE_ACTIVITY_HEIGHT = "activity_size_height";
	public static final String ATTRIBUTE_EXPERIMENTAL_WORKFLOW_ACTIVITY_DURATION = "experimental workflow activity duration";
	public static final String ATTRIBUTE_COMPOUND_COMMAND_NAME = "compound_command_name";
	public static final String ATTRIBUTE_CONSTRAINT_ID = "constraint_id";
	public static final String ATTRIBUTE_UNSUCCESFUL_LAYOUT_CAUSE = "unsuccessful_layout_cause";
	public static final String ATTRIBUTE_LAYOUT_DURATION = "layout_duration";

	public static final int MAXIMUM_EDGE_LABEL_WIDTH = 150;

	public static final int DEFAULT_COMPREHENSION_THRESHOLD = 15000;
	public static final int DEFAULT_COMPREHENSION_AGGREGATION_THRESHOLD = 4000;
	public static final IModelingPhaseDetectionStrategy DEFAULT_DETECTION_STRATEGY = new DurationModelingPhaseDetectionStrategy(2000, 60000);
}
