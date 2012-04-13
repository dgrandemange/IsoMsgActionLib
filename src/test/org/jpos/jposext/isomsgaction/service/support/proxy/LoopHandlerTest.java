package org.jpos.jposext.isomsgaction.service.support.proxy;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionLoop;
import org.mockito.Mockito;

/**
 * @author dgrandemange
 *
 */
public class LoopHandlerTest extends TestCase {

	private LoopHandler handler;
	private IISOMsgCommonInfoProvider realAction;
	private ISOMsgActionLoop[] loopActions;
	private ISOMsgActionLoop loopAction1;
	private ISOMsgActionLoop loopAction2;
	private Method method;
	private Object[] args;
	
	protected void setUp() throws Exception {
		super.setUp();
		loopAction1 = Mockito.mock(ISOMsgActionLoop.class);
		Mockito.when(loopAction1.getToken()).thenReturn("i");
		
		loopAction2 = Mockito.mock(ISOMsgActionLoop.class);		
		Mockito.when(loopAction2.getToken()).thenReturn("j");

		Mockito.when(loopAction1.findCurrIteratorValByThread(-1L)).thenReturn("99");
		Mockito.when(loopAction2.findCurrIteratorValByThread(-1L)).thenReturn("99");

		Mockito.when(loopAction1.findCurrIteratorValByThread(-2L)).thenReturn("98");
		Mockito.when(loopAction2.findCurrIteratorValByThread(-2L)).thenReturn("98");		
		
		Mockito.when(loopAction1.findCurrIteratorValByThread(Thread.currentThread().getId())).thenReturn("5");
		Mockito.when(loopAction2.findCurrIteratorValByThread(Thread.currentThread().getId())).thenReturn("7");		
		
		loopActions = new ISOMsgActionLoop[2];
		loopActions[0] = loopAction1;		
		loopActions[1] = loopAction2;
		
		realAction = Mockito.mock(IISOMsgCommonInfoProvider.class);
		Mockito.when(realAction.getIdPath()).thenReturn("1.${j}.3.${i}");
		Mockito.when(realAction.getSrcIdPath()).thenReturn("${i}.4.${j}.6");
		
		handler = new LoopHandler(loopActions, realAction);
				
		args = new Object[] {};
	}

	public void testReplace() {
		assertThat(handler.replace("1.${i}.2", loopActions)).isEqualTo("1.5.2");
		assertThat(handler.replace("${j}.3.${i}", loopActions)).isEqualTo("7.3.5");
		assertThat(handler.replace("1.${k}.2", loopActions)).isEqualTo("1.${k}.2");
	}
	
	public void testInvoke_getIdPath() throws Throwable {		
		method = this.getClass().getMethod("getIdPath");
		assertThat(handler.invoke(null, method, args)).isEqualTo("1.7.3.5");
	}
	
	public void testInvoke_getSrcIdPath() throws Throwable {		
		method = this.getClass().getMethod("getSrcIdPath");
		assertThat(handler.invoke(null, method, args)).isEqualTo("5.4.7.6");
	}	
	
	public String getIdPath() {
		return "dummy";
	}

	public String getSrcIdPath() {
		return "dummy";
	}	
	
}
