package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;

/**
 * A basic abstract iso msg action that provides some standard properties
 * 
 * @author dgrandemange
 *
 */
public abstract class ISOMsgAbstractAction implements IISOMsgAction {

	private IISOMsgCommonInfoProvider isoMsgCommonInfoProvider;
	
	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		process(new ISOMsg[] { msg }, ctx);
	}

	public int getSrcMsgIndex() {
		return isoMsgCommonInfoProvider.getSrcMsgIndex();
	}

	public String getIdPath() {
		return isoMsgCommonInfoProvider.getIdPath();
	}

	public String getSrcIdPath() {
		return isoMsgCommonInfoProvider.getSrcIdPath();
	}

	public int getMsgIndex() {
		return isoMsgCommonInfoProvider.getMsgIndex();
	}

	public boolean isBinary() {
		return isoMsgCommonInfoProvider.isBinary();
	}

	public void setSrcMsgIndex(int srcMsgIndex) {
		isoMsgCommonInfoProvider.setSrcMsgIndex(srcMsgIndex);		
	}

	public void setIdPath(String idPath) {
		isoMsgCommonInfoProvider.setIdPath(idPath);
	}

	public void setSrcIdPath(String srcIdPath) {
		isoMsgCommonInfoProvider.setSrcIdPath(srcIdPath);
	}

	public void setMsgIndex(int msgIndex) {
		isoMsgCommonInfoProvider.setMsgIndex(msgIndex);		
	}

	public void setBinary(boolean binary) {
		isoMsgCommonInfoProvider.setBinary(binary);		
	}	
	
	/**
	 * @param isoMsgCommonInfoProvider the isoMsgCommonInfoProvider to set
	 */
	public void setIsoMsgCommonInfoProvider(
			IISOMsgCommonInfoProvider isoMsgCommonInfoProvider) {
		this.isoMsgCommonInfoProvider = isoMsgCommonInfoProvider;
	}

	/**
	 * @return the isoMsgCommonInfoProvider
	 */
	public IISOMsgCommonInfoProvider getIsoMsgCommonInfoProvider() {
		return isoMsgCommonInfoProvider;
	}

}
