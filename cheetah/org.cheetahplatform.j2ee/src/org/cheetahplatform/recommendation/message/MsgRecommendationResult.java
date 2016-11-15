package org.cheetahplatform.recommendation.message;

import java.util.List;

/**
 * This message returns a recommendation result to the client
 * 
 * @author Christian Haisjackl
 */
public class MsgRecommendationResult extends Message {
	private static final long serialVersionUID = 1L;
	private List<List<Long>> recoResults;
	private List<List<Double>> doDonts;


	/**
	 * This returns recommendation results.
	 * 
	 * @param recoResults List of traces
	 * @param doDonts list of do and don't values,
	 */
	public MsgRecommendationResult( List<List<Long>> recoResults, List<List<Double>> doDonts ){
		this.recoResults = recoResults;
		this.doDonts = doDonts;
	}


	/**
	 * This is a list of the recommended activity IDs.
	 * 
	 * @return the recoResults
	 */
	public List<List<Long>> getRecoResults(){
		return recoResults;
	}


	/**
	 * This is a list of the do- and don't values.
	 * 
	 * @return the doDonts
	 */
	public List<List<Double>> getDoDonts(){
		return doDonts;
	}

}
