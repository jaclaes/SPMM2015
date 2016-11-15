package converter;



import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

/**
 * converts a business process model in BPMN notation to a process graph
 */
public class ModelToGraphConverter {
	/**
	 * returns all nodes with specified id
	 * 
	 * @param nodes - list of nodes
	 * @param id - id
	 * @return
	 * 		list of nodes with specified id
	 */
	public static ArrayList<Node> getNodesById(List<Node> nodes, String id)
	{
		ArrayList<Node> nodesById = new ArrayList<Node>();
		
		for(Node n : nodes)	{
			if(n.getDescriptor().getId().equals(id))
				nodesById.add(n);
		}
		
		return nodesById;
	}
	
	/**
	 * remove all start events from business process model
	 * 
	 * @param model - business process model
	 * @return
	 * 		number of removed start events
	 */
	public static int removeStartEvents(Graph model)
	{
		int removed = 0;
		ArrayList<Node> startEvents = getNodesById(model.getNodes(), "BPMN.START_EVENT");
				
		for(Node n : startEvents)	{
			List<Edge> edges = n.getSourceConnections();
			if(n.getTargetConnections().size() > 0)	{
				for(Edge target : n.getTargetConnections())	{
					if(target.getSource() != null)	{
						for(Edge source : edges)	{
							Edge edge = new Edge(target.getParent(), target.getDescriptor());
							edge.setSource(target.getSource());
							edge.setTarget(source.getTarget());
							model.addEdge(edge);
						}
						model.removeEdge(target);
					}
				}
			}
			
			for(Edge e : edges)	{
				model.removeEdge(e);
			}
			model.removeNode(n);
			removed++;
		}
		
		return removed;
	}
	
	/**
	 * remove all end events from business process model
	 * 
	 * @param model - business process model
	 * @return
	 * 		number of removed end events
	 */
	public static int removeEndEvents(Graph model)
	{
		int removed = 0;
		ArrayList<Node> endEvents = getNodesById(model.getNodes(), "BPMN.END_EVENT");
		
		for(Node n : endEvents)	{
			List<Edge> edges = n.getTargetConnections();
			if(n.getSourceConnections().size() > 0)	{
				for(Edge source : n.getSourceConnections())	{
					if(source.getTarget() != null)	{
						for(Edge target : edges)	{
							Edge edge = new Edge(source.getParent(), source.getDescriptor());
							edge.setSource(target.getSource());
							edge.setTarget(source.getTarget());
							model.addEdge(edge);
						}
						model.removeEdge(source);
					}
				}
			}
			for(Edge e : edges)	{
				model.removeEdge(e);
			}
			model.removeNode(n);
			removed++;
		}
		return removed;
	}
	
	/**
	 * returns all nodes which has no incoming and outgoing edges
	 * 
	 * @param nodes - list of nodes
	 * @return
	 * 		nodes without incoming and outgoing edges
	 */
	public static ArrayList<Node> getConnectionlessNodes(List<Node> nodes)
	{
		ArrayList<Node> connectionless = new ArrayList<Node>();
		
		for(Node n : nodes)	{
			if((n.getSourceConnections().size() == 0) && (n.getTargetConnections().size() == 0))
				connectionless.add(n);
		}
		
		return connectionless;
	}
	
	/**
	 * removes all nodes which has no incoming and ougoing edges
	 * 
	 * @param model - business process model
	 * @return
	 * 		number of removed nodes
	 */
	public static int removeConnectionlessNodes(Graph model)
	{
		int removed = 0;
		ArrayList<Node> connectionless = getConnectionlessNodes(model.getNodes());
		for(Node n : connectionless)	{
			model.removeNode(n);
			removed++;
		}
		return removed;
	}
	
	/**
	 * returns all events and gateways of business process model
	 * 
	 * @param nodes - list of nodes
	 * @return
	 * 		list of events and gateways
	 */
	public static ArrayList<Node> getEventsAndGateways(List<Node> nodes)
	{
		ArrayList<Node> eventsAndGateways = new ArrayList<Node>(); 
		
		for(Node n : nodes)	{
			if(!n.getDescriptor().getId().equals("BPMN.ACTIVITY"))
				eventsAndGateways.add(n);
		}
		
		return eventsAndGateways;
	}
	
