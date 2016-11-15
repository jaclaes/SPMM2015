/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

/**
 * 
 */
package org.cheetahplatform.test.literatemodeling;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.graph.model.Edge;

public class DummyEdge extends Edge {
	public DummyEdge() {
		super(null, EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN).get(0));
	}

	@Override
	protected void firePropertyChanged(Class<? extends GenericModel> target, String property) {
		// do nothing
	}

	@Override
	public void firePropertyChanged(String property) {
		// do nothing
	}

}