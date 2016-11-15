/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.ui;

import java.util.ArrayList;
import java.util.List;

import org.alaskasimulator.ui.agile.model.AgileAction;
import org.alaskasimulator.ui.model.ExecutionPath;

import fit.RowFixture;

public class ExecutionPathFixture extends RowFixture {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getTargetClass() {
		return String.class;
	}

	@Override
	public Object[] query() throws Exception {
		List<ExecutionPath> paths = AbstractAgileCalendarFixture.CALENDAR.getDays().get(Integer.valueOf(args[0])).getPlanningArea()
				.computeExecutionPaths();

		List<String> result = new ArrayList<String>();
		for (ExecutionPath path : paths) {
			StringBuilder temp = new StringBuilder();
			boolean first = true;

			for (AgileAction action : path.getPath()) {
				if (!first) {
					temp.append(", ");
				}

				first = false;
				temp.append(action.getAction().getConfig().getName());
			}

			result.add(temp.toString());
		}

		return result.toArray();
	}

}
