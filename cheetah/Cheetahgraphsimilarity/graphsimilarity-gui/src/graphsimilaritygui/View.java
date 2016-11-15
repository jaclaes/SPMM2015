package graphsimilaritygui;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import mappings.Mapping;
import metrics.SimilarityMetrics;
import models.DBInput;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;

import converter.ModelToGraphConverter;

import algorithm.AStarAlgorithm;
import algorithm.GreedyAlgorithm;


public class View extends ViewPart {
	public static final String ID = "graphsimilarity-gui.view";

	private ComboViewer combo;
	private String comboSel;
	private ArrayList<String> selectedModels;
	private ListViewer list;
	private boolean greedySelected;
	private boolean editDistanceSelected;
	private boolean subnSelected;
	private boolean skipnSelected;
	private boolean skipeSelected;
	private boolean timeSelected;
	private Button greedy;
	private Button astar;
	private Button editDistance;
	private Button subn;
	private Button skipn;
	private Button skipe;
	private Button compute;
	private Button duplicate;
	private Button time;
	private Table table;
	private Set<String> keys;
	private HashMap<String, Graph> graphs;
	private ArrayList<Items> items;
	
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 30;
		layout.verticalSpacing = 20;
		parent.setLayout(layout);
				
		Group search = new Group(parent, SWT.SHADOW_ETCHED_IN);
		search.setText("reference model");
		GridLayout referenceLayout = new GridLayout();
		referenceLayout.numColumns = 2;
		search.setLayout(referenceLayout);
		GridData gridData = new GridData(220, 80);
		search.setLayoutData(gridData);
		
