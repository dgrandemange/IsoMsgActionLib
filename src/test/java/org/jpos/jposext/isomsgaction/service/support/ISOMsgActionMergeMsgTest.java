package org.jpos.jposext.isomsgaction.service.support;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionMergeMsgTest extends TestCase {

	private ISOMsgActionMergeMsg action;

	private ISOMsg msg;

	private ISOMsg submsg1;

	private ISOMsg msg2;
	
	private ISOMsg msg3;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionMergeMsg();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });

		submsg1 = new ISOMsg(5);
		ISOMsgTestHelper.populateMsg(submsg1, new String[][] {
				new String[] { "1", "sub1_valeur1" },
				new String[] { "2", "sub1_valeur2" },
				new String[] { "3", "sub1_valeur3" } });

		msg.set(submsg1);
		
		msg2 = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg2, new String[][] {
				new String[] { "1", "msg2_valeur1" },
				new String[] { "3", "msg2_valeur3" } });
		
		msg3 = new ISOMsg();
	}

	public void testMergeSimple() throws ISOException {
		action.setMsgIndex(0);
		action.setSrcMsgIndex(1);
		assertFalse(msg3.hasFields(new int[] {1,2,3,4,5}));
		
		action.process(new ISOMsg[] {msg3, msg}, null);
		
		assertTrue(msg3.hasFields(new int[] {1,2,3,4,5}));
		ISOMsg champ5 = (ISOMsg) msg3.getComponent(5);
		assertTrue(champ5.hasFields(new int[] {1,2,3}));
		assertSame((ISOMsg) msg3.getComponent(5), (ISOMsg) msg.getComponent(5));
	}

	public void testMergeSurMsgDejaPartiellementPeuple() throws ISOException {
		action.setMsgIndex(0);
		action.setSrcMsgIndex(1);
		assertFalse(msg2.hasFields(new int[] {2,4,5}));
		
		action.process(new ISOMsg[] {msg2, msg}, null);
		
		assertTrue(msg2.hasFields(new int[] {1,2,3,4,5}));
		assertEquals(msg.getString(1), msg2.getString(1));
		assertEquals(msg.getString(3), msg2.getString(3));
	}	
	
	public void testMerge_CloneSrcMsg() throws ISOException {
		action.setMsgIndex(0);
		action.setSrcMsgIndex(1);
		action.setClone(true);
		assertFalse(msg3.hasFields(new int[] {1,2,3,4,5}));
		
		action.process(new ISOMsg[] {msg3, msg}, null);
		
		assertTrue(msg3.hasFields(new int[] {1,2,3,4,5}));
		ISOMsg champ5 = (ISOMsg) msg3.getComponent(5);
		assertTrue(champ5.hasFields(new int[] {1,2,3}));
		assertNotSame((ISOMsg) msg3.getComponent(5), (ISOMsg) msg.getComponent(5));
	}	
}
