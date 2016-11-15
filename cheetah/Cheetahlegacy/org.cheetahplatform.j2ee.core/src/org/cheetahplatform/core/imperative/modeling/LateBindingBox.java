/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;

public class LateBindingBox extends AbstractNode {
	private static final long serialVersionUID = -4837255403316363374L;
	private List<ImperativeProcessSchema> sequences;

	@SuppressWarnings("unused")
	private LateBindingBox() {
		// hibernate
	}

	public LateBindingBox(String name) {
		super(name);
		sequences = new ArrayList<ImperativeProcessSchema>();
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitLateBindingBoxStart(this);
		for (ImperativeProcessSchema schema : sequences) {
			schema.accept(visitor);
		}
		visitSuccessors(visitor);
	}

	/**
	 * Adds the given sequence to the selectable sequences of the box.
	 * 
	 * @param sequence
	 *            the sequence to add
	 */
	public void addSequence(ImperativeProcessSchema sequence) {
		sequences.add(sequence);
	}

	public boolean containsSequence(ImperativeProcessSchema sequence) {
		return sequences.contains(sequence);
	}

	public ImperativeProcessSchema getSequence(long sequenceId) {
		for (ImperativeProcessSchema sequence : sequences) {
			if (sequence.getCheetahId() == sequenceId) {
				return sequence;
			}
		}

		return null;
	}

	/**
	 * Returns the processSchemas.
	 * 
	 * @return the processSchemas
	 */
	public List<ImperativeProcessSchema> getSequences() {
		return Collections.unmodifiableList(sequences);
	}

	public String getType() {
		return IImperativeNode.TYPE_LATE_BINDING_NODE;
	}

	public INodeInstance instantiate(IProcessInstance processInstance) {
		return new LateBindingBoxInstance((ImperativeProcessInstance) processInstance, this);
	}
}
