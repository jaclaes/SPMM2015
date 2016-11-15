package org.cheetahplatform.literatemodeling.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class SingleGraphPartSelectionDialog extends GraphPartSelectionDialog {

	/**
	 * @param parentShell
	 * @param graph
	 * @param allowedTypes
	 */
	public SingleGraphPartSelectionDialog(Shell parentShell, Graph graph, Class<? extends IGraphElementDescriptor> allowedType) {
		super(parentShell, graph, asList(allowedType));

	}

	private static List<Class<? extends IGraphElementDescriptor>> asList(Class<? extends IGraphElementDescriptor> allowedType) {
		List<Class<? extends IGraphElementDescriptor>> list = new ArrayList<Class<? extends IGraphElementDescriptor>>();
		list.add(allowedType);
		return list;
	}

	public GraphElement getSelectedElement() {
		return getSelectedElements().get(0);
	}

	@Override
	protected void handleSelectionChanged(ISelection selection) {
		super.handleSelectionChanged(selection);

		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.toList().size() > 1) {
			setError("Please select only one element");
		}
	}
}
