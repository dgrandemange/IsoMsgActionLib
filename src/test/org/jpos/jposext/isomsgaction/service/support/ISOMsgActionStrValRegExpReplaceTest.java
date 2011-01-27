package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionStrValRegExpReplaceTest extends TestCase {

	private ISOMsgActionStrValRegExpReplace action;

	private ISOMsg msg;

	private ISOMsg submsg1;

	private ISOMsg msg2;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionStrValRegExpReplace();

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
		
		msg2 = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg2, new String[][] {
				new String[] { "1", "msg2_valeur1_source" },
				new String[] { "2", "msg2_valeur2_source" },
				new String[] { "3", "msg2_valeur3_source" },
				new String[] { "4", "msg2_valeur4_source" } });		
	}

	public void testRemplacementInterneChampSimple() throws ISOException {
		action.setIdPath("3");
		action.setSrcIdPath("2");
		action.setRegexpPattern("^valeur(.*)$");
		action.setRegexpReplace("v$1");
		action.process(msg, null);
		assertEquals("v2_source", msg.getString(3));
	}

	public void testRemplacementInterneChampVersSousChamp() throws ISOException {
		action.setIdPath("5.1");
		action.setSrcIdPath("3");
		action.setRegexpPattern("^valeur(.*)$");
		action.setRegexpReplace("v$1");		
		action.process(msg, null);
		assertEquals("v3_source", submsg1.getString(1));
	}	
	
	public void testRemplacementDepuisMsgExterneChampSimple() throws ISOException {
		action.setIdPath("3");
		action.setSrcIdPath("2");
		action.setSrcMsgIndex(1);
		action.setRegexpPattern("^msg2_valeur(.*)$");
		action.setRegexpReplace("v$1");
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("v2_source", msg.getString(3));
	}	
	
}
