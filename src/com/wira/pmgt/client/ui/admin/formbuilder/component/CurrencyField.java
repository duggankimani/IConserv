package com.wira.pmgt.client.ui.admin.formbuilder.component;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.CurrencyData;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.NumberFormat;
import com.wira.pmgt.client.util.ENV;
import com.wira.pmgt.shared.model.DataType;
import com.wira.pmgt.shared.model.form.KeyValuePair;
import com.wira.pmgt.shared.model.form.Property;

public class CurrencyField extends NumberField{
	
	private static CurrencyList currencyCodeMapConstants = GWT.create(CurrencyList.class);
	
	public CurrencyField(){
		super();
		lblEl.setInnerText("Currency Field");
		Iterator<CurrencyData> iter=  currencyCodeMapConstants.iterator();
		Property property = new Property(CURRENCY, "Currency", DataType.SELECTBASIC);
		while(iter.hasNext()){
			CurrencyData d = iter.next();
			property.addSelectionItem(new KeyValuePair(d.getCurrencyCode(), currencyCodeMapConstants.lookupName(d.getCurrencyCode())));
		}
		
		property.sort();				
		addProperty(property);
	}
	
	@Override
	public void setValue(Object value) {
		if(value!=null){
			if(!(value instanceof Double)){
				try{
					value = new Double(value.toString());
				}catch(Exception e){return;}
			}
			
			ENV.setContext(field, (Double)value);			
			txtComponent.setValue((Double)value);
			
			String currencyCode = getPropertyValue(CURRENCY);
			
			if(currencyCode!=null){
				CurrencyData data = currencyCodeMapConstants.lookup(currencyCode); 
				
				NumberFormat fmt = NumberFormat.getCurrencyFormat(data);
			    String formatted = fmt.format((Double)value);
			    lblReadOnly.setText(formatted);
				
			}else{
				lblReadOnly.setText(value.toString());	
			}
		}
	}
	
	
	@Override
	public FieldWidget cloneWidget() {
		return new CurrencyField();
	}
	
}
