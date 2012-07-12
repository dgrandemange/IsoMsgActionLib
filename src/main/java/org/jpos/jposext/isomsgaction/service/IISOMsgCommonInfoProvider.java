package org.jpos.jposext.isomsgaction.service;

/**
 * @author dgrandemange
 *
 */
public interface IISOMsgCommonInfoProvider {

	public int getSrcMsgIndex();
	
	public void setSrcMsgIndex(int srcMsgIndex);
	
	public String getIdPath();
	
	public void setIdPath(String idPath);

	public String getSrcIdPath();
	
	public void setSrcIdPath(String srcIdPath);
	
	public int getMsgIndex();
	
	public void setMsgIndex(int msgIndex);

	public boolean isBinary();
	
	public void setBinary(boolean binary);
	
	public String getValue();

	public void setValue(String value);

	public String getValueBeanPath();

	public void setValueBeanPath(String valueBeanPath);
}