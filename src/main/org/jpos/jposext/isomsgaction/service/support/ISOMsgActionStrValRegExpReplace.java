package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Value copy action from a source field in a source message to a dest field in
 * a dest message. Actually, the source field value is matched
 * against a regular expression. A specified replacement expression is then applied
 * and the replace operation result is finally put in dest field.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionStrValRegExpReplace extends ISOMsgAbstractAction
		implements IISOMsgAction {

	/**
	 * Pattern d'expression régulière
	 */
	private String regexpPattern;

	/**
	 * Expression de remplacement
	 */
	private String regexpReplace;

	public ISOMsgActionStrValRegExpReplace() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Récupération de la valeur du champ source ...
		String strVal = ISOMsgHelper.getStringValue(msg[getSrcMsgIndex()],
				getSrcIdPath());

		String strReplacedValue = strVal.replaceFirst(regexpPattern,
				regexpReplace);

		// ... et positionnement de la valeur du champ cible
		ISOMsgHelper
				.setValue(msg[getMsgIndex()], getIdPath(), strReplacedValue, isBinary());
	}

	public String getRegexpPattern() {
		return regexpPattern;
	}

	public void setRegexpPattern(String regexpPattern) {
		this.regexpPattern = regexpPattern;
	}

	public String getRegexpReplace() {
		return regexpReplace;
	}

	public void setRegexpReplace(String regexpReplace) {
		this.regexpReplace = regexpReplace;
	}

}
