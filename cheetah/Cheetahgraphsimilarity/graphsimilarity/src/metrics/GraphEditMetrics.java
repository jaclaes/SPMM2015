package metrics;


import mappings.Mapping;
import mappings.NodePair;

public class GraphEditMetrics {
	/**
	 * returns graph edit distance induced by some mapping
	 *  
	 * @param mapping - Mapping
	 * @return
	 * 		graph edit distance
	 */
	public static double computeGraphEditDistance(Mapping mapping)
	{
		double sum = 0.0;
		
		for(NodePair map : mapping.getMappedNodes())	{
			if(!map.getNode2().equals(mapping.getDummy()))	
				sum += (1.0 - SimilarityMetrics.computeNodeSimilarity(map.getNode1(), map.getNode2()));
		}
		
		return (mapping.getSkipn().size() + mapping.getSkipe().size() + 2*sum);
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
		double fsubn = computeFsubn(mapping);
		double fskipn = computeFskipn(mapping);
		double fskipe = computeFskipe(mapping);
		
		double total = wskipn + wskipe + wsubn;
		return (1.0 -((wskipn*fskipn + wskipe*fskipe + wsubn*fsubn)/total));
	}
	
	/**
	 * returns fraction of inserted or deleted nodes induced by some mapping
	 * 
	 * @param mapping - Mapping
	 * @return
	 * 		fraction of inserted/deleted nodes
	 */
	public static double computeFskipn(Mapping mapping)
	{
		double skipnSize = (double)(mapping.getSkipn().size());
		double n1 = (double)(mapping.getSourceGraph().getNodes().size());
		double n2 = (double)(mapping.getTargetGraph().getNodes().size());
		
		return skipnSize / (n1 + n2);
	}
	
	/**
	 * returns fraction of inserted or deleted edges induced by some mapping
	 * 
	 * @param mapping - Mapping
	 * @return
	 * 		fraction of inserted/deleted edges
	 */
	public static double computeFskipe(Mapping mapping)
	{
		double skipeSize = (double)(mapping.getSkipe().size());
		double e1 = (double)(mapping.getSourceGraph().getEdges().size());
		double e2 = (double)(mapping.getTargetGraph().getEdges().size());
		
		if((e1 == 0) || (e2 == 0))
			return 1.0;
			
		return skipeSize / (e1 + e2);
	}
	
	/**
	 * returns average distance of substituted nodes induced by some mapping
	 * 
	 * @param mapping - Mapping
	 * @return
	 * 		average distance of substituted nodes
	 */
	public static double computeFsubn(Mapping mapping)
	{
		double sum = 0.0;
		double subnSize = (double)(mapping.getSubn().size());
		if(subnSize == 0)
			return 1.0;

		for(NodePair pair : mapping.getMappedNodes())	{
			if(!pair.getNode2().equals(mapping.getDummy()))	{
				double sim = SimilarityMetrics.computeNodeSimilarity(pair.getNode1(), pair.getNode2());
				sum += (1.0 - sim);
			}
		}

		return (2.0*sum)/subnSize;
	}
}