	/**
	 * remove all events or gateways with their edges which has no incoming edges 
	 * or no outgoing edges
	 * @param model - business process model
	 * @param nodes - list of nodes
	 */
	public static void removeFinals(Graph model, List<Node> nodes)
	{
		ArrayList<Node> finals = getEventsAndGateways(nodes);
		
		for(Node node : finals)	{
			if(node.getSourceConnections().size() == 0)	{
				List<Edge> edges = node.getTargetConnections();
				for(Edge e : edges)	{
					model.removeEdge(e);
				}
				model.removeNode(node);
			}else if(node.getTargetConnections().size() == 0)	{
				List<Edge> edges = node.getSourceConnections();
				for(Edge e : edges)	{
					model.removeEdge(e);
				}
				model.removeNode(node);
			}
		}
	}
	
	/**
	 * returns all edges which do not have an activity node as source or target
	 * 
	 * @param edges - list of edges
	 * @return
	 * 		all edges which do not have an activity node as source or target
	 */
	public static ArrayList<Edge> getEdgesToDelete(List<Edge> edges)
	{
		ArrayList<Edge> edgesToDelete = new ArrayList<Edge>();

		for(Edge e : edges)	{
			if(!e.getSource().getDescriptor().getId().equals("BPMN.ACTIVITY") || !e.getTarget().getDescriptor().getId().equals("BPMN.ACTIVITY"))
				edgesToDelete.add(e);
		}
				
		return edgesToDelete;
	}
	
	/**
	 * checks if business process model contains any events or gateways
	 * 
	 * @param model - business process model
	 * @return
	 * 		true, if model contains any events or gateways, false otherwise
	 */
	public static boolean containsEventsOrGateways(Graph model)	
	{
		for(Node node : model.getNodes())	{
			if(!node.getDescriptor().getId().equals("BPMN.ACTIVITY"))
				return true;
		}
		
		return false;
	}
	
	/**
	 * convert a business process model in BPMN notation to a process graph
	 * 
	 * @param model - business process model
	 * @return
	 * 		process graph
	 */
	public static Graph convert(Graph model)
	{
		ArrayList<Node> activities = getNodesById(model.getNodes(), "BPMN.ACTIVITY");
		if(activities.size() == 0)	{
			
			ArrayList<Edge> edges = new ArrayList<Edge>(model.getEdges());
			for(Edge e : edges)	{
				model.removeEdge(e);
			}
			
			ArrayList<Node> nodes = new ArrayList<Node>(model.getNodes());
			for(Node n : nodes)	{
				model.removeNode(n);
			}
				
			return model;
		}
			
		removeStartEvents(model);
		removeEndEvents(model);
		
		while(containsEventsOrGateways(model))	{
			removeFinals(model, model.getNodes());
			
			for(Node n : activities)	{
				ArrayList<Edge> sourcesToDelete = getEdgesToDelete(n.getSourceConnections());
				for(Edge e : sourcesToDelete)	{
					Node nodeToReplace = e.getTarget();
					List<Edge> sources = nodeToReplace.getSourceConnections();
					for(Edge ed : sources)	{
						Node target = ed.getTarget();
						Edge newEdge = new Edge(e.getParent(), e.getDescriptor());
						newEdge.setSource(n);
						newEdge.setTarget(target);
						model.addEdge(newEdge);
						model.removeEdge(e);
						model.removeEdge(ed);
					}
				}
				
				ArrayList<Edge> targetsToDelete = getEdgesToDelete(n.getTargetConnections());
				for(Edge e : targetsToDelete)	{
					Node nodeToReplace = e.getSource();
					List<Edge> targets = nodeToReplace.getTargetConnections();
					for(Edge ed : targets)	{
						Node source = ed.getSource();
						Edge newEdge = new Edge(e.getParent(), e.getDescriptor());
						newEdge.setSource(source);
						newEdge.setTarget(n);
						model.addEdge(newEdge);
						model.removeEdge(e);
						model.removeEdge(ed);
					}
				}
			}
			
			removeConnectionlessNodes(model);
		}
		return model;
	}
}
