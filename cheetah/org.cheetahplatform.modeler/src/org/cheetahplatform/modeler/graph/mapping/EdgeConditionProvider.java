package org.cheetahplatform.modeler.graph.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.engine.configurations.ExperimentLayout;
import org.cheetahplatform.modeler.engine.configurations.ExperimentLayoutNovember2011;
import org.cheetahplatform.modeler.engine.configurations.NovicesVersusExpertsExperiment;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.EdgePresenceEvaluation;
import org.eclipse.swt.graphics.RGB;

public class EdgeConditionProvider {
	public static List<EdgeCondition> CONDITIONS;

	static {
		CONDITIONS = new ArrayList<EdgeCondition>();

		String[] processes = new String[] { ExperimentLayout.MODELING_TASK_1_WITH_LAYOUT.getId(),
				ExperimentLayout.MODELING_TASK_1_WITHOUT_LAYOUT.getId() };
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.ALL_CUSTOMER_INFORMATION_AVAILABLE, "All customer information available",
				new RGB(254, 62, 62), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.MISSING_CUSTOMER_INFORMATION, "Missing customer information", new RGB(254, 156,
				156), processes));
		CONDITIONS
				.add(new EdgeCondition(ExperimentLayout.BANK_REFUSES_MORTGAGE, "Bank refuses mortgage", new RGB(255, 69, 235), processes));
		CONDITIONS
				.add(new EdgeCondition(ExperimentLayout.BANK_ACCEPTS_MORTGAGE, "Bank accepts mortgage", new RGB(130, 69, 255), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.CUSTOMER_REJECTS_OFFER, "Customer rejects mortgage offer",
				new RGB(187, 154, 255), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.CUSTOMER_ACCEPTS_OFFER, "Customer accepts mortgage offer", new RGB(49, 181, 249),
				processes));

		processes = new String[] { ExperimentLayout.MODELING_TASK_2_WITH_LAYOUT.getId(),
				ExperimentLayout.MODELING_TASK_2_WITHOUT_LAYOUT.getId() };
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.NO_PREVIOUS_MORTGAGE, "No previous mortgage", new RGB(254, 62, 62), processes));
		CONDITIONS
				.add(new EdgeCondition(ExperimentLayout.ONE_PREVIOUS_MORTGAGE, "One previous mortgage", new RGB(254, 156, 156), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.MORE_THAN_ONE_PREVIOUS_MORTGAGE, "More than one previous mortgage", new RGB(255,
				69, 235), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.ONE_CHECK_NEGATIVE, "One check is negative", new RGB(130, 69, 255), processes));
		CONDITIONS
				.add(new EdgeCondition(ExperimentLayout.ALL_CHECKS_POSITIVE, "All checks are positive", new RGB(187, 154, 255), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.MORTGAGE_LESS_THAN_ONE_MIO, "Mortgage less than 1 mio", new RGB(49, 181, 249),
				processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayout.MORTGAGE_GREATER_OR_EQUAL_ONE_MIO, "Mortgage greater or equal 1 mio", new RGB(83,
				141, 213), processes));

		processes = new String[] { NovicesVersusExpertsExperiment.MODELING_TASK_1.getId() };
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.FILE_FLIGHT_PLAN, "File Flight Plan", new RGB(15, 62, 180),
				processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.LARGE_AIRPORT, "Large Airport", new RGB(254, 62, 62), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.HAS_TOWER_CLEARANCE, "Has Tower (Clearance)",
				new RGB(254, 156, 156), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.NO_TOWER_CLEARANCE, "No Tower (Clearance)", new RGB(83, 141, 213),
				processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.HAS_TOWER_TAKE_OFF, "Has Tower (Take-Off)", new RGB(130, 69, 255),
				processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.NO_TOWER_TAKE_OFF, "No Tower (Take-Off)", new RGB(49, 181, 249),
				processes));
		CONDITIONS.add(new EdgeCondition(EdgePresenceEvaluation.INCORRECT_EDGE_ID, EdgePresenceEvaluation.INCORRECT_EDGE,
				new RGB(255, 0, 0), new String[] { NovicesVersusExpertsExperiment.MODELING_TASK_1.getId(),
						NovicesVersusExpertsExperiment.MODELING_TASK_2.getId() }));

		processes = new String[] { NovicesVersusExpertsExperiment.MODELING_TASK_2.getId() };

		CONDITIONS
				.add(new EdgeCondition(NovicesVersusExpertsExperiment.WATCH_TAPE_LOOP, "Watch Tape Loop", new RGB(15, 62, 180), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.NOT_INTERESTED_AFTER_TALK, "Not Interested (Talk)", new RGB(254,
				62, 62), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.NOT_INTERESTED_EVALUATION, "Not Interested (Evaluation)", new RGB(
				254, 156, 156), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.PLAYER_NOT_AVAILABLE, "Player Not Available",
				new RGB(83, 141, 213), processes));
		CONDITIONS.add(new EdgeCondition(NovicesVersusExpertsExperiment.PLAYER_NOT_AVAILABLE_OR_NOT_INTERESTED,
				"Player Not Available/Not Interested (Evaluation)", new RGB(49, 181, 249), processes));

		processes = new String[] { ExperimentLayoutNovember2011.MODELING_TASK_1_WITH_LAYOUT.getId(),
				ExperimentLayoutNovember2011.MODELING_TASK_1_WITHOUT_LAYOUT.getId() };
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.ALL_CUSTOMER_INFORMATION_AVAILABLE,
				"All customer information available", new RGB(254, 62, 62), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.MISSING_CUSTOMER_INFORMATION, "Missing customer information",
				new RGB(254, 156, 156), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.NO_PREVIOUS_MORTGAGE, "No previous mortgage", new RGB(254, 62, 62),
				processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.ONE_PREVIOUS_MORTGAGE, "One previous mortgage",
				new RGB(254, 156, 156), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.MORE_THAN_ONE_PREVIOUS_MORTGAGE, "More than one previous mortgage",
				new RGB(255, 69, 235), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.LESS_THAN_ONE_PREVIOUS_MORTGAGE,
				"Less or equal one previous mortgage", new RGB(255, 123, 235), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.MORTGAGE_LESS_THAN_ONE_MIO, "Mortgage less than 1 mio", new RGB(49,
				181, 249), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.MORTGAGE_GREATER_OR_EQUAL_ONE_MIO, "Mortgage greater or equal 1 mio",
				new RGB(83, 141, 213), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.BANK_REFUSES_MORTGAGE, "Bank refuses mortgage",
				new RGB(255, 69, 235), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.BANK_ACCEPTS_MORTGAGE, "Bank accepts mortgage",
				new RGB(130, 69, 255), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.CUSTOMER_REJECTS_OFFER, "Customer rejects mortgage offer", new RGB(
				187, 154, 255), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.CUSTOMER_ACCEPTS_OFFER, "Customer accepts mortgage offer", new RGB(
				49, 181, 249), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.UPDATE_OFFER, "Update Offer", new RGB(49, 255, 249), processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.UPDATE_OFFER_2_POINTS, "Update Offer (2p)", new RGB(255, 69, 235),
				processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.DO_NOT_UPDATE_OFFER, "Do not Update Offer", new RGB(255, 141, 213),
				processes));
		CONDITIONS.add(new EdgeCondition(ExperimentLayoutNovember2011.LOOP_BACK_EDGE, "Long Back Edge to Update Offer", new RGB(255, 141,
				10), processes));
		CONDITIONS.add(new EdgeCondition(EdgePresenceEvaluation.INCORRECT_EDGE_ID, EdgePresenceEvaluation.INCORRECT_EDGE,
				new RGB(255, 0, 0), processes));
	}

	public static List<EdgeCondition> getConditions() {
		return Collections.unmodifiableList(CONDITIONS);
	}

	public static EdgeCondition getEdgeCondition(long edgeConditionId) {
		for (EdgeCondition condition : CONDITIONS) {
			if (condition.getId() == edgeConditionId) {
				return condition;
			}
		}

		return null;
	}

	public static List<EdgeCondition> getEdgeConditions(String process) {
		List<EdgeCondition> matches = new ArrayList<EdgeCondition>();
		for (EdgeCondition edgeCondition : CONDITIONS) {
			if (edgeCondition.belongsTo(process)) {
				matches.add(edgeCondition);
			}
		}

		return matches;
	}
}
