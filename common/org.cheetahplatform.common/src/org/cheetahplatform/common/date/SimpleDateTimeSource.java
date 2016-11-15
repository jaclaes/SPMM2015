package org.cheetahplatform.common.date;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         25.06.2009
 */
public class SimpleDateTimeSource implements IDateTimeSource {
	@Override
	public DateTime getCurrentTime(boolean inclusive) {
		return new DateTime(new Date(), inclusive);
	}

}
