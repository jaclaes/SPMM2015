/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import java.io.File;
import java.util.Iterator;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.xml.XMLPromReader;
import org.eclipse.draw2d.geometry.Point;

import fit.ActionFixture;
import fit.Parse;

public class BPMNCommandFixture extends ActionFixture {
	private Iterator<AuditTrailEntry> logIterator;

	public void checkCoordinates() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		long id = Long.parseLong(cells.more.text().trim());
		Point coordinates = extractCoordinates(cells.more.more);

		Point actualLocation = BPMNCommandHelper.getCoordinate(id);

		if (coordinates.equals(actualLocation))
			right(cells.more.more);
		else {
			wrong(cells.more.more, actualLocation.x + "," + actualLocation.y);
		}
	}

	public void checkEdges() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}
		long id = Long.parseLong(cells.more.text());
		long fromId = Long.parseLong(cells.more.more.text());
		long toId = Long.parseLong(cells.more.more.more.text());

		long sourceId = BPMNCommandHelper.getSourceNodeId(id);
		long targetId = BPMNCommandHelper.getTargetNodeId(id);

		if (fromId == sourceId) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(sourceId));
		}

		if (toId == targetId) {
			right(cells.more.more.more);
		} else {
			wrong(cells.more.more.more, String.valueOf(targetId));
		}
	}

	public void checkName() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		long id = Long.parseLong(cells.more.text().trim());
		String name = cells.more.more.text().trim();

		String nameInGraph = BPMNCommandHelper.getName(id);
		if (nameInGraph == null || nameInGraph.trim().isEmpty())
			right(cells.more.more);
		else if (nameInGraph.equals(name)) {
			right(cells.more.more);
		} else
			wrong(cells.more.more, nameInGraph);
	}

	public void containsElement() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		long id = Long.parseLong(cells.more.text());
		boolean expected = Boolean.parseBoolean(cells.more.more.text());

		boolean actual = BPMNCommandHelper.containsElement(id);
		if (actual == expected)
			right(cells.more.more);
		else
			wrong(cells.more.more, String.valueOf(actual));
	}

	public void createActivity() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least five columns");
		}
		String name = cells.more.text().trim();
		long id = Long.parseLong(cells.more.more.text().trim());
		Point point = extractCoordinates(cells.more.more.more);

		BPMNCommandHelper.createActivity(id, name, point);

		ok(cells.more.more.more.more);
	}

	public void createEdge() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null
				|| cells.more.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least six columns");
		}
		String name = cells.more.text().trim();
		long id = Long.parseLong(cells.more.more.text());
		long fromId = Long.parseLong(cells.more.more.more.text());
		long toId = Long.parseLong(cells.more.more.more.more.text());

		BPMNCommandHelper.createEdge(id, name, fromId, toId);

		ok(cells.more.more.more.more.more);
	}

	public void deleteEdge() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		long id = Long.parseLong(cells.more.text());

		BPMNCommandHelper.deleteEdge(id);

		ok(cells.more.more);
	}

	public void deleteNode() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		long id = Long.parseLong(cells.more.text());

		BPMNCommandHelper.deleteNode(id);

		ok(cells.more.more);
	}

	public void edgeCount() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		int expected = Integer.parseInt(cells.more.text());
		int actual = BPMNCommandHelper.getEdgeCount();

		if (expected == actual) {
			right(cells.more);
		} else {
			wrong(cells.more, String.valueOf(actual));
		}
	}

	public void executeSteps() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		int step = Integer.parseInt(cells.more.text());
		for (int i = 0; i < step; i++) {
			AuditTrailEntry next = logIterator.next();
			BPMNCommandHelper.executeAuditTrailEntry(next);
		}

		ok(cells.more.more);
	}

	private Point extractCoordinates(Parse cell) {
		String[] coodrinates = cell.text().trim().split(",");
		Point point = new Point(Integer.parseInt(coodrinates[0]), Integer.parseInt(coodrinates[1]));
		return point;
	}

	public void loadLog() throws Exception {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String path = "..\\org.cheetahplatform.test\\testdata\\fitnesse\\";
		String filename = cells.more.text().trim();

		XMLPromReader reader = new XMLPromReader(new File(path + filename));
		logIterator = reader.getProcess().getInstances().get(0).getEntries().iterator();

		ok(cells.more.more);
	}

	public void moveNode() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		long id = Long.parseLong(cells.more.text());
		Point moveDelta = extractCoordinates(cells.more.more);

		BPMNCommandHelper.moveNode(id, moveDelta);

		ok(cells.more.more.more);
	}

	public void nextLogEntry() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		if (!logIterator.hasNext())
			throw new RuntimeException("No more AuditTrainEntries");

		AuditTrailEntry entry = logIterator.next();

		BPMNCommandHelper.executeAuditTrailEntry(entry);
		ok(cells.more);
	}

	public void nodeCount() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		int expected = Integer.parseInt(cells.more.text());
		int actual = BPMNCommandHelper.getNodeCount();

		if (expected == actual) {
			right(cells.more);
		} else {
			wrong(cells.more, String.valueOf(actual));
		}
	}

	private void ok(Parse cell) {
		cell.addToBody("Ok!");
		right(cell);
	}

	public void reconnectEdge() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least five columns");
		}
		long id = Long.parseLong(cells.more.text());
		long fromId = Long.parseLong(cells.more.more.text());
		long toId = Long.parseLong(cells.more.more.more.text());

		BPMNCommandHelper.reconnectEdge(id, fromId, toId);

		ok(cells.more.more.more.more);
	}

	public void renameActivity() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		long id = Long.parseLong(cells.more.text());
		String name = cells.more.more.text().trim();

		BPMNCommandHelper.renameNode(id, name);

		ok(cells.more.more.more);
	}

	@Override
	public void start() throws Throwable {
		super.start();
		BPMNCommandHelper.initialize();
	}
}
