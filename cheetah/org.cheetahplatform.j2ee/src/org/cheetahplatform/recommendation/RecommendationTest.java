package org.cheetahplatform.recommendation;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.cheetahplatform.recommendation.message.MsgGetRecommendation;
import org.cheetahplatform.recommendation.message.MsgRecommendationResult;
import org.cheetahplatform.recommendation.message.MsgSetStrategy;

public class RecommendationTest {

	public static void main(String[] args) {
		Vector<Object> vector = new Vector<Object>();
		vector.add(2);
		RecoServer recoServer = new RecoServer("C:\\tmp");

		MsgSetStrategy msgSetStrategy = new MsgSetStrategy("org.cheetahplatform.recommendation.PartialTraceStrategy", "", "duration", true,
				"complete", vector);
		recoServer.setStrategy(msgSetStrategy);

		List<Long> completedActivities = Arrays.asList(new Long[] { 100l, 103l });
		List<Long> possibleActivites = Arrays.asList(new Long[] { 104l, 101l, 102l });

		System.out.println("Completed Activites: " + completedActivities);
		System.out.println("Possible Activites: " + possibleActivites);
		System.out.println("Firing up Recommendation Engine!");
		MsgGetRecommendation getRecommendationMessage = new MsgGetRecommendation(completedActivities, possibleActivites);
		MsgRecommendationResult recommendationResult = recoServer.getRecommendation(getRecommendationMessage);
		List<List<Long>> recoResults = recommendationResult.getRecoResults();
		List<List<Double>> doDonts = recommendationResult.getDoDonts();
		System.out.println("Recommendation: " + recoResults);
		System.out.println("DoDonts: " + doDonts);
	}
}
