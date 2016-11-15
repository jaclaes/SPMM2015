/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class XorFragment extends AbstractFragment {

	public XorFragment(ISingleEntrySingleExitComponent parent, XorSplit start) {
		super(parent, start);
	}

	@Override
	public void setEnd(INode end) {
		Assert.isTrue(end.getType().equals(IImperativeNode.TYPE_XOR_JOIN));
		super.setEnd(end);
	}

}
