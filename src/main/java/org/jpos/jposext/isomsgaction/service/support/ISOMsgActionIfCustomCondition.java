package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.factory.service.ICustomCondition;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * If action using a custom condition
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionIfCustomCondition extends ISOMsgAbstractIfAction {

	private String customConditionClazzName;

	private ICustomCondition customCondition;
	
	public ISOMsgActionIfCustomCondition() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.support.ISOMsgAbstractIfAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		
		boolean conditionFulfilled = customCondition.isConditionFulfilled(msg, ctx);

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

	public String getCustomConditionClazzName() {
		return customConditionClazzName;
	}

	public void setCustomConditionClazzName(String customConditionClazzName) {
		this.customConditionClazzName = customConditionClazzName;
	}

	/**
	 * @return the customCondition
	 */
	public ICustomCondition getCustomCondition() {
		return customCondition;
	}

	/**
	 * @param customCondition the customCondition to set
	 */
	public void setCustomCondition(ICustomCondition customCondition) {
		this.customCondition = customCondition;
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IFulfillCondition#isConditionFulfilled(org.jpos.iso.ISOMsg, int)
	 */
	public boolean isConditionFulfilled(ISOMsg msg, int id) {
		throw new RuntimeException("Shouldn't be invoked here !");
	}

}
