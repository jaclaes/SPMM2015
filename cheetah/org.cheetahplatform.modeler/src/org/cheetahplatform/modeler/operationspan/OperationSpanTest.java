package org.cheetahplatform.modeler.operationspan;

import org.cheetahplatform.common.logging.Process;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.engine.OperationSpanActivity;

public class OperationSpanTest {

	public OperationSpanActivity createOperationSpanTest(Process process) throws InvalidExpressionException {
		List<Exercise> demoLevel = newLevel("6+5-3=8", "1+3+2=6");

		List<List<Exercise>> exercises = new ArrayList<List<Exercise>>();
		exercises.add(newLevel("1+5+3=9", "6-2+4=8"));
		exercises.add(newLevel("7+5-9=3", "8-1-3=4"));
		exercises.add(newLevel("4+3-2=5", "3-1+7=9"));

		exercises.add(newLevel("9-6-1=2", "5+8-7=6", "6-3+4=7"));
		exercises.add(newLevel("3+9-5=7", "6+1+2=9", "7-2-3=2"));
		exercises.add(newLevel("2+1+5=8", "5-2+6=9", "6-1-2=3"));

		exercises.add(newLevel("8+7-9=6", "1+4+3=8", "9-3-4=2", "6-2-3=1"));
		exercises.add(newLevel("5-3+1=3", "8-2-4=2", "2+4+1=7", "7+9-8=8"));
		exercises.add(newLevel("1+6-5=2", "2-1+6=7", "3+1+5=9", "7-6+2=3"));

		exercises.add(newLevel("3-1+2=4", "2+3+4=9", "6-4+5=7", "9+3-7=5", "4+1+3=8"));

		exercises.add(newLevel("7+8-6=9", "7-4-2=1", "1+2+3=6", "4-3+7=8", "9-6-1=2"));

		exercises.add(newLevel("4+2+1=7", "6+5-3=8", "5-1-3=1", "7-5+2=4", "3+5-6=2"));

		exercises.add(newLevel("2+4-3=3", "3+9-5=7", "3+4+2=9", "5+2-1=6", "9-3-5=1", "7-6+8=9"));
		exercises.add(newLevel("7-4+2=5", "8-4-1=3", "9+2-6=5", "2+3-4=1", "5+4-6=3", "5+1+2=8"));
		exercises.add(newLevel("3+6-7=2", "7-2+4=9", "3-2+5=6", "7-5-1=1", "1+2+6=9", "8+4-7=5"));

		exercises.add(newLevel("2+7-1=8", "1+3+5=9", "3+6-2=7", "9-8+7=8", "8-1-4=3", "9-2+1=8", "3+8-5=6"));
		exercises.add(newLevel("3+4-5=2", "4+1+3=8", "1+7-4=4", "7-1-5=1", "4+5-3=6", "3+5-1=7", "4+7-9=2"));
		exercises.add(newLevel("5+6-4=7", "7-4+2=5", "5-3+1=3", "2+1+5=8", "6-1-4=1", "9-7+5=7", "5-2+3=6"));

		OperationSpanActivity ac = new OperationSpanActivity(demoLevel, exercises, process);

		return ac;
	}

	private List<Exercise> newLevel(String... exercises) throws InvalidExpressionException {
		List<Exercise> level = new ArrayList<Exercise>();
		for (String ex : exercises) {
			level.add(new Exercise(ex));
		}
		return level;
	}
}
