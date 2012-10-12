package org.jpos.jposext.isomsgaction.service.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * @author dgrandemange
 *
 */
public class ISOMsgActionIfSetInContextTest extends TestCase {

	private static final String DUMMY_BEAN_KEY = "dummyBean";

	private ISOMsgActionIfSetInContext action;
	
	private Map<String, Object> ctx;	
	
	protected void setUp() throws Exception {
		super.setUp();
		
		action = new ISOMsgActionIfSetInContext();
		
		action.setKey(DUMMY_BEAN_KEY);
		
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		action.setChilds(new ArrayList<IISOMsgAction>());

		action.add(new IISOMsgAction() {

			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
			 */
			public void process(ISOMsg[] msg, Map<String, Object> ctx)
					throws ISOException {
				putSomeFlag(ctx);
			}

			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg, java.util.Map)
			 */
			public void process(ISOMsg msg, Map<String, Object> ctx)
					throws ISOException {
				putSomeFlag(ctx);
			}

			private void putSomeFlag(Map<String, Object> ctx) {
				ctx.put("conditionFulfilled", "conditionFulfilled");
			}

		});
		
		ctx = new HashMap<String, Object>();		
	}

	public void testProcess_BeanIsSet() throws ISOException {
		ctx.put(DUMMY_BEAN_KEY, new Object());
		action.process((ISOMsg) null, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testProcess_BeanIsSetButNull() throws ISOException {
		ctx.put(DUMMY_BEAN_KEY, null);
		action.process((ISOMsg) null, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

	public void testProcess_BeanIsNotSet() throws ISOException {
		action.process((ISOMsg) null, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

}
