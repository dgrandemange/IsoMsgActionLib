package org.jpos.jposext.isomsgaction.testing.service.support;

import junit.framework.Test;

import org.jpos.jposext.isomsgaction.testing.annotation.TestIsoMapping;

/**
 * @author dgrandemange
 * 
 */
public class ISOMsgActionTestTool {

	public static Test createTestSuite(Object testDecl)
			throws SecurityException, NoSuchMethodException {
		String mappingsDir = null;
		String mappingID = null;
		boolean interactive = false;
		
		TestIsoMapping annotation = testDecl.getClass().getAnnotation(TestIsoMapping.class);
		if (annotation != null) {
			mappingsDir = ((TestIsoMapping) annotation).mappingsDir();
			mappingID = ((TestIsoMapping) annotation).mappingId();
			interactive = ((TestIsoMapping) annotation).interactive();
		}
		
		TestSuiteFactoryImpl testSuiteFactory = new TestSuiteFactoryImpl(
				mappingsDir);		
		testSuiteFactory.setInteractive(interactive);
		Test test = null;

		if ("*".equals(mappingID)) {
			test = testSuiteFactory.create();
		} else if ((null != mappingID) && (!("".equals(mappingID.trim())))) {
			test = testSuiteFactory.createByMappingId(mappingID);
		}

		return test;
	}
}
