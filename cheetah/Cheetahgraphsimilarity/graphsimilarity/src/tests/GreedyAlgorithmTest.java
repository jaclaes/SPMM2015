package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import mappings.Mapping;
import models.DBInput;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Before;
import org.junit.Test;

import algorithm.GreedyAlgorithm;

import converter.ModelToGraphConverter;

public class GreedyAlgorithmTest {
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
	public void testComputeMapping() {
		Iterator<String> it = keys.iterator();
		Graph s = graphs.get("Graph ID 478");
		s = ModelToGraphConverter.convert(s);
		
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			GreedyAlgorithm greedy = new GreedyAlgorithm(s, t);
			double sim = greedy.computeMapping();

			assertTrue(sim >= 0.0);
			assertTrue(sim <= 1.0);
			
			Mapping m = greedy.getMapping();
			for(Node n : m.getSubn())
				assertFalse(m.getSkipn().contains(n));	//nodes in subn must not appear in skipn
			
			for(Node n : m.getSkipn())
				assertFalse(m.getSubn().contains(n)); //nodes in skipn must not appear in subn
		
		}
	}
}
