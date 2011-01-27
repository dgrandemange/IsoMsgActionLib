package org.jpos.jposext.isomsgaction.helper;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;

/**
 * @author dgrandemange
 *
 */
public class ISOMsgHelperTest extends TestCase {
	
	private ISOMsg msg;
	private ISOField cmp1;
	private ISOField cmp2;
	private ISOField cmp3;
	private ISOMsg submsglvl1;
	private ISOField cmp4_1;
	private ISOField cmp4_2;
	private ISOField cmp4_3;
	private ISOMsg submsglvl2;
	private ISOField cmp4_4_1;
	private ISOField cmp4_4_2;
	private ISOField cmp4_4_3;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		msg = new ISOMsg();
		
		cmp1 = new ISOField(1,"1");
		msg.set(cmp1);
		
		cmp2 = new ISOField(2,"2");
		msg.set(cmp2);
		
		cmp3 = new ISOField(3,"3");
		msg.set(cmp3);
		
		submsglvl1 = new ISOMsg(4);

		cmp4_1 = new ISOField(1,"4_1");
		submsglvl1.set(cmp4_1);

		cmp4_2 = new ISOField(2,"4_2");
		submsglvl1.set(cmp4_2);
		
		cmp4_3 = new ISOField(3,"4_3");
		submsglvl1.set(cmp4_3);
		
		msg.set(submsglvl1);		
		
		submsglvl2 = new ISOMsg(4);		
		cmp4_4_1 = new ISOField(1,"4_4_1");
		submsglvl2.set(cmp4_4_1);

		cmp4_4_2 = new ISOField(2,"4_4_2");
		submsglvl2.set(cmp4_4_2);
		
		cmp4_4_3 = new ISOField(3,"4_4_3");
		submsglvl2.set(cmp4_4_3);
		
