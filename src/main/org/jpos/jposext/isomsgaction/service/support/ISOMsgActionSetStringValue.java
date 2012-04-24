package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper.IFulfillCondition;
import org.jpos.jposext.isomsgaction.model.SimpleContextWrapper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Sets a message field with a string value<BR/>
 * 
 * Set value action : in dest message, dest field is valued with a specified string value.<BR/>
 * It is also possible to use a property of action execution context. In this case, the full path of the property key should be specified.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetStringValue extends ISOMsgAbstractAction implements
		IISOMsgAction {

	private int fixedLength=-1;

	public ISOMsgActionSetStringValue() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		String finalValue = getValue();

		if (null != getValueBeanPath()) {
			Object srcObj;
			try {
				srcObj = PropertyUtils.getProperty(
						new SimpleContextWrapper(ctx), getValueBeanPath());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (null != srcObj) {
				if (srcObj instanceof byte[]) {
					if (isBinary()) {
						finalValue = ISOUtil.hexString((byte[]) srcObj);
					} else {
						finalValue = new String((byte[]) srcObj);
					}
				}
				else {
					finalValue = srcObj.toString();
				}
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
					(IFulfillCondition) null, isBinary());
		}
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
