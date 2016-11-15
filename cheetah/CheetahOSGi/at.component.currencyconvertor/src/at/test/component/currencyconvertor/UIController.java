package at.test.component.currencyconvertor;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.eclipse.swt.widgets.Composite;

import at.test.component.currencyconvertor.impl.Currency;
import at.test.component.currencyconvertor.impl.CurrencyConvertorLocator;

public class UIController {

	public UIController(Composite composite, CurrencyConverterComponent group) {
		new ComponentUI(composite, this);
	}

	public Double getConversionRate() {
		try {
			CurrencyConvertorLocator locator = new CurrencyConvertorLocator();
			
			return locator.getCurrencyConvertorSoap().conversionRate(Currency.EUR, Currency.USD);
		} catch (AxisFault e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
