package org.cheetahplatform.modeler.graph;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.palette.ToolEntry;

public class MarkingToolEntry extends ToolEntry {

	public MarkingToolEntry(String label, String shortDesc) {
		super(label, shortDesc, SharedImages.DESC_SELECTION_TOOL_16, SharedImages.DESC_SELECTION_TOOL_24, MarkingTool.class);
	}

}
