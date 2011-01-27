package org.jpos.jposext.isomsgaction.service.support;

import org.jpos.iso.ISOMsg;

/**
 * 
 * Message field value testing action : value is checked against a specified regexp pattern<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfMatchesRegExp extends
		ISOMsgAbstractIfAction {
	
	public ISOMsgActionIfMatchesRegExp() {
		super();
	}

	/**
	 * Expression régulière à matcher
	 */
	private String regexp;	
	
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		if ((null == msg) || (!(msg.hasField(id)))) {
			return false;
		}
		
		String currentValue = msg.getString(id);
		return currentValue.matches(regexp);
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

}
