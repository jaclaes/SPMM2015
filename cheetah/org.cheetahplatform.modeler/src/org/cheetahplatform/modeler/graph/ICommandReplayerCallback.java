package org.cheetahplatform.modeler.graph;

public interface ICommandReplayerCallback {
	void processed(CommandDelegate command, boolean last);
}
