package algorithm;

import java.util.ArrayList;
import java.util.List;

import mappings.Mapping;
import mappings.NodePair;
import metrics.SimilarityMetrics;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;


public class AStarAlgorithm {
	//private static Logger logger = Logger.getLogger(AStarAlgorithm.class);
	
	private static final double WSUBN = 0.1; //weight for substituted nodes
	private static final double WSKIPN = 0.2; //weight for inserted/deleted nodes
	private static final double WSKIPE = 0.7; //weight for inserted/deleted edges
	private static final double LEDCUTOFF = 0.5; //label edit cut-off
	
	private Graph sourceGraph;
	private Graph targetGraph;
	private ArrayList<Mapping> open;	//list with partial mappings
	private Node dummy;		//dummy node representing the case that node has been deleted
	
	private Mapping mapping;
	private double time;
	
	
	public AStarAlgorithm(Graph sourceGraph, Graph targetGraph)
	{
		this.sourceGraph = sourceGraph;
		this.targetGraph = targetGraph;
		open = new ArrayList<Mapping>();
		//dummy node
		dummy = new Node(targetGraph, (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY));
		dummy.setName("");
	}
	
	
	
	/**
	 * create mappings for some node in source graph with all nodes in target graph
	 * where node similarity bigger than label edit cut-off
	 */
	public void init()
	{
		if(sourceGraph.getNodes().size() == 0)
			return;
		
		Node n1 = sourceGraph.getNodes().get(0);
		
			//create mapping for some n1 with all n2 where Sim(n1,n2) > label edit cut-off
			for(Node n2 : targetGraph.getNodes())	{
				if(SimilarityMetrics.computeNodeSimilarity(n1, n2) > LEDCUTOFF)	{
					Mapping map = new Mapping(sourceGraph, targetGraph, dummy);
					NodePair pair = new NodePair(n1, n2);
					map.addNodePairToMapping(pair);
					open.add(map);
				}
			}
			//in addition create mapping (n1, dummy) which represents the case where node n1 has been deleted
			Mapping map = new Mapping(sourceGraph, targetGraph, dummy);
			NodePair pair = new NodePair(n1, dummy);
			map.addNodePairToMapping(pair);
			open.add(map);	
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
		
		init();
		//start with an empty mapping
		Mapping max = new Mapping(sourceGraph, targetGraph, dummy);

		//while there exist partial mappings to check
		while(open.size() != 0)	{
			max = getMaxMapping();		//get mapping with maximal graph edit similarity
			removeFromOpenList(max);	//and remove from list
				
			if(isDomOf(max, sourceGraph.getNodes()))	{	//if all nodes are mapped
				mapping = max;
				time = System.currentTimeMillis() - start;
				return s(max);
			}else	{		//create new mappings with next unmapped node
				createNewMappings(max);
			}
		}
		mapping = max;
		time = System.currentTimeMillis() - start;
		//return graph edit similarity
		return s(max);
	}
	
	/**
	 * removes the mapping from the list open
	 * 
	 * @param map - mapping to remove from list
	 */
	public void removeFromOpenList(Mapping map)
	{
		for(Mapping m : open)	{
			if(m.equals(map))	{
				open.remove(m);
				break;
			}
		}
	}
	
	/**
	 * create new mappings by adding next unmapped node pair to maximal mapping
	 * 
	 * @param max - current partial mapping with maximal graph edit similarity
	 */
	public void createNewMappings(Mapping max)
	{
		//select n1 from N1 such that n1 not element dom(map)
		Node n1 = getSomeUnmappedNode(max, sourceGraph.getNodes());
		//foreach n2 from N2
		for(Node n2 : targetGraph.getNodes())	{
			//such that n2 is not element cod(map) and Sim(n1,n2) > label edit cut-off
			if(!isCodOf(max, n2) && (SimilarityMetrics.computeNodeSimilarity(n1, n2) > LEDCUTOFF))	{
				Mapping map = new Mapping(max, dummy);
				NodePair pair = new NodePair(n1, n2);
				map.addNodePairToMapping(pair);
				open.add(map);
			}
		}
		//or n2 = dummy
		Mapping map = new Mapping(max, dummy);
		NodePair pair = new NodePair(n1, dummy);
		map.addNodePairToMapping(pair);
		open.add(map);
	}
	
	/**
	 * returns the partial mapping with maximal graph edit similarity
	 * 
	 * @return
	 * 		mapping with maximal graph edit similarity
	 */
	public Mapping getMaxMapping()
	{
		double max = -1.0;
		Mapping best = new Mapping(sourceGraph, targetGraph, dummy);
		
		for(Mapping o : open)
		{
			double similarity = s(o);
			if(similarity > max) 	{
				max = similarity;
				best = o;
			}
		}
		return best;
	}
	
	/**
	 * returns node from source graph that has not been mapped yet
	 * 
	 * @param maps - current partial mapping
	 * @param nodes - nodes from source graph
	 * @return
	 * 		an unmapped node from source graph
	 */
	public Node getSomeUnmappedNode(Mapping map, List<Node> nodes)
	{
		for(Node node : nodes)	{
			boolean mapped = false;
						
			for(NodePair pair : map.getMappedNodes())	{
				if(node.equals(pair.getNode1()))	{
					mapped = true;
				}
			}
			if(!mapped)	{
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * returns similarity induced by a mapping
	 * 
	 * @param map - mapping
	 * @return
	 * 		graph edit similarity
	 */
	public double s(Mapping map)
	{
		return SimilarityMetrics.computeGraphEditSimilarity(map, WSUBN, WSKIPN, WSKIPE);
	}
	
	/**
	 * check if mapping contains all nodes from source graph
	 * 
	 * @param maps - current partial mapping
	 * @param nodes - nodes (domain) from source graph
	 * @return
	 * 		true if all nodes from source graph are mapped, false otherwise
	 */
	public boolean isDomOf(Mapping m, List<Node> nodes)
	{
		for(Node node : nodes)	{
			boolean isMapped = false;
			
			for(NodePair pair : m.getMappedNodes())	{
				if(node.equals(pair.getNode1()))	{
					isMapped = true;
				}
			}
			if(!isMapped) 
				return false;
		}
		
		return true;
	}
	
	/**
	 * check if node from target graph has already been mapped
	 * 
	 * @param maps - partial current mapping
	 * @param node - node from target graph
	 * @return
	 * 		true if all nodes from target graph are mapped, false otherwise
	 */
	public boolean isCodOf(Mapping m, Node node)
	{
		for(NodePair pair : m.getMappedNodes())	{
			if(pair.getNode2().equals(node))	{
				return true;
			}
		}
		
		return false;
	}


	public Node getDummy() {
		return dummy;
	}

	public void setDummy(Node dummy) {
		this.dummy = dummy;
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

	public ArrayList<Mapping> getOpen() {
		return open;
	}

	public void setOpen(ArrayList<Mapping> open) {
		this.open = open;
	}
}
