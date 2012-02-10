package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class ISOMsgActionIfMatchesRegExpTest extends TestCase {

	private ISOMsgActionIfMatchesRegExp action;

	private ISOMsg msg;	

	private Map<String, Object> ctx;	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionIfMatchesRegExp();

		action.setChilds(new ArrayList<IISOMsgAction>());

		action.add(new IISOMsgAction() {

			@Override
			public void process(ISOMsg[] msg, Map<String, Object> ctx)
					throws ISOException {
				setResultToTrue(ctx);
			}

			@Override
			public void process(ISOMsg msg, Map<String, Object> ctx)
					throws ISOException {
				setResultToTrue(ctx);
			}

			private void setResultToTrue(Map<String, Object> ctx) {
				ctx.put("conditionFulfilled", "conditionFulfilled");
			}

		});

		ctx = new HashMap<String, Object>();		
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "opt3" } });
		
		action.setIdPath("4");		
	}

	public void testMatch1() throws ISOException, ParseException {		
		action.setRegexp("^opt3$");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}
	
	public void testMatch2() throws ISOException, ParseException {		
		action.setRegexp("^__opt3$");
		action.process(msg, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}	
	
	public void testMatch3() throws ISOException, ParseException {		
		action.setRegexp("^(opt[1-9])$");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}
	
	public void testMatch4() throws ISOException, ParseException {
		action.setIdPath("5");
		action.setRegexp("^opt3$");
		action.process(msg, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}	
}
