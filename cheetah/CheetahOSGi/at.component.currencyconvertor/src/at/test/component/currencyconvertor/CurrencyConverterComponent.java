package at.test.component.currencyconvertor;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;

import at.component.AbstractComponent;

public class CurrencyConverterComponent extends AbstractComponent {

	@Override
	public void addUI(Composite composite) {
		try {
			new UIController(composite, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, String> getData() {
		return null;
	}

	@Override
	public String getName() {
		if (name == null) {
			Object bundleName = Activator.getBundleContext().getBundle().getHeaders().get(Constants.BUNDLE_NAME);

			if (bundleName != null && !bundleName.toString().trim().equals(""))
				name = bundleName.toString().trim();
			else
				name = Activator.getBundleContext().getBundle().getSymbolicName();
		}

		return name;
	}

	@Override
	public String getNameWithId() {
		return getName() + " (ID = " + getComponentId() + ")";
	}

	@Override
	public void initialize(HashMap<String, String> data) {
	}
}
