package org.jpos.jposext.isomsgaction.service.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Composite action implementation (follwing compoiste design pattern. See GoF)
 * Default behaviour is : all child action classes are processed one after
 * another, following childs list order.<BR/>
 * If one child execution throws an exception, processing is stopped unless
 * execption is ISOException and ignoreISOExceptions is true.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgCompositeAction extends ISOMsgAbstractAction implements
		IISOMsgAction {

	/**
	 * Actions filles
	 */
	private List<IISOMsgAction> childs = new ArrayList<IISOMsgAction>();

	/**
	 * Indique si l'action maman doit ignorer les ISOExceptions remontées par
	 * ses actions filles
	 */
	private boolean ignoreISOExceptions = false;

	public ISOMsgCompositeAction() {
		super();
	}

	public ISOMsgCompositeAction(List<IISOMsgAction> childs,
			boolean ignoreISOExceptions) {
		super();
		this.childs = childs;
		this.ignoreISOExceptions = ignoreISOExceptions;
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> map)
			throws ISOException {
		for (IISOMsgAction action : childs) {
			try {
				action.process(msg, map);
			} catch (ISOException e) {
				if (!ignoreISOExceptions) {
					throw e;
				}
			}
		}
	}

	protected List<IISOMsgAction> getChilds() {
		return childs;
	}

	public void setChilds(List<IISOMsgAction> childs) {
		this.childs = childs;
	}

	public void add(IISOMsgAction action) {
		childs.add(action);
	}

	public void remove(IISOMsgAction action) {
		childs.remove(action);
	}

	public IISOMsgAction get(int idx) {
		return childs.get(idx);
	}

	public boolean isIgnoreISOExceptions() {
		return ignoreISOExceptions;
	}

	public void setIgnoreISOExceptions(boolean ignoreISOExceptions) {
		this.ignoreISOExceptions = ignoreISOExceptions;
	}

}
