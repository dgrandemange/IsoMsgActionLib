package org.jpos.jposext.isomsgaction.testing.service.support;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;
import org.jpos.jposext.isomsgaction.testing.model.ISOCmpDiff;
import org.jpos.jposext.isomsgaction.testing.model.ManualCheck;

public class ISOComponentComparatorTest extends TestCase {

	private ISOMsg expectedMsg;
	private ISOMsg srcMsg;
	private ISOComponentComparator cmp;
	private ComparisonContext comparisonContext;
	private Map<String, ManualCheck> mapManualChecks;
	private List<ISOCmpDiff> diffList;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
				
		comparisonContext = new ComparisonContext();
		mapManualChecks = new TreeMap<String, ManualCheck>();
		comparisonContext.setMapManualChecks(mapManualChecks);
		diffList = new ArrayList<ISOCmpDiff>();
		comparisonContext.setResList(diffList);
		cmp = new ISOComponentComparator(comparisonContext, "", "");
		
		expectedMsg = new ISOMsg();
		
		expectedMsg.set(1, "valeur1");
		
		ISOMsg submsg2 = new ISOMsg(2);
		submsg2.set(1, "valeur2_1");
		submsg2.set(2, "valeur2_2");
		ISOBinaryField binaryField2_3= new ISOBinaryField(3, new byte[] {0x01, 0x02, 0x03, 0x04, 0x05});
		submsg2.set(binaryField2_3);
		expectedMsg.set(submsg2);
		
		expectedMsg.recalcBitMap();
		srcMsg = (ISOMsg) expectedMsg.clone();
	}
	
	protected void dumpAndClear(PrintStream out, List<ISOCmpDiff> pDiffList) {
		for (ISOCmpDiff diff : pDiffList) {
			out.println(diff);
		}
		pDiffList.clear();
	}

	protected boolean hasDiff(ComparisonContext pComparisonContext,
			String idPath) {
		for (ISOCmpDiff current : pComparisonContext.getResList()) {
			if (idPath.equals(current.getIdPath())) {
				return true;
			}
		}
		return false;
	}		
	
	public void testCompare_Nominal() {
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		dumpAndClear(System.out, comparisonContext.getResList());
	}

	public void testCompare_Bitmap() throws ISOException {					
		srcMsg.unset(1);
		srcMsg.recalcBitMap();
		
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "1"));
		dumpAndClear(System.out, comparisonContext.getResList());
	}	

	public void testCompare_BitmapCase2() throws ISOException {					
		srcMsg.unset(1);
		srcMsg.recalcBitMap();
		
		cmp.setSkipBitmapComparison(false);
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "-1"));
		assertTrue(hasDiff(comparisonContext, "1"));
		dumpAndClear(System.out, comparisonContext.getResList());
	}	
	
	public void testCompare_Case1() throws ISOException {
		ISOMsg submsg2 = (ISOMsg) srcMsg.getComponent(2);
		submsg2.unset(1);		

		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.1"));
		dumpAndClear(System.out, comparisonContext.getResList());

		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.1"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		cmp.setSkipBitmapComparison(true);
		mapManualChecks.put("2", null);
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		assertFalse(hasDiff(comparisonContext, "2.1"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		mapManualChecks.remove("2");
		mapManualChecks.put("2.2", null);		
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.1"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		mapManualChecks.put("2.1", null);		
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		assertFalse(hasDiff(comparisonContext, "2.1"));
		dumpAndClear(System.out, comparisonContext.getResList());
	}

	public void testCompare_Case2() throws ISOException {
		ISOMsg submsg2 = (ISOMsg) srcMsg.getComponent(2);
		ISOBinaryField newBinaryField2_3= new ISOBinaryField(3, new byte[] {0x06, 0x07, 0x08, 0x09, 0x0A});
		submsg2.set(newBinaryField2_3);		
		submsg2.set(newBinaryField2_3);		

		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.3"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		mapManualChecks.put("2.3", null);
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		assertFalse(hasDiff(comparisonContext, "2.3"));
		dumpAndClear(System.out, comparisonContext.getResList());		
	}
	
	public void testCompare_Case3() throws ISOException {
		ISOMsg submsg2 = (ISOMsg) srcMsg.getComponent(2);
		submsg2.set(4, "valeur2_4");
		
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.4"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		mapManualChecks.put("2.4", null);
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		assertFalse(hasDiff(comparisonContext, "2.4"));
		dumpAndClear(System.out, comparisonContext.getResList());
	}	

	public void testCompare_Case4() throws ISOException {
		ISOMsg submsg2 = (ISOMsg) expectedMsg.getComponent(2);
		submsg2.set(4, "valeur2_4");
		
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "2.4"));
		dumpAndClear(System.out, comparisonContext.getResList());
		
		mapManualChecks.put("2.4", null);
		assertEquals(0, cmp.compare(expectedMsg, srcMsg));
		assertFalse(hasDiff(comparisonContext, "2"));
		dumpAndClear(System.out, comparisonContext.getResList());
	}	

	public void testCompare_Case5() throws ISOException {
		srcMsg.set(1, "new_value_field1");		
		
		assertEquals(-1, cmp.compare(expectedMsg, srcMsg));
		assertTrue(hasDiff(comparisonContext, "1"));
		dumpAndClear(System.out, comparisonContext.getResList());	
	}
}
