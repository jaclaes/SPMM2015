package org.cheetahplatform.experiment.editor.prop;

import java.util.Iterator;

import org.cheetahplatform.experiment.editor.desc.ModelListDescriptor;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.hierarchical.OutlineContentProvider;
import org.cheetahplatform.modeler.hierarchical.OutlineLabelProvider;
import org.cheetahplatform.modeler.hierarchical.OutlineViewNode;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class SelectModelDialog extends Dialog {

	private ModelContainer modelContainer;
	private TreeViewer treeViewer; 
	private Graph graph;
	
	public SelectModelDialog(Shell parentShell, Graph graph) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.SHELL_TRIM);
		this.graph = graph;
	}
	
	public ModelContainer getModelContainer(){
		return modelContainer;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		final GridLayout gridLayout = new GridLayout(1, true);
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;

		treeViewer = new TreeViewer(container, SWT.SINGLE);
		treeViewer.setContentProvider(new OutlineContentProvider());
		treeViewer.setLabelProvider(new OutlineLabelProvider());

		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if ("win32".equals(SWT.getPlatform())) {
			// turn on dotted lines in tree
			OS.SetWindowLong(treeViewer.getTree().handle, OS.GWL_STYLE, OS.GetWindowLong(treeViewer.getTree().handle, OS.GWL_STYLE)
					| OS.TVS_HASLINES);
		}

		createInput();
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelection() instanceof IStructuredSelection) {
						IStructuredSelection selection = (IStructuredSelection) event.getSelection();
						@SuppressWarnings("rawtypes")
						Iterator it = selection.iterator();
						if (it.hasNext()) {
							OutlineViewNode<?> node = (OutlineViewNode<?>) it.next();
							if (node.getData() instanceof ModelContainer){
								modelContainer = (ModelContainer) node.getData();
								getButton(IDialogConstants.OK_ID).setEnabled(true);
							} else {
								getButton(IDialogConstants.OK_ID).setEnabled(false);
							}
						}

					}

				}
			});
		
		return container;
	} 
	
	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return control;
	}

	private void createInput() {
		OutlineViewNode<?> root = new OutlineViewNode<Graph>("root", "root", null, null);
		
		for (Node node : graph.getNodes()){
			if (node.getDescriptor() instanceof ModelListDescriptor){
				ModelsNode modelsNode = ((ModelsNode)node);
				OutlineViewNode<?> treeNode = new OutlineViewNode<ModelsNode>(modelsNode.getName(), modelsNode.getName(), modelsNode, root);
				root.addChild(treeNode);
				for (ModelContainer model: modelsNode.getModels()){
					OutlineViewNode<?> modelNode = new OutlineViewNode<ModelsNode.ModelContainer>(model.getName() + " (" + model.getType()+ ")", model.getName(), model, treeNode);
					treeNode.addChild(modelNode);
				}
			}
		}
		treeViewer.setInput(root);
		treeViewer.expandAll();
	}

	
	
}
