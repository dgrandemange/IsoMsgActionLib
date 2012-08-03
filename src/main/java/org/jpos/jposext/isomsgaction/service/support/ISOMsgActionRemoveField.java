package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Field removal action : in dest message, removal of specified dest field
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionRemoveField extends ISOMsgAbstractAction implements IISOMsgAction {

	public ISOMsgActionRemoveField() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		ISOMsgHelper.unsetValue(msg[getMsgIndex()], getIdPath());
	}

}
