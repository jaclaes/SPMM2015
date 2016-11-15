/**
 * 
 */
package org.cheetahplatform.common.date;


/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         25.06.2009
 */
public class DateTimeProvider {
	private static IDateTimeSource dateTimeSource = new SimpleDateTimeSource();

	/**
	 * Returns the dateTimeSource.
	 * 
	 * @return the dateTimeSource
	 */
	public static IDateTimeSource getDateTimeSource() {
		return dateTimeSource;
	}

	/**
	 * Sets the dateTimeSource.
	 * 
	 * @param dateTimeSource
	 *            the dateTimeSource to set
	 */
	public static void setDateTimeSource(IDateTimeSource dateTimeSource) {
		DateTimeProvider.dateTimeSource = dateTimeSource;
	}

}
