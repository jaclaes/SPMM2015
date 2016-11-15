package org.cheetahplatform.modeler.bpmn;

import java.io.InputStream;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.geometry.Point;

public class BPMNModelingActivity extends AbstractModelingActivity {

	public static final String ID = "BPMN_MODELING";

	private Point initialSize;

	public BPMNModelingActivity(Graph initialGraph, Process process) {
		this(initialGraph, process, false);
	}

	public BPMNModelingActivity(Graph initialGraph, Process process, boolean optional) {
		super(ID, EditorRegistry.BPMN, initialGraph, process, optional);

		this.initialSize = new Point(120, 50);
	}

	public BPMNModelingActivity(InputStream initialProcess, Graph initialGraph, Process process) throws Exception {
		this(restoreGraph(initialProcess, initialGraph), process);
	}

	public BPMNModelingActivity(InputStream initialProcess, Graph initialGraph, Process process, boolean optional) throws Exception {
		this(restoreGraph(initialProcess, initialGraph), process, optional);
	}

	@Override
	protected List<Attribute> collectAttributes() {
		List<Attribute> attributes = super.collectAttributes();

		if (initialSize != null) {
			attributes.add(new Attribute(ModelerConstants.ATTRIBUTE_ACTIVITY_WIDTH, initialSize.x));
			attributes.add(new Attribute(ModelerConstants.ATTRIBUTE_ACTIVITY_HEIGHT, initialSize.y));
		}

		return attributes;
	}

	@Override
	public Object getName() {
		return "BPMN Modeling";
	}

	/**
	 * @param initialSize
	 *            the initialSize to set
	 */
	public void setInitialSize(Point initialSize) {
		this.initialSize = initialSize;
	}

}
