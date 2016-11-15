package at.component.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import at.component.IComponent;

public class ConnectorDialog extends Dialog {

	private Shell shell;
	private List<IComponent> connectedTargetComponents;
	private IComponent sourceComponent;
	private List<IComponent> potentialTargetComponents;

	public ConnectorDialog(Shell parent, int style, IComponent component, List<IComponent> connectedTargetComponents,
			List<IComponent> potentialTargetComponents) {
		super(parent, style);

		sourceComponent = component;
		this.connectedTargetComponents = connectedTargetComponents;
		this.potentialTargetComponents = potentialTargetComponents;

		createShell();
	}

	private Composite addConnectionInterface() {
		Composite eventComposite = new Composite(shell, SWT.NONE);
		eventComposite.setLayout(new GridLayout(3, false));
		eventComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final TableViewer potentialTargetComponentTableViewer = new TableViewer(eventComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.H_SCROLL);
		final Table potentialTargetComponentTable = potentialTargetComponentTableViewer.getTable();
		potentialTargetComponentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		potentialTargetComponentTable.setLinesVisible(true);
		potentialTargetComponentTable.setHeaderVisible(true);
		potentialTargetComponentTableViewer.setLabelProvider(new ComponentsLabelProvider());
		potentialTargetComponentTableViewer.setContentProvider(new ArrayContentProvider());
		potentialTargetComponentTableViewer.setInput(potentialTargetComponents);

		final TableColumn potentialTargetComponentTableColumn = new TableColumn(potentialTargetComponentTable, SWT.NONE);
		potentialTargetComponentTableColumn.setText("Target Components");
		potentialTargetComponentTableColumn.setResizable(true);
		potentialTargetComponentTableColumn.setWidth(250);

		Composite shiftComposite = new Composite(eventComposite, SWT.NONE);
		shiftComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));
		shiftComposite.setLayout(new GridLayout());

		final TableViewer connectedTargetComponentTableViewer = new TableViewer(eventComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.H_SCROLL);
		final Table connectedTargetComponentTable = connectedTargetComponentTableViewer.getTable();
		connectedTargetComponentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		connectedTargetComponentTable.setLinesVisible(true);
		connectedTargetComponentTable.setHeaderVisible(true);
		connectedTargetComponentTableViewer.setLabelProvider(new ComponentsLabelProvider());
		connectedTargetComponentTableViewer.setContentProvider(new ArrayContentProvider());
		connectedTargetComponentTableViewer.setInput(connectedTargetComponents);

		final TableColumn selectedTargetComponentTableColumn = new TableColumn(connectedTargetComponentTable, SWT.NONE);
		selectedTargetComponentTableColumn.setText("Selected Target Components");
		selectedTargetComponentTableColumn.setResizable(true);
		selectedTargetComponentTableColumn.setWidth(250);

		Button arrowRightButton = new Button(shiftComposite, SWT.PUSH);
		arrowRightButton.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true));
		arrowRightButton.setText(">>");
		arrowRightButton.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectionIndices = potentialTargetComponentTable.getSelectionIndices();
				List<IComponent> input = (List<IComponent>) potentialTargetComponentTableViewer.getInput();
				List<IComponent> newInput = (List<IComponent>) connectedTargetComponentTableViewer.getInput();

				LinkedList<IComponent> componentsToMove = new LinkedList<IComponent>();

				for (int selectionIndex : selectionIndices) {
					componentsToMove.add(input.get(selectionIndex));
				}

				input.removeAll(componentsToMove);
				newInput.addAll(componentsToMove);

				potentialTargetComponentTableViewer.setInput(input);
				connectedTargetComponentTableViewer.setInput(newInput);
			}
		});

		Button arrowLeftButton = new Button(shiftComposite, SWT.PUSH);
		arrowLeftButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true));
		arrowLeftButton.setText("<<");
		arrowLeftButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectionIndices = connectedTargetComponentTable.getSelectionIndices();
				List<IComponent> input = (List<IComponent>) connectedTargetComponentTableViewer.getInput();
				List<IComponent> newInput = (List<IComponent>) potentialTargetComponentTableViewer.getInput();

				List<IComponent> componentsToMove = new LinkedList<IComponent>();

				for (int selectionIndex : selectionIndices)
					componentsToMove.add(input.get(selectionIndex));

				input.removeAll(componentsToMove);
				newInput.addAll(componentsToMove);

				connectedTargetComponentTableViewer.setInput(input);
				potentialTargetComponentTableViewer.setInput(newInput);
			}
		});

		return eventComposite;
	}

	private void addMenuComposite() {
		Composite menuComposite = new Composite(shell, SWT.NONE);
		menuComposite.setLayout(new GridLayout());
		menuComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		GridData okButtonLayoutData = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		okButtonLayoutData.widthHint = 100;

		Button okButton = new Button(menuComposite, SWT.NONE);
		okButton.setLayoutData(okButtonLayoutData);
		okButton.setText("Ok");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	/**
	 * Creates the shell for the dialog and adds interfaces for specifying source and target events with their properties
	 */
	private void createShell() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;

		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setText("Configure the connections of \"" + sourceComponent.getNameWithId() + "\""); //$NON-NLS-1$
		shell.setLayout(gridLayout);
		shell.setSize(536, 400);

		if (connectedTargetComponents.size() > 0 || potentialTargetComponents.size() > 0) {
			addConnectionInterface();
		} else {
			Label informationLabel = new Label(shell, SWT.NONE);
			informationLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
			informationLabel.setText("Es existieren keine Komponenten, mit denen eine Verbindung\nhergestellt werden könnte.");
			informationLabel.setAlignment(SWT.CENTER);
		}

		addMenuComposite();
	}

	public void open() {
		shell.open();
		Display display = Display.getCurrent();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
