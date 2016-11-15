/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.IProcessInstance;

public class NodeRegistry<E extends INodeInstance> implements Serializable {
	private static final long serialVersionUID = -7006770052088975438L;

	private final Map<INode, E> registeredNodes;
	private final IProcessInstance processInstance;

	public NodeRegistry(IProcessInstance processInstance) {
		this.processInstance = processInstance;
		this.registeredNodes = new HashMap<INode, E>();
	}

	/**
	 * Register the given node and create an instance if not created yet.
	 * 
	 * @param node
	 *            the node to be registered
	 * @return an instance
	 */
	@SuppressWarnings("unchecked")
	public synchronized E getInstance(INode node) {
		E instance = registeredNodes.get(node);
		if (instance == null) {
			instance = (E) node.instantiate(processInstance);
		}

		registeredNodes.put(node, instance);
		return instance;
	}

	public void removeCachedInstance(INode node) {
		registeredNodes.remove(node);
	}
}
