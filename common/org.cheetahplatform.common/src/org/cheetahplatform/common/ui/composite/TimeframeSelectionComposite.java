/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.common.ui.composite;

import org.cheetahplatform.common.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;

public class TimeframeSelectionComposite extends Composite {

	private final DateTime startingDate;
	private final DateTime startingTime;
	private final DateTime finishTime;
	private final DateTime finishDate;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public TimeframeSelectionComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		setLayout(layout);
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final Group fromGroup = new Group(this, SWT.NONE);
		fromGroup.setText(Messages.TimeframeSelectionComposite_0);
		final GridData gd_fromGroup = new GridData(SWT.FILL, SWT.FILL, true, false);
		fromGroup.setLayoutData(gd_fromGroup);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		fromGroup.setLayout(gridLayout);
		startingDate = new DateTime(fromGroup, SWT.CALENDAR | SWT.BORDER);
		startingDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		startingTime = new DateTime(fromGroup, SWT.TIME | SWT.BORDER);
		startingTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Group toGroup = new Group(this, SWT.NONE);
		toGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		toGroup.setText(Messages.TimeframeSelectionComposite_1);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 1;
		toGroup.setLayout(gridLayout_1);

		finishDate = new DateTime(toGroup, SWT.CALENDAR | SWT.BORDER);
		finishDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		finishTime = new DateTime(toGroup, SWT.TIME | SWT.BORDER);
		finishTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Returns the finishDate.
	 * 
	 * @return the finishDate
	 */
	public DateTime getFinishDate() {
		return finishDate;
	}

	/**
	 * Returns the finishTime.
	 * 
	 * @return the finishTime
	 */
	public DateTime getFinishTime() {
		return finishTime;
	}

	/**
	 * Returns the startingDate.
	 * 
	 * @return the startingDate
	 */
	public DateTime getStartingDate() {
		return startingDate;
	}

	/**
	 * Returns the startingTime.
	 * 
	 * @return the startingTime
	 */
	public DateTime getStartingTime() {
		return startingTime;
	}

}
