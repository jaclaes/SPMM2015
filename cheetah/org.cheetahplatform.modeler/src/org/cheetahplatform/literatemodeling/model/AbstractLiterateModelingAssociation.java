package org.cheetahplatform.literatemodeling.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.06.2010
 */
public abstract class AbstractLiterateModelingAssociation implements ILiterateModelingAssociation {
	protected int offset;
	protected int length;

	/**
	 * @param offset
	 * @param length
	 */
	public AbstractLiterateModelingAssociation(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}

	/**
	 * @param selection
	 */
	public AbstractLiterateModelingAssociation(ITextSelection selection) {
		this(selection.getOffset(), selection.getLength());
	}

	/**
	 * @param deletedLength
	 */
	@Override
	public void decreaseLength(int deletedLength) {
		Assert.isLegal(deletedLength >= 0);
		length -= deletedLength;
	}

	/**
	 * @param deletedLength
	 */
	@Override
	public void decreaseOffset(int deletedLength) {
		Assert.isLegal(deletedLength >= 0);
		Assert.isLegal(deletedLength <= offset);
		offset -= deletedLength;
	}

	/**
	 * @return
	 */
	@Override
	public int getEnd() {
		return offset + length;
	}

	/**
	 * Returns the length.
	 * 
	 * @return the length
	 */
	@Override
	public int getLength() {
		return length;
	}

	/**
	 * Returns the offset.
	 * 
	 * @return the offset
	 */
	@Override
	public int getOffset() {
		return offset;
	}

	/**
	 * @param color
	 * @return
	 */
	@Override
	public StyleRange getStyleRange(Color foreground, Color background) {
		return getStyleRange(foreground, background, SWT.NORMAL);
	}

	/**
	 * @param color
	 * @return
	 */
	@Override
	public StyleRange getStyleRange(Color foreground, Color background, int style) {
		return new StyleRange(offset, length, foreground, background, style);
	}

	/**
	 * 
	 */
	@Override
	public void increaseLength(int toIncrease) {
		Assert.isLegal(toIncrease >= 0);
		length += toIncrease;
	}

	/**
	 * @param insertedLength
	 */
	@Override
	public void increaseOffset(int insertedLength) {
		Assert.isLegal(insertedLength >= 0);
		offset += insertedLength;
	}

	/**
	 * @param toCheck
	 * @return
	 */
	@Override
	public boolean isAfter(int toCheck) {
		return toCheck >= offset + length;
	}

	/**
	 * @param offsetToCheck
	 * @return
	 */
	@Override
	public boolean isBefore(int offsetToCheck) {
		return offset > offsetToCheck;
	}
}