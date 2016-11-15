/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly;

import org.cheetahplatform.common.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Used to store relative bounds. Absolute bounds are normalized to a defined coordinate system ({@link #NORMALIZED_WIDTH} x
 * {@link #NORMALIZED_HEIGHT}).
 * 
 * @author Stefan Zugal
 * 
 */
public class RelativeBounds {
	private static final double NORMALIZED_WIDTH = 10000;
	private static final double NORMALIZED_HEIGHT = 10000;

	private double relativeX;
	private double relativeY;
	private int width;
	private int height;

	public RelativeBounds(double relativeX, double relativeY, int width, int height) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.width = width;
		this.height = height;
	}

	public RelativeBounds(int x, int weekWidth, int y, int weekHeight, int width, int height) {
		this.width = width;
		this.height = height;

		setRelativeX(x, weekWidth);
		setRelativeY(y, weekHeight);
	}

	public int getAbsoluteX(int weekWidth) {
		double factorX = NORMALIZED_WIDTH / weekWidth;
		return (int) (relativeX / factorX);
	}

	public int getAbsoluteY(int weekHeight) {
		double factorY = NORMALIZED_HEIGHT / weekHeight;
		return (int) (relativeY / factorY);
	}

	/**
	 * Return the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	public double getRelativeX() {
		return relativeX;
	}

	public double getRelativeY() {
		return relativeY;
	}

	/**
	 * Return the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the height.
	 * 
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public void setRelativeX(int absoluteX, int weekWidth) {
		double factorX = NORMALIZED_WIDTH / weekWidth;
		relativeX = factorX * absoluteX;
	}

	public void setRelativeY(int absoluteY, int weekHeight) {
		double factorY = NORMALIZED_HEIGHT / weekHeight;
		relativeY = factorY * absoluteY;
	}

	/**
	 * Set the width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public Rectangle toAbsolute(int weekWidth, int weekHeight) {
		int x = getAbsoluteX(weekWidth);
		int y = getAbsoluteY(weekHeight);

		return new Rectangle(x, y, width, height);
	}

	@Override
	public String toString() {
		return "x: " + relativeX + ", y: " + relativeY;
	}

	public void translate(Point delta, int weekWidth, int weekHeight) {
		double factorX = NORMALIZED_WIDTH / weekWidth;
		double factorY = NORMALIZED_HEIGHT / weekHeight;
		double deltaX = delta.x * factorX;
		double deltaY = delta.y * factorY;

		relativeX += deltaX;
		relativeY += deltaY;
	}

	/**
	 * Translate the x coordinate by the given factor - [-1 ... 1]. If the resulting x coordinate is outside the normalized coordinates,
	 * modulo operations are applied.
	 * 
	 * @param factor
	 *            the factor
	 */
	public void translateXModulo(double factor) {
		Assert.isTrue(-1 <= factor && factor <= 1);

		relativeX += factor * NORMALIZED_WIDTH;
		if (relativeX < 0) {
			relativeX += NORMALIZED_WIDTH;
		}
		relativeX = relativeX % NORMALIZED_WIDTH;
	}

}
