package org.jpos.jposext.isomsgaction.service.support.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionLoop;
import org.nfunk.jep.JEP;

/**
 * @author dgrandemange
 * 
 */
public class LoopHandler implements InvocationHandler {

	public static Pattern EXPR_REGEXP = Pattern.compile("(.*)EXPR\\((.*)\\)(.*)");
	
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
		} else if ("getValue".equals(methodName)) {
			return this.getValue();
		} else if ("getValueBeanPath".equals(methodName)) {
			return this.getValueBeanPath();
		} else {
			return method.invoke(realInfoProvider, args);
		}
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

	public String getIdPath() {
		String idPath = realInfoProvider.getIdPath();
		return replace(idPath, loopActions);
	}

	public String getSrcIdPath() {
		String srcIdPath = realInfoProvider.getSrcIdPath();
		return replace(srcIdPath, loopActions);
	}

	public String getValue() {
		String value = realInfoProvider.getValue();
		return replace(value, loopActions);
	}

	public String getValueBeanPath() {
		String valueBeanPath = realInfoProvider.getValueBeanPath();
		return replace(valueBeanPath, loopActions);
	}

	protected String replace(String attrValue, ISOMsgActionLoop[] pLoopActions) {
		if (null == attrValue) {
			return null;
		}

		String res = attrValue;

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

		res = checkForExprToEvaluate(res);
		
		return res;
	}

	/**
	 * @param res
	 * @return
	 */
	protected String checkForExprToEvaluate(String res) {
		
		String initial = res;		
		String workingVar = res;
		
		Matcher matcher = EXPR_REGEXP.matcher(workingVar);
		while (matcher.matches()) {
			String exprToEval = matcher.group(2);
			JEP parser = new JEP();
			parser.parseExpression(exprToEval);
			String errorInfo = parser.getErrorInfo();
			if (null != errorInfo) {
				return initial;
			}
			Double evaluationResult = parser.getValue();
			workingVar = String.format("%s%d%s", matcher.group(1), evaluationResult.longValue(), matcher.group(3));
			matcher = EXPR_REGEXP.matcher(workingVar);
		}
		
		return workingVar;
	}	
	
}
