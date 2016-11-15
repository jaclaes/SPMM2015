/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;

public class MessageLookup {
	private static MessageLookup INSTANCE;

	public static MessageLookup getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MessageLookup();
		}

		return INSTANCE;
	}

	private Map<DeclarativeActivityInstance, String> messages;

	private MessageLookup() {
		this.messages = new HashMap<DeclarativeActivityInstance, String>();
	}

	public void assignMessage(DeclarativeActivityInstance activity, String message) {
		messages.put(activity, message);
	}

	public Map<DeclarativeActivityInstance, String> getAllMessages() {
		return Collections.unmodifiableMap(messages);
	}

	public String getMessage(DeclarativeActivityInstance activity) {
		return messages.get(activity);
	}

}
