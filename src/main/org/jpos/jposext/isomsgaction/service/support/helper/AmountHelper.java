package org.jpos.jposext.isomsgaction.service.support.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class AmountHelper {
	
	private static Map<String, String> map;

	static {
		map = new HashMap<String, String>();

		map.put("100", "BGL");
		map.put("104", "MMK");
		map.put("108", "BIF");
		map.put("116", "KHR");
		map.put("12", "DZD");
		map.put("124", "CAD");
		map.put("132", "CVE");
		map.put("136", "KYD");
		map.put("144", "LKR");
		map.put("152", "CLP");
		map.put("156", "CNY");
		map.put("170", "COP");
		map.put("174", "KMF");
		map.put("180", "ZRN");
		map.put("188", "CRC");
		map.put("191", "HRK");
		map.put("192", "CUP");
		map.put("196", "CYP");
		map.put("20", "ADP");
		map.put("203", "CZK");
		map.put("208", "DKK");
		map.put("208", "DKK");
		map.put("208", "DKK");
		map.put("214", "DOP");
		map.put("218", "ECS");
		map.put("222", "SVC");
		map.put("230", "ETB");
		map.put("232", "ERN");
		map.put("233", "EEK");
		map.put("238", "FKP");
		map.put("24", "AON");
		map.put("242", "FJD");
		map.put("246", "FIM");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("250", "FRF");
		map.put("262", "DJF");
		map.put("270", "GMD");
		map.put("280", "DEM");
		map.put("288", "GHC");
		map.put("292", "GIP");
		map.put("300", "GRD");
		map.put("31", "AZM");
		map.put("32", "ARS");
		map.put("320", "GTQ");
		map.put("324", "GNF");
		map.put("328", "GYD");
		map.put("332", "HTG");
		map.put("340", "HNL");
		map.put("344", "HKD");
		map.put("348", "HUF");
		map.put("352", "ISK");
		map.put("356", "INR");
		map.put("356", "INR");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("36", "AUD");
		map.put("360", "IDR");
		map.put("360", "IDR");
		map.put("364", "IRR");
		map.put("368", "IQD");
		map.put("372", "IEP");
		map.put("376", "ILS");
		map.put("380", "ITL");
		map.put("380", "ITL");
		map.put("380", "ITL");
		map.put("388", "JMD");
		map.put("392", "JPY");
		map.put("398", "KZT");
		map.put("4", "AFA");
		map.put("40", "ATS");
		map.put("400", "JOD");
		map.put("404", "KES");
		map.put("408", "KPW");
		map.put("410", "KRW");
		map.put("414", "KWD");
		map.put("417", "KGS");
		map.put("418", "LAK");
		map.put("422", "LBP");
		map.put("426", "LSL");
		map.put("428", "LVL");
		map.put("430", "LRD");
		map.put("434", "LYD");
		map.put("44", "BSD");
		map.put("440", "LTL");
		map.put("442", "LUF");
		map.put("446", "MOP");
		map.put("450", "MGF");
		map.put("454", "MWK");
		map.put("458", "MYR");
		map.put("462", "MVR");
		map.put("470", "MTL");
		map.put("478", "MRO");
		map.put("48", "BHD");
		map.put("480", "MUR");
		map.put("484", "MXN");
		map.put("496", "MNT");
		map.put("498", "MDL");
		map.put("50", "BDT");
		map.put("504", "MAD");
		map.put("504", "MAD");
		map.put("508", "MZM");
		map.put("51", "AMD");
		map.put("512", "OMR");
		map.put("516", "NAD");
		map.put("52", "BBD");
		map.put("524", "NPR");
		map.put("528", "NLG");
		map.put("532", "ANG");
		map.put("533", "AWG");
		map.put("548", "VUV");
		map.put("554", "NZD");
		map.put("554", "NZD");
		map.put("554", "NZD");
		map.put("554", "NZD");
		map.put("554", "NZD");
		map.put("558", "NIO");
		map.put("56", "BEF");
		map.put("566", "NGN");
		map.put("578", "NOK");
		map.put("578", "NOK");
		map.put("578", "NOK");
		map.put("586", "PKR");
		map.put("590", "PAB");
		map.put("598", "PGK");
		map.put("60", "BMD");
		map.put("600", "PYG");
		map.put("604", "PEN");
		map.put("608", "PHP");
		map.put("620", "PTE");
		map.put("624", "GWP");
		map.put("626", "TPE");
		map.put("634", "QAR");
		map.put("64", "BTN");
		map.put("642", "ROL");
		map.put("643", "RUB");
		map.put("646", "RWF");
		map.put("654", "SHP");
		map.put("678", "STD");
		map.put("682", "SAR");
		map.put("690", "SCR");
		map.put("694", "SLL");
		map.put("702", "SGD");
		map.put("703", "SKK");
		map.put("704", "VND");
		map.put("705", "SIT");
		map.put("706", "SOS");
		map.put("710", "ZAR");
		map.put("710", "ZAR");
		map.put("710", "ZAR");
		map.put("716", "ZWD");
		map.put("72", "BWP");
		map.put("724", "ESP");
		map.put("724", "ESP");
		map.put("736", "SDD");
		map.put("740", "SRG");
		map.put("748", "SZL");
		map.put("752", "SEK");
		map.put("756", "CHF");
		map.put("756", "CHF");
		map.put("760", "SYP");
		map.put("762", "TJR");
		map.put("764", "THB");
		map.put("776", "TOP");
		map.put("780", "TTD");
		map.put("784", "AED");
		map.put("788", "TND");
		map.put("792", "TRL");
		map.put("795", "TMM");
		map.put("8", "ALL");
		map.put("800", "UGX");
		map.put("807", "MKD");
		map.put("810", "RUR");
		map.put("818", "EGP");
		map.put("826", "GBP");
		map.put("834", "TZS");
		map.put("84", "BZD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("840", "USD");
		map.put("858", "UYU");
		map.put("860", "UZS");
		map.put("862", "VEB");
		map.put("882", "WST");
		map.put("886", "YER");
		map.put("891", "YUM");
		map.put("894", "ZMK");
		map.put("90", "SBD");
		map.put("901", "TWD");
		map.put("950", "XAF");
		map.put("950", "XAF");
		map.put("950", "XAF");
		map.put("950", "XAF");
		map.put("950", "XAF");
		map.put("950", "XAF");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("951", "XCD");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("952", "XOF");
		map.put("953", "XPF");
		map.put("953", "XPF");
		map.put("953", "XPF");
		map.put("955", "XBA");
		map.put("956", "XBB");
		map.put("957", "XBC");
		map.put("958", "XBD");
		map.put("959", "XAU");
		map.put("96", "BND");
		map.put("960", "XDR");
		map.put("961", "XAG");
		map.put("962", "XPT");
		map.put("963", "XTS");
		map.put("964", "XPD");
		map.put("972", "TJS");
		map.put("974", "BYR");
		map.put("975", "BGN");
		map.put("976", "CDF");
		map.put("977", "BAM");
		map.put("978", "EUR");
		map.put("979", "MXV");
		map.put("980", "UAH");
		map.put("981", "GEL");
		map.put("982", "AOR");
		map.put("983", "ECV");
		map.put("985", "PLN");
		map.put("986", "BRL");
		map.put("990", "CLF");
		map.put("991", "ZAL");
		map.put("997", "USN");
		map.put("998", "USS");
		map.put("999", "XXX");
	}

	public static String getCurrencyCode(String paramCurrencyCode) {
		String currencyCode;
		
		try {
			Integer.parseInt(paramCurrencyCode);
			currencyCode = map.get(paramCurrencyCode);
		}
		catch(NumberFormatException e) {
			currencyCode = paramCurrencyCode;
		}
		
		return currencyCode;
	}
	
	public static String formatAmount(String paramCurrencyCode, String amount,
			int currencySymbolShowMode, String fmtPattern,
			Character decimalSep, Character groupingSep) {
		
		String currencyCode = getCurrencyCode(paramCurrencyCode);
		Currency curr = Currency.getInstance(currencyCode);
		int fractionDigits = curr.getDefaultFractionDigits();

		String decFmtPattern = String.format(fmtPattern, filler(fractionDigits,
				'0'));

		double diviseur = Math.pow(10, fractionDigits);
		BigDecimal dividende = new BigDecimal(amount);
		BigDecimal quotient = dividende.divide(new BigDecimal(diviseur));
		BigDecimal scaled = quotient.setScale(fractionDigits);

		String currencyInfo;

		switch (currencySymbolShowMode) {
		case 0:
			currencyInfo = "";
			break;
		case 1:
			currencyInfo = curr.getCurrencyCode();
			break;
		case 2:
			currencyInfo = curr.getSymbol();
			break;
		default:
			currencyInfo = null;
			break;
		}

		curr.getSymbol();

		String formattedAmount = formatDecimalAmount(scaled.doubleValue(),
				currencyCode, decimalSep, groupingSep, currencyInfo,
				decFmtPattern);
		return formattedAmount;
	}

	public static String formatDecimalAmount(double amount,
			String currencyCode, Character decimalSep, Character groupingSep,
			String currencySymbol, String formatPattern) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();

		if (null != currencySymbol) {
			dfs.setCurrencySymbol(currencySymbol);
		}

		if (null != decimalSep) {
			dfs.setDecimalSeparator(decimalSep);
		}

		if (null != groupingSep) {
			dfs.setGroupingSeparator(groupingSep);
		}

		DecimalFormat decf = new DecimalFormat(formatPattern, dfs);

		return decf.format(amount);
	}

	public static String filler(int fillingLength, char fillingChar) {
		char[] buf = new char[fillingLength];

		Arrays.fill(buf, 0, buf.length, fillingChar);

		String res = new String(buf);

		return res;
	}

	public static void main(String[] args) {
		String currencyCode = "978";
		String val = "000100010015";
		int currencySymbolShowMode = 1; // 0=pas d'info devise, 1=code devise,
										// 2=symbole devise, 3=defaut
		String fmtPattern = "########0.%1$s###### \u00A4";//"###,###,###.%1$s######\u00A4";
		
		Character decimalSep = ',';
		Character groupingSep = '\'';

		String formattedAmount = AmountHelper.formatAmount(currencyCode, val,
				currencySymbolShowMode, fmtPattern, decimalSep, groupingSep);

		System.out.println(formattedAmount);
	}

}
