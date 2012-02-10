package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Sets a message field with a binary value<BR> 
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetBinary extends ISOMsgAbstractAction implements
		IISOMsgAction {

	private byte[] bytes;
	
	public ISOMsgActionSetBinary() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// ... set dest field value
		ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(),
				(bytes == null ? new byte[] {} : bytes));
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
