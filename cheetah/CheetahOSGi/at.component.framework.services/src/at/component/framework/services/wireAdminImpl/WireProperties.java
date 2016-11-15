package at.component.framework.services.wireAdminImpl;

import java.util.Hashtable;

/**
 * @author Pavlin Dobrev
 * @version 1.0
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
class WireProperties extends Hashtable {

	static final long serialVersionUID = -8836718065933570367L;

	@Override
	public synchronized Object put(Object key, Object value) {
		throw new RuntimeException("Unsupported operation");
	}

	Object put0(Object key, Object value) {
		return super.put(key, value);
	}

	@Override
	public synchronized Object remove(Object key) {
		throw new RuntimeException("Unsupported operation");
	}

	Object remove0(Object key) {
		return super.remove(key);
	}
}
