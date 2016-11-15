package org.cheetahplatform.literatemodeling.model;

import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.text.ITextSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public abstract class AbstractSingleElementLiterateModelingAssociation extends AbstractLiterateModelingAssociation {

	public AbstractSingleElementLiterateModelingAssociation(int offset, int length) {
		super(offset, length);
	}

	/**
	 * @param selection
	 * @param graphElement
	 */
	public AbstractSingleElementLiterateModelingAssociation(ITextSelection selection) {
		super(selection);
	}

	public abstract GraphElement getGraphElement();

	@Override
	public boolean isSingleLiterateModelingAssociation() {
		return true;
	}

	/**
	 * @param element
	 * @return
	 */
	@Override
	public boolean matches(GraphElement element) {
		return getGraphElement().equals(element);
	}

	@Override
	public void removeGraphElement(GraphElement graphElement) {
		throw new UnsupportedOperationException("Graphelements cannot be removed from SingleElementLiterateModelingAssociations");
	}
}