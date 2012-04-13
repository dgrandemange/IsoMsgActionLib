package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.mockito.Mockito;
import static org.fest.assertions.Assertions.*;

import junit.framework.TestCase;

/**
 * @author dgrandemange
 *
 */
public class ISOMsgActionLoopTest extends TestCase {

	private ISOMsgActionLoop loopAction;
	private ISOMsg[] msgTab;
	private Map<String, Object> ctx;
	private IISOMsgAction childAction1;
	private IISOMsgAction childAction2;
	private ISOMsg dummyIsoMsg1;
	private ISOMsg dummyIsoMsg2;
	
	protected void setUp() throws Exception {
		super.setUp();
		loopAction = new ISOMsgActionLoop();
		loopAction.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		loopAction.mapCurrIteratorValByThread = Mockito.mock(Map.class);
		
		childAction1 = Mockito.mock(IISOMsgAction.class);
		childAction2 = Mockito.mock(IISOMsgAction.class);
		
		loopAction.add(childAction1);
		loopAction.add(childAction2);		
		
		ctx = Mockito.mock(Map.class);
		
		dummyIsoMsg1 = new ISOMsg();
		dummyIsoMsg2 = new ISOMsg();
		
		msgTab = new ISOMsg[] {dummyIsoMsg1, dummyIsoMsg2};
	}

	private void populateDummyMsgWithFields(ISOMsg dummyIsoMsg, String fieldsCommaDelimited) throws NumberFormatException, ISOException {
		String[] fieldNumberTab = fieldsCommaDelimited.split(",");
		for (String fieldNumber : fieldNumberTab) {
			dummyIsoMsg.set(Integer.parseInt(fieldNumber), "dummyValueField"+fieldNumber);
		}
	}

	public void testProcess_IntervalMode() throws ISOException {
		loopAction.setIntervalMode(true);
		loopAction.setBegin(1);
		loopAction.setEnd(4);				
		loopAction.setToken("i");
		loopAction.process(msgTab, ctx);
		Mockito.verify(childAction1, Mockito.times(4)).process(msgTab, ctx);
		Mockito.verify(childAction2, Mockito.times(4)).process(msgTab, ctx);
		for (int i=1;i<=4;i++) {
			Mockito.verify(loopAction.mapCurrIteratorValByThread, Mockito.times(1)).put(Thread.currentThread().getId(), ""+i);
		}
		assertThat(loopAction.mapCurrIteratorValByThread.containsKey(Thread.currentThread().getId())).isFalse();
	}

	public void testProcess_ISOMsgChildsIterationMode() throws NumberFormatException, ISOException {
		populateDummyMsgWithFields(dummyIsoMsg2, "5,1,12,8,15");
		loopAction.setIntervalMode(false);
		loopAction.setIdPath("");
		loopAction.setMsgIndex(1);
		loopAction.setToken("i");
		loopAction.process(msgTab, ctx);
		Mockito.verify(childAction1, Mockito.times(5)).process(msgTab, ctx);
		Mockito.verify(childAction2, Mockito.times(5)).process(msgTab, ctx);
		int[] fieldNumberTab=new int[] {5,1,12,8,15}; 
		for (int fieldNumber : fieldNumberTab) {
			Mockito.verify(loopAction.mapCurrIteratorValByThread, Mockito.times(1)).put(Thread.currentThread().getId(), ""+fieldNumber);
		}
		assertThat(loopAction.mapCurrIteratorValByThread.containsKey(Thread.currentThread().getId())).isFalse();
	}	
	
}
