package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * A basic abstract iso msg action that provides some standard properties
 * 
 * @author dgrandemange
 *
 */
public abstract class ISOMsgAbstractAction implements IISOMsgAction {

	/**
	 * Index of message (in messages array) to use a the source message
	 */
	private int srcMsgIndex;

	/**
	 * Index of message (in messages array) to use a the dest message
	 */
	private int msgIndex;

	/**
	 * Dest field identifier path<BR/>
	 */
	private String idPath;

	/**
	 * Source field identifier path<BR/>
	 */
	private String srcIdPath;

	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		process(new ISOMsg[] { msg }, ctx);
	}

	public int getSrcMsgIndex() {
		return srcMsgIndex;
	}

	public void setSrcMsgIndex(int srcMsgIndex) {
		this.srcMsgIndex = srcMsgIndex;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public String getSrcIdPath() {
		return srcIdPath;
	}

	public void setSrcIdPath(String srcIdPath) {
		this.srcIdPath = srcIdPath;
	}

	public int getMsgIndex() {
		return msgIndex;
	}

	public void setMsgIndex(int msgIndex) {
		this.msgIndex = msgIndex;
	}

}
