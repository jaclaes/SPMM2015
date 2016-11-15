package org.cheetahplatform.recommendation;



import java.util.List;
import java.util.Random;
import java.util.Vector;



/**
 * This list is a sorted list of recommendation results. The recommendation with the highest do
 * value has the lowest index in the list.
 * 
 * @author Christian Haisjackl
 */
public class RecommendationResultList {
	private List<RecommendationResult> results = new Vector<RecommendationResult>();
	private Random rand;



	/**
	 * This method generates a recommendation result list.
	 */
	public RecommendationResultList() {
		rand = new Random( System.currentTimeMillis() );
	}



	/**
	 * This method adds a recommendation result to the sorted by the scale. If the scale is null, it
	 * will be sorted by the do value.
	 * 
	 * @param result The result to add
	 */
	public void addResult( RecommendationResult result ) {
		// sort recommendation
		for( int i = 0; i < results.size(); i++ ) {
			// System.out.println( "isBetter:" + isBetter( result, results.get( i ) ) );
			if( isEquals( result, results.get( i ) ) ) {
				if( rand.nextBoolean() )
					continue; // if it is equal, only by a chance of 50% add before, else add later.
				results.add( i, result );
				return;
			} else if( isBetter( result, results.get( i ) ) ) {
				results.add( i, result );
				return;
			}
		}
		// if there is no result or it's the least result
		results.add( result );
	}



	/**
	 * Compares the two recommendations.
	 * 
	 * @param currentRecommendation The recommendation
	 * @param nextRecommendation The former recommendation
	 * @return True if both have the same do and don't value
	 */
	private boolean isEquals( RecommendationResult currentRecommendation, RecommendationResult nextRecommendation ) {
		Double doExpectedValueCur = currentRecommendation.getDoValue();
		Double dontExpectedValueCur = currentRecommendation.getDontValue();
		Double doExpectedValueNext = nextRecommendation.getDoValue();
		Double dontExpectedValueNext = nextRecommendation.getDontValue();
		return ( doExpectedValueCur.equals( doExpectedValueNext ) && dontExpectedValueCur.equals( dontExpectedValueNext ) );
	}



	/**
	 * This method compares two recommendations and returns if the first one is the better one
	 * 
	 * @param currentRecommendation The recommendation
	 * @param nextRecommendation The former recommendation
	 * @return True if the first recommendation is better than the second one, else false
	 */
	private boolean isBetter( RecommendationResult currentRecommendation, RecommendationResult nextRecommendation ) {
		Double doExpectedValueCur = currentRecommendation.getDoValue();
		Double dontExpectedValueCur = currentRecommendation.getDontValue();
		Double doExpectedValueNext = nextRecommendation.getDoValue();
		Double dontExpectedValueNext = nextRecommendation.getDontValue();

		if( dontExpectedValueCur.isNaN() ) {
			if( !dontExpectedValueNext.isNaN() )
				return true;
			if( doExpectedValueCur.isNaN() )
				return false;
			if( doExpectedValueNext.isNaN() )
				return true;
			return doExpectedValueCur.doubleValue() > doExpectedValueNext.doubleValue();
		} else if( doExpectedValueCur.isNaN() ) {
			if( !doExpectedValueNext.isNaN() )
				return false;
			if( dontExpectedValueCur.isNaN() )
				return true;
			if( dontExpectedValueNext.isNaN() )
				return false;
			return dontExpectedValueCur.doubleValue() < dontExpectedValueNext.doubleValue();
		} else {
			if( doExpectedValueNext.isNaN() )
				return true;
			if( dontExpectedValueNext.isNaN() )
				return false;
			return ( doExpectedValueCur.doubleValue() - dontExpectedValueCur.doubleValue() > doExpectedValueNext.doubleValue() - dontExpectedValueNext.doubleValue() );
		}
	}



	/**
	 * @return the results
	 */
	public List<RecommendationResult> getResults() {
		return results;
	}
}
