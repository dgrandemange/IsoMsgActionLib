package isomsgaction.model;

import org.jpos.iso.ISOUtil;

/**
 * Facility used to inject byte array into a wrapped Store<BR>
 * Injection methods take hex strings as parameter
 * 
 * @author dgrandemange
 * 
 */
public class StoreWrapper {

	/**
	 * The holy grail
	 */
	private Store store;

	/**
	 * @return the store
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * @param hexaSomeByteArray
	 */
	public void setStoreByteArrayInjector(String hexaSomeByteArray) {
		store.setSomeByteArray(ISOUtil.hex2byte(hexaSomeByteArray));
	}

	/**
	 * @param hexaSecondByteArray
	 */
	public void setStoreSecondByteArrayInjector(String hexaSecondByteArray) {
		store.setSecondByteArray(ISOUtil.hex2byte(hexaSecondByteArray));
	}

	/**
	 * @return the someByteArray
	 */
	public String getStoreByteArrayInjector() {
		return ISOUtil.hexString(store.getSomeByteArray());
	}

	/**
	 * @return the secondByteArray
	 */
	public String getStoreSecondByteArrayInjector() {
		return ISOUtil.hexString(store.getSecondByteArray());
	}

	/**
	 * @return the strArray
	 */
	public String getStrArray() {
		String strs[] =  store.getStrArray();
		if (null == strs) {
			return null;
		} else {
			StringBuffer strArrayPipeDelim = new StringBuffer();
			String sep="";
			for (String str : strs) {
				strArrayPipeDelim.append(sep);
				strArrayPipeDelim.append(str);
				sep="|";	
			}
			return strArrayPipeDelim.toString();
		}		
	}

	/**
	 * @param strArray the strArray to set
	 */
	public void setStrArray(String strArrayPipeDelim) {
		if (null == strArrayPipeDelim) {
			store.setStrArray(new String[] {});
		} else {
			String strs[] =  strArrayPipeDelim.split("\\|");			
			store.setStrArray(strs);
		}
	}
	
}
