/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.NodeEdgeAlternationStatistic;
import org.junit.Before;
import org.junit.Test;

public class NodeEdgeAlternationStatisticTest extends LocaleSensitiveTest {
	private ProcessInstance processInstance;

	public void create(String type, int times) {
		for (int i = 0; i < times; i++) {
			processInstance.addEntry(new AuditTrailEntry(type));
		}
	}

	@Test
	public void edgeAtTheEnd() {
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 2);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);

		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals("0,6667", value);
	}

	@Test
	public void noEntries() {
		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals(IPpmStatistic.N_A, value);
	}

	@Test
	public void noProcessInstance() {
		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(null, null, null);
		assertEquals(IPpmStatistic.N_A, value);
	}

	@Test
	public void oneLargerBlockAlternation() {
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 2);
		create(AbstractGraphCommand.CREATE_NODE, 1);

		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals("0,5000", value);
	}

	@Test
	public void perfectAlternation() {
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);

		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals("1,0000", value);
	}

	@Test
	public void realExample() {

		create(AbstractGraphCommand.CREATE_NODE, 3);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 3);
		create(AbstractGraphCommand.CREATE_EDGE, 4);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 2);
		create(AbstractGraphCommand.CREATE_EDGE, 2);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 2);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 3);
		create(AbstractGraphCommand.CREATE_EDGE, 3);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 3);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 2);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 2);

		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals("0,5769", value);
	}

	@Before
	public void setUp() {
		processInstance = new ProcessInstance("instance");
	}

	@Test
	public void twoLargeBlocks() {
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 4);
		create(AbstractGraphCommand.CREATE_NODE, 3);
		create(AbstractGraphCommand.CREATE_EDGE, 3);
		create(AbstractGraphCommand.CREATE_NODE, 1);
		create(AbstractGraphCommand.CREATE_EDGE, 1);

		NodeEdgeAlternationStatistic nodeEdgeAlternationStatistic = new NodeEdgeAlternationStatistic();
		String value = nodeEdgeAlternationStatistic.getValue(processInstance, null, null);
		assertEquals("0,3750", value);
	}

}
