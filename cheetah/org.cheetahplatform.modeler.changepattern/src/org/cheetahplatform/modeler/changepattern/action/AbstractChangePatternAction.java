package org.cheetahplatform.modeler.changepattern.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.06.2010
 */
public abstract class AbstractChangePatternAction extends Action {

	protected final GraphicalGraphViewerWithFlyoutPalette viewer;
	protected final PlainMultiLineButton button;
	private ISelectionChangedListener selectionChangeListener;

	public AbstractChangePatternAction(PlainMultiLineButton button, GraphicalGraphViewerWithFlyoutPalette viewer) {
		Assert.isNotNull(viewer);
		Assert.isNotNull(button);
		this.button = button;
		this.viewer = viewer;

		selectionChangeListener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) AbstractChangePatternAction.this.viewer.getViewer().getSelection();
				setEnabled(isChangePatternExecutable(selection));
			}
		};
		viewer.getViewer().addSelectionChangedListener(selectionChangeListener);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				run();
			}
		});
	}

	protected abstract AbstractChangePattern createChangePattern(IStructuredSelection selection);

	public void dispose() {
		viewer.getViewer().removeSelectionChangedListener(selectionChangeListener);
	}

	protected Edge extractConnection(IStructuredSelection selection) {
		if (selection.size() != 2) {
			return null;
		}

		Object object = selection.toList().get(0);
		if (!(object instanceof NodeEditPart)) {
			return null;
		}
		Node node1 = ((NodeEditPart) object).getModel();
		Object object2 = selection.toList().get(1);
		if (!(object2 instanceof NodeEditPart)) {
			return null;
		}
		Node node2 = ((NodeEditPart) object2).getModel();

		Edge connection = getConnection(node1, node2);
		return connection;
	}

	protected String getActivityName(String initialString) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InputDialog dialog = new InputDialog(shell, "Activity Name", "Please enter a name.", initialString, new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if (newText.trim().isEmpty()) {
					return "Please enter a valid name";
				}
				return null;
			}
		});
		if (!(dialog.open() == Window.OK)) {
			return null;
		}
		return dialog.getValue();
	}

	private Edge getConnection(Node node1, Node node2) {
		for (Edge edge : node1.getSourceConnections()) {
			Node target = edge.getTarget();
			if (target == null) {
				continue;
			}
			if (target.equals(node2)) {
				return edge;
			}
		}

		for (Edge edge : node2.getSourceConnections()) {
			Node target = edge.getTarget();
			if (target == null) {
				continue;
			}
			if (target.equals(node1)) {
				return edge;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected SESEChecker getSESEChecker(IStructuredSelection selection) {
		Set<Node> nodes = new HashSet<Node>();
		List<NodeEditPart> list = selection.toList();
		for (NodeEditPart nodeEditPart : list) {
			nodes.add(nodeEditPart.getModel());
		}

		SESEChecker seseChecker = new SESEChecker(nodes);
		return seseChecker;
	}

	@SuppressWarnings("rawtypes")
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		HashSet<Node> nodes = new HashSet<Node>();
		List list = selection.toList();
		for (Object object : list) {
			if (!(object instanceof NodeEditPart)) {
				return false;
			}
			Node node = ((NodeEditPart) object).getModel();
			nodes.add(node);
		}

		SESEChecker seseChecker = new SESEChecker(nodes);
		return seseChecker.isSESEFragment();
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getViewer().getSelection();
		AbstractChangePattern changePattern = createChangePattern(selection);
		if (changePattern != null) {
			viewer.getViewer().getEditDomain().getCommandStack().execute(changePattern);
		}
		viewer.getViewer().setSelection(new StructuredSelection());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
	}
}