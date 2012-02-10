package org.jpos.jposext.isomsgaction.testing.model;

import java.util.List;
import java.util.Map;

public class ComparisonContext {

	public static final String DEFAULT_ID_PATH_DELIMITER = ".";

	private Map<String, ManualCheck> mapManualChecks;

	private List<ISOCmpDiff> resList;

	public List<ISOCmpDiff> getResList() {
		return resList;
	}

	public void setResList(List<ISOCmpDiff> resList) {
		this.resList = resList;
	}

	public Map<String, ManualCheck> getMapManualChecks() {
		return mapManualChecks;
	}

	public void setMapManualChecks(Map<String, ManualCheck> mapManualChecks) {
		this.mapManualChecks = mapManualChecks;
	}

	public void addDiff(String idPath, String res) {
		resList.add(new ISOCmpDiff(idPath, res));
	}

	public boolean isManualCheck(String idPath) {
		String[] pathTab = idPath.split("\\" + DEFAULT_ID_PATH_DELIMITER);
		return isManualCheck(mapManualChecks, pathTab, 0);
	}
	
	protected static boolean isManualCheck(Map<String, ManualCheck> pMapManualChecks, String[] pathTab, int idx) {
		if (pMapManualChecks == null) {
			return false;
		}

		if (pathTab.length == 0) {
			return false;
		}

		if (idx >= pathTab.length) {
			return false;
		}

		String currentPath = "";
		for (int i = 0; i <= idx; i++) {
			if (i > 0) {
				currentPath += DEFAULT_ID_PATH_DELIMITER;
			}
			currentPath += pathTab[i];
		}

		if (pMapManualChecks.containsKey(currentPath)) {
			return true;
		} else {
			return isManualCheck(pMapManualChecks, pathTab, (idx + 1));
		}

	}
}
