package org.jpos.jposext.isomsgaction.service.support.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionLoop;

/**
 * @author dgrandemange
 * 
 */
public class LoopHandler implements InvocationHandler {

	private ISOMsgActionLoop[] loopActions;

	private IISOMsgCommonInfoProvider realInfoProvider;

	public LoopHandler(ISOMsgActionLoop[] loopActions,
			IISOMsgCommonInfoProvider realAction) {
		super();
		this.loopActions = loopActions;
		this.realInfoProvider = realAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName = method.getName();

		if ("getIdPath".equals(methodName)) {
			return this.getIdPath();
		} else if ("getSrcIdPath".equals(methodName)) {
			return this.getSrcIdPath();
		} else {
			return method.invoke(realInfoProvider, args);
		}
	}

	public String getIdPath() {
		String idPath = realInfoProvider.getIdPath();
		return replace(idPath, loopActions);
	}

	public String getSrcIdPath() {
		String srcIdPath = realInfoProvider.getSrcIdPath();
		return replace(srcIdPath, loopActions);
	}

	protected String replace(String idPath, ISOMsgActionLoop[] pLoopActions) {
		if (null == idPath) {
			return null;
		}

		String res = idPath;

		for (ISOMsgActionLoop loopAction : pLoopActions) {
			String token = loopAction.getToken();
			String currIteratorVal = loopAction
					.findCurrIteratorValByThread(Thread.currentThread().getId());

			if ((null != currIteratorVal) && (null != token)) {
				res = res.replace("${" + token + "}", currIteratorVal);
			} else {
				break;
			}
		}

		return res;
	}

	/**
	 * @param loopActions
	 *            the loopActions to set
	 */
	public void setLoopActions(ISOMsgActionLoop[] loopActions) {
		this.loopActions = loopActions;
	}

	/**
	 * @param realInfoProvider
	 *            the realInfoProvider to set
	 */
	public void setRealInfoProvider(IISOMsgCommonInfoProvider realInfoProvider) {
		this.realInfoProvider = realInfoProvider;
	}

	/**
	 * @return the realInfoProvider
	 */
	public IISOMsgCommonInfoProvider getRealInfoProvider() {
		return realInfoProvider;
	}

}
