package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import junit.framework.TestCase;

public class ISOMsgActionUpdateExecutionContextTest extends TestCase {
	
	private ISOMsgActionUpdateExecutionContext action;

	private ISOMsg msg;	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionUpdateExecutionContext();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });

	}
	
	public void testUpdateContextAttribute() throws ISOException, ParseException {
		String valueBeanPath = "valueProp";		
		Map<String, Object> ctx = new HashMap<String, Object>();		
		action.setSrcIdPath("4");
		action.setSrcMsgIndex(0);
		action.setValueBeanPath("ctx(" + valueBeanPath + ")");
		action.process(new ISOMsg[] {msg}, ctx);

		assertEquals("valeur4_source", ctx.get(valueBeanPath));
	}
	
	public void testUpdateContextAttribute_FixedLength() throws ISOException, ParseException {
		String valueBeanPath = "valueProp";		
		Map<String, Object> ctx = new HashMap<String, Object>();		
		action.setSrcIdPath("4");
		action.setSrcMsgIndex(0);
		action.setFixedLength(30);
		action.setValueBeanPath("ctx(" + valueBeanPath + ")");
		action.process(new ISOMsg[] {msg}, ctx);

		assertEquals("valeur4_source                ", ctx.get(valueBeanPath));
	}
	
}
