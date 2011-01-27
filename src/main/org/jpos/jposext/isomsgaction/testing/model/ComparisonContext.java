package org.jpos.jposext.isomsgaction.testing.model;

import java.util.List;
import java.util.Map;

public class ComparisonContext {
	
	private Map<String, ManualCheck> mapManualChecks;
	
	private List<String> resList;	

	public List<String> getResList() {
		return resList;
	}

	public void setResList(List<String> resList) {
		this.resList = resList;
	}

	public Map<String, ManualCheck> getMapManualChecks() {
		return mapManualChecks;
	}

	public void setMapManualChecks(Map<String, ManualCheck> mapManualChecks) {
		this.mapManualChecks = mapManualChecks;
	}
	
}
