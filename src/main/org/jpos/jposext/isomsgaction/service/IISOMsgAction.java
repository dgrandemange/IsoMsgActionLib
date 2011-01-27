package org.jpos.jposext.isomsgaction.service;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 * Main iso msg action interface
 * 
 * @author dgrandemange
 * 
 */
public interface IISOMsgAction {

	/**
	 * @param msg Array of messages to use during action processing
	 * @param ctx Context that may be read during action processing
	 * @throws ISOException
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException;

	/**
	 * @param msg Message to use during action processing
	 * @param ctx Context that may be read during action processing
	 * @throws ISOException
	 */
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException;
}
