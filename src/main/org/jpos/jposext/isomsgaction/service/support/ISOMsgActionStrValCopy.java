package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Value copy action from a source field in a source message to a dest field
 * in a dest message<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionStrValCopy extends ISOMsgAbstractAction implements
		IISOMsgAction {

	private boolean concat;
	
	public ISOMsgActionStrValCopy() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Récupération de la valeur du champ source ...
		String strVal = ISOMsgHelper.getStringValue(msg[getSrcMsgIndex()],
				getSrcIdPath());
		// Récupération de la valeur du champ actuel
		String strCurrentVal = ISOMsgHelper.getStringValue(msg[getMsgIndex()],
				getIdPath());
		
		// ... et positionnement de la valeur du champ cible
		// écrasement du champ cible si et seulement si concat = false ou non positionné
		if (concat){
				ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), (strCurrentVal==null ? "" : strCurrentVal)+ (strVal==null ? "" :strVal ));
		}
		else{
			ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), (strVal==null ? "" :strVal ));
		}

	}

	public boolean isConcat() {
		return concat;
	}

	public void setConcat(boolean concat) {
		this.concat = concat;
	}

}
