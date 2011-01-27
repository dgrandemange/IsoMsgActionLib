package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class ISOMsgActionIfPresentTest extends TestCase {

	private ISOMsgActionIfPresent action;

	private ISOMsg msg;	

	private Map<String, Object> ctx;	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionIfPresent();

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
	}

	public void testChampPresent() throws ISOException, ParseException {
		action.setIdPath("4");		
		action.process(msg, ctx);		
		assertNotNull(ctx.get("conditionFulfilled"));
	}
	
	public void testChampNonPresent() throws ISOException, ParseException {
		action.setIdPath("5");		
		action.process(msg, ctx);		
		assertNull(ctx.get("conditionFulfilled"));
	}	
	
	public void testChampPresentAvecApplicationOperateurNot() throws ISOException, ParseException {
		action.setIdPath("4");	
		action.setApplyNotOperator(true);		
		action.process(msg, ctx);		
		assertNull(ctx.get("conditionFulfilled"));
	}	

	public void testSousChampNonPresent() throws ISOException, ParseException {
		action.setIdPath("6.4");		
		action.process(msg, ctx);			
		assertNull(ctx.get("conditionFulfilled"));
	}		
}
