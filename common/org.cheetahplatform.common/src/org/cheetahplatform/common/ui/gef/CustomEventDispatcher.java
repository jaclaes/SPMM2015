package org.cheetahplatform.common.ui.gef;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolTipHelper;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

/**
 * Subclassed in order to be able to use a custom tool tip.
 * 
 * @author Stefan Zugal
 * 
 */
public class CustomEventDispatcher extends DomainEventDispatcher {
	private CustomToolTipHelper toolTipHelper;

	private Figure hoverSource;

	public CustomEventDispatcher(EditDomain d, EditPartViewer v) {
		super(d, v);
	}

	@Override
	protected ToolTipHelper getToolTipHelper() {
		if (toolTipHelper == null)
			toolTipHelper = new CustomToolTipHelper(control);
		return toolTipHelper;
	}

	@Override
	public void setControl(Control c) {
		super.setControl(c);

		if (c != null)
			c.addDisposeListener(new org.eclipse.swt.events.DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (toolTipHelper != null)
						toolTipHelper.dispose();
				}
			});
	}

	@Override
	protected void setHoverSource(Figure figure, MouseEvent me) {
		super.setHoverSource(figure, me);
		this.hoverSource = figure;

		IFigure currentToolTip = null;
		if (hoverSource != null) {
			currentToolTip = hoverSource.getToolTip();
		}

		if (figure != null && toolTipHelper != null) {
			// Update with null to clear hoverSource in ToolTipHelper
			toolTipHelper.updateToolTip(hoverSource, currentToolTip, me.x, me.y);
		}
	}
}
