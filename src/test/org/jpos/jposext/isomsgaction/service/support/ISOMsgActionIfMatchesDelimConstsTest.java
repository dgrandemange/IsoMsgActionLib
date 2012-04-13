package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class ISOMsgActionIfMatchesDelimConstsTest extends TestCase {

	private ISOMsgActionIfMatchesDelimConsts action;

	private ISOMsg msg;

	private Map<String, Object> ctx;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionIfMatchesDelimConsts();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
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
		action.setValuesToMatch("opt1 opt2 opt3 opt4 opt5");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testMatch2() throws ISOException, ParseException {
		action.setValuesToMatch("opt1 opt2");
		action.process(msg, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

	public void testMatch3() throws ISOException, ParseException {
		action.setValuesToMatch("opt3 opt1 opt2");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testMatch4() throws ISOException, ParseException {
		action.setValuesToMatch("opt1 opt2 opt3");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testMatch5() throws ISOException, ParseException {
		action.setValuesToMatch("opt3");
		action.process(msg, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testMatch6() throws ISOException, ParseException {
		action.setElseAction(new IISOMsgAction() {

			@Override
			public void process(ISOMsg[] msg, Map<String, Object> ctx)
					throws ISOException {
				setResultToFalse(ctx);
			}

			@Override
			public void process(ISOMsg msg, Map<String, Object> ctx)
					throws ISOException {
				setResultToFalse(ctx);
			}

			private void setResultToFalse(Map<String, Object> ctx) {
				ctx.put("conditionNotFulfilled", "conditionNotFulfilled");
			}

		});

		action.setIdPath("5");
		action.setValuesToMatch("opt1 opt2");
		action.process(msg, ctx);
		
		assertNull(ctx.get("conditionFulfilled"));
		assertNotNull(ctx.get("conditionNotFulfilled"));
	}

	public void testMatch7() throws ISOException, ParseException {
		action.setIdPath("5");
		action.setValuesToMatch("opt3");
		action.process(msg, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}	
	
}
