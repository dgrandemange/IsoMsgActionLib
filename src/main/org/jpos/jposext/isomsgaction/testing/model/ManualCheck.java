package org.jpos.jposext.isomsgaction.testing.model;

public class ManualCheck {
	
	private String idPath;
	
	private String desc;

	public ManualCheck(String idPath, String desc) {
		super();
		this.idPath = idPath;
		this.desc = desc;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}	
	
}
