package org.cheetahplatform.modeler.graph.export;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cheetahplatform.common.logging.AuditTrailEntry;

public class OperationSpanIncrementalComputation {

	private boolean summationAborted;
	private double cntR = 0;
	private Map<Integer, Boolean> levelHasOneCorrect;
	private int prevSize = 0; // Size of the previous level;

	public OperationSpanIncrementalComputation() {
		summationAborted = false;
		levelHasOneCorrect = new HashMap<Integer, Boolean>();
		prevSize = 0;
	}

	public void add(Iterator<AuditTrailEntry> it) {
		AuditTrailEntry ate = it.next();
		while (!summationAborted && it.hasNext()) {
			// printATE(ate);

			String strSize = ate.getAttributeSafely("size");
			int size = strSize.equals("") ? 0 : Integer.valueOf(strSize);
			if (size > 0) { // attribute size is available
				if (levelHasOneCorrect.get(size) == null) {
					levelHasOneCorrect.put(size, false);
				}

				if (prevSize < size) { // level changed from e.g. 3 exercises to 4 exercises
					boolean prePrevOneCorrect = levelHasOneCorrect.get(size - 2) == null ? true : levelHasOneCorrect.get(size - 2);
					boolean prevOneCorrect = levelHasOneCorrect.get(size - 1) == null ? true : levelHasOneCorrect.get(size - 1);

					if (!prePrevOneCorrect && !prevOneCorrect) {
						summationAborted = true;
						break; // increasing cntR is stopped when the two previous levels don't have a single correct exercise.
					}
					prevSize = size;
				}

				Map<String, AuditTrailEntry> solutionMap = new HashMap<String, AuditTrailEntry>();
				// add the entered solutions
				boolean moreInput = true;
				while (it.hasNext() && moreInput) {
					ate = it.next();
					// printATE(ate);

					moreInput = ate.getAttributeSafely("size").equals("");
					// attribute calculation is available and attribute isDemo is not true
					if (!ate.getAttributeSafely("calculation").equals("") && !ate.getAttribute("isDemo").equals("true")) {
						solutionMap.put(ate.getAttribute("calculation"), ate);
					}
				}
				// check for correctness and increase cntR
				boolean isCorrect = true;
				for (AuditTrailEntry ate3 : solutionMap.values()) {
					if (!ate3.getAttribute("solution").equals(ate3.getAttribute("solutionEntered"))
							|| !ate3.getAttribute("endDigit").equals(ate3.getAttribute("endDigitEntered"))) {
						isCorrect = false;
					} else {
						levelHasOneCorrect.put(size, true);
					}
				}
				if (isCorrect && !solutionMap.isEmpty()) {
					cntR++;
				}
			} else if (it.hasNext()) {
				ate = it.next();
			}
		}
	}

	public double getRSP() {
		double RSP = 0;
		if (cntR > 0) {
			RSP = 2 - 0.5 + cntR / 3;
		} else {
			RSP = 1.5;
		}
		return RSP;
	}

	public boolean isSummationAborted() {
		return summationAborted;
	}

	// private void printATE(AuditTrailEntry ate) {
	// for (Attribute a : ate.getAttributes()) {
	// System.out.print(a.getName() + ": " + a.getContent() + ", ");
	// }
	// System.out.println();
	// }

}
