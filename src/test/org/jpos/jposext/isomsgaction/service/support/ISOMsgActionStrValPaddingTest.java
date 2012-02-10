package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.model.PadDirectionEnum;

public class ISOMsgActionStrValPaddingTest extends TestCase {

	private ISOMsgActionStrValPadding action;

	private ISOMsg msg;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionStrValPadding();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" },
				new String[] { "5", "AAFF" }
				});	
	}

	public void testPaddingGauche() throws ISOException {
		action.setIdPath("3");
		action.setExpLen(20);
		action.setPadChar("0");
		action.setPadDir(PadDirectionEnum.LEFT);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("000000valeur3_source", msg.getString(3));
	}

	public void testPaddingDroite() throws ISOException {
		action.setIdPath("2");
		action.setExpLen(20);
		action.setPadChar(" ");
		action.setPadDir(PadDirectionEnum.RIGHT);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("valeur2_source      ", msg.getString(2));
	}	
	
	public void testPaddingExpLenInferieurLgChamp() throws ISOException {
		action.setIdPath("2");
		action.setExpLen(10);
		action.setPadChar(" ");
		action.setPadDir(PadDirectionEnum.RIGHT);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("valeur2_source", msg.getString(2));
	}		

	public void testPaddingChampInexistant() throws ISOException {
		action.setIdPath("6");
		action.setExpLen(10);
		action.setPadChar("#");
		action.setPadDir(PadDirectionEnum.RIGHT);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("##########", msg.getString(6));
	}		

	public void testPaddingGauche_Binary() throws ISOException {
		action.setIdPath("5");
		action.setExpLen(8);
		action.setPadChar("0");
		action.setBinary(true);
		action.setPadDir(PadDirectionEnum.LEFT);
		action.process(new ISOMsg[] {msg}, null);
		assertEquals("0000AAFF", msg.getString(5));
	}
	
	
}
