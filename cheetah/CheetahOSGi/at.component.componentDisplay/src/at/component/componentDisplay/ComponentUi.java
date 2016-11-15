package at.component.componentDisplay;

import java.util.LinkedList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.ServiceReference;

public class ComponentUi {
	private TableViewer componentTableViewer;

	public ComponentUi(Composite composite) {
		createUi(composite);
	}

	private void createUi(Composite composite) {
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		componentTableViewer = new TableViewer(composite, SWT.BORDER);
		componentTableViewer.setLabelProvider(new ComponentTableLabelProvider());
		componentTableViewer.setContentProvider(new ArrayContentProvider());
		componentTableViewer.setInput(new LinkedList<ServiceReference>());

		Table componentTable = componentTableViewer.getTable();
		componentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		componentTable.setLinesVisible(true);
	}

	public TableViewer getComponentTableViewer() {
		return componentTableViewer;
	}
}
