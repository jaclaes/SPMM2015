package hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.cheetahplatform.common.date.DateTime;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         14.07.2009
 */
@SuppressWarnings("rawtypes")
public class DateTimeUserType implements CompositeUserType {

	protected static final int TIME_INDEX = 0;
	protected static final int INCLUSIVE_INDEX = 1;

	@Override
	public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
		return deepCopy(cached);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		DateTime time = (DateTime) value;
		return new DateTime(time);
	}

	@Override
	public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
		return (Serializable) deepCopy(value);
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}

		return x.equals(y);
	}

	@Override
	public String[] getPropertyNames() {
		return new String[] { "time", "inclusive" }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Type[] getPropertyTypes() {
		return new Type[] { Hibernate.LONG, Hibernate.BOOLEAN };
	}

	@Override
	public Object getPropertyValue(Object component, int property) throws HibernateException {
		if (property == TIME_INDEX) {
			return ((DateTime) component).getTimeInMilliseconds();
		} else if (property == INCLUSIVE_INDEX) {
			return ((DateTime) component).isInclusive();
		}

		throw new HibernateException("Unmapped column index for DateTime: " + property); //$NON-NLS-1$
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException,
			SQLException {
		long time = rs.getLong(names[TIME_INDEX]);
		if (rs.wasNull()) {
			return null;
		}

		boolean inclusive = rs.getBoolean(names[INCLUSIVE_INDEX]);
		if (rs.wasNull()) {
			return null;
		}

		return new DateTime(new Date(time), inclusive);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException,
			SQLException {
		if (value == null) {
			st.setNull(index, Hibernate.LONG.sqlType());
			st.setNull(index + INCLUSIVE_INDEX, Hibernate.BOOLEAN.sqlType());
		} else {
			DateTime toPersist = (DateTime) value;
			st.setLong(index, toPersist.getTimeInMilliseconds());
			st.setBoolean(index + INCLUSIVE_INDEX, toPersist.isInclusive());
		}
	}

	@Override
	public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
		if (target == null) {
			return original;
		}

		return deepCopy(original);
	}

	@Override
	public Class returnedClass() {
		return DateTime.class;
	}

	@Override
	public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
		if (property == TIME_INDEX) {
			((DateTime) component).setTimeInMilliseconds((Long) value);
		} else if (property == INCLUSIVE_INDEX) {
			((DateTime) component).setInclusive((Boolean) value);
		} else {
			throw new HibernateException("Unmapped column index for DateTime: " + property); //$NON-NLS-1$
		}
	}
}
