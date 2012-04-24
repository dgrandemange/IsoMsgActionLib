package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.SimpleContextWrapper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Action that populates an attribute of the execution context using a message
 * field<BR/>
 * 
 * Set value action : in dest message, dest field is valued with a specified
 * string value.<BR/>
 * It is also possible to use a property of action execution context. In this
 * case, the full path of the property key should be specified.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionUpdateExecutionContext extends ISOMsgAbstractAction
		implements IISOMsgAction {

	private int fixedLength = -1;

	public ISOMsgActionUpdateExecutionContext() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Récupération de la valeur du champ source ...
		String attrValue = ISOMsgHelper.getStringValue(msg[getSrcMsgIndex()],
				getSrcIdPath());

		if (null != attrValue) {
			if (fixedLength > 0) {
				// Ajout soit d'un padding à blanc à doite soit d'un truncate à
				// fixedLength
				if (attrValue.length() > fixedLength) {
					// On fait un truncate sur la valeur.
					attrValue = attrValue.substring(0, fixedLength);
				} else {
					// on pad à doite avec des blancs
					attrValue = padright(attrValue, fixedLength, ' ');
				}
			}
		}
		
		if (null != getValueBeanPath()) {			
			try {
				PropertyUtils.setProperty(new SimpleContextWrapper(ctx), getValueBeanPath(), attrValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
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
