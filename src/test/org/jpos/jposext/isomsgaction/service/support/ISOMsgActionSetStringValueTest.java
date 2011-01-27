package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionSetStringValueTest extends TestCase {

	private ISOMsgActionSetStringValue action;

	private ISOMsg msg;	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionSetStringValue();

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });

	}

	public void testSimple() throws ISOException, ParseException {
		String value = "toto";		
		
		action.setIdPath("4");		
		action.setValue(value);
		action.process(msg, null);
		
		assertEquals(value, msg.getString(4));
	}

	public void testAvecValeurDansContexte() throws ISOException, ParseException {
		String value = "toto";		
		
		String valueBeanPath = "valueProp";		
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(valueBeanPath, new String("valeur pêcho dans le contexte"));
		action.setIdPath("4");		
		action.setValue(value);
		action.setValueBeanPath("ctx(" + valueBeanPath + ")");
		action.process(msg, ctx);
		
		assertEquals("valeur pêcho dans le contexte", msg.getString(4));
	}	
	
	public void testAvecValeurIntrouvableDansContexte() throws ISOException, ParseException {
		String value = "toto";			
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put("valueProp", new String("valeur pêcho dans le contexte"));
		action.setIdPath("4");		
		action.setValue(value);
		action.setValueBeanPath("ctx(proprieteInexistante)");
		action.process(msg, ctx);
		
		assertEquals("toto", msg.getString(4));
	}	
	
	public void testAvecfixedLengthPlusLongue() throws ISOException, ParseException {
		String value = "toto";			
		action.setIdPath("4");		
		action.setValue(value);
		action.setFixedLength(10);
		action.process(msg, null);
		assertEquals("toto      ", msg.getString(4));
	}	
	
	public void testAvecfixedLengthPlusCourte() throws ISOException, ParseException {
		String value = "toto";			
		action.setIdPath("4");		
		action.setValue(value);
		action.setFixedLength(2);
		action.process(msg, null);
		assertEquals("to", msg.getString(4));
	}	
	
}
