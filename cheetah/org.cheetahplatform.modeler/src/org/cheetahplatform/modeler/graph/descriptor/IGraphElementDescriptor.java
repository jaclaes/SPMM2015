package org.cheetahplatform.modeler.graph.descriptor;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Image;

public interface IGraphElementDescriptor {
	/**
	 * Add a delegate, i.e., all calls are first processed by the descriptor object and then forwarded to the delegates.
	 * 
	 * @param delegate
	 *            the delegate
	 */
	void addDelegate(IGraphElementDescriptor delegate);

	/**
	 * Enrich the context menu with descriptor specific actions.
	 * 
	 * @param editPart
	 *            the edit part for that the menu should be built
	 * 
	 * @param menu
	 *            the menu
	 * @param dropLocation
	 *            the point at which the menu occurs
	 */
	void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation);

	/**
	 * Called before the creation command is executed. Allows to perform additional actions, e.g., prompting for input.
	 * 
	 * @param command
	 *            the command to be executed
	 * @param element
	 *            the element for which the command is performed
	 * @return <code>true</code> if the command is allowed to be executed, <code>false</code> if not
	 */
	boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element);

	/**
	 * Create the command for the given audit trail entry.
	 * 
	 * @param entry
	 *            the entry
	 * @param graph
	 *            the graph
	 * @return the corresponding command, <code>null</code> if the entry does not represent a command
	 */
	AbstractGraphCommand createCommand(AuditTrailEntry entry, Graph graph);

	/**
	 * Install the edit policies for the given edit part.
	 * 
	 * @param editPart
	 *            the edit part for which the policies should be installed
	 */
	void createEditPolicies(EditPart editPart);

	/**
	 * Create the figure for given element. When implementing, also consider {@link INodeDescriptor#updateName(IFigure, String)}, if
	 * applicable.
	 * 
	 * @param element
	 *            the element
	 * @return the figure
	 */
	IGraphElementFigure createFigure(GraphElement element);

	/**
	 * Same as for {@link #createModel(Graph)}, but allows to assign an id to the model.-
	 * 
	 * @param graph
	 *            the graph the node belongs to
	 * @param id
	 *            the id
	 * 
	 * @return a new instance of the described element
	 */
	GraphElement createModel(Graph graph);

	/**
	 * Create the corresponding graph element.
	 * 
	 * @param graph
	 *            the graph the node belongs to
	 * @param id
	 * 
	 * @return a new instance of the described element
	 */
	GraphElement createModel(Graph graph, long id, AuditTrailEntry entry);

	IInputValidator createRenameValidator(GraphElement element);

	/**
	 * Some application has dropped an event on the edit part.
	 * 
	 * @param event
	 *            the event
	 * @param targetEditPart
	 *            the edit part
	 */
	void dropped(DropTargetEvent event, AbstractGraphicalEditPart targetEditPart);

	/**
	 * Compute the command for the given request on given edit part.
	 * 
	 * @param editPart
	 *            the edit part for which the request has been issued
	 * @param request
	 *            the request
	 * @return the corresponding command, <code>null</code> if the request cannot be answered
	 */
	Command getCommand(EditPart editPart, Request request);

	/**
	 * Create the command label for the given command.
	 * 
	 * @param entry
	 *            the entry
	 * @return the label, "" if no label can be provided
	 */
	String getCommandLabel(AuditTrailEntry entry);

	/**
	 * Returns the icon describing the element, e.g., in context menus or palette. The image is not disposed by the framework.
	 * 
	 * @return the icon
	 */
	Image getIcon();

	/**
	 * Returns an image descriptor describing the element's icon.
	 * 
	 * @return the descriptor
	 */
	ImageDescriptor getIconDescriptor();

	/**
	 * Return a unique id identifying this descriptor.
	 * 
	 * @return an unique id (within the provided descriptors)
	 */
	String getId();

	/**
	 * Return the name describing the element.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Determine whether this element can have a custom name. E.g., an activity usually has a special name, while connectors usually don't
	 * have a name.
	 * 
	 * @return <code>true</code> if this element can have a custom name, <code>false</code> if not
	 */
	boolean hasCustomName();

	/**
	 * A property change event for the given edit part has been issued.
	 * 
	 * @param editPart
	 *            the edit part
	 * @param event
	 *            the event
	 */
	void propertyChanged(EditPart editPart, PropertyChangeEvent event);

	/**
	 * Remove a delegate.
	 * 
	 * @param delegate
	 *            the delegate to be removed
	 */
	void removeDelegate(IGraphElementDescriptor delegate);

}
