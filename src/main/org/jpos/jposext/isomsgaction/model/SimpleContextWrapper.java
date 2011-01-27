package org.jpos.jposext.isomsgaction.model;

import java.util.Map;

public class SimpleContextWrapper {
	private Map<String, Object> ctx;

	public SimpleContextWrapper(Map<String, Object> ctx) {
		super();
		this.ctx = ctx;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}
	
}
