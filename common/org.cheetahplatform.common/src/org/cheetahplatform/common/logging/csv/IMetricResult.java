package org.cheetahplatform.common.logging.csv;

public interface IMetricResult {
	/**
	 * Return the underlying metric.
	 * 
	 * @return the metric
	 */
	String getName();

	/**
	 * Transform the result to a string.
	 * 
	 * @return the result
	 */
	String resultAsString();
}
