package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Reference copy action from a source field in a source message to a dest field
 * in a dest message<BR/>
 * <b>As this is a copy by ref, use with great care.</b>
 * @author dgrandemange
 * 
 */
public class ISOMsgActionCopyFieldByRef extends ISOMsgAbstractAction implements IISOMsgAction {

	public ISOMsgActionCopyFieldByRef() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// R�cup�ration du composant depuis le message source ...
		ISOComponent cmp = ISOMsgHelper.getComponent(msg[getSrcMsgIndex()], getSrcIdPath());
		
		// ... et recopie vers le message cible
		ISOMsgHelper.setComponent(msg[getMsgIndex()], getIdPath(), cmp);
	}

}
