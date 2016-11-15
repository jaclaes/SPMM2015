package org.cheetahplatform.literatemodeling.model;

import java.util.List;

import org.cheetahplatform.literatemodeling.report.GraphElementRendererFactory;
import org.cheetahplatform.literatemodeling.report.IGraphElementRenderer;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public interface ILiterateModelingAssociation {

	/**
	 * Returns the offset.
	 * 
	 * @return the offset
	 */
	public abstract int getOffset();

	/**
	 * Returns the length.
	 * 
	 * @return the length
	 */
	public abstract int getLength();

	/**
	 * @param color
	 * @return
	 */
	public abstract StyleRange getStyleRange(Color foreground, Color background);

	/**
	 * @param color
	 * @return
	 */
	public abstract StyleRange getStyleRange(Color foreground, Color background, int style);

	/**
	 * @param offsetToCheck
	 * @return
	 */
	public abstract boolean isBefore(int offsetToCheck);

	/**
	 * @param insertedLength
	 */
	public abstract void increaseOffset(int insertedLength);

	/**
	 * @param toCheck
	 * @return
	 */
	public abstract boolean isAfter(int toCheck);

	/**
	 * 
	 */
	public abstract void increaseLength(int toIncrease);

	/**
	 * @param deletedLength
	 */
	public abstract void decreaseOffset(int deletedLength);

	/**
	 * @param deletedLength
	 */
	public abstract void decreaseLength(int deletedLength);

	/**
	 * @return
	 */
	public abstract int getEnd();

	/**
	 * @param element
	 * @return
	 */
	public abstract boolean matches(GraphElement element);

	/**
	 * @param graphElementRendererFactory
	 * @param model
	 * @return
	 */
	public abstract IGraphElementRenderer createGraphElementRenderer(GraphElementRendererFactory graphElementRendererFactory,
			LiterateModel model);

	List<GraphElement> getGraphElements();

	/**
	 * @param graphElement
	 */
	void removeGraphElement(GraphElement graphElement);

	/**
	 * @return
	 */
	boolean isSingleLiterateModelingAssociation();
}