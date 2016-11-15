package org.cheetahplatform.modeler.dialog;

import java.util.List;

public interface IDemonstratable {

	String getDescription();

	List<IDemonstratable> getChildren();

	String getName();

}
