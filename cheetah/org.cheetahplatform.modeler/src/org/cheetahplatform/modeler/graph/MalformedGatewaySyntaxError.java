package org.cheetahplatform.modeler.graph;

public class MalformedGatewaySyntaxError implements ISyntaxError {

	@Override
	public String getDescription() {
		return "A gateway must either split of or merge the execution flow";
	}

}
