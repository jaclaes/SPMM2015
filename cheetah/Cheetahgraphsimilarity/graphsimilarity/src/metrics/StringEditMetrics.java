package metrics;

import org.cheetahplatform.modeler.graph.model.Node;

public class StringEditMetrics {
	/**
	 * uses the algorithm of levenshtein to compute the string edit distance between
	 * two strings
	 * 
	 * @param s1 - first string
	 * @param s2 - second string
	 * @return
	 * 		string edit distance
	 */
	public static int computeStringEditDistance(String s1, String s2)
	{
		//table with length(s1)+1 rows and length(s2)+1 columns
		int[][] distance = new int[s1.length()+1][s2.length()+1];
		
		//initialize first row of table with values from 0...length(s2) 
		//and first column with values from 0...length(s1)
		for(int i=0; i<=s1.length(); i++)	{
			distance[i][0] = i;
			for(int j=0; j<=s2.length(); j++)	{
				distance[0][j] = j;
			}
		}
			
		//compute distance
		for(int j=1; j<=s2.length(); j++)	{
			for(int i=1; i<=s1.length(); i++)	{
				//if characters the same -> new string edit distance = old string edit distance
				if(s1.charAt(i-1) == s2.charAt(j-1))	{
					distance[i][j] = distance[i-1][j-1];
				}else	{
					int deletion = distance[i-1][j]+1;
					int insertion = distance[i][j-1]+1;
					int substitution = distance[i-1][j-1]+1;
					
					distance[i][j] = min(deletion, insertion, substitution);
				}
			}
		}
		
		//at the end the bottom right element of the array contains the distance
		return distance[s1.length()][s2.length()];
	}
	
	/**
	 * returns the minimum of three numbers
	 * 
	 * @param x - Integer
	 * @param y - Integer
	 * @param z - Integer
	 * @return
	 * 		minimum of the three numbers
	 */
	public static int min(int x, int y, int z)
	{
		int min = Math.min(x, y);
		min = Math.min(min, z);
				
		return min;
	}
	
	/**
	 * compute the similarity between two nodes
	 * 
	 * @param n1 - node from G1
	 * @param n2 - node from G2
	 * @return
	 * 		node similarity
	 */
	public static double computeStringEditSimilarity(Node n1, Node n2)
	{
		if(n1 == null || n2 == null) 
			return 0.0;
		double stringEditDistance = SimilarityMetrics.computeStringEditDistance(n1.getName(), n2.getName());
		double max = Math.max(n1.getName().length(), n2.getName().length());
		if(max == 0.0)
			return 1.0;
		
		double sim = 1.0 - (stringEditDistance/max);

		return sim;
	}
}
