package org.cheetahplatform.modeler.changepattern;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.net.URL;
import java.util.HashMap;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.06.2010
 */
public class ChangePatternModelingActivity extends AbstractModelingActivity {
	public static final String ID = "CHANGE_PATTERN_MODELING";

	protected static Graph createGraph(String path) {
		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + path), new HashMap<Object, Object>());

		try {
			return restoreGraph(input.openStream(), graph);
		} catch (Exception e) {
			Activator.logError("Could not create configuration: " + path, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param modelingProcess
	 */
	public ChangePatternModelingActivity(Process process, String path) {
		this(process, createGraph(path));
	}

	public ChangePatternModelingActivity(Process process, Graph initialGraph) {
		this(process, initialGraph, false);
	}

	public ChangePatternModelingActivity(Process process, Graph initialGraph, boolean optional) {
		super(ID, EditorRegistry.CHANGE_PATTERN, initialGraph, process, optional);
	}

	@Override
	public Object getName() {
		return "Modeling using Change Pattern";
	}
}
