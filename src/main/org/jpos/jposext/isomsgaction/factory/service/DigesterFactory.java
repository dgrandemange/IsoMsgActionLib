/*
 * Created on 16 mai 08 by dgrandemange
 *
 */
package org.jpos.jposext.isomsgaction.factory.service;

import org.apache.commons.digester.Digester;

/**
 * Factory interface to create XML common digester
 * 
 * @author dgrandemange
 *
 */
public interface DigesterFactory {

    /**
     * XML common digester factory method
     * 
     * @return
     */
    public Digester createDigester();

}
