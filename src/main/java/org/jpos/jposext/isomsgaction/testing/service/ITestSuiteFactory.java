package org.jpos.jposext.isomsgaction.testing.service;

import junit.framework.TestSuite;

/**
 * Fabrique de suite de test de configuration de mappings
 * 
 * @author dgrandemange
 *
 */
public interface ITestSuiteFactory {
	
	/**
	 * @return
	 */
	public TestSuite create();
	
	/**
	 * @param id Identifiant de la configuration de mapping à tester
	 * @return
	 */
	public TestSuite createByMappingId(String id);
}
