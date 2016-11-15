package metrics;

import mappings.Mapping;

import org.cheetahplatform.modeler.graph.model.Node;


public class SimilarityMetrics {
	/**
	 * returns string edit distance between two labels
	 * 
	 * @param name - first label
	 * @param name2 - second label
	 * @return
	 * 		string edit distance
	 */
	public static double computeStringEditDistance(String name, String name2) {
		return StringEditMetrics.computeStringEditDistance(name, name2);
	}
	
	/**
	 * returns node similarity between two nodes
	 * 
	 * @param node1 - Node
	 * @param node2 - Node
	 * @return
	 * 		node similarity
	 */
	public static double computeNodeSimilarity(Node node1, Node node2) {
		return StringEditMetrics.computeStringEditSimilarity(node1, node2);
	}

	/**
	 * returns graph edit distance induced by some mapping
	 * 
	 * @param mapping - Mapping
	 * @return
	 * 		graph edit distance
	 */
	public static double computeGraphEditDistance(Mapping mapping)
	{
		return GraphEditMetrics.computeGraphEditDistance(mapping);
	}
	
	/**
	 * returns graph edit similarity induced by some mapping
	 * 
	 * @param mapping - Mapping
	 * @param wsubn - weight for substituted nodes
	 * @param wskipn - weight for inserted/deleted nodes
	 * @param wskipe - weight for inserted/deleted edges
	 * @return
	 * 		graph edit similarity
	 */
	public static double computeGraphEditSimilarity(Mapping mapping, double wsubn, double wskipn, double wskipe)
	{
		return GraphEditMetrics.computeGraphEditSimilarity(mapping, wsubn, wskipn, wskipe);
	}
}
