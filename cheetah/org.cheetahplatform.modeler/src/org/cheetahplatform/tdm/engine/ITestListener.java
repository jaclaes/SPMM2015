package org.cheetahplatform.tdm.engine;

import java.util.List;


public interface ITestListener {

	void testFailed(TDMTest tdmTest, List<ITDMStep> failures);

	void testPassed(TDMTest tdmTest);

}
