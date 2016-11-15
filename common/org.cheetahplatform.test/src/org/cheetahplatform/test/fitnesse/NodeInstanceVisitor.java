/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.core.imperative.runtime.AbstractNodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.EndNodeInstance;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeActivityInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;
import org.cheetahplatform.core.imperative.runtime.StartNodeInstance;
import org.cheetahplatform.core.imperative.runtime.routing.AbstractJoinInstance;
import org.cheetahplatform.core.imperative.runtime.routing.AndSplitInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopEndInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopStartInstance;
import org.cheetahplatform.core.imperative.runtime.routing.XorSplitInstance;
import org.eclipse.core.runtime.Assert;

public class NodeInstanceVisitor extends AbstractNodeInstanceVisitor {
	private final Map<String, List<IImperativeNodeInstance>> nameToInstances;

	public NodeInstanceVisitor() {
		this.nameToInstances = new HashMap<String, List<IImperativeNodeInstance>>();
	}

	public boolean contains(String name, int occurrence) {
		List<IImperativeNodeInstance> instances = nameToInstances.get(name);
		if (instances == null) {
			return false;
		}

		return instances.size() > occurrence;
	}

	public IImperativeNodeInstance getNodeInstance(long id) {
		Collection<List<IImperativeNodeInstance>> values = nameToInstances.values();
		for (List<IImperativeNodeInstance> instances : values) {
			for (IImperativeNodeInstance instance : instances) {
				if (instance.getCheetahId() == id)
					return instance;
			}
		}
		return null;
	}

	public IImperativeNodeInstance getNodeInstance(String name, int occurrence) {
		List<IImperativeNodeInstance> instances = nameToInstances.get(name);
		Assert.isNotNull(instances, "There is no instance for name " + name + ", occurrence: " + occurrence);
		Assert.isTrue(instances.size() > occurrence, "Index out of bounds: " + occurrence);
		return instances.get(occurrence);
	}

	public List<IImperativeNodeInstance> getNodeInstances(String name) {
		List<IImperativeNodeInstance> list = nameToInstances.get(name);
		if (list == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(list);
	}

	private void storeNodeInstance(IImperativeNodeInstance instance) {
		String name = instance.getNode().getName();
		List<IImperativeNodeInstance> list = nameToInstances.get(name);

		if (list == null) {
			list = new ArrayList<IImperativeNodeInstance>();
			nameToInstances.put(name, list);
		}

		list.add(instance);
	}

	@Override
	public void visitActivity(ImperativeActivityInstance instance) {
		super.visitActivity(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitAndJoin(AbstractJoinInstance instance) {
		super.visitAndJoin(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitAndSplit(AndSplitInstance instance) {
		super.visitAndSplit(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitEndNodeInstance(EndNodeInstance instance) {
		super.visitEndNodeInstance(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitLateBindingBox(LateBindingBoxInstance instance) {
		super.visitLateBindingBox(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitLateModelingBox(LateModelingBoxInstance instance) {
		super.visitLateModelingBox(instance);

		storeNodeInstance(instance);
	}

	@Override
	public void visitLoopEnd(LoopEndInstance instance) {
		super.visitLoopEnd(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitLoopStart(LoopStartInstance instance) {
		super.visitLoopStart(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitStartNode(StartNodeInstance instance) {
		super.visitStartNode(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitXorJoin(AbstractJoinInstance instance) {
		super.visitXorJoin(instance);
		storeNodeInstance(instance);
	}

	@Override
	public void visitXorSplit(XorSplitInstance instance) {
		super.visitXorSplit(instance);
		storeNodeInstance(instance);
	}

}
