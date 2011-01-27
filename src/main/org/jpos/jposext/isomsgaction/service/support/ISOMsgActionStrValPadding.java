package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.PadDirectionEnum;

/**
 * 
 * Field value padding action
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionStrValPadding extends ISOMsgAbstractAction {

	private String padChar;

	private PadDirectionEnum padDir;

	private int expLen;

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		String res = null;
		String strVal = ISOMsgHelper.getStringValue(msg[getMsgIndex()],
				getIdPath());
		if (null == strVal) {
			strVal="";
		}
		
		if (PadDirectionEnum.LEFT.equals(padDir)) {
			res = padleft(strVal, expLen, padChar.charAt(0));
		} else {
			res = padright(strVal, expLen, padChar.charAt(0));
		}
		
		// ... et positionnement de la valeur du champ cible
		ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), res);
	}

	public String getPadChar() {
		return padChar;
	}

	public void setPadChar(String padChar) {
		this.padChar = padChar;
	}

	public PadDirectionEnum getPadDir() {
		return padDir;
	}

	public void setPadDir(PadDirectionEnum padDir) {
		this.padDir = padDir;
	}

	public int getExpLen() {
		return expLen;
	}

	public void setExpLen(int expLen) {
		this.expLen = expLen;
	}

	/**
	 * pad to the left
	 * 
	 * @param s
	 *            - original string
	 * @param len
	 *            - desired len
	 * @param c
	 *            - padding char
	 * @return padded string
	 */
	public static String padleft(String s, int len, char c) throws ISOException {
		s = s.trim();
		if (s.length() > len) {
			return s;
		}
		StringBuffer d = new StringBuffer(len);
		int fill = len - s.length();
		while (fill-- > 0)
			d.append(c);
		d.append(s);
		return d.toString();
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
