package org.cheetahplatform.modeler.graph.export;

import org.cheetahplatform.common.Assert;

public class ModelingPhase {
	private final long start;
	private final int endNumberOfElements;
	private final String type;
	private final long end;
	private final int chunkSize;
	private final int startNumberOfElements;

	public ModelingPhase(long start, long end, int startNumberOfElements, int endNumberOfElements, int chunkSize, String type) {
		Assert.isNotNull(type);
		this.startNumberOfElements = startNumberOfElements;
		this.chunkSize = chunkSize;
		this.end = end;
		this.start = start;
		this.endNumberOfElements = endNumberOfElements;
		this.type = type;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public long getEnd() {
		return end;
	}

	public int getEndNumberOfElements() {
		return endNumberOfElements;
	}

	public long getStart() {
		return start;
	}

	public int getStartNumberOfElements() {
		return startNumberOfElements;
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}