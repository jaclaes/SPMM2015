package at.component.group.ui;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import at.component.IComponent;
import at.component.group.Activator;
import at.component.group.ComponentUiController;
import at.component.util.Connector;
import at.component.util.ImagePathPool;
import at.component.util.ImageProvider;

public class ComponentUiRunnable implements Runnable {

	protected static final int MARGIN_WIDTH = 3;
	protected static final int MARGIN_HEIGHT = 3;
	private Composite parent;
	private IComponent component;
	private ComponentUiController controller;
	protected Point originalMousePosition;
	protected Point originalComponentDecorationCompositePosition;
	private Composite componentDecorationComposite;

	public ComponentUiRunnable(ComponentUiController controller, Composite parent, IComponent component) {
		this.controller = controller;
		this.parent = parent;
		this.component = component;
	}

	private void createComponentDecorationComposite() {
		GridLayout componentDecorationGridLayout = new GridLayout();
		componentDecorationGridLayout.marginHeight = MARGIN_HEIGHT;
		componentDecorationGridLayout.marginWidth = MARGIN_WIDTH;
		componentDecorationGridLayout.verticalSpacing = 0;

		componentDecorationComposite = new Composite(parent, SWT.NONE);
		componentDecorationComposite.setLayout(componentDecorationGridLayout);
		componentDecorationComposite.setVisible(true);

		ComponentDecorationCompositeListener listener = new ComponentDecorationCompositeListener(component);
		componentDecorationComposite.addListener(SWT.Dispose, listener);
		componentDecorationComposite.addListener(SWT.Resize, listener);
		componentDecorationComposite.addListener(SWT.MouseMove, listener);
		componentDecorationComposite.addListener(SWT.Paint, listener);
		componentDecorationComposite.addListener(SWT.MouseDown, listener);
		componentDecorationComposite.addListener(SWT.MouseUp, listener);
		componentDecorationComposite.addListener(SWT.MouseExit, listener);
	}

