/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_LAYOUT;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_MOVE_TO_TOP;
import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_STATE;

import java.beans.PropertyChangeEvent;
import java.util.List;

import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.MessageLookup;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.AbstractActivity;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.action.CancelWeeklyActivityAction;
import org.cheetahplatform.tdm.weekly.action.CompleteWeeklyActivityAction;
import org.cheetahplatform.tdm.weekly.action.EditMessageAction;
import org.cheetahplatform.tdm.weekly.action.ExecuteWeeklyActivityAction;
import org.cheetahplatform.tdm.weekly.action.SkipWeeklyActivityAction;
import org.cheetahplatform.tdm.weekly.policy.MoveWeeklyActivityEditPolicy;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class WeeklyActivityEditPart extends GenericEditPart implements NodeEditPart {

	public static final int HEIGHT = 40;
	public static final int SPACING_Y = 15;
	public static final int WIDTH = 150;
	public static final int SPACING_X = 10;

	public WeeklyActivityEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (((IStructuredSelection) getViewer().getSelectionManager().getSelection()).size() > 1) {
			return; // disable context menu for multi selection
		}

		WeeklyActivity model = (WeeklyActivity) getModel();
		menu.add(new ExecuteWeeklyActivityAction(model));
		CancelWeeklyActivityAction action = new CancelWeeklyActivityAction(model);
		if (action.isEnabled()) {
			menu.add(action); // add only if enabled to avoid confusion
		}
		menu.add(new SkipWeeklyActivityAction(model));
		menu.add(new CompleteWeeklyActivityAction(model));
		menu.add(new EditMessageAction(model));
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MoveWeeklyActivityEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		WeeklyActivity model = (WeeklyActivity) getModel();
		String label = model.getLabel();
		String toolTip = model.getToolTip();

		ActivityFigure figure = new ActivityFigure(label, toolTip);
		figure.setBackgroundColor(getBackgroundColor());
		figure.setState(((AbstractActivity) getModel()).getActivity().getState());
		figure.setMessage(MessageLookup.getInstance().getMessage(model.getActivity()));

		return figure;
	}

	private ConnectionAnchor getAnchor() {
		return new ChopboxAnchor(figure);
	}

	private Color getBackgroundColor() {
		RGB rgb = ((AbstractActivity) getModel()).getBackgroundColor();
		return SWTResourceManager.getColor(rgb);
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getAnchor();
	}

	@SuppressWarnings("unchecked")
	public void moveToTop() {
		List children = figure.getParent().getChildren();
		children.remove(figure);
		children.add(Math.max(0, children.size()), figure);
	}

	@Override
	public void performRequest(Request req) {
		super.performRequest(req);

		if (REQ_OPEN.equals(req.getType())) {
			EditMessageAction action = new EditMessageAction((WeeklyActivity) getModel());
			action.run();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		WeeklyActivity model = (WeeklyActivity) getModel();

		String property = evt.getPropertyName();
		if (PROPERTY_LAYOUT.equals(property)) {
			figure.revalidate();
		} else if (PROPERTY_MOVE_TO_TOP.equals(property)) {
			moveToTop();
		} else if (PROPERTY_STATE.equals(property)) {
			updateBackgroundColor();
			updateFigureVisibility(model);
		} else if (TDMConstants.PROPERTY_MESSAGE.equals(property)) {
			((ActivityFigure) getFigure()).setMessage(model.getMessage());
			refresh();
			updateBackgroundColor();
		}
	}

	private void updateBackgroundColor() {
		figure.setBackgroundColor(getBackgroundColor());
		((ActivityFigure) figure).setState(((AbstractActivity) getModel()).getActivity().getState());
	}

	private void updateFigureVisibility(WeeklyActivity model) {
		if (!model.isVisible()) {
			getFigure().setVisible(false);
			setSelected(SELECTED_NONE);

			for (Object source : getSourceConnections()) {
				((GraphicalEditPart) source).getFigure().setVisible(false);
			}
			for (Object source : getTargetConnections()) {
				((GraphicalEditPart) source).getFigure().setVisible(false);
			}
		}
	}
}
