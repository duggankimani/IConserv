package com.wira.pmgt.client.ui.util;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberUtils {

	private static String NUMBER_PATTERN="###,###.##";
	
	public static NumberFormat NUMBERFORMAT= NumberFormat.getFormat(NUMBER_PATTERN);
	public static NumberFormat CURRENCYFORMAT= NumberFormat.getCurrencyFormat("KES");
}
