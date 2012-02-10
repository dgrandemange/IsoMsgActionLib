package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionSetResponseMTITest extends TestCase {
	private ISOMsgActionSetResponseMTI action;

	private ISOMsg msg;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionSetResponseMTI();
		action.setDefaultResponseMTI("9919");
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "2", "valeur2" },
				new String[] { "3", "valeur3" },
				new String[] { "4", "valeur4" } });

	}

	public void testSimple() throws ISOException, ParseException {
		msg.setMTI("0100");
		action.process(msg, null);
		assertEquals("0110", msg.getMTI());
	}
	
	public void testInitialMTIIsNotARequestMTI() throws ISOException, ParseException {
		msg.setMTI("0110");		
		action.process(msg, null);
		assertEquals("9919", msg.getMTI());
	}	
}
