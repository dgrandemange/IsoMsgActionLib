package org.jpos.jposext.isomsgaction.service.support;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import bsh.BshClassManager;
import bsh.EvalError;
import bsh.Interpreter;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * BSH script execution action<BR/>
 * Messages array is made available in BSH through a "messages" variable.<BR/>
 * Context is also made available through a "context" variable.<BR/>
 * As the messages and context are fully exposed, one may take handle them carefully.<BR/>
 *  
 * BSH script must provide a "entrypoint()" method. This entrypoint is the
 * method the action will call when processed.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionBshScript extends ISOMsgAbstractAction implements
		IISOMsgAction {

	private static final String DEFAULT_MAIN_METHOD_MAIN = "entrypoint";

	private static Map<String, String> mapScripts = new HashMap<String, String>();

	private String bshScript;

	private String mainMethodName = DEFAULT_MAIN_METHOD_MAIN;

	private Interpreter bshInterpreter;

	private boolean evaluated = false;

	private String scriptId;

	private String includes;

	public ISOMsgActionBshScript() {
		super();
		bshInterpreter = new Interpreter();
		BshClassManager bcm = bshInterpreter.getClassManager();
		bcm.setClassLoader(this.getClass().getClassLoader());
	}

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		if (null != bshScript) {
			try {
				if (null == scriptId) {
					bshInterpreter.set("messages", msg);
					bshInterpreter.set("context", ctx);
					if (!evaluated) {
						StringBuffer buf = new StringBuffer();

						if (null != includes) {
							StringTokenizer tokenizer = new StringTokenizer(
									includes, ", ");
							while (tokenizer.hasMoreTokens()) {
								String token = tokenizer.nextToken();
								if (!("".equals(token))) {
									String includeScript = mapScripts
											.get(token);
									if (null != includeScript) {
										buf.append(includeScript.trim());
										buf.append("\n");
									} else {
										throw new ISOException(
												String
														.format(
																"BSHScript '%s' : Referenced script '%s' does not exist.",
																this.scriptId,
																token));
									}
								}
							}
						}

						buf.append(bshScript.trim());
						bshInterpreter.eval(buf.toString());
						evaluated = true;
					}
					bshInterpreter.eval(mainMethodName + "()");
				}
			} catch (EvalError e) {
				throw new ISOException(e);
			}
		}
	}

	protected void registerScript() {
		if (null != scriptId) {
			synchronized (mapScripts) {
				mapScripts.put(scriptId, bshScript);
			}
		}
	}

	public String getBshScript() {
		return bshScript;
	}

	public void setBshScript(String bshScript) {
		this.bshScript = bshScript;
		registerScript();
	}

	public String getMainMethodName() {
		return mainMethodName;
	}

	public void setMainMethodName(String mainMethodName) {
		this.mainMethodName = mainMethodName;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
		registerScript();
	}

	public String getIncludes() {
		return includes;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

}
