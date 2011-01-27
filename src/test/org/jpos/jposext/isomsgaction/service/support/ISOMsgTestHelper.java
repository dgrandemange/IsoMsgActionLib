package org.jpos.jposext.isomsgaction.service.support;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgTestHelper {
	
	public static void populateMsg(ISOMsg msg, String[][] dummyData)
			throws NumberFormatException, ISOException {
		for (String[] fieldIdValuePair : dummyData) {
			msg.set(Integer.parseInt(fieldIdValuePair[0]), fieldIdValuePair[1]);
		}
	}
}
