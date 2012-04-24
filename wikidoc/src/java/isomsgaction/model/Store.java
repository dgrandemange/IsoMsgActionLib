package isomsgaction.model;

/**
 * @author dgrandemange
 * 
 */
public class Store {

	private int id;

	private String name;

	private String number;

	private byte[] someByteArray;

	private byte[] secondByteArray;
	
	private String[] strArray;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the someByteArray
	 */
	public byte[] getSomeByteArray() {
		return someByteArray;
	}

	/**
	 * @param someByteArray
	 *            the someByteArray to set
	 */
	public void setSomeByteArray(byte[] someByteArray) {
		this.someByteArray = someByteArray;
	}

	/**
	 * @return the secondByteArray
	 */
	public byte[] getSecondByteArray() {
		return secondByteArray;
	}

	/**
	 * @param secondByteArray
	 *            the secondByteArray to set
	 */
	public void setSecondByteArray(byte[] secondByteArray) {
		this.secondByteArray = secondByteArray;
	}

	/**
	 * @return the strArray
	 */
	public String[] getStrArray() {
		return strArray;
	}

	/**
	 * @param strArray the strArray to set
	 */
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}

}
