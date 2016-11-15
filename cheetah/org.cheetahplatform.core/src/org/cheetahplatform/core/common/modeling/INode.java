/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.core.common.IIdentifiableObject;

/**
 * Represents a node within an imperative workflow specification. A node may be a control flow connector or an activity.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public interface INode extends INamed, IIdentifiableObject {
	String TYPE_ACTIVITY = "Activity";

	/**
	 * Returns the type of the node.
	 * 
	 * @return the node's type
	 */
	String getType();

	/**
	 * Create a new instance of the node.
	 * 
	 * @param processInstance
	 *            the instance for which the node should be instantiated
	 * @return the instance
	 */
	INodeInstance instantiate(IProcessInstance processInstance);
}
