package org.jpos.jposext.isomsgaction.testing.service.support;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Class to use as a base class for all ism msg action XML mapping unit tests
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionJunit3 extends TestCase {

	private Test testSuite;

	private void init() {
		try {
			testSuite = ISOMsgActionTestTool.createTestSuite(this);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public ISOMsgActionJunit3() {
		super();
		init();
	}

	public ISOMsgActionJunit3(String name) {
		super(name);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#run(junit.framework.TestResult)
	 */
	public void run(TestResult result) {
		testSuite.run(result);
	}

	public void test() {
	}

	@Override
	public int countTestCases() {
		return testSuite.countTestCases();
	}

}
