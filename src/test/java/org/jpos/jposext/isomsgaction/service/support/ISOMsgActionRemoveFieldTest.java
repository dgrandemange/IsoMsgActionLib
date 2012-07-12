package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionRemoveFieldTest extends TestCase {
	
	private ISOMsgActionRemoveField action;
	private ISOMsg msg;
	private ISOMsg submsg1;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionRemoveField();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1" },
				new String[] { "2", "valeur2" },
				new String[] { "3", "valeur3" },
				new String[] { "4", "valeur4" } });

		submsg1 = new ISOMsg(5);
		ISOMsgTestHelper.populateMsg(submsg1, new String[][] {
				new String[] { "1", "sub1_valeur1" },
				new String[] { "2", "sub1_valeur2" },
				new String[] { "3", "sub1_valeur3" } });

		msg.set(submsg1);			
	}
	
	public void testSuppressionSimple() throws ISOException {
		action.setIdPath("2");
		action.process(new ISOMsg[] {msg}, null);
		assertFalse(msg.hasField(2));
	}

	public void testSuppressionSouChamp() throws ISOException {
		action.setIdPath("5.3");
		action.process(new ISOMsg[] {msg}, null);
		assertTrue(msg.hasField(5));
		assertFalse(submsg1.hasField(3));
	}
		
}
