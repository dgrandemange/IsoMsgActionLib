package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Set MTI action : in dest message, replace MTI by its corresponding response
 * MTI. If a failure occurs in determining response MTI, a default response MTI
 * is used.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetResponseMTI extends ISOMsgAbstractAction implements
		IISOMsgAction {

	private String defaultResponseMTI;

	public ISOMsgActionSetResponseMTI() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		ISOMsg targetMsg = msg[getMsgIndex()];
		try {
			targetMsg.setResponseMTI();
		} catch (ISOException e) {
			targetMsg.setMTI(defaultResponseMTI);
		}
	}

	public String getDefaultResponseMTI() {
		return defaultResponseMTI;
	}

	public void setDefaultResponseMTI(String defaultResponseMTI) {
		this.defaultResponseMTI = defaultResponseMTI;
	}

}
