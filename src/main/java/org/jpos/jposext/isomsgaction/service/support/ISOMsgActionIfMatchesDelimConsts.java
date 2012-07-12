package org.jpos.jposext.isomsgaction.service.support;

import java.util.StringTokenizer;

import org.jpos.iso.ISOMsg;

/**
 * 
 * Message field value testing action : value is checked against a specified list of space delimited values<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfMatchesDelimConsts extends ISOMsgAbstractIfAction {

	/**
	 * Valeurs à matcher
	 */
	private String valuesToMatch;

	/**
	 * Indique si la casse est sinificative ou non
	 */
	private boolean caseSensitive = true;	

	public ISOMsgActionIfMatchesDelimConsts() {
		super();
	}	
	
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		if ((null == msg) || (!(msg.hasField(id)))) {
			return false;
		}
		
		String currentValue = msg.getString(id);
		boolean res = false;
		for (StringTokenizer tokenizer = new StringTokenizer(valuesToMatch, " "); (!res)
				&& tokenizer.hasMoreTokens();) {
			String token = tokenizer.nextToken();
			String trimmed = token.trim();
			res = caseSensitive ? trimmed.equals(currentValue) : trimmed
					.equalsIgnoreCase(currentValue);
		}
		return res;
	}

	public String getValuesToMatch() {
		return valuesToMatch;
	}

	public void setValuesToMatch(String valuesToMatch) {
		this.valuesToMatch = valuesToMatch;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