		submsglvl1.set(submsglvl2);		
	}	
	
	public void testGetComponent() throws ISOException {		
		assertEquals(cmp1, ISOMsgHelper.getComponent(msg, "1"));
		assertEquals(cmp2, ISOMsgHelper.getComponent(msg, "2"));
		assertEquals(cmp3, ISOMsgHelper.getComponent(msg, "3"));

		assertEquals(submsglvl1, ISOMsgHelper.getComponent(msg, "4"));
		assertEquals(cmp4_1, ISOMsgHelper.getComponent(msg, "4.1"));
		assertEquals(cmp4_2, ISOMsgHelper.getComponent(msg, "4.2"));
		assertEquals(cmp4_3, ISOMsgHelper.getComponent(msg, "4.3"));

		assertEquals(submsglvl2, ISOMsgHelper.getComponent(msg, "4.4"));
		assertEquals(cmp4_4_1, ISOMsgHelper.getComponent(msg, "4.4.1"));
		assertEquals(cmp4_4_2, ISOMsgHelper.getComponent(msg, "4.4.2"));
		assertEquals(cmp4_4_3, ISOMsgHelper.getComponent(msg, "4.4.3"));		
	}

	public void testGetComponent_SpecialDelimChar() throws ISOException {
		assertEquals(cmp1, ISOMsgHelper.getComponent(msg, "1", "-"));
		assertEquals(cmp4_2, ISOMsgHelper.getComponent(msg, "4-2", "-"));
		assertEquals(cmp4_4_3, ISOMsgHelper.getComponent(msg, "4-4-3", "-"));		
	}		
	
	public void testSetComponent() throws ISOException {
		ISOField replacementCmp2 = new ISOField(2, "replaced");
		ISOMsgHelper.setComponent(msg, "2", replacementCmp2);
		assertEquals(replacementCmp2, ISOMsgHelper.getComponent(msg, "2"));
		
		ISOField replacementCmp4_3 = new ISOField(3, "replaced");
		ISOMsgHelper.setComponent(msg, "4.3", replacementCmp4_3);
		assertEquals(replacementCmp4_3, ISOMsgHelper.getComponent(msg, "4.3"));
		
		ISOField replacementCmp4_4_1 = new ISOField(1, "replaced");
		ISOMsgHelper.setComponent(msg, "4.4.1", replacementCmp4_4_1);
		assertEquals(replacementCmp4_4_1, ISOMsgHelper.getComponent(msg, "4.4.1"));		
	}
	
	public void testGetValue() throws ISOException {
		assertEquals("3", ISOMsgHelper.getStringValue(msg, "3"));	
		
		assertEquals(null, ISOMsgHelper.getStringValue(msg, "50"));
		
		assertEquals("4_2", ISOMsgHelper.getStringValue(msg, "4.2"));
		
		assertEquals(null, ISOMsgHelper.getStringValue(msg, "4.50"));
		
		assertEquals("4_4_1", ISOMsgHelper.getStringValue(msg, "4.4.1"));
		
		assertEquals(null, ISOMsgHelper.getStringValue(msg, "4.4.50"));		
	}
	
	public void testSetValue() throws ISOException {
		String valueChamp3ToSet = "replaced value champ 3 present";
		ISOMsgHelper.setValue(msg, "3", valueChamp3ToSet);
		assertEquals(valueChamp3ToSet, msg.getValue(3));
		
		String valueChamp50ToSet = "replaced value champ 50 non present";
		ISOMsgHelper.setValue(msg, "50", valueChamp50ToSet);
		assertEquals(valueChamp50ToSet, msg.getValue(50));
	
		String valueChamp4_2ToSet = "replaced value champ 4_2 present";
		ISOMsgHelper.setValue(msg, "4.2", valueChamp4_2ToSet);
		assertEquals(valueChamp4_2ToSet, submsglvl1.getValue(2));

		String valueChamp4_50ToSet = "replaced value champ 4_50 non present";
		ISOMsgHelper.setValue(msg, "4.50", valueChamp4_50ToSet);
		assertEquals(valueChamp4_50ToSet, submsglvl1.getValue(50));

		String valueChamp4_4_1ToSet = "replaced value champ 4_4_1 present";
		ISOMsgHelper.setValue(msg, "4.4.1", valueChamp4_4_1ToSet);
		assertEquals(valueChamp4_4_1ToSet, submsglvl2.getValue(1));

		String valueChamp4_4_50ToSet = "replaced value champ 4_4_50 non present";
		ISOMsgHelper.setValue(msg, "4.4.50", valueChamp4_4_50ToSet);
		assertEquals(valueChamp4_4_50ToSet, submsglvl2.getValue(50));		
	}
	
	public void testSetValue_FulfilledConditionRequired() throws ISOException {
		String valueChamp3ToSet = "replaced value champ 3 present";
		ISOMsgHelper.setValue(msg, "3", valueChamp3ToSet, new ISOMsgHelper.IFulfillCondition() {

			@Override
			public boolean isConditionFulfilled(ISOMsg msg, int id) {
				return false;
			}
			
		});
		assertNotSame(valueChamp3ToSet, msg.getValue(3));
		assertEquals(cmp3.getValue(), msg.getValue(3));
		
		ISOMsgHelper.setValue(msg, "3", valueChamp3ToSet, new ISOMsgHelper.IFulfillCondition() {

			@Override
			public boolean isConditionFulfilled(ISOMsg msg, int id) {
				return true;
			}
			
		});
		assertEquals(valueChamp3ToSet, msg.getValue(3));
	}

	public void testUnsetValue() throws ISOException {
		ISOMsgHelper.unsetValue(msg, "3");
		assertTrue(msg.hasField(1));
		assertTrue(msg.hasField(2));
		assertFalse(msg.hasField(3));
		assertTrue(msg.hasField(4));
		
		ISOMsgHelper.unsetValue(msg, "4.2");
		assertTrue(submsglvl1.hasField(1));
		assertFalse(submsglvl1.hasField(2));
		assertTrue(submsglvl1.hasField(3));
		assertTrue(submsglvl1.hasField(4));
		
		ISOMsgHelper.unsetValue(msg, "4.4.1");
		assertFalse(submsglvl2.hasField(1));
		assertTrue(submsglvl2.hasField(2));
		assertTrue(submsglvl2.hasField(3));		
	}
	
}
