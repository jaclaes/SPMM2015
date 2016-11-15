package org.cheetahplatform.modeler.graph;

import java.util.List;

import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public abstract class GraphEditor extends EditorPart {

	protected GraphicalGraphViewerWithFlyoutPalette viewer;

	/**
	 * Create the descriptors describing the graph's edges.
	 * 
	 * @return the edge descriptors
	 */
	protected abstract List<IEdgeDescriptor> createEdgeDescriptors();

	protected IGraphicalGraphViewerAdvisor createGraphAdvisor() {
		GraphEditorInput editorInput = (GraphEditorInput) getEditorInput();
		IGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(createNodeDescriptors(), createEdgeDescriptors());

		if (editorInput.hasGraph()) {
			Graph graph = editorInput.getGraph();
			advisor = new DefaultGraphicalGraphViewerAdvisor(createNodeDescriptors(), createEdgeDescriptors(), graph);
		}

		return advisor;
	}

	/**
	 * Create the descriptors describing the graph's nodes.
	 * 
	 * @return the node descriptors
	 */
	protected abstract List<INodeDescriptor> createNodeDescriptors();

	@Override
	public void createPartControl(Composite parent) {
		IGraphicalGraphViewerAdvisor advisor = createGraphAdvisor();

		viewer = new GraphicalGraphViewerWithFlyoutPalette(parent, advisor);
		initializeViewer();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// not necessary
	}

	@Override
	public void doSaveAs() {
		// not necessary
	}

	public Graph getGraph() {
		return viewer.getGraph();
	}

	public GraphicalGraphViewerWithFlyoutPalette getGraphViewer() {
		return viewer;
	}

	public ScrollingGraphicalViewer getViewer() {
		return viewer.getViewer();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof GraphEditorInput)) {
			throw new PartInitException("Input must be of type " + GraphEditorInput.class);
		}

		setSite(site);
		setInput(input);
	}

	/**
	 * Initialize the viewer - subclasses may extend.
	 */
	protected void initializeViewer() {
		// no initialization required yet
	}

	@Override
	public boolean isDirty() {
		// never dirty, as all commands are logged immediately
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setEditable(boolean editable) {
		((GraphEditDomain) viewer.getViewer().getEditDomain()).setEditable(editable);
	}

	@Override
	public void setFocus() {
		viewer.getViewer().getControl().setFocus();
	}
}