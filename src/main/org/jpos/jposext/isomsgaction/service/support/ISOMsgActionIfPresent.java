package org.jpos.jposext.isomsgaction.service.support;

import org.jpos.iso.ISOMsg;

/**
 * Message field presence testing action : presence of field in message is checked<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfPresent extends
		ISOMsgAbstractIfAction {
	
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		if (null == msg) {
			return false;
		}
		
		return msg.hasField(id);
	}

}
