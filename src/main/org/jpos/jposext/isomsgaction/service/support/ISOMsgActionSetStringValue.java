package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper.IFulfillCondition;
import org.jpos.jposext.isomsgaction.model.SimpleContextWrapper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Action de positionnement d'un champ d'un message avec une valeur chaîne<BR/>
 * 
 * Set value action : in dest message, dest field is valued with a specified string value.<BR/>
 * It is also possible to use a property of action execution context. In this case, the full path of the property key should be specified.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetStringValue extends ISOMsgAbstractAction implements
		IISOMsgAction {

	/**
	 * Valeur fixe à positionner
	 */
	private String value;

	/**
	 * Chemin d'une propriété du contexte à utiliser comme valeur
	 */
	private String valueBeanPath;
	
	private int fixedLength=-1;

	public ISOMsgActionSetStringValue() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		String finalValue = value;

		if (null != valueBeanPath) {
			Object srcObj;
			try {
				srcObj = PropertyUtils.getProperty(
						new SimpleContextWrapper(ctx), valueBeanPath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (null != srcObj) {
				finalValue = srcObj.toString();
			}
		}

		if (null != finalValue) {
			if (fixedLength > 0) {
				//Ajout soit d'un padding à blanc à doite soit d'un truncate à fixedLength
				if (finalValue.length() > fixedLength){
					// On fait un truncate sur la valeur.
					finalValue = finalValue.substring(0, fixedLength);
				}else{
					// on pad à doite avec des blancs
					finalValue = padright(finalValue, fixedLength, ' ');
				}
			}
			ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), finalValue,
					(IFulfillCondition) null);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueBeanPath() {
		return valueBeanPath;
	}

	public void setValueBeanPath(String valueBeanPath) {
		this.valueBeanPath = valueBeanPath;
	}

	public int getFixedLength() {
		return fixedLength;
	}

	public void setFixedLength(int fixedLength) {
		this.fixedLength = fixedLength;
	}

	
	/**
	 * pad to the right
	 * 
	 * @param s
	 *            - original string
	 * @param len
	 *            - desired len
	 * @param c
	 *            - padding char
	 * @return padded string
	 */
	public static String padright(String s, int len, char c)
			throws ISOException {
		s = s.trim();
		if (s.length() > len) {
			return s;
		}
		StringBuffer d = new StringBuffer(len);
		int fill = len - s.length();
		d.append(s);
		while (fill-- > 0)
			d.append(c);
		return d.toString();
	}
	
}
