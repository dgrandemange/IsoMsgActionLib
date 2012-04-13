package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionStrValCopyTest extends TestCase {

	private ISOMsgActionStrValCopy action;

	private ISOMsg msg;

	private ISOMsg submsg1;

	private ISOMsg msg2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionStrValCopy();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" },
				new String[] { "5", "" },
				new String[] { "6", "0123" }});

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
				new String[] { "4", "msg2_valeur4_source" },
				new String[] { "5", "" }});		
	}

	public void testCopieInterneChampSimple() throws ISOException {
		action.setIdPath("3");
		action.setSrcIdPath("2");
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("valeur2_source", msg.getString(3));
	}

	public void testCopieInterneChampVersSousChamp() throws ISOException {
		action.setIdPath("5.1");
		action.setSrcIdPath("3");
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("valeur3_source", submsg1.getString(1));
	}	
	
	public void testCopieDepuisMessageExterneChampSimple() throws ISOException {
		action.setIdPath("3");
		action.setSrcIdPath("2");
		action.setSrcMsgIndex(1);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("msg2_valeur2_source", msg.getString(3));
	}
	
	public void testCopieDepuisMessageExterneChampSimpleConcatenationChampRempli() throws ISOException {
		action.setIdPath("4");
		action.setSrcIdPath("4");
		action.setSrcMsgIndex(1);
		action.setConcat(true);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("valeur4_sourcemsg2_valeur4_source", msg.getString(4));
	}
	
	public void testCopieDepuisMessageExterneChampSimpleConcatenationChampVide() throws ISOException {
		action.setIdPath("5");
		action.setSrcIdPath("4");
		action.setSrcMsgIndex(1);
		action.setConcat(true);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("msg2_valeur4_source", msg.getString(5));
	}
	
	public void testCopieDepuisMessageExterneChampSimpleConcatenationChampInexistant() throws ISOException {
		action.setIdPath("7");
		action.setSrcIdPath("4");
		action.setSrcMsgIndex(1);
		action.setConcat(true);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("msg2_valeur4_source", msg.getString(7));
	}
	
	public void testCopieDepuisMessageExterneChampSimpleConcatenationChampSourceVide() throws ISOException {
		action.setIdPath("4");
		action.setSrcIdPath("5");
		action.setSrcMsgIndex(1);
		action.setConcat(true);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("valeur4_source", msg.getString(4));
	}
	
	public void testCopieDepuisMessageExterneChampSimpleConcatenationChampSourceInexistant() throws ISOException {
		action.setIdPath("4");
		action.setSrcIdPath("6");
		action.setSrcMsgIndex(1);
		action.setConcat(true);
		action.process(new ISOMsg[] {msg, msg2}, null);
		assertEquals("valeur4_source", msg.getString(4));
	}
	
	public void testCopieInterneChampSimple_Binary() throws ISOException {
		action.setIdPath("3");
		action.setSrcIdPath("6");
		action.setBinary(true);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("0123", msg.getString(3));
		assertTrue(msg.getComponent(3) instanceof ISOBinaryField);
	}	
	
}
