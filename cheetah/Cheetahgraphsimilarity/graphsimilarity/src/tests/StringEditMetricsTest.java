package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Set;

import metrics.StringEditMetrics;
import models.DBInput;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Before;
import org.junit.Test;

import converter.ModelToGraphConverter;

public class StringEditMetricsTest {
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
	public void testComputeStringEditDistance() {
		for(int i=0; i<ids.length; i++)	{
			Graph s = graphs.get(ids[i]);
			s = ModelToGraphConverter.convert(s);
			for(int j=ids.length-1; j>=0; j--)	{
				Graph t = graphs.get(ids[j]);
				t = ModelToGraphConverter.convert(t);
				for(Node n1 : s.getNodes())	{
					double equal = StringEditMetrics.computeStringEditDistance(n1.getName(), n1.getName());
					assertTrue(equal == 0.0);
					for(Node n2 : t.getNodes())	{
						double dist = StringEditMetrics.computeStringEditDistance(n1.getName(), n2.getName());
						//string edit distance has to be a number between 0.0 and max(|n1|, |n2|)
						assertTrue(dist >= 0.0);
						assertTrue(dist <= Math.max(n1.getName().length(), n2.getName().length()));
						double reverse = StringEditMetrics.computeStringEditDistance(n2.getName(), n1.getName());
						//there should no difference if strings are swapped
						assertTrue(dist == reverse);
						
						//string edit distance has to be the length of first string
						double diff = StringEditMetrics.computeStringEditDistance(n2.getName(), "");
						assertTrue(diff == n2.getName().length());
					}
				}
			}
		}
	}

	@Test
	public void testComputeStringEditSimilarity() {
		for(int i=0; i<ids.length; i++)	{
			Graph s = graphs.get(ids[i]);
			s = ModelToGraphConverter.convert(s);
			for(int j=ids.length-1; j>=0; j--)	{
				Graph t = graphs.get(ids[j]);
				t = ModelToGraphConverter.convert(t);
				for(Node n1 : s.getNodes())	{
					double equal = StringEditMetrics.computeStringEditSimilarity(n1, n1);
					//node similarity for same strings has to bee 1.0
					assertTrue(equal == 1.0);
					for(Node n2 : t.getNodes())	{
						double distance = StringEditMetrics.computeStringEditDistance(n1.getName(), n2.getName());
						double sim = StringEditMetrics.computeStringEditSimilarity(n1, n2);
						double reverse = StringEditMetrics.computeStringEditSimilarity(n2, n1);
						
						//there should be no difference if parameters are swapped
						assertTrue(sim == reverse);
						//node similarity has to be a number between 0.0 and 1.0
						assertTrue(sim >= 0.0);
						assertTrue(sim <= 1.0);
						
						//if string edit distance is 0.0 -> node similarity has to be 1.0
						if(distance == 0.0)
							assertTrue(sim == 1.0);
						
						//if string edit distance is maximal -> node similarity has to be 0.0
						if((distance == Math.max(n1.getName().length(), n2.getName().length())) && (Math.max(n1.getName().length(), n2.getName().length())>0))
							assertTrue(sim == 0.0);
					}
				}
			}
		}
	}
}
