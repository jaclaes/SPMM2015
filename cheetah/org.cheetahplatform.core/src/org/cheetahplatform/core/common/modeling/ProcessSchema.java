/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.NamedIdentifiableObject;

public abstract class ProcessSchema extends NamedIdentifiableObject {
	private static final long serialVersionUID = 3896427255784171717L;
	protected List<INode> nodes;

	protected ProcessSchema() {
		this("");
	}

	protected ProcessSchema(String name) {
		super(name);

		this.nodes = new ArrayList<INode>();
	}

	public INode getNode(long id) {
		for (INode node : nodes) {
			if (node.getCheetahId() == id) {
				return node;
			}
		}

		return null;
	}

	/**
	 * Returns the activities.
	 * 
	 * @return the activities
	 */
	public List<INode> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	public abstract ProcessInstance instantiate(String name);
}
