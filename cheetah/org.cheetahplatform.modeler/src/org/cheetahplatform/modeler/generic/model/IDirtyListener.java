/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.model;

public interface IDirtyListener {
	/**
	 * Called whenever the model gets dirty.
	 */
	void dirty();
}
