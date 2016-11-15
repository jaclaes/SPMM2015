package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import models.DBInput;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	DBInput input;
	@Before
	public void setUp() throws Exception {
		input = new DBInput();
	}
	
	@Test
	public void testGetGraphs() {
		HashMap<String, Graph> graphs = input.getGraphs();
		assertNotNull(graphs);
		
				
		Set<String> keys = graphs.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext())	{
			String id = (String) it.next();
			//System.out.println(id);
			Graph g = graphs.get(id);
			for(Node n : g.getNodes())	{
				assertNotNull("node is null", n);
				assertNotNull("descriptor is null", n.getDescriptor());
			}
			for(Edge e : g.getEdges())	{
				assertNotNull("edge is null", e);
				assertNotNull("source node of edge is null", e.getSource());
				assertNotNull("target node of edge is null", e.getTarget());
			}
			for(Node n : g.getNodes())	{
				assertNotNull("node has no ID", n.getDescriptor().getId());
			}
			for(int i=0; i<g.getEdges().size()-1; i++)	{
				for(int j=0; j<g.getEdges().size(); j++)	{
					//assertNotSame(g.getEdges().get(i), g.getEdges().get(j));
				}
			}
		}
	}
}
