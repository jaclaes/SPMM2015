/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.daily.model.Workspace;

public abstract class GenericTDMModel extends GenericModel {

	protected GenericTDMModel(IGenericModel parent) {
		super(parent);
	}

	public Workspace getWorkspace() {
		return (Workspace) getParentType(Workspace.class);
	}

}
