package org.jpos.jposext.isomsgaction.service.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.factory.service.ICustomCondition;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.mockito.Mockito;

public class ISOMsgActionIfCustomConditionTest extends TestCase {

	private ICustomCondition customCondition;
	private ISOMsg[] dummyMsgTab;
	private Map<String, Object> dummyActionContext;
	private ISOMsgActionIfCustomCondition action;
	private IISOMsgAction childAction;
	private IISOMsgAction elseAction;
	
	protected void setUp() throws Exception {
		super.setUp();
		customCondition = Mockito.mock(ICustomCondition.class);
		dummyMsgTab = new ISOMsg[] {};
		dummyActionContext = new HashMap<String, Object>();
		
		action = new ISOMsgActionIfCustomCondition();
		action.setCustomCondition(customCondition);
		
		childAction = Mockito.mock(IISOMsgAction.class);
		List<IISOMsgAction> childs = new ArrayList<IISOMsgAction>();
		childs.add(childAction);
		action.setChilds(childs);
		
		elseAction = Mockito.mock(IISOMsgAction.class);
	}

	public void testProcess_ConditionFulfilled() throws ISOException {
		action.setElseAction(elseAction);
		
		Mockito.when(customCondition.isConditionFulfilled(dummyMsgTab, dummyActionContext)).thenReturn(true);
		action.process(dummyMsgTab, dummyActionContext);
		Mockito.verify(childAction, Mockito.times(1)).process(dummyMsgTab, dummyActionContext);
		Mockito.verify(elseAction, Mockito.times(0)).process(dummyMsgTab, dummyActionContext);
	}

	public void testProcess_ConditionFulfilled_ApplyNotOperator() throws ISOException {
		action.setElseAction(elseAction);
		
		Mockito.when(customCondition.isConditionFulfilled(dummyMsgTab, dummyActionContext)).thenReturn(true);
		action.setApplyNotOperator(true);
		action.process(dummyMsgTab, dummyActionContext);
		Mockito.verify(childAction, Mockito.times(0)).process(dummyMsgTab, dummyActionContext);
		Mockito.verify(elseAction, Mockito.times(1)).process(dummyMsgTab, dummyActionContext);
	}	
	
	public void testProcess_ConditionNotFulfilled() throws ISOException {
		action.setElseAction(elseAction);
		
		Mockito.when(customCondition.isConditionFulfilled(dummyMsgTab, dummyActionContext)).thenReturn(false);
		action.process(dummyMsgTab, dummyActionContext);
		Mockito.verify(childAction, Mockito.times(0)).process(dummyMsgTab, dummyActionContext);
		Mockito.verify(elseAction, Mockito.times(1)).process(dummyMsgTab, dummyActionContext);
	}

	public void testProcess_ConditionNotFulfilled_NoElseActionDefined() throws ISOException {
		Mockito.when(customCondition.isConditionFulfilled(dummyMsgTab, dummyActionContext)).thenReturn(false);
		action.process(dummyMsgTab, dummyActionContext);
		Mockito.verify(childAction, Mockito.times(0)).process(dummyMsgTab, dummyActionContext);
		Mockito.verify(elseAction, Mockito.times(0)).process(dummyMsgTab, dummyActionContext);
	}
	
	
}
