/*
 * Created on 16 mai 08 by dgrandemange
 *
 * Copyright (c) 2005 Setib
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Setib ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Setib.
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
