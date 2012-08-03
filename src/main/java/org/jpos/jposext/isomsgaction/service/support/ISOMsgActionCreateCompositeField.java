package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Sub-fields structure creation action in a dest message
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionCreateCompositeField extends ISOMsgAbstractAction
		implements IISOMsgAction {

	public ISOMsgActionCreateCompositeField() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		String strFieldId = ISOMsgHelper.findLastAtomicId(getIdPath());
		int fieldId = ISOMsgHelper.getIntIdFromStringId(strFieldId);
		ISOMsgHelper.setComponent(msg[getMsgIndex()], getIdPath(), new ISOMsg(
				fieldId));
	}

}
