package org.cheetahplatform.modeler.generic;

import org.cheetahplatform.common.ui.gef.ScrollingGraphicalViewerWithForeignSupport;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

public class GraphicalViewerWithFlyoutPalette {

	protected PaletteRoot palette;
	protected ScrollingGraphicalViewer viewer;

	public GraphicalViewerWithFlyoutPalette(Composite parent) {
		this(parent, FlyoutPaletteComposite.STATE_PINNED_OPEN);
	}

	public GraphicalViewerWithFlyoutPalette(Composite parent, int initialFlyoutPaletteState) {
		createViewer(parent, initialFlyoutPaletteState);
	}

	protected EditDomain createEditDomain() {
		return new EditDomain();
	}

	/**
	 * Create the palette - subclasses may extend.
	 */
	protected void createPalette() {
		palette = new PaletteRoot();
		PaletteGroup group = new PaletteGroup("Tools");
		palette.add(group);

		PanningSelectionToolEntry selectionTool = new PanningSelectionToolEntry();
		group.add(selectionTool);
		palette.setDefaultEntry(selectionTool);
	}

	@SuppressWarnings("deprecation")
	protected void createViewer(Composite parent, int initialFlyoutPaletteState) {
		EditDomain domain = createEditDomain();
		domain.setPaletteRoot(getPalette());

		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		PaletteViewerProvider provider = new PaletteViewerProvider(domain);
		FlyoutPreferences preferences = FlyoutPaletteComposite.createFlyoutPreferences(Activator.getDefault().getPluginPreferences());
		preferences.setPaletteState(initialFlyoutPaletteState);

		FlyoutPaletteComposite flyoutPalette = new FlyoutPaletteComposite(parent, SWT.NONE, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage(), provider, preferences);
		flyoutPalette.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer = new ScrollingGraphicalViewerWithForeignSupport();

		Control control = viewer.createControl(flyoutPalette);
		control.setData(ScrollingGraphicalViewerWithForeignSupport.GRAPHICAL_VIEWER, viewer);
		viewer.setEditDomain(domain);
		viewer.setEditPartFactory(new GenericEditPartFactory());
		viewer.setContextMenu(new GenericMenuManager(viewer));

		flyoutPalette.setGraphicalControl(viewer.getControl());
		flyoutPalette.hookDropTargetListener(viewer);
	}

	/**
	 * Creates and returns the palette.
	 * 
	 * @return the palette
	 */
	public PaletteRoot getPalette() {
		if (palette == null) {
			createPalette();
		}

		return palette;
	}

	public ScrollingGraphicalViewer getViewer() {
		return viewer;
	}

}
