package org.jpos.jposext.isomsgaction.testing.model;

public class ISOCmpDiff {
	
	private String idPath;
	
	private String desc;

	public ISOCmpDiff(String idPath, String desc) {
		super();
		this.idPath = idPath;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return String.format("%s", desc);
	}

	public String getIdPath() {
		return idPath;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
