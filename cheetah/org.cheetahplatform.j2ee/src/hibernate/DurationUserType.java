package hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         14.07.2009
 */
public class DurationUserType extends DateTimeUserType {
	@Override
	public Object deepCopy(Object value) throws HibernateException {
		Duration duration = (Duration) value;
		return new Duration(duration);
	}

	@Override
	public Object getPropertyValue(Object component, int property) throws HibernateException {
		if (property == TIME_INDEX) {
			return ((Duration) component).getTimeInMilliseconds();
		} else if (property == INCLUSIVE_INDEX) {
			return ((Duration) component).isInclusive();
		}

		throw new HibernateException("Unmapped column index for DateTime: " + property); //$NON-NLS-1$
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException,
			SQLException {
		DateTime dateTime = (DateTime) super.nullSafeGet(rs, names, session, owner);
		return new Duration(dateTime);
	}

	@Override
	public Class returnedClass() {
		return DurationUserType.class;
	}
}
