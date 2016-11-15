package org.cheetahplatform.shared;

public class EdgeHandle extends TypedHandle {
	public static final String SEQUENCE_FLOW = "SEQUENCE_FLOW";

	private NodeHandle source;
	private NodeHandle target;

	public EdgeHandle(String type, NodeHandle source, NodeHandle target) {
		super(0, "", type);

		this.source = source;
		this.target = target;
	}

	/**
	 * @return the source
	 */
	public NodeHandle getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public NodeHandle getTarget() {
		return target;
	}

}
