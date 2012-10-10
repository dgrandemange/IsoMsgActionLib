package org.jpos.jposext.isomsgaction.service.support;

import java.util.List;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Test presence of validation errors in iso action context<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfValidationErrors extends
		ISOMsgAbstractIfAction {
	
	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IFulfillCondition#isConditionFulfilled(org.jpos.iso.ISOMsg, int)
	 */
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		
		int errorCount = 0;
		
		Object object = ctx.get(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME);
		if ((null != object) && (object instanceof List)) {
			errorCount = ((List) object).size();
		}
		
		boolean conditionFulfilled = (errorCount > 0);

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
	
}
