package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Test if bean is set in the iso action context<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfSetInContext extends
		ISOMsgAbstractIfAction {
	
	private String key;
	
	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IFulfillCondition#isConditionFulfilled(org.jpos.iso.ISOMsg, int)
	 */
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		return true;
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		
		Object object = ctx.get(key);
		
		boolean conditionFulfilled = (object != null);

		if (isApplyNotOperator() ? (!conditionFulfilled) : conditionFulfilled) {
			for (IISOMsgAction child : getChilds()) {
				child.process(msg, ctx);
			}
		} else {
			if (null != getElseAction()) {
				getElseAction().process(msg, ctx);
			}
		}
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
}
