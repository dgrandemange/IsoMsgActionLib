package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionCreateCompositeFieldTest extends TestCase {

	private ISOMsgActionCreateCompositeField action;

	private ISOMsg msg;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionCreateCompositeField();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });
	}

	public void testCopieChampSimple() throws ISOException {
		action.setIdPath("5");
		action.process(new ISOMsg[] {msg}, null);
		assertTrue(msg.hasField(5));
				
		assertTrue(msg.getValue(5) instanceof ISOMsg);
		ISOMsg field5 = (ISOMsg) msg.getValue(5);
		
		action.setIdPath("5.1");
		action.process(new ISOMsg[] {msg}, null);
		assertTrue(field5.hasField(1));
		
		Object field5_1 = msg.getValue(5);
		assertTrue(field5_1 instanceof ISOMsg);
		
	}

}
