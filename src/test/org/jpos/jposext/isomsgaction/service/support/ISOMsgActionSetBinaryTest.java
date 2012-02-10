package org.jpos.jposext.isomsgaction.service.support;

import java.util.Arrays;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import junit.framework.TestCase;

public class ISOMsgActionSetBinaryTest extends TestCase {
	private ISOMsgActionSetBinary action;

	private ISOMsg msg;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionSetBinary();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" },
				new String[] { "5", "" }});
		msg.set(6, new byte[] {0x05, 0x04, 0x03, 0x02, 0x01});
	}

	public void testCopieInterneChampSimple() throws ISOException {
		action.setIdPath("6");
		action.setBytes(new byte[] {0x15, 0x14, 0x13, 0x12, 0x11});
		action.process(new ISOMsg[] {msg}, null);
		assertTrue(Arrays.equals(new byte[] {0x15, 0x14, 0x13, 0x12, 0x11}, msg.getBytes(6)));
	}
}
