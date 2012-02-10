package org.jpos.jposext.isomsgaction.service.support;

import java.math.BigInteger;

import junit.framework.TestCase;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionSetRandomNumberTest extends TestCase {

	private ISOMsgActionSetRandomNumber action;

	private ISOMsg msg;

	private ISOMsg submsg1;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionSetRandomNumber();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });

		submsg1 = new ISOMsg(5);
		ISOMsgTestHelper.populateMsg(submsg1, new String[][] {
				new String[] { "1", "sub1_valeur1" },
				new String[] { "2", "sub1_valeur2" },
				new String[] { "3", "sub1_valeur3" } });

		msg.set(submsg1);
	}

	public void testGenerationRandom1() throws ISOException {
		action.setIdPath("3");
		action.setNbDigits(6);
		String lastGenNum = null;
		for (int i = 0; i <= 100; i++) {
			action.process(msg, null);

			String genNum = msg.getString(3);
			assertNotSame(lastGenNum, genNum);			
			assertEquals(6, genNum.length());
			BigInteger bigInteger = new BigInteger(genNum);
			assertTrue(bigInteger.intValue() >= 0);
			assertTrue(bigInteger.intValue() <= 999999);
			
			lastGenNum = genNum;
		}
	}

	public void testGenerationRandom2() throws ISOException {
		action.setIdPath("5.1");
		action.setNbDigits(3);		
		String lastGenNum = null;
		for (int i = 0; i <= 100; i++) {
			action.process(msg, null);
			
			String genNum = submsg1.getString(1);
			assertNotSame(lastGenNum, genNum);			
			assertEquals(3, genNum.length());
			BigInteger bigInteger = new BigInteger(genNum);
			assertTrue(bigInteger.intValue() >= 0);
			assertTrue(bigInteger.intValue() <= 999);
			
			lastGenNum = genNum;
		}
	}

	public void testGenerationRandom1_Binary() throws ISOException {
		action.setIdPath("3");
		action.setNbDigits(6);
		action.setBinary(true);
		String lastGenNum = null;
		for (int i = 0; i <= 100; i++) {
			action.process(msg, null);

			String genNum = msg.getString(3);
			assertTrue(msg.getComponent(3) instanceof ISOBinaryField);
			assertNotSame(lastGenNum, genNum);			
			assertEquals(6, genNum.length());
			BigInteger bigInteger = new BigInteger(genNum);
			assertTrue(bigInteger.intValue() >= 0);
			assertTrue(bigInteger.intValue() <= 999999);
			
			lastGenNum = genNum;
		}
	}	
	
}
