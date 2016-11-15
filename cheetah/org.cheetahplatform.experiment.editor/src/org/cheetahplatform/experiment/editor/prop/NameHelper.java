package org.cheetahplatform.experiment.editor.prop;

public class NameHelper {
	private static int counter = 0;

	public String next(String name){
		return name + " " + counter++;
	}
}