		Label label = new Label(search, SWT.NONE);
		label.setText("select search model:");
		combo = new ComboViewer(search, SWT.READ_ONLY);
		combo.setContentProvider(new ArrayContentProvider());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element)
			{
				if (element instanceof String) {
					String id = (String) element;
					return id;
				}
				return null;
			}
		});
		DBInput inputData = null;
		
		try {
			inputData = new DBInput();
			graphs = inputData.getGraphs();
			keys = graphs.keySet();
			combo.setInput(keys);
			String firstKey = keys.iterator().next();
			combo.setSelection(new StructuredSelection(firstKey));
			comboSel = firstKey;
		} catch (SQLException e) {
			setStatusLine("unable to load objects from DB");
			StatusManager.getManager().handle(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "DBError - can not load objects from database", e), StatusManager.SHOW);
			e.printStackTrace();
		}
		
		combo.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				 comboSel = ((String)sel.getFirstElement()).toString();
			}	
		});
		search.pack();
		
		Group models = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		models.setText("models");
		GridLayout modelsLayout = new GridLayout();
		modelsLayout.numColumns = 2;
		models.setLayout(modelsLayout);
		GridData gData = new GridData(250, 200);
		gData.verticalSpan = 2;
		models.setLayoutData(gData);

		Label label2 = new Label(models, SWT.NONE);
		label2.setText("select models:");
		list = new ListViewer(models);
		if(keys != null)	
			list.add(keys.toArray(new String[]{}));
		gridData = new GridData(145, 170);
		gridData.verticalSpan = 2;
		list.getControl().setLayoutData(gridData);
		list.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				selectedModels = new ArrayList<String>();
				for(Iterator<?> iterator = sel.iterator(); iterator.hasNext(); ) {
			          selectedModels.add((String) iterator.next());
			    }
			}
			
		});
		models.pack();
		
		Group options = new Group(parent, SWT.SHADOW_ETCHED_IN);
		options.setText("Options");
		GridLayout optionsLayout = new GridLayout();
		optionsLayout.numColumns = 1;
		options.setLayout(optionsLayout);
		gridData = new GridData(220, 200);
		gridData.verticalSpan = 2;
		options.setLayoutData(gridData);
		editDistance = new Button(options, SWT.CHECK);
		editDistance.setText("display Graph Edit Distance");
		subn = new Button(options, SWT.CHECK);
		subn.setText("display number of substituted nodes");
		skipn = new Button(options, SWT.CHECK);
		skipn.setText("display number of skiped nodes");
		skipe = new Button(options, SWT.CHECK);
		skipe.setText("display number of skiped edges");
		time = new Button(options, SWT.CHECK);
		time.setText("display computation time");
		options.pack();
		
		Group algorithm = new Group(parent, SWT.SHADOW_ETCHED_IN);
		algorithm.setText("Algorithm");
		GridLayout algorithmLayout = new GridLayout();
		algorithmLayout.numColumns = 1;
		algorithmLayout.makeColumnsEqualWidth = true;
		algorithm.setLayout(algorithmLayout);
		gridData = new GridData(220, 80);
		algorithm.setLayoutData(gridData);
		greedy = new Button(algorithm, SWT.RADIO);
		greedy.setText("Greedy Algorithm");
		greedy.setSelection(true);
		astar = new Button(algorithm, SWT.RADIO);
		astar.setText("A-Star Algorithm");
		algorithm.pack();
		
		Group buttons = new Group(parent, SWT.NONE);
		GridLayout buttonsLayout = new GridLayout();
		buttonsLayout.numColumns = 2;
		buttonsLayout.makeColumnsEqualWidth = true;
		buttons.setLayout(buttonsLayout);
		gridData = new GridData(102, 30);
		compute = new Button(buttons, SWT.PUSH);
		compute.setText("compute similarity");
		compute.setLayoutData(gridData);
		compute.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				if((selectedModels == null) || (selectedModels.size() == 0))	{
					ErrorDialog.openError(getViewSite().getShell(), "ERROR", "error", new Status(IStatus.ERROR, Activator.PLUGIN_ID, "no models selected"));
					setStatusLine("no models for search selected!");
				}else	{
					compute(false);	
				}
			}	
		});
		
		duplicate = new Button(buttons, SWT.PUSH);
		duplicate.setText("find duplicates");
		gridData = new GridData(102, 30);
		duplicate.setLayoutData(gridData);
		duplicate.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				compute(true);
			}
		});
		

		Group result = new Group(parent, SWT.SHADOW_ETCHED_IN);
		result.setText("Result");
		GridLayout resultLayout = new GridLayout();
		resultLayout.numColumns = 1;
		result.setLayout(resultLayout);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		result.setLayoutData(gridData);
		table = new Table(result, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.horizontalSpan = 2;
		data.heightHint = 120;
		table.setLayoutData(data);

		String[] titles = { "Search Model", "Model", "Graph Edit Similarity", "Graph Edit Distance", "Substituted Nodes", "Skiped Nodes", "Skiped Edges", "Computation Time" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			table.getColumn(i).pack();
		}
					
		result.pack();
	}
	
	private void setStatusLine(String message) {
		// Get the status line and set the text
		IActionBars bars = getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
		bars.updateActionBars();
	}
	
	private void compute(boolean duplicates)	{
		clearTable();		
		setSelections();
		enableButtons(false);
		
		double time = 0.0;
		Graph sourceGraph = ModelToGraphConverter.convert(graphs.get(comboSel));
		items = new ArrayList<Items>();
		
		if(greedySelected)	{
			for(String id : selectedModels)	{
				try	{
					GreedyAlgorithm greedy = new GreedyAlgorithm(sourceGraph, ModelToGraphConverter.convert(graphs.get(id)));
					double sim = greedy.computeMapping();
						
					packResult(duplicates, id, sim, greedy.getMapping(), greedy.getTime());
					
					time += greedy.getTime();
				}catch(Exception e)	{
					handleError(duplicates, id);
				}
			}
		}else	{
			for(String id : selectedModels)	{
				try	{
					AStarAlgorithm astar = new AStarAlgorithm(sourceGraph, ModelToGraphConverter.convert(graphs.get(id)));
					double sim = astar.computeMapping();
					
					packResult(duplicates, id, sim, astar.getMapping(), astar.getTime());
					
					time += astar.getTime();
				}catch(Exception e)	{
					handleError(duplicates, id);
				}
			}
		}

		sortItems();
		addItems();
		enableButtons(true);
		
		if(duplicates)	{
			setStatusLine(items.size() + " duplicate(s) found (computation finished in total time: " + (time/1000) + " sec)");
		}else	{
			setStatusLine("computation finished in total time: " + (time/1000) + " sec");
		}
	}
	
	private void handleError(boolean duplicates, String id)
	{
		if(!duplicates)
			items.add(new Items(comboSel, id, -1.0, null, 0.0));
	}
	
	private void packResult(boolean duplicates, String id, double sim, Mapping mapping, double time)
	{
		if(duplicates)	{
			if((sim == 1.0) && (!comboSel.equals(id)))	{
				items.add(new Items(comboSel, id, sim, mapping, time));
			}
		}else	{
			items.add(new Items(comboSel, id, sim, mapping, time));
		}
	}
	
	private void enableButtons(boolean enable)
	{
		if(enable)	{
			compute.setEnabled(true);
			duplicate.setEnabled(true);
		}else	{
			compute.setEnabled(false);
			duplicate.setEnabled(false);
		}
	}
	
	private void clearTable()
	{
		table.removeAll();
		table.update();
	}
	
	private void setSelections()
	{
		if((selectedModels == null) || (selectedModels.size() == 0))	{
			selectedModels = new ArrayList<String>();
			Iterator<String> it = keys.iterator();
			while(it.hasNext())	{
				String id = it.next();
				selectedModels.add(id);
			}
			list.setSelection(new StructuredSelection(keys.toArray(new String[]{})), true);
		}
		
		greedySelected = greedy.getSelection();
		editDistanceSelected = editDistance.getSelection();
		subnSelected = subn.getSelection();
		skipnSelected = skipn.getSelection();
		skipeSelected = skipe.getSelection();
		timeSelected = time.getSelection();
	}
		
	public void sortItems()
	{
		for(int x=0; x<items.size(); x++)	{
			Items k = items.get(x);
			int j = x;
			while(j!=0 && items.get(j-1).getSimilarity() < k.getSimilarity())	{
				items.set(j, items.get(j-1)); 
				j--;
			}
			items.set(j,k);
		}
	}
	
	public void addItems()
	{
		for(int x=0; x<items.size(); x++)	{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, comboSel);
			item.setText(1, items.get(x).getModel());
			item.setText(2, String.valueOf(items.get(x).getSimilarity()));
			
			if(items.get(x).getMapping() != null)	{
				if(editDistanceSelected)	{
					double editDist = SimilarityMetrics.computeGraphEditDistance(items.get(x).getMapping());
					item.setText(3, String.valueOf(editDist));
				}
				if(subnSelected)	{
					item.setText(4, String.valueOf(items.get(x).getMapping().getSubn().size()));
				}
				if(skipnSelected)	{
					item.setText(5, String.valueOf(items.get(x).getMapping().getSkipn().size())+"/"+String.valueOf(items.get(x).getMapping().getSourceGraph().getNodes().size()+items.get(x).getMapping().getTargetGraph().getNodes().size()));
				}
				if(skipeSelected)	{
					item.setText(6, String.valueOf(items.get(x).getMapping().getSkipe().size())+"/"+String.valueOf(items.get(x).getMapping().getSourceGraph().getEdges().size()+items.get(x).getMapping().getTargetGraph().getEdges().size()));
				}
				if(timeSelected)	{
					item.setText(7, String.valueOf(items.get(x).getTime()/1000 + " sec"));
				}
			}else	{
				item.setText(3, "ERROR");
			}			
		}
			
		TableColumn[] columns = table.getColumns();
		for(int x=0; x<columns.length; x++) {
	        columns[x].pack();
		}
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}