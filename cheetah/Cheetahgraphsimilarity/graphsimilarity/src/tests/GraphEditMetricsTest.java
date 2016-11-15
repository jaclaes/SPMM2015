package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import mappings.Mapping;
import metrics.GraphEditMetrics;
import models.DBInput;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import algorithm.GreedyAlgorithm;

import converter.ModelToGraphConverter;

public class GraphEditMetricsTest {
	DBInput input;
	HashMap<String, Graph> graphs;
	Set<String> keys;
	String[] ids;
	
	@Before
	public void setUp() throws Exception {
		input = new DBInput();
		graphs = input.getGraphs();
		keys = graphs.keySet();
		ids = keys.toArray(new String[]{});
	}
	
	@Test
	public void testComputeGraphEditDistance() {
		Iterator<String> it = keys.iterator();
		String source = it.next();
		Graph s = graphs.get(source);
		s = ModelToGraphConverter.convert(s);
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			
			Mapping m = new Mapping(s, t);
			double dist = GraphEditMetrics.computeGraphEditDistance(m);
			//sting edit distance of an empty mapping has to be the sum of nodes and edges of both graphs
			assertTrue(dist == s.getNodes().size()+t.getNodes().size()+s.getEdges().size()+t.getEdges().size());
		}
	}

	@Test
	public void testComputeGraphEditSimilarity() {
		Iterator<String> it = keys.iterator();
		String source = it.next();
		Graph s = graphs.get(source);
		s = ModelToGraphConverter.convert(s);
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			
			Mapping m = new Mapping(s, t);
			double dist = GraphEditMetrics.computeGraphEditSimilarity(m, 0.1, 0.3, 0.7);
			//sting edit similarity of an empty mapping has to be 0.0
			assertTrue(dist == 0.0);
		}
	}

	@Test
	public void testComputeFskipn() {
		Iterator<String> it = keys.iterator();
		String source = it.next();
		Graph s = graphs.get(source);
		s = ModelToGraphConverter.convert(s);
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			
			GreedyAlgorithm greedy = new GreedyAlgorithm(s, t);
			greedy.computeMapping();
			Mapping m = greedy.getMapping();
			
			double fskipn = GraphEditMetrics.computeFskipn(m);
			//fskipn has to be a number between 0.0 and 1.0
			assertTrue(fskipn >= 0.0);
			assertTrue(fskipn <= 1.0);
		}
	}

	@Test
	public void testComputeFskipe() {
		Iterator<String> it = keys.iterator();
		String source = it.next();
		Graph s = graphs.get(source);
		s = ModelToGraphConverter.convert(s);
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			
			GreedyAlgorithm greedy = new GreedyAlgorithm(s, t);
			greedy.computeMapping();
			Mapping m = greedy.getMapping();
			
			double fskipe = GraphEditMetrics.computeFskipe(m);
			//fskipn has to be a number between 0.0 and 1.0
			assertTrue(fskipe >= 0.0);
			assertTrue(fskipe <= 1.0);
		}
	}

	@Test
	public void testComputeFsubn() {
		Iterator<String> it = keys.iterator();
		String source = it.next();
		Graph s = graphs.get(source);
		s = ModelToGraphConverter.convert(s);
		while(it.hasNext())	{
			String id = it.next();
			Graph t = graphs.get(id);
			t = ModelToGraphConverter.convert(t);
			
			GreedyAlgorithm greedy = new GreedyAlgorithm(s, t);
			greedy.computeMapping();
			Mapping m = greedy.getMapping();
			
			double fsubn = GraphEditMetrics.computeFsubn(m);
			//fskipn has to be a number between 0.0 and 1.0
			assertTrue(fsubn >= 0.0);
			assertTrue(fsubn <= 1.0);
		}
	}
}
