package algorithm;


import java.util.ArrayList;

import mappings.Mapping;
import mappings.NodePair;
import metrics.SimilarityMetrics;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;


public class GreedyAlgorithm {
	//private static Logger logger = Logger.getLogger(GreedyAlgorithm.class);
	
	private static final double WSUBN = 0.9; //weight for substituted nodes
	private static final double WSKIPN = 0.1; //weight for inserted/deleted nodes
	private static final double WSKIPE = 0.4; //weight for inserted/deleted edges
	
	private Graph sourceGraph;
	private Graph targetGraph;
	
	private Mapping mapping;
	private ArrayList<NodePair> openPairs;	//list with possible node pairs
	private double time;
	
	
	public GreedyAlgorithm(Graph sourceGraph, Graph targetGraph)
	{
		this.sourceGraph = sourceGraph;
		this.targetGraph = targetGraph;
	
		mapping = new Mapping(sourceGraph, targetGraph);
	}

	
	
	/**
	 * the algorithm starts by marking all possible pairs of nodes from 
	 * the two graphs as open pairs
	 */
	public void init()
	{
		openPairs = new ArrayList<NodePair>();
		
		for(Node n1 : sourceGraph.getNodes())	{
			for(Node n2 : targetGraph.getNodes())	{
				openPairs.add(new NodePair(n1, n2));
			}
		}
	}
	
	/**
	 * computes mapping between a pair of process graphs
	 * 
	 * @return
	 * 		graph edit similarity
	 */
	public double computeMapping()
	{
		long start = System.currentTimeMillis();
		double similarity = 0.0;
		init();

		//while there exists a mapping in open pairs which increases the similarity
		while(openPairs.size() != 0)	{
			Mapping best = null;
			int index = 0;
			for(int i=0; i<openPairs.size(); i++)	{
				Mapping temp =  new Mapping(mapping);
				temp.addNodePairToMapping(openPairs.get(i));
				
				double tmp = s(temp);
				if(tmp > similarity)	{
					best = new Mapping(temp);
					similarity = tmp;
					index = i;
				}
			}
			
			//if there exists no mapping that increases the similarity -> finish
			if(best == null) 	
				break;

			//add open pair that increases similarity most to mapping and remove from open pairs
			mapping = new Mapping(best);
			removeMappedNodes(openPairs.get(index));
		}
		time = System.currentTimeMillis() - start;
		return similarity;
	}
	
	/**
	 * remove all node pairs from list openPairs which contain one of the mapped nodes
	 * 
	 * @param map - mapped node pair
	 */
	public void removeMappedNodes(NodePair map)
	{
		ArrayList<NodePair> pairsToKeep = new ArrayList<NodePair>();
		for(NodePair m : openPairs)	{
			if(!m.getNode1().equals(map.getNode1()) && !m.getNode2().equals(map.getNode2()))	{
				pairsToKeep.add(m);
			}
		}
		
		openPairs = pairsToKeep;
	}

	/**
	 * returns similarity induced by a mapping
	 * 
	 * @param map - mapping
	 * @return
	 * 		graph edit similarity
	 */
	public double s(Mapping mapping)
	{
		return SimilarityMetrics.computeGraphEditSimilarity(mapping, WSUBN, WSKIPN, WSKIPE);
	}

	public Graph getSourceGraph() {
		return sourceGraph;
	}

	public void setSourceGraph(Graph sourceGraph) {
		this.sourceGraph = sourceGraph;
	}

	public Graph getTargetGraph() {
		return targetGraph;
	}

	public void setTargetGraph(Graph targetGraph) {
		this.targetGraph = targetGraph;
	}
	
	public ArrayList<NodePair> getOpenPairs() {
		return openPairs;
	}

	public void setOpenPairs(ArrayList<NodePair> openPairs) {
		this.openPairs = openPairs;
	}

	public static double getWsubn() {
		return WSUBN;
	}

	public static double getWskipn() {
		return WSKIPN;
	}

	public static double getWskipe() {
		return WSKIPE;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
}
