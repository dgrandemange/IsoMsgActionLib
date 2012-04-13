package org.jpos.jposext.isomsgaction.service.support;

import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;

/**
 * @author dgrandemange
 *
 */
public class ISOMsgCommonInfoProviderImpl implements
		IISOMsgCommonInfoProvider {

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
	
	/**
	 * Binary dest field indicator 
	 */
	private boolean binary;

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.support.IISOMsgCommonInfoProvider#getSrcMsgIndex()
	 */
	public int getSrcMsgIndex() {
		return srcMsgIndex;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#setSrcMsgIndex(int)
	 */
	public void setSrcMsgIndex(int srcMsgIndex) {
		this.srcMsgIndex = srcMsgIndex;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.support.IISOMsgCommonInfoProvider#getIdPath()
	 */
	public String getIdPath() {
		return idPath;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#setIdPath(java.lang.String)
	 */
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.support.IISOMsgCommonInfoProvider#getSrcIdPath()
	 */
	public String getSrcIdPath() {
		return srcIdPath;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#setSrcIdPath(java.lang.String)
	 */
	public void setSrcIdPath(String srcIdPath) {
		this.srcIdPath = srcIdPath;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.support.IISOMsgCommonInfoProvider#getMsgIndex()
	 */
	public int getMsgIndex() {
		return msgIndex;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#setMsgIndex(int)
	 */
	public void setMsgIndex(int msgIndex) {
		this.msgIndex = msgIndex;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#isBinary()
	 */
	public boolean isBinary() {
		return binary;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider#setBinary(boolean)
	 */
	public void setBinary(boolean binary) {
		this.binary = binary;
	}

}
