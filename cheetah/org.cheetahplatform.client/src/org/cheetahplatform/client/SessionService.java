package org.cheetahplatform.client;

public class SessionService {
	private static String SESSION_ID;

	/**
	 * Returns the sessionId.
	 * 
	 * @return the sessionId
	 */
	public static String getSessionId() {
		return SESSION_ID;
	}

	/**
	 * Sets the sessionId.
	 * 
	 * @param sessionId
	 *            the sessionId to set
	 */
	public static void setSessionId(String sessionId) {
		SessionService.SESSION_ID = sessionId;
	}
}
