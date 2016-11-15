package org.cheetahplatform.modeler.graph.policy;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.command.CreateEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.BendpointCreationHandle;
import org.eclipse.gef.handles.BendpointMoveHandle;
import org.eclipse.gef.requests.BendpointRequest;

@SuppressWarnings({ "unchecked", "rawtypes" })
// copied most of it from BendPointEditPolicy to fix a minor bug
public class EdgeBendPointEditPolicy extends BendpointEditPolicy {

	private static final List NULL_CONSTRAINT = new ArrayList();

	private List originalConstraint;

	private boolean isDeleting = false;

	private static final Point ref1 = new Point();

	private static final Point ref2 = new Point();

	private List createHandlesForAutomaticBendpoints() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		for (int i = 0; i < points.size() - 1; i++)
			list.add(new BendpointCreationHandle(connEP, 0, i));

		return list;
	}

	private List createHandlesForUserBendpoints() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		List bendPoints = (List) getConnection().getRoutingConstraint();
		int bendPointIndex = 0;
		Point currBendPoint = null;

		if (bendPoints == null)
			bendPoints = NULL_CONSTRAINT;
		else if (!bendPoints.isEmpty())
			currBendPoint = ((Bendpoint) bendPoints.get(0)).getLocation();

		for (int i = 0; i < points.size() - 1; i++) {
			// Put a create handle on the middle of every segment
			list.add(new BendpointCreationHandle(connEP, bendPointIndex, i));

			// If the current user bendpoint matches a bend location, show a move handle
			if (i < points.size() - 1 && bendPointIndex < bendPoints.size() && currBendPoint.equals(points.getPoint(i + 1))) {
				list.add(new BendpointMoveHandle(connEP, bendPointIndex, i + 1));

				// Go to the next user bendpoint
				bendPointIndex++;
				if (bendPointIndex < bendPoints.size())
					currBendPoint = ((Bendpoint) bendPoints.get(bendPointIndex)).getLocation();
			}
		}

		return list;
	}

	/**
	 * Creates selection handles for the bendpoints. Explicit (user-defined) bendpoints will have {@link BendpointMoveHandle}s on them with
	 * a single {@link BendpointCreationHandle} between 2 consecutive explicit bendpoints. If implicit bendpoints (such as those created by
	 * the {@link AutomaticRouter}) are used, one {@link BendpointCreationHandle} is placed in the middle of the Connection.
	 * 
	 * @see SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	@Override
	protected List createSelectionHandles() {
		List list = new ArrayList();
		if (isAutomaticallyBending())
			list = createHandlesForAutomaticBendpoints();
		else
			list = createHandlesForUserBendpoints();
		return list;
	}

	/**
	 * Erases all bendpoint feedback. Since the original <code>Connection</code> figure is used for feedback, we just restore the original
	 * constraint that was saved before feedback started to show.
	 * 
	 * @param request
	 *            the BendpointRequest
	 */
	@Override
	protected void eraseConnectionFeedback(BendpointRequest request) {
		restoreOriginalConstraint();
		originalConstraint = null;
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType()) || REQ_CREATE_BENDPOINT.equals(request.getType()))
			eraseConnectionFeedback((BendpointRequest) request);
	}

	/**
	 * Factors the Request into either a MOVE, a DELETE, or a CREATE of a bendpoint.
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	@Override
	public Command getCommand(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
			if (isDeleting)
				return getDeleteBendpointCommand((BendpointRequest) request);
			return getMoveBendpointCommand((BendpointRequest) request);
		}
		if (REQ_CREATE_BENDPOINT.equals(request.getType()))
			return getCreateBendpointCommand((BendpointRequest) request);
		return null;
	}

	/**
	 * Convenience method for obtaining the host's <code>Connection</code> figure.
	 * 
	 * @return the Connection figure
	 */
	@Override
	protected Connection getConnection() {
		return (Connection) ((ConnectionEditPart) getHost()).getFigure();
	}

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		Point location = request.getLocation().getCopy();
		((GraphicalEditPart) getHost()).getFigure().translateToRelative(location);
		return new CreateEdgeBendPointCommand((Edge) request.getSource().getModel(), location, request.getIndex());
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		return new DeleteEdgeBendPointCommand((Edge) request.getSource().getModel(), request.getIndex());
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		Point location = request.getLocation().getCopy();
		((GraphicalEditPart) getHost()).getFigure().translateToRelative(location);
		Edge edge = (Edge) request.getSource().getModel();
		return new MoveEdgeBendPointCommand(edge, request.getIndex(), location);
	}

	private boolean isAutomaticallyBending() {
		List constraint = (List) getConnection().getRoutingConstraint();
		PointList points = getConnection().getPoints();
		return ((points.size() > 2) && (constraint == null || constraint.isEmpty()));
	}

	private boolean lineContainsPoint(Point p1, Point p2, Point p) {
		int tolerance = 7;
		Rectangle rect = Rectangle.SINGLETON;
		rect.setSize(0, 0);
		rect.setLocation(p1.x, p1.y);
		rect.union(p2.x, p2.y);
		rect.expand(tolerance, tolerance);
		if (!rect.contains(p.x, p.y))
			return false;

		int v1x, v1y, v2x, v2y;
		int numerator, denominator;
		double result = 0.0;

		if (p1.x != p2.x && p1.y != p2.y) {

			v1x = p2.x - p1.x;
			v1y = p2.y - p1.y;
			v2x = p.x - p1.x;
			v2y = p.y - p1.y;

			numerator = v2x * v1y - v1x * v2y;
			denominator = v1x * v1x + v1y * v1y;

			result = ((numerator << 10) / denominator * numerator) >> 10;
		}

		// if it is the same point, and it passes the bounding box test,
		// the result is always true.
		return result <= tolerance * tolerance;
	}

	/**
	 * If the number of bendpoints changes, handles are updated.
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// $TODO optimize so that handles aren't added constantly.
		if (getHost().getSelected() != EditPart.SELECTED_NONE)
			addSelectionHandles();
	}

	/**
	 * Restores the original constraint that was saved before feedback began to show.
	 */
	@Override
	protected void restoreOriginalConstraint() {
		if (originalConstraint != null) {
			if (originalConstraint == NULL_CONSTRAINT)
				getConnection().setRoutingConstraint(null);
			else
				getConnection().setRoutingConstraint(originalConstraint);
		}
	}

	/**
	 * Since the original figure is used for feedback, this method saves the original constraint, so that is can be restored when the
	 * feedback is erased.
	 */
	@Override
	protected void saveOriginalConstraint() {
		originalConstraint = (List) getConnection().getRoutingConstraint();
		if (originalConstraint == null)
			originalConstraint = NULL_CONSTRAINT;
		getConnection().setRoutingConstraint(new ArrayList(originalConstraint));
	}

	private void setReferencePoints(BendpointRequest request) {
		PointList points = getConnection().getPoints();
		int bpIndex = -1;
		List bendPoints = (List) getConnection().getRoutingConstraint();
		Point bp = ((Bendpoint) bendPoints.get(request.getIndex())).getLocation();

		int smallestDistance = -1;

		for (int i = 0; i < points.size(); i++) {
			if (smallestDistance == -1 || points.getPoint(i).getDistance2(bp) < smallestDistance) {
				bpIndex = i;
				smallestDistance = points.getPoint(i).getDistance2(bp);
				if (smallestDistance == 0)
					break;
			}
		}

		// BUG FIX BEGIN
		if (bpIndex == 0 || bpIndex == (points.size() - 1)) {
			points.getPoint(ref1, 0);
			getConnection().translateToAbsolute(ref1);
			points.getPoint(ref2, (points.size() - 1));
			getConnection().translateToAbsolute(ref2);
		} else { // BUG FIX END
			points.getPoint(ref1, bpIndex - 1);
			getConnection().translateToAbsolute(ref1);
			points.getPoint(ref2, bpIndex + 1);
			getConnection().translateToAbsolute(ref2);
		}
	}

	/**
	 * Shows feedback when a bendpoint is being created. The original figure is used for feedback and the original constraint is saved, so
	 * that it can be restored when feedback is erased.
	 * 
	 * @param request
	 *            the BendpointRequest
	 */
	@Override
	protected void showCreateBendpointFeedback(BendpointRequest request) {
		Point p = new Point(request.getLocation());
		List constraint;
		getConnection().translateToRelative(p);
		Bendpoint bp = new AbsoluteBendpoint(p);
		if (originalConstraint == null) {
			saveOriginalConstraint();
			constraint = (List) getConnection().getRoutingConstraint();
			constraint.add(request.getIndex(), bp);
		} else {
			constraint = (List) getConnection().getRoutingConstraint();
		}
		constraint.set(request.getIndex(), bp);
		getConnection().setRoutingConstraint(constraint);
	}

	/**
	 * Shows feedback when a bendpoint is being deleted. This method is only called once when the bendpoint is first deleted, not every
	 * mouse move. The original figure is used for feedback and the original constraint is saved, so that it can be restored when feedback
	 * is erased.
	 * 
	 * @param request
	 *            the BendpointRequest
	 */
	@Override
	protected void showDeleteBendpointFeedback(BendpointRequest request) {
		if (originalConstraint == null) {
			saveOriginalConstraint();
			List constraint = (List) getConnection().getRoutingConstraint();
			constraint.remove(request.getIndex());
			getConnection().setRoutingConstraint(constraint);
		}
	}

	/**
	 * Shows feedback when a bendpoint is being moved. Also checks to see if the bendpoint should be deleted and then calls
	 * {@link #showDeleteBendpointFeedback(BendpointRequest)} if needed. The original figure is used for feedback and the original
	 * constraint is saved, so that it can be restored when feedback is erased.
	 * 
	 * @param request
	 *            the BendpointRequest
	 */
	@Override
	protected void showMoveBendpointFeedback(BendpointRequest request) {
		Point p = new Point(request.getLocation());
		if (!isDeleting)
			setReferencePoints(request);

		if (lineContainsPoint(ref1, ref2, p)) {
			if (!isDeleting) {
				isDeleting = true;
				eraseSourceFeedback(request);
				showDeleteBendpointFeedback(request);
			}
			return;
		}
		if (isDeleting) {
			isDeleting = false;
			eraseSourceFeedback(request);
		}
		if (originalConstraint == null)
			saveOriginalConstraint();
		List constraint = (List) getConnection().getRoutingConstraint();
		getConnection().translateToRelative(p);
		Bendpoint bp = new AbsoluteBendpoint(p);
		constraint.set(request.getIndex(), bp);
		getConnection().setRoutingConstraint(constraint);
	}

	/**
	 * Shows feedback when appropriate. Calls a different method depending on the request type.
	 * 
	 * @see #showCreateBendpointFeedback(BendpointRequest)
	 * @see #showMoveBendpointFeedback(BendpointRequest)
	 * @param request
	 *            the Request
	 */
	@Override
	public void showSourceFeedback(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType()))
			showMoveBendpointFeedback((BendpointRequest) request);
		else if (REQ_CREATE_BENDPOINT.equals(request.getType()))
			showCreateBendpointFeedback((BendpointRequest) request);
	}

}
