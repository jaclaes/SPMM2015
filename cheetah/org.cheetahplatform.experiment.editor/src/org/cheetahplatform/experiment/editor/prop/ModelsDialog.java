package org.cheetahplatform.experiment.editor.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.alaskasimulator.config.rcp.ConfigResourceRegistry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.experiment.editor.model.ModelsNode.ModelContainer;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ModelsDialog extends Dialog {
	private static class ModelLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ModelContainer model = (ModelContainer) element;

			if (columnIndex == 0) {
				return model.getName();
			}
			if (columnIndex == 1) {
				if (model.getType().equals(EditorRegistry.BPMN)) {
					return "BPMN";
				} else if (model.getType().equals(EditorRegistry.DECSERFLOW)){
					return "DecSerFlow";
				} else {
					return "Image";
				}
			}
			return null;
		}

	}

	private static class ModelsContentProvider implements
			IStructuredContentProvider {

		@Override
		public void dispose() {
			// nothing to do
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// nothing to do
		}

		@Override
		public Object[] getElements(Object inputElement) {
			@SuppressWarnings("unchecked")
			List<ModelContainer> models = (List<ModelContainer>) inputElement;
			return models.toArray();
		}

	}

	private class MyEditingSupport extends EditingSupport {
		private CellEditor textEditor;

		public MyEditingSupport(TableViewer viewer) {
			super(viewer);
			textEditor = new TextCellEditor(viewer.getTable());
		}

		protected boolean canEdit(Object element) {
			return true;
		}

		protected CellEditor getCellEditor(Object element) {
			return textEditor;
		}

		protected Object getValue(Object element) {
			return ((ModelContainer) element).getName();
		}

		protected void setValue(Object element, Object value) {
			((ModelContainer) element).setName(value.toString());
			getViewer().update(element, null);
		}

	}
	
	private List<ModelContainer> models;
	private TableViewer modelsTable;
	private ModelsComposite modelsComposite;

	protected ModelsDialog(Shell parentShell, List<ModelContainer> models) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.SHELL_TRIM);
		this.models = models;
	}

	public List<ModelContainer> getModels() {
		return models;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		ManagedForm managedForm = new ManagedForm(container);
		ScrolledForm form = managedForm.getForm();
		form.setText("Process Models");
		form.setImage(ConfigResourceRegistry
				.getImage(ConfigResourceRegistry.IMAGE_GENERAL_INFO));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.setLayout(new GridLayout());
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		modelsComposite = new ModelsComposite(managedForm);
		modelsTable = new TableViewer(modelsComposite.getTable());
		

//		final TableColumn nameColumn = new TableColumn(table, SWT.NONE);
//		nameColumn.setWidth(160);
//		nameColumn.setText("Name");
//
//		final TableColumn modelColumn = new TableColumn(table, SWT.NONE);
//		modelColumn.setWidth(150);
//		modelColumn.setText("Type");
		
		
		
		
		
		addListeners();
		fillUi();
		return form;
	}

	private void addListeners() {
		modelsComposite.getEditButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						editModel();
					}
				});
		modelsComposite.getRemoveButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						removeModel();
					}
				});
		modelsComposite.getAddBPMNButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addModel(EditorRegistry.BPMN);
					}
				});
		modelsComposite.getAddDecSerFlowButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addModel(EditorRegistry.DECSERFLOW);
					}
				});
		modelsComposite.getUpButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveModel(-1);
					}
				});
		modelsComposite.getAddImageButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addImage();
					}
				});
		modelsComposite.getDownButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveModel(1);
					}
				});
		modelsTable.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				editModel();
			}
		});
	}

	private void fillUi() {
		TableViewerColumn nameCol = new TableViewerColumn(modelsTable, SWT.NONE);
		nameCol.getColumn().setWidth(200);
		nameCol.getColumn().setText("Name");
		nameCol.setEditingSupport(new MyEditingSupport(modelsTable));
		TableViewerColumn typeCol = new TableViewerColumn(modelsTable, SWT.NONE);
		typeCol.getColumn().setWidth(200);
		typeCol.getColumn().setText("Type");
		
		modelsTable.setContentProvider(new ModelsContentProvider());
		modelsTable.setLabelProvider(new ModelLabelProvider());
		modelsTable.setInput(models);
	}

	private void addModel(String type) {
		InitialGraphDialog dialog = new InitialGraphDialog(this.getShell(), new Graph(EditorRegistry.getDescriptors(type)), type);       
        dialog.setBlockOnOpen(true);
        int returnCode = dialog.open();
			
        if (returnCode == Window.OK) {
        	models.add(new ModelContainer(type, dialog.getGraph(), "model"));
        	modelsTable.refresh();
        }
	}
	
	private void removeModel() {
		IStructuredSelection selection = (IStructuredSelection) modelsTable.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(modelsComposite.getShell(), "No Selection",
					"Please select the model to remove.");
			return;
		}

		ModelContainer model = (ModelContainer) selection.getFirstElement();
		models.remove(model);
		modelsTable.refresh();
	}
	
	private void editModel() {
		IStructuredSelection selection = (IStructuredSelection) modelsTable.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(modelsComposite.getShell(), "No Selection",
					"Please select the model edit.");
			return;
		}
		
		ModelContainer model = (ModelContainer) selection.getFirstElement();
		
		if (model.getType().equals(EditorRegistry.BPMN) || model.getType().equals(EditorRegistry.DECSERFLOW)){
		
			Graph newGraph = ExperimentGraph.copyGraph((Graph)model.getGraph());		
			InitialGraphDialog dialog = new InitialGraphDialog(this.getShell(), newGraph, model.getType());       
	        dialog.setBlockOnOpen(true);
	        int returnCode = dialog.open();
				
	        if (returnCode == Window.OK) {
	        	model.setGraph(newGraph);
	        }	
		} else {
			byte[] image = getImage();
			if (image != null){
				model.setImage(image);
			}
		}
	}
	
	private void moveModel(int delta) {
		IStructuredSelection selection = (IStructuredSelection) modelsTable.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(modelsComposite.getShell(), "No Selection",
					"Please select the model to move ");
			return;
		}
		ModelContainer model = (ModelContainer) selection.getFirstElement();
		
		int newIndex = models.indexOf(model) + delta;
		if (newIndex < 0 || newIndex >= models.size()) {
			return;
		}

		models.remove(model);
		models.add(newIndex, model);
		modelsTable.refresh();
	}
	
	private void addImage(){
		byte[] image = getImage();
		if (image != null){
			models.add(new ModelContainer(image, "name"));
			modelsTable.refresh();
		}
	}

	private byte[] getImage(){
		FileDialog fileDialog = new FileDialog(this.getShell());
		fileDialog.setText("Select File");
		fileDialog.setFilterExtensions(new String[] { "*.png" });
		fileDialog.setFilterNames(new String[] { "PNG Files(*.png)" });
		String selected = fileDialog.open();
		if (selected != null){
			byte[] bytes = null;
			try {
				File file = new File(selected);
				FileInputStream in = new FileInputStream(selected);
				bytes = new byte[(int) file.length()];
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
					offset += numRead;
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}

			return bytes;
		}
		else
			return null;
	}
	
}
