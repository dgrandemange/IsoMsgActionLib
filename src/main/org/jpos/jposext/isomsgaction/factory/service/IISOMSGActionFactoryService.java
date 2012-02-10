package org.jpos.jposext.isomsgaction.factory.service;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * ISO message action factory interface
 * 
 * @author dgrandemange
 *
 */
public interface IISOMSGActionFactoryService {
	
	/**
	 * @param id Action id to produce
	 * @return Action produced if id exists, else returns null
	 */
	public IISOMsgAction create(String id);
}
