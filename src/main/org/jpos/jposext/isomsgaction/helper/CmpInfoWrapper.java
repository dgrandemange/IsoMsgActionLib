package org.jpos.jposext.isomsgaction.helper;

import org.jpos.iso.ISOMsg;

/**
 * @author dgrandemange
 *
 */
public class CmpInfoWrapper {
	
	private ISOMsg cmpParentMsg;

	private String id;

	public CmpInfoWrapper(ISOMsg cmpParentMsg, String id) {
		super();
		this.cmpParentMsg = cmpParentMsg;
		this.id = id;
	}

	public ISOMsg getCmpParentMsg() {
		return cmpParentMsg;
	}

	public String getId() {
		return id;
	}
}
