package at.test.component.currencyconvertor.impl;

public class CurrencyConvertorSoapProxy implements at.test.component.currencyconvertor.impl.CurrencyConvertorSoap {
  private String _endpoint = null;
  private at.test.component.currencyconvertor.impl.CurrencyConvertorSoap currencyConvertorSoap = null;
  
  public CurrencyConvertorSoapProxy() {
    _initCurrencyConvertorSoapProxy();
  }
  
  public CurrencyConvertorSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCurrencyConvertorSoapProxy();
  }
  
  private void _initCurrencyConvertorSoapProxy() {
    try {
      currencyConvertorSoap = (new at.test.component.currencyconvertor.impl.CurrencyConvertorLocator()).getCurrencyConvertorSoap();
      if (currencyConvertorSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)currencyConvertorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)currencyConvertorSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (currencyConvertorSoap != null)
      ((javax.xml.rpc.Stub)currencyConvertorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public at.test.component.currencyconvertor.impl.CurrencyConvertorSoap getCurrencyConvertorSoap() {
    if (currencyConvertorSoap == null)
      _initCurrencyConvertorSoapProxy();
    return currencyConvertorSoap;
  }
  
  public double conversionRate(at.test.component.currencyconvertor.impl.Currency fromCurrency, at.test.component.currencyconvertor.impl.Currency toCurrency) throws java.rmi.RemoteException{
    if (currencyConvertorSoap == null)
      _initCurrencyConvertorSoapProxy();
    return currencyConvertorSoap.conversionRate(fromCurrency, toCurrency);
  }
  
  
}