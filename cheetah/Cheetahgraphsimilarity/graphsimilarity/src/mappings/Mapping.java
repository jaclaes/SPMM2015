package mappings;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class Mapping {
	//private static Logger logger = Logger.getLogger(Mapping.class);
	
	private Graph sourceGraph;
	private Graph targetGraph;
	private ArrayList<Node> subn;
	private ArrayList<Node> skipn;
	private ArrayList<Edge> skipe;
	private ArrayList<NodePair> mappedNodes;
	private Node dummy;
	private double similarity;
	
	
	public Mapping(Graph sourceGraph, Graph targetGraph)
	{
		this.sourceGraph = sourceGraph;
		this.targetGraph = targetGraph;
		subn = new ArrayList<Node>();
		skipn = new ArrayList<Node>();
		skipe = new ArrayList<Edge>();
		mappedNodes = new ArrayList<NodePair>();
		dummy = null;
		similarity = 0.0;
		init();
	}
	
	public Mapping(Graph sourceGraph, Graph targetGraph, Node dummy)
	{
		this.sourceGraph = sourceGraph;
		this.targetGraph = targetGraph;
		this.dummy = dummy;
		subn = new ArrayList<Node>();
		skipn = new ArrayList<Node>();
		skipe = new ArrayList<Edge>();
		mappedNodes = new ArrayList<NodePair>();
		similarity = 0.0;
		init();
	}
	
	public Mapping(Mapping m)
	{
		sourceGraph = m.getSourceGraph();
		targetGraph = m.getTargetGraph();
		subn = new ArrayList<Node>();
		skipn = new ArrayList<Node>();
		skipe = new ArrayList<Edge>();
		mappedNodes = new ArrayList<NodePair>();
		subn.addAll(m.getSubn());
		skipn.addAll(m.getSkipn());
		skipe.addAll(m.getSkipe());
		dummy = null;
		similarity = m.getSimilarity();
		if(m.getMappedNodes().size() > 0)
			mappedNodes.addAll(m.getMappedNodes());
	}
	
	public Mapping(Mapping m, Node dummy)
	{
		this.dummy = dummy;
		sourceGraph = m.getSourceGraph();
		targetGraph = m.getTargetGraph();
		subn = new ArrayList<Node>();
		skipn = new ArrayList<Node>();
		skipe = new ArrayList<Edge>();
		mappedNodes = new ArrayList<NodePair>();
		subn.addAll(m.getSubn());
		skipn.addAll(m.getSkipn());
		skipe.addAll(m.getSkipe());
		similarity = m.getSimilarity();
		if(m.getMappedNodes().size() > 0)
			mappedNodes.addAll(m.getMappedNodes());
	}
	
	
	
	/**
	 * initialize empty mapping, mark all nodes and edges as skiped
	 */
	private void init()
	{
		skipn.addAll(sourceGraph.getNodes());
		skipn.addAll(targetGraph.getNodes());
		skipe.addAll(sourceGraph.getEdges());
		skipe.addAll(targetGraph.getEdges());
	}
	
	/**
	 * add node pair to mapping and update subn, skipn and skipe
	 * 
	 * @param pair - node pair to add
	 */
	public void addNodePairToMapping(NodePair pair)
	{
		mappedNodes.add(pair);
		
		computeSubn(pair);
		computeSkipn(pair);
		computeSkipe(pair);
	}

	/**
	 * update list of substituted nodes by adding nodes of node pair
	 * 
	 * @param pair - node pair
	 */
	public void computeSubn(NodePair pair)
	{
		if(containsDummy(pair))	
			return;
				
		subn.add(pair.getNode1());
		subn.add(pair.getNode2());
	}
	
	/**
	 * update list of skiped nodes by removing nodes of node pair
	 * 
	 * @param pair - node pair
	 */
	public void computeSkipn(NodePair pair)
	{
		if(containsDummy(pair))
			return;
		
		skipn.remove(pair.getNode1());
		skipn.remove(pair.getNode2());
	}
	
	/**
	 * returns target node of node pair
	 * @param n - source node
	 * @return
	 * 		target node
	 */
	public Node getMappedTargetNode(Node n)
	{
		for(NodePair pair : mappedNodes)	{
			if(n.equals(pair.getNode1()))	
				return pair.getNode2();
		}
		
		return null;
	}
	
	/**
	 * update list of skiped edges
	 * 
	 * @param pair - node pair
	 */
	public void computeSkipe(NodePair pair)
	{
		if(containsDummy(pair))
			return;
		
		for(Edge edge : sourceGraph.getEdges())	{
			Node n2 = getMappedTargetNode(edge.getSource());
			Node m2 = getMappedTargetNode(edge.getTarget());

			if((n2 != null) && (m2 != null))	{
				for(Edge e : targetGraph.getEdges())	{
					if(e.getSource().equals(n2) && e.getTarget().equals(m2))	{
							skipe.remove(edge);
							skipe.remove(e);
					}
				}
			}
		}
	}
	
	/**
	 * checks if node pair contains dummy node
	 * 
	 * @param pair - node pair
	 * @return
	 * 		true, if pair contains dummy node, false otherwise
	 */
	public boolean containsDummy(NodePair pair)
	{
		if(dummy == null)
			return false; 
		
		if((pair == null) || (pair.getNode2().equals(dummy)))
			return true;
		
		return false;
	}

	public ArrayList<Node> getSubn() {
		return subn;
	}

	public void setSubn(ArrayList<Node> subn) {
		this.subn = subn;
	}

	public ArrayList<Node> getSkipn() {
		return skipn;
	}

	public void setSkipn(ArrayList<Node> skipn) {
		this.skipn = skipn;
	}

	public ArrayList<Edge> getSkipe() {
		return skipe;
	}

	public void setSkipe(ArrayList<Edge> skipe) {
		this.skipe = skipe;
	}

	public ArrayList<NodePair> getMappedNodes() {
		return mappedNodes;
	}

	public void setMappedNodes(ArrayList<NodePair> mappedNodes) {
		this.mappedNodes = mappedNodes;
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
	
	public Node getDummy() {
		return dummy;
	}

	public void setDummy(Node dummy) {
		this.dummy = dummy;
	}
	
	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	/*public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof Mapping))
			return false;
		
		Mapping other = (Mapping)o;
		if(!this.getSourceGraph().equals(other.getSourceGraph()))	
			return false;
			
		if(!this.getTargetGraph().equals(other.getTargetGraph()))
			return false;
		if(this.mappedNodes.size() == other.mappedNodes.size())	{
			for(int i=0; i<this.mappedNodes.size(); i++)	{
				
			}
		}else	{
			return false;
		}
		
		return true;
	}*/
}
