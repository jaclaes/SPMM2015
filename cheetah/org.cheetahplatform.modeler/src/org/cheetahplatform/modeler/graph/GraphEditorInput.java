package org.cheetahplatform.modeler.graph;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class GraphEditorInput implements IEditorInput {

	private final Graph graph;
	private Process process;
	private String taskDescription;

	/**
	 * A list of attributes which describe the graph.
	 */
	private List<Attribute> attributes;

	public GraphEditorInput(Graph graph, List<Attribute> attributes, Process process) {
		this.graph = graph;
		this.attributes = attributes;
		this.process = process;
	}

	public GraphEditorInput(Graph graph, List<Attribute> attributes, Process process, String taskdescription) {
		this(graph, attributes, process);
		this.taskDescription = taskdescription;
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public Graph getGraph() {
		return graph;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * Returns the process.
	 * 
	 * @return the process
	 */
	public Process getProcess() {
		return process;
	}

	public String getTaskDescription() {
		if (taskDescription == null) {
			return "";
		}
		return taskDescription;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

	public boolean hasGraph() {
		return graph != null;
	}
}