	private void createMainComposite() {
		final Composite composite = new Composite(componentDecorationComposite, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		component.addUI(composite);
		composite.layout(true, true);
	}

	private void createTopComposite() {
		GridLayout topCompositeLayout = new GridLayout(2, false);
		topCompositeLayout.marginWidth = 0;
		topCompositeLayout.marginHeight = 0;

		final Composite topComposite = new Composite(componentDecorationComposite, SWT.NONE);
		topComposite.setLayout(topCompositeLayout);
		topComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridData titleLabelGridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		titleLabelGridData.minimumWidth = 50;

		final Label titleLabel = new Label(topComposite, SWT.NONE);
		titleLabel.setText(component.getNameWithId());
		titleLabel
				.setToolTipText("Left Mouse-button to move the component on the same component\nRight Mouse-button to drag the component to another component\nDouble-Click on the label to rename the component");
		titleLabel.setLayoutData(titleLabelGridData);

		final ToolBar toolBar = new ToolBar(topComposite, SWT.FLAT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true));

		final ToolItem shiftItem = new ToolItem(toolBar, SWT.PUSH);
		shiftItem.setImage(ImageProvider.getImage(ImagePathPool.SHIFT_COMPONENT));
		shiftItem.setToolTipText("Shift Component");
		shiftItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<IComponent> targetComponents = controller.getShiftTargetComponents(component);

				if (targetComponents.size() > 0) {
					ShiftComponentDialog dialog = new ShiftComponentDialog(topComposite.getShell(), SWT.NONE);

					Point dialogLocation = toolBar.toDisplay(shiftItem.getBounds().x, shiftItem.getBounds().y);
					dialogLocation.y = dialogLocation.y + shiftItem.getBounds().height;
					dialogLocation.x = dialogLocation.x + MARGIN_WIDTH;
					dialogLocation.y = dialogLocation.y + MARGIN_HEIGHT;

					int result = dialog.open(targetComponents, dialogLocation);

					if (result == SWT.OK) {
						controller.shiftComponent(ComponentUiRunnable.this, component, dialog.getSelectedTargetComponent(), null);
					}
				} else {
					MessageDialog
							.openInformation(componentDecorationComposite.getShell(), "Can't be shifted",
									"There is no component which could be a new parent of this component.\nTherefore the component can't be shifted");
				}
			}
		});

		final ToolItem connectItem = new ToolItem(toolBar, SWT.PUSH);
		connectItem.setImage(ImageProvider.getImage(ImagePathPool.CONNECT_COMPONENT));
		connectItem.setToolTipText("Connect Components");

		connectItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Connector.getInstance().showConnectorDialog(parent.getShell(), SWT.NONE, component);
			}
		});

		ToolItem showEventsItem = new ToolItem(toolBar, SWT.PUSH);
		showEventsItem.setImage(ImageProvider.getImage(ImagePathPool.SHOW_EVENT));
		showEventsItem.setToolTipText("Display events that are published by this component");
		showEventsItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getComponentEventService().showEventDialog(component);
			}
		});

		final ToolItem closeItem = new ToolItem(toolBar, SWT.PUSH);
		closeItem.setImage(ImageProvider.getImage(ImagePathPool.REMOVE_COMPONENT));
		closeItem.setToolTipText("Close and Unregister Component");

		closeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				componentDecorationComposite.dispose();
			}
		});

		titleLabel.addMouseListener(new MouseAdapter() {

			private DragSource titleLabelDragSource;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				InputDialog dialog = new InputDialog(componentDecorationComposite.getShell(), "Neuer Name",
						"Geben Sie einen neunen Namen für die Komponente \"" + component.getNameWithId() + "\" an!", component.getName(), new IInputValidator() {

							@Override
							public String isValid(String newText) {
								if (newText.trim().length() == 0)
									return "Der Name muss mindestens ein Zeichen enthalten!";

								return null;
							}
						});

				if (dialog.open() == Window.OK) {
					String newName = dialog.getValue();
					Activator.getComponentService().renameComponent(component, newName);
					titleLabel.setText(component.getNameWithId());
					titleLabel.getParent().layout();
				}

				originalMousePosition = null;
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Activator.dragRightClickButton = e.button;

				if (e.button == 1) {
					if (titleLabelDragSource != null && !titleLabelDragSource.isDisposed())
						titleLabelDragSource.dispose();
					componentDecorationComposite.moveAbove(null);
					originalMousePosition = new Point(e.x, e.y);
					originalComponentDecorationCompositePosition = componentDecorationComposite.getLocation();
				}

				if (e.button == 3) {
					Activator.dragRightClickSourceComponent = component;

					if (titleLabelDragSource == null || titleLabelDragSource.isDisposed()) {
						titleLabelDragSource = new DragSource(titleLabel, DND.DROP_MOVE);
						titleLabelDragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
						titleLabelDragSource.addDragListener(new DragSourceAdapter() {

							private int offsetX;
							private int offsetY;

							@Override
							public void dragFinished(DragSourceEvent event) {
								try {
									if (Activator.dragRightClickButton == 3) {
										if (Activator.dragRightClickTargetComponent != null
												&& Activator.dragRightClickTargetComponent != component.getParent()) {
											controller.shiftComponent(ComponentUiRunnable.this, component,
													Activator.dragRightClickTargetComponent, Activator.dragRightClickComponentUILocation);
										}
										Activator.resetDrag();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void dragSetData(DragSourceEvent event) {
								if (Activator.dragRightClickButton == 3)
									event.data = offsetX + "@" + offsetY;
							}

							@Override
							public void dragStart(DragSourceEvent event) {
								if (Activator.dragRightClickButton == 3) {
									offsetX = event.offsetX;
									offsetY = event.offsetY;
								}
							}
						});
					}
					Event event = new Event();
					event.button = e.button;
					event.x = e.x;
					event.y = e.y;

					titleLabel.notifyListeners(SWT.DragDetect, event);
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					originalMousePosition = null;
					positionComponentUi();
					
					if (originalComponentDecorationCompositePosition != null && (componentDecorationComposite.getLocation().x != originalComponentDecorationCompositePosition.x || componentDecorationComposite.getLocation().y != originalComponentDecorationCompositePosition.y)) {
						Activator.getComponentService().getActiveProject().setDirty(true);
					}
					
					originalComponentDecorationCompositePosition = null;
				}
			}
		});

		titleLabel.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				titleLabel.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_HAND));
			}

			@Override
			public void mouseExit(MouseEvent e) {
				titleLabel.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_ARROW));
			}
		});

		titleLabel.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (originalMousePosition == null)
					return;

				Point parentLocation = componentDecorationComposite.getLocation();
				Point newMousePosition = new Point(e.x, e.y);

				int newParentLocationX = parentLocation.x + (newMousePosition.x - originalMousePosition.x);
				int newParentLoactionY = parentLocation.y + (newMousePosition.y - originalMousePosition.y);

				if (newParentLocationX < 0 && newParentLoactionY < 0)
					return;

				if (newParentLocationX < 0)
					newParentLocationX = 0;
				if (newParentLoactionY < 0)
					newParentLoactionY = 0;

				componentDecorationComposite.setLocation(newParentLocationX, newParentLoactionY);
			}
		});

		toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void drawHorizontalLine() {
		Label horizontalLine1 = new Label(componentDecorationComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		horizontalLine1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}

	public Composite getComponentUi() {
		return componentDecorationComposite;
	}

	private void positionComponentUi() {
		Point location = componentDecorationComposite.getLocation();

		Rectangle parentCompositeBounds = componentDecorationComposite.getParent().getBounds();
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
		componentDecorationComposite.setLocation(newLocation);
	}

	@Override
	public void run() {
		// Create the surrounding composite with all it's listeners
		createComponentDecorationComposite();

		// Create the composite positioned at the top (title, connect Button, close Button ...)
		createTopComposite();

		// Draw a horizontal line
		drawHorizontalLine();

		// Create the main composite, where the UI of the component will be added
		createMainComposite();

		if (Activator.dragRightClickComponentUILocation != null)
			componentDecorationComposite.setBounds(Activator.dragRightClickComponentUILocation.x,
					Activator.dragRightClickComponentUILocation.y, 100, 100);
		else
			componentDecorationComposite.setBounds(0, 0, 100, 100);
		componentDecorationComposite.pack();

		// Set the location the size of the componentDecorationComposite (according to saved values)
		positionComponentUi();
	}

	public void setController(ComponentUiController controller) {
		this.controller = controller;
	}
	
	public void dispose() {
		if (componentDecorationComposite != null && ! componentDecorationComposite.isDisposed())
			componentDecorationComposite.dispose();
	}
}
