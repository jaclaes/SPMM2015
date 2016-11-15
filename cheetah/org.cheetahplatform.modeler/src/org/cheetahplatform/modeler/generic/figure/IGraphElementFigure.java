package org.cheetahplatform.modeler.generic.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Handle;

/**
 * Interface for figures allowing selection. Usually selection is handled by {@link Handle} objects, but for some figures decorating is not
 * possible (as it is done by handles).
 * 
 * @author Stefan Zugal
 * 
 */
public interface IGraphElementFigure extends IFigure {
	/**
	 * Set the name of the figure.
	 * 
	 * @param name
	 *            the name, <code>null</code> to reset the name
	 */
	void setName(String name);

	/**
	 * Sets the selected state of the figure.
	 * 
	 * @param state
	 *            the state
	 */
	void setSelected(boolean state);
}
