package at.component.group.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.component.ComponentConstants;
import at.component.IComponent;
import at.component.group.Activator;
import at.component.group.ComponentUiController;
import at.component.group.Group;

public class GroupUI {

	private class WorkingAreaPaintListener implements PaintListener {

		@Override
		public void paintControl(PaintEvent e) {
			Composite drawingComposite = (Composite) e.widget;

			GC gc = e.gc;
			Rectangle bounds = drawingComposite.getBounds();

			gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

			int currentX = 0;
			int currentY = 0;

			int height = bounds.y + bounds.height;

			if (bounds.y < 0)
				height = (bounds.y * -1) + bounds.height;

			int width = bounds.x + bounds.width;

			if (bounds.x < 0)
				width = (bounds.x * -1) + bounds.width;

			while (currentX < (bounds.width)) {
				gc.drawLine(currentX, bounds.y, currentX, height);
				currentX = currentX + ComponentUiController.GRID_LINE_SPACING;
			}

			while (currentY < (bounds.height)) {
				gc.drawLine(bounds.x, currentY, width, currentY);
				currentY = currentY + ComponentUiController.GRID_LINE_SPACING;
			}

			adjustScrolledComposite();
		}
	}

	private ComponentUiController controller;
	private Composite groupComposite;
	private Composite composite;

	private Group group;
	private ScrolledComposite scrolledComposite;

	public GroupUI(Composite composite, ComponentUiController uiController, Group group) {
		this.composite = composite;
		controller = uiController;
		this.group = group;
		createUI(composite);
	}

	public void addNewComponentUI(IComponent component) {
		try {
			ComponentUiRunnable runnable = new ComponentUiRunnable(controller, groupComposite, component);
			controller.addComponentUiRunnable(component.getComponentId(), runnable);
			composite.getShell().getDisplay().syncExec(runnable);
		} catch (Exception e) {
			Activator.getComponentLogService().logError(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void addShiftedComponentUI(Composite childComponentUI, IComponent component) {
		if (childComponentUI != null) {
			childComponentUI.setParent(groupComposite);
			if (Activator.dragRightClickComponentUILocation != null)
				childComponentUI.setLocation(Activator.dragRightClickComponentUILocation.x, Activator.dragRightClickComponentUILocation.y);
			else
				childComponentUI.setLocation(0, 0);

			positionComponentUi(childComponentUI, component);
			childComponentUI.moveAbove(null);

			adjustScrolledComposite();
		}
	}

	private void adjustScrolledComposite() {
		if (scrolledComposite != null && !scrolledComposite.isDisposed()) {
			scrolledComposite.setMinSize(groupComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}

	private void createUI(Composite composite) {
		FillLayout compositeLayout = new FillLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;

		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

		scrolledComposite = new ScrolledComposite(composite, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);

		groupComposite = new Composite(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(groupComposite);
		groupComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		groupComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupComposite.addPaintListener(new WorkingAreaPaintListener());
		groupComposite.setData(ComponentConstants.COMPONENT_ID, group.getComponentId());
		groupComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlMoved(ControlEvent e) {
				groupComposite.redraw();
			}
		});

		scrolledComposite.setMinSize(groupComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);

		DropTarget dropTarget = new DropTarget(groupComposite, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			@Override
			public void dragOver(DropTargetEvent event) {
				try {
					if (Activator.dragRightClickButton == 3) {
						if (Activator.dragRightClickSourceComponent == null || Activator.dragRightClickSourceComponent.getParent() == group
								|| Activator.dragRightClickSourceComponent == group)
							event.detail = DND.DROP_NONE;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void drop(DropTargetEvent event) {
				try {
					if (Activator.dragRightClickButton == 3) {
						Activator.dragRightClickTargetComponent = group;
						Point relativeGroupCompositeLocation = groupComposite.toDisplay(groupComposite.getLocation());

						Activator.dragRightClickComponentUILocation = controller.calculateComponentLocation((String) event.data, new Point(
								event.x - relativeGroupCompositeLocation.x, event.y - relativeGroupCompositeLocation.y));
					} else {
						Point relativeGroupCompositeLocation = groupComposite.toDisplay(groupComposite.getLocation());

						Activator.dragRightClickComponentUILocation = controller.calculateComponentLocation((String) event.data, new Point(
								event.x - relativeGroupCompositeLocation.x, event.y - relativeGroupCompositeLocation.y));

						controller.startComponentOrProject((String) event.data, new Rectangle(event.x - relativeGroupCompositeLocation.x,
								event.y - relativeGroupCompositeLocation.y, SWT.DEFAULT, SWT.DEFAULT));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Composite getGroupComposite() {
		return groupComposite;
	}

	public Shell getShell() {
		return composite.getShell();
	}

	private void positionComponentUi(Composite childComponentUI, IComponent component) {
		Point location = childComponentUI.getLocation();

		Rectangle parentCompositeBounds = childComponentUI.getParent().getBounds();
		int currentX = parentCompositeBounds.x;
		int currentY = parentCompositeBounds.y;
		int newLocationX = 0;
		int newLocationY = 0;
		while (currentX < (parentCompositeBounds.width)) {
			if ((location.x >= (currentX - ComponentUiController.GRID_LINE_SPACING / 2))
					&& (location.x <= (currentX + ComponentUiController.GRID_LINE_SPACING / 2))) {
				newLocationX = currentX;
				break;
			}
			currentX = currentX + ComponentUiController.GRID_LINE_SPACING;
		}

		while (currentY < (parentCompositeBounds.height)) {
			if ((location.y >= (currentY - ComponentUiController.GRID_LINE_SPACING / 2))
					&& (location.y <= (currentY + ComponentUiController.GRID_LINE_SPACING / 2))) {
				newLocationY = currentY;
				break;
			}
			currentY = currentY + ComponentUiController.GRID_LINE_SPACING;
		}

		Point newLocation = new Point(newLocationX, newLocationY);
		childComponentUI.setLocation(newLocation);
	}
}
