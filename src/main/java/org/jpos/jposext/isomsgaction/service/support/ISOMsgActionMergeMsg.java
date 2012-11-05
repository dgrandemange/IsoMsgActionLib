package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 * Merging action : source message is merged in dest message
 * 
 * @author dgrandemange
 *
 */
public class ISOMsgActionMergeMsg extends ISOMsgAbstractAction {

	private boolean clone;
	
	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		
		ISOMsg srcMsgToMerge;
		
		if (clone) {
			srcMsgToMerge = (ISOMsg) msg[this.getSrcMsgIndex()].clone();
		} else {
			srcMsgToMerge = msg[this.getSrcMsgIndex()];
		}
		
		msg[this.getMsgIndex()].merge(srcMsgToMerge);
	}

	/**
	 * @return the clone
	 */
	public boolean isClone() {
		return clone;
	}

	/**
	 * @param clone the clone to set
	 */
	public void setClone(boolean clone) {
		this.clone = clone;
	}

}
