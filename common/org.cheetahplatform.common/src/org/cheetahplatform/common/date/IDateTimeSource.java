package org.cheetahplatform.common.date;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         25.06.2009
 */
public interface IDateTimeSource {
	DateTime getCurrentTime(boolean inclusive);
}
