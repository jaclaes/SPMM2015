package org.cheetahplatform.j2ee.beans;

import java.util.HashMap;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.cheetahplatform.core.declarative.constraint.MinSelectConstraint;
import org.cheetahplatform.core.declarative.constraint.MutualExclusionConstraint;
import org.cheetahplatform.core.declarative.constraint.ResponseConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.jboss.cache.pojo.PojoCache;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.07.2009
 */
public class TestDataGenerator extends AbstractMessageDrivenBean implements MessageListener {
	public static final String SERVICE_NAME = "TestDataGenerator";

	private void createDeclarativeProcess() {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema("Late Composition");
		DeclarativeActivity a = schema.createActivity("A");
		DeclarativeActivity b = schema.createActivity("B");
		DeclarativeActivity c = schema.createActivity("C");
		DeclarativeActivity d = schema.createActivity("D");

		schema.addConstraint(new ResponseConstraint(a, b));
		schema.addConstraint(new MutualExclusionConstraint(c, d));
		schema.addConstraint(new MinSelectConstraint(a, 1));

		DeclarativeProcessInstance instance = schema.instantiate();
		PojoCache cache = getCache();
		cache.attach(getPathToInstance(instance.getCheetahId()), instance);
		cache.attach(getPathToSchema(schema.getCheetahId()), schema);
	}

	private void createImperativeProcess() {
		ImperativeProcessSchema processSchema1 = new ImperativeProcessSchema("BusinessTravelProcess");
		LateBindingBox box = processSchema1.createLateBindingNode("Late Binding Box");

		ImperativeProcessSchema sequence1 = new ImperativeProcessSchema("Late Binding Sequence 1");
		ImperativeActivity lbbA = sequence1.createActivity("LBB Activity in Sequence 1");
		sequence1.getStart().addSuccessor(lbbA);
		lbbA.addSuccessor(sequence1.getEnd());
		box.addSequence(sequence1);

		ImperativeProcessSchema sequence2 = new ImperativeProcessSchema("Late Binding Sequence 2");
		ImperativeActivity lbb1 = sequence1.createActivity("LBB Activity in Sequence 2 (1)");
		ImperativeActivity lbb2 = sequence1.createActivity("LBB Activity in Sequence 2 (2)");
		sequence2.getStart().addSuccessor(lbb1);
		lbb1.addSuccessor(lbb2);
		lbb2.addSuccessor(sequence1.getEnd());
		box.addSequence(sequence2);

		LateModelingBox lateModelingBox = new LateModelingBox("Late Modeling Box");
		lateModelingBox.addActivity(processSchema1.createActivity("Late Modeling Activity 1"));
		lateModelingBox.addActivity(processSchema1.createActivity("Late Modeling Activity 2"));
		lateModelingBox.addActivity(processSchema1.createActivity("Late Modeling Activity 3"));
		lateModelingBox.addActivity(processSchema1.createActivity("Late Modeling Activity 4"));
		lateModelingBox.addActivity(processSchema1.createActivity("Late Modeling Activity 5"));

		AndSplit andSplit = processSchema1.createAndSplit("andsplit");
		AndJoin andJoin = processSchema1.createAndJoin("andjoin");
		andSplit.addSuccessor(box);
		andSplit.addSuccessor(lateModelingBox);
		lateModelingBox.addSuccessor(andJoin);
		box.addSuccessor(andJoin);
		processSchema1.getStart().addSuccessor(andSplit);
		andJoin.addSuccessor(processSchema1.getEnd());

		ImperativeActivity activityA = processSchema1.createActivity("personendaten");
		ImperativeActivity activityB = processSchema1.createActivity("reiseziel");
		ImperativeActivity activityC = processSchema1.createActivity("flugDatum");
		ImperativeActivity activityD = processSchema1.createActivity("hotel");
		andSplit.addSuccessor(activityA);
		AndSplit split = processSchema1.createAndSplit("split");
		activityA.addSuccessor(split);
		split.addSuccessor(activityB);
		split.addSuccessor(activityC);
		AndJoin join = processSchema1.createAndJoin("join");
		activityB.addSuccessor(join);
		activityC.addSuccessor(join);
		join.addSuccessor(activityD);
		activityD.addSuccessor(andJoin);

		ImperativeProcessInstance instance1 = processSchema1.instantiate(processSchema1.getName() + "_instance1");
		PojoCache cache = getCache();
		cache.attach(getPathToInstance(instance1.getCheetahId()), instance1);
		cache.attach(getPathToSchema(processSchema1.getCheetahId()), processSchema1);
	}

	private void createRecommendationDemoProcess() {
		ImperativeProcessSchema recommendationProcess = new ImperativeProcessSchema("RecommendationDemoProcess");
		ImperativeActivity activity100 = recommendationProcess.createActivity("A");
		activity100.setCheetahId(100);
		ImperativeActivity activity101 = recommendationProcess.createActivity("B");
		activity101.setCheetahId(101);
		ImperativeActivity activity102 = recommendationProcess.createActivity("C");
		activity102.setCheetahId(102);
		ImperativeActivity activity103 = recommendationProcess.createActivity("D");
		activity103.setCheetahId(103);
		ImperativeActivity activity104 = recommendationProcess.createActivity("E");
		activity104.setCheetahId(104);

		AndSplit andSplit = recommendationProcess.createAndSplit("And Split");
		AndJoin andJoin = recommendationProcess.createAndJoin("And Join");

		recommendationProcess.getStart().addSuccessor(activity100);
		activity100.addSuccessor(andSplit);
		andSplit.addSuccessor(activity101);
		andSplit.addSuccessor(activity102);
		andSplit.addSuccessor(activity103);
		andSplit.addSuccessor(activity104);

		activity101.addSuccessor(andJoin);
		activity102.addSuccessor(andJoin);
		activity103.addSuccessor(andJoin);
		activity104.addSuccessor(andJoin);
		andJoin.addSuccessor(recommendationProcess.getEnd());

		ImperativeProcessInstance instance1 = recommendationProcess.instantiate(recommendationProcess.getName() + "_instance1");
		PojoCache cache = getCache();
		cache.attach(getPathToInstance(instance1.getCheetahId()), instance1);
		cache.attach(getPathToSchema(recommendationProcess.getCheetahId()), recommendationProcess);
	}

	@Override
	protected void doOnMessage(Message message) throws Exception {
		System.out.println("Detaching instances");
		LoginService.destroyCache(getCache());

		System.out.println("Start creating instances");
		createImperativeProcess();
		createRecommendationDemoProcess();
		createDeclarativeProcess();
		System.out.println("Completed creating instances");

		sendReply(message, new HashMap<String, Object>());
	}
}
