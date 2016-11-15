package org.cheetahplatform.modeler.changepattern.model;

import java.util.List;

import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.06.2010
 */
public interface IGraphOptimizer {
	boolean optimize();
	List<AbstractGraphCommand> getCommands();
}