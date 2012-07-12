package org.jpos.jposext.isomsgaction.testing.service.support;

import junit.framework.TestCase;

public class TestSuiteFactoryImplTest extends TestCase {

	private TestSuiteFactoryImpl factory;
	
	protected void setUp() throws Exception {
		super.setUp();
		factory = new TestSuiteFactoryImpl();
	}

	public void testHexPathToDecPath() {
		assertEquals("1", factory.hexPathToDecPath("1","."));
		assertEquals("16", factory.hexPathToDecPath("0x10","."));
		assertEquals("10.1.16.2.32", factory.hexPathToDecPath("0x0A.1.0x10.2.0x20","."));
	}

}
