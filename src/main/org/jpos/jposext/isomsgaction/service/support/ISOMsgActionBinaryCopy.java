package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Raw binary value copy action from a source field in a source message to a dest field
 * in a dest message<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionBinaryCopy extends ISOMsgAbstractAction implements
		IISOMsgAction {

	public ISOMsgActionBinaryCopy() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Get binary value from source field
		ISOComponent cmp = ISOMsgHelper.getComponent(msg[getSrcMsgIndex()],
				getSrcIdPath());
		byte[] bytes = cmp.getBytes();

		// ... set dest field value
		ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(),
				(bytes == null ? new byte[] {} : bytes));
	}

}
