package org.jpos.jposext.isomsgaction.service;

import org.jpos.iso.ISOMsg;

/**
 * 
 * @author dgrandemange
 *
 */
public interface IFulfillCondition {
	
	/**
	 * @param msg ISO Message
	 * @param id Field id in message
	 * @return true if condition is fulfilled
	 */
	public boolean isConditionFulfilled(ISOMsg msg, int id);
}
