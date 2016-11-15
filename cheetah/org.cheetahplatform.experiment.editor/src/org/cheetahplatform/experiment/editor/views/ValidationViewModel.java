package org.cheetahplatform.experiment.editor.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ValidationViewModel {
	
	private Map<ExperimentGraph, List<Message>> msgMap;
	private static ValidationViewModel instance ;
	private List<IPropertyChangeListener> listeners;
 	
	public static ValidationViewModel getInstance(){
		if (instance == null){
			instance = new ValidationViewModel();
		}
		return instance;
	}
		
	private ValidationViewModel(){
		msgMap = new HashMap<ExperimentGraph, List<Message>>();
		listeners = new ArrayList<IPropertyChangeListener>();
	}
	
	
	public void update(ExperimentGraph graph, List<Message> msgs){
		msgMap.put(graph, msgs);
		firePropertyChanged();
	}
	
	protected void firePropertyChanged(){
		for (IPropertyChangeListener listener : listeners){
			listener.propertyChange(new PropertyChangeEvent(this, "messages", null, null));
		}
	}
	
	public Iterable<ExperimentGraph> getModels(){
		return msgMap.keySet();
	}
	
	public List<Message>getMessages(ExperimentGraph model){
		return msgMap.get(model);
	}
	
	public void addListener(IPropertyChangeListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(IPropertyChangeListener listener){
		listeners.remove(listener);
	}
	
	public void remove(ExperimentGraph graph){
		msgMap.remove(graph);
		firePropertyChanged();
	}
	
}
