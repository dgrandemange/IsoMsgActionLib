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

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		msg[this.getMsgIndex()].merge(msg[this.getSrcMsgIndex()]);
	}

}
