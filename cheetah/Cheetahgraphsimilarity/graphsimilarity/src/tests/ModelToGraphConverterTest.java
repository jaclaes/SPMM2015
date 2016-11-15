package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.DBInput;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Before;
import org.junit.Test;

import converter.ModelToGraphConverter;

public class ModelToGraphConverterTest {
	DBInput input;
	HashMap<String, Graph> graphs;
	Set<String> keys;

	@Before
	public void setUp() throws Exception {
		input = new DBInput();
		graphs = input.getGraphs();
		keys = graphs.keySet();
	}

	@Test
	public void testGetNodesById() {
		Iterator<String> it = keys.iterator();
		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			ArrayList<Node> bpmn = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.ACTIVITY");
			for(Node n : bpmn)
				assertEquals(n.getDescriptor().getId(), "BPMN.ACTIVITY");
			ArrayList<Node> xor = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.XOR");
			for(Node n : xor)
				assertEquals(n.getDescriptor().getId(), "BPMN.XOR");
			ArrayList<Node> and = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.AND");
			for(Node n : and)
				assertEquals(n.getDescriptor().getId(), "BPMN.AND");
			ArrayList<Node> or = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.OR");
			for(Node n : or)
				assertEquals(n.getDescriptor().getId(), "BPMN.OR");
			ArrayList<Node> start = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.START_EVENT");
			for(Node n : start)
				assertEquals(n.getDescriptor().getId(), "BPMN.START_EVENT");
			ArrayList<Node> end = ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.END_EVENT");
			for(Node n : end)
				assertEquals(n.getDescriptor().getId(), "BPMN.END_EVENT");
			
			int totalNodes = bpmn.size()+xor.size()+and.size()+or.size()+start.size()+end.size();
			assertEquals(g.getNodes().size(), totalNodes);
		}
	}

	@Test
	public void testRemoveStartEvents() {
		Iterator<String> it = keys.iterator();
		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			int nodeSize = g.getNodes().size();

			int removed = ModelToGraphConverter.removeStartEvents(g);
			//node size of graph has equal to size of model minus start event nodes
			assertEquals(g.getNodes().size(), nodeSize-removed);
			//after removing start_events, no start event has to appear in graph
			for(Node n : g.getNodes())
				assertNotSame(n.getDescriptor().getId(), "BPMN.START_EVENT");
			
			for(Edge e : g.getEdges())	{
				assertNotNull(e.getSource().getDescriptor().getId());
				assertNotNull(e.getTarget().getDescriptor().getId());
			}
		}
	}

	@Test
	public void testRemoveEndEvents() {
		Iterator<String> it = keys.iterator();
		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			int nodeSize = g.getNodes().size();
			int removed = ModelToGraphConverter.removeEndEvents(g);
			//node size of graph has equal to size of model minus start event nodes
			assertEquals(g.getNodes().size(), nodeSize-removed);
			//after removing start_events, no start event has to appear in graph
			for(Node n : g.getNodes())
				assertNotSame(n.getDescriptor().getId(), "BPMN.END_EVENT");
			
			for(Edge e : g.getEdges())	{
				assertNotNull(e.getSource().getDescriptor().getId());
				assertNotNull(e.getTarget().getDescriptor().getId());
			}
		}
	}

	@Test
	public void testGetConnectionlessNodes() {
		Iterator<String> it = keys.iterator();
		
		while(it.hasNext())	{
			Graph g = graphs.get(it.next());

			for(Node n : g.getNodes())	{
				List<Edge> edges = n.getSourceConnections();
				edges.addAll(n.getTargetConnections());
				for(Edge e : edges)	{
					g.removeEdge(e);
				}
		
				ArrayList<Node> connectionLess = ModelToGraphConverter.getConnectionlessNodes(g.getNodes());
				
				//all connectionless nodes must not have any source or target edges
				for(Node node : connectionLess)	{
					assertEquals(node.getSourceConnections().size(), 0);
					assertEquals(node.getTargetConnections().size(), 0);
				}
			}
		}
	}

	@Test
	public void testGetEventsAndGateways() {
		Iterator<String> it = keys.iterator();

		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			ArrayList<Node> noActivities = ModelToGraphConverter.getEventsAndGateways(g.getNodes());
			
			for(Node n : noActivities)	{
				//must not contain any activity nodes
				assertNotSame(n.getDescriptor().getId(), "BPMN.ACTIVITY");
				//size of events/gateways+activities has to be the same as size of graph
				assertEquals(g.getNodes().size(), noActivities.size()+ModelToGraphConverter.getNodesById(g.getNodes(), "BPMN.ACTIVITY").size());
			}
		}
	}

	@Test
	public void testRemoveFinals() {
		Iterator<String> it = keys.iterator();
		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			List<Edge> edges = g.getEdges();
			for(Edge e : edges)	{
				assertNotNull(e.getTarget().getDescriptor().getId());
				assertNotNull(e.getSource().getDescriptor().getId());
			}
		}
	}

	@Test
	public void testContainsEventsOrGateways() {
		Iterator<String> it = keys.iterator();

		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			boolean bool = ModelToGraphConverter.containsEventsOrGateways(g);
			if(!bool)	{
				//if not containing any events or gateway all nodes have to be activities
				for(Node n : g.getNodes())
					assertEquals(n.getDescriptor().getId(), "BPMN.ACTIVITY");
			}
		}
	}

	@Test
	public void testConvert() {
		Iterator<String> it = keys.iterator();

		while(it.hasNext())	{
			Graph g = graphs.get(it.next());
			g = ModelToGraphConverter.convert(g);
			for(Node n : g.getNodes())	{
				//every node has to be an activity
				assertEquals(n.getDescriptor().getId(), "BPMN.ACTIVITY");
				//it must not contain any events or gateways
				assertFalse(ModelToGraphConverter.containsEventsOrGateways(g));
				//node must not be null
				assertNotNull(n.getDescriptor().getId());
			}
			for(Edge e : g.getEdges())	{
				//source and target must not be null
				assertNotNull(e.getSource());
				assertNotNull(e.getTarget());
				assertNotNull(e.getSource().getDescriptor().getId());
				assertNotNull(e.getTarget().getDescriptor().getId());
			}
		}
	}
	
	@Test
	public void test()	{
		Iterator<String> it = keys.iterator();

		while(it.hasNext())	{
			Graph model = graphs.get(it.next());
			
			ArrayList<Node> activities = ModelToGraphConverter.getNodesById(model.getNodes(), "BPMN.ACTIVITY");
			for(Node n : activities)	
				assertEquals(n.getDescriptor().getId(), "BPMN.ACTIVITY");
			
			if(activities.size() == 0)	{
				assertEquals(activities.size(), 0);
				
				ArrayList<Edge> edges = new ArrayList<Edge>(model.getEdges());
				for(Edge e : edges)	{
					model.removeEdge(e);
				}
				
				ArrayList<Node> nodes = new ArrayList<Node>(model.getNodes());
				for(Node n : nodes)	{
					model.removeNode(n);
				}
				//no activities -> delete all, size of edges and nodes has to be 0
				assertEquals(model.getNodes().size(), 0);
				assertEquals(model.getEdges().size(), 0);
			}
			
			ModelToGraphConverter.removeStartEvents(model);
			for(Node n : model.getNodes())	
				assertNotSame(n.getDescriptor().getId(), "BPMN.START_EVENT");
			
			for(Edge e : model.getEdges())	{
				assertNotNull(e.getSource().getDescriptor().getId());
				assertNotNull(e.getTarget().getDescriptor().getId());
			}
			
			ModelToGraphConverter.removeEndEvents(model);
			
			for(Edge e : model.getEdges())	{
				assertNotNull(e.getSource().getDescriptor().getId());
				assertNotNull(e.getTarget().getDescriptor().getId());
			}
			
			for(Node n : model.getNodes())	
				assertNotSame(n.getDescriptor().getId(), "BPMN.END_EVENT");
			
			while(ModelToGraphConverter.containsEventsOrGateways(model))	{
				for(Edge e : model.getEdges())	{
					assertNotNull(e.getSource().getDescriptor().getId());
					assertNotNull(e.getTarget().getDescriptor().getId());
				}
				ModelToGraphConverter.removeFinals(model, model.getNodes());
				//start and end nodes must not be an event or gateway
					
				for(Edge e : model.getEdges())	{
					assertNotNull(e.getSource().getDescriptor().getId());
					assertNotNull(e.getTarget().getDescriptor().getId());
				}
				
				for(Node n : activities)	{
					ArrayList<Edge> sourcesToDelete = ModelToGraphConverter.getEdgesToDelete(n.getSourceConnections());
					for(Edge e : sourcesToDelete)	{
						Node nodeToReplace = e.getTarget();
						assertNotSame(nodeToReplace.getDescriptor().getId(), "BPMN.ACTIVITY");
						List<Edge> sources = nodeToReplace.getSourceConnections();
						for(Edge ed : sources)	{
							Node target = ed.getTarget();
							Edge newEdge = new Edge(e.getParent(), e.getDescriptor());
							newEdge.setSource(n);
							newEdge.setTarget(target);
							model.addEdge(newEdge);
							model.removeEdge(e);
							model.removeEdge(ed);
							assertTrue(model.contains(newEdge));
							assertNotNull(newEdge.getSource().getDescriptor().getId());
							assertNotNull(newEdge.getTarget().getDescriptor().getId());
						}
					}
					
					for(Edge e : model.getEdges())	{
						assertNotNull(e.getSource().getDescriptor().getId());
						assertNotNull(e.getTarget().getDescriptor().getId());
					}
					
					ArrayList<Edge> targetsToDelete = ModelToGraphConverter.getEdgesToDelete(n.getTargetConnections());
					for(Edge e : targetsToDelete)	{
						Node nodeToReplace = e.getSource();
						assertNotSame(nodeToReplace.getDescriptor().getId(), "BPMN.ACTIVITY");
						List<Edge> targets = nodeToReplace.getTargetConnections();
						for(Edge ed : targets)	{
							Node source = ed.getSource();
							Edge newEdge = new Edge(e.getParent(), e.getDescriptor());
							newEdge.setSource(source);
							newEdge.setTarget(n);
							model.addEdge(newEdge);
							model.removeEdge(e);
							model.removeEdge(ed);
							assertTrue(model.contains(newEdge));
							assertNotNull(newEdge.getSource().getDescriptor().getId());
							assertNotNull(newEdge.getTarget().getDescriptor().getId());
						}
					}
					
					for(Edge e : model.getEdges())	{
						assertNotNull(e.getSource().getDescriptor().getId());
						assertNotNull(e.getTarget().getDescriptor().getId());
					}
				}
				
				ModelToGraphConverter.removeConnectionlessNodes(model);
			}			
		}
	}
}
