package org.jpos.jposext.isomsgaction.testing.model;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

public class ComparisonContextTest extends TestCase {
	
	private Map<String, ManualCheck> mapManualChecks;

	protected void setUp() throws Exception {
		super.setUp();
		mapManualChecks = new TreeMap<String, ManualCheck>();

		mapManualChecks.put("1", null);		
	}
	
	protected String[] splitIdPath(String idPath) {
		return idPath.split("\\" + ComparisonContext.DEFAULT_ID_PATH_DELIMITER);
	}
	
	public void testIsManualCheck_Case1() {
		String idPath="1";		
		assertTrue(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		
	}

	public void testIsManualCheck_Case2() {
		String idPath="1.2.3";				
		assertTrue(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		
	}
	
	public void testIsManualCheck_Case3() {
		mapManualChecks.put("2.3", null);	
		
		String idPath="2";				
		assertFalse(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		

		idPath="2.1";				
		assertFalse(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		

		idPath="2.3";				
		assertTrue(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		

		idPath="2.3.5";				
		assertTrue(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		
	}	
	
	public void testIsManualCheck_Case4() {
		String idPath="-1";				
		assertFalse(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));
		
		mapManualChecks.put("-1", null);
		idPath="-1";				
		assertTrue(ComparisonContext.isManualCheck(mapManualChecks, splitIdPath(idPath), 0));		
	}	
}
