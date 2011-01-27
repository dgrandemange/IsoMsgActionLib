package org.jpos.jposext.isomsgaction.service.support;

import java.util.Arrays;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionBinaryCopyTest extends TestCase {

	private ISOMsgActionBinaryCopy action;

	private ISOMsg msg;

	private ISOMsg submsg1;

	private ISOMsg msg2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionBinaryCopy();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" },
				new String[] { "5", "" }});
		msg.set(6, new byte[] {0x05, 0x04, 0x03, 0x02, 0x01});

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
		action.setSrcIdPath("6");
		action.process(new ISOMsg[] {msg}, null);
		assertTrue(Arrays.equals(new byte[] {0x05, 0x04, 0x03, 0x02, 0x01}, msg.getBytes(3)));
	}

}
