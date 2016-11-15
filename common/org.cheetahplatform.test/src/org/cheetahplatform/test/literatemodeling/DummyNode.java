/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

public class DummyNode extends Node {

	public static INodeDescriptor initalizeDescriptor() {
		CheetahPlatformConfigurator.getInstance().set(IConfiguration.INITIAL_ACTIVITIY_SIZE, new Point(120, 40));
		return EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN).get(0);
	}

	public DummyNode() {
		super(null, initalizeDescriptor());
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return null;
	}
}
