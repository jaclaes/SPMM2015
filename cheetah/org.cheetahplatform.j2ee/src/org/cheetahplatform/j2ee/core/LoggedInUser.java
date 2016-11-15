package org.cheetahplatform.j2ee.core;

import java.io.Serializable;
import java.util.Date;

/**
 * A class representing a logged in user
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class LoggedInUser implements Serializable {
	private static final long serialVersionUID = 16608004687474547L;

	private String name;
	private String sessionId;
	private Date loginTime;

	public LoggedInUser(String name, String sessionId, Date loginTime) {
		this.name = name;
		this.sessionId = sessionId;
		this.loginTime = loginTime;
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
}