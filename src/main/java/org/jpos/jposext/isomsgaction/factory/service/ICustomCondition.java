package org.jpos.jposext.isomsgaction.factory.service;

import java.util.Map;

import org.jpos.iso.ISOMsg;

/**
 * Describes a custom condition. Implementations should be used conjointly with an ISOMsgActionIfCustomCondition
 * 
 * @author dgrandemange
 *
 */
public interface ICustomCondition {

	/**
	 * @param msgTab	Messages tabulation
	 * @param ctx		Action context
	 * @return true if condition is fulfilled, false otherwise
	 */
	boolean isConditionFulfilled(ISOMsg[] msgTab, Map<String, Object> ctx);
	
}
