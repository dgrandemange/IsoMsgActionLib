package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.exception.ParentMsgDoesNotExistException;
import org.jpos.jposext.isomsgaction.helper.CmpInfoWrapper;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IFulfillCondition;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * Base class for all actions that needs to test a condition
 * 
 * @author dgrandemange
 * 
 */
public abstract class ISOMsgAbstractIfAction extends ISOMsgCompositeAction
		implements IFulfillCondition {

	private IISOMsgAction elseAction;

	private boolean applyNotOperator = false;

	public ISOMsgAbstractIfAction() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		ISOMsg cmpParentMsg = null;
		int id = -1;

		try {
			CmpInfoWrapper cmpInfos = ISOMsgHelper.findParentMsg(
					msg[getSrcMsgIndex()], getIdPath(), getIdPath());
			cmpParentMsg = cmpInfos.getCmpParentMsg();
			String sid = cmpInfos.getId();
			id = Integer.parseInt(sid);
		} catch (ParentMsgDoesNotExistException e) {
		}
		
		boolean conditionFulfilled = isConditionFulfilled(cmpParentMsg, id);

		if (applyNotOperator ? (!conditionFulfilled) : conditionFulfilled) {
			for (IISOMsgAction child : getChilds()) {
				child.process(msg, ctx);
			}
		} else {
			if (null != elseAction) {
				elseAction.process(msg, ctx);
			}
		}
	}

	public IISOMsgAction getElseAction() {
		return elseAction;
	}

	public void setElseAction(IISOMsgAction elseAction) {
		this.elseAction = elseAction;
	}

	public boolean isApplyNotOperator() {
		return applyNotOperator;
	}

	public void setApplyNotOperator(boolean applyNotOperator) {
		this.applyNotOperator = applyNotOperator;
	}

}
