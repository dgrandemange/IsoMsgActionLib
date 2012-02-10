package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * An iso msg action that delegates process to a user customized iso action
 * 
 * @author dgrandemange
 *
 */
public class ISOMsgActionUserCustomized implements IISOMsgAction {

	private String isoActionClazzName;
	
	private IISOMsgAction isoAction;

	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		isoAction.process(new ISOMsg[] { msg }, ctx);
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		isoAction.process(msg, ctx);		
	}	
	
	public IISOMsgAction getIsoAction() {
		return isoAction;
	}

	public void setIsoAction(IISOMsgAction isoAction) {
		this.isoAction = isoAction;
	}

	public String getIsoActionClazzName() {
		return isoActionClazzName;
	}

	public void setIsoActionClazzName(String isoActionClazzName) {
		this.isoActionClazzName = isoActionClazzName;
	}

}
