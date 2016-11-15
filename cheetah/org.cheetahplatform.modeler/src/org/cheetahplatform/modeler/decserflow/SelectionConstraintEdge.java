package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;

public class SelectionConstraintEdge extends SingleNodeEdge {

	private static final int NO_MINIMUM_SET = 0;
	private static final int NO_MAXIMUM_SET = Integer.MAX_VALUE;

	private int minimum;
	private int maximum;

	public SelectionConstraintEdge(IGenericModel parent, IGraphElementDescriptor descriptor) {
		super(parent, descriptor);

		initializeMinimumMaximum();
	}

	public SelectionConstraintEdge(IGenericModel parent, IGraphElementDescriptor descriptor, long id) {
		super(parent, descriptor, id);

		initializeMinimumMaximum();
	}

	/**
	 * @return the maximum
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * @return the minimum
	 */
	public int getMinimum() {
		return minimum;
	}

	public boolean hasMaximum() {
		return maximum != NO_MAXIMUM_SET;
	}

	public boolean hasMinimum() {
		return minimum != NO_MINIMUM_SET;
	}

	private void initializeMinimumMaximum() {
		minimum = NO_MINIMUM_SET;
		maximum = NO_MAXIMUM_SET;
	}

	/**
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
		firePropertyChanged(ModelerConstants.PROPERTY_NAME);
	}

	/**
	 * @param minimum
	 *            the minimum to set
	 */
	public void setMinimum(int minimum) {
		this.minimum = minimum;
		firePropertyChanged(ModelerConstants.PROPERTY_NAME);
	}

	public void unsetMaximum() {
		setMaximum(NO_MAXIMUM_SET);
	}

	public void unsetMinimum() {
		setMinimum(NO_MINIMUM_SET);
	}

}
