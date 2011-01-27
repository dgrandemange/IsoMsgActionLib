package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class ISOMsgActionBshScriptTest extends TestCase {

	private ISOMsgActionBshScript action;

	private ISOMsg msg0;

	private ISOMsg msg1;

	Map<String, Object> ctx;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionBshScript();

		msg0 = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg0, new String[][] {
				new String[] { "1", "valeur1" },
				new String[] { "2", "valeur2" },
				new String[] { "3", "valeur3" },
				new String[] { "4", "valeur4" } });

		msg1 = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg1, new String[][] { new String[] { "1",
				"ca roule(msg)" } });

		String[] instructions = new String[] { "entrypoint() {",
				"org.jpos.iso.ISOMsg destMsg = messages[0];",
				"org.jpos.iso.ISOMsg srcMsg = messages[1];", "",
				"String valChamp2MsgDest = srcMsg.getString(2);", "",
				"if (\"babebibobu\".equals(valChamp2MsgDest)) {",
				"	destMsg.set(4, srcMsg.getString(1));", "} else {",
				"	destMsg.set(4, context.get(\"defaultValue\"));", "}", "}" };

		StringBuffer bshScript = new StringBuffer();
		for (String currentInstruction : instructions) {
			bshScript.append(currentInstruction);
		}

		action.setBshScript(bshScript.toString());

		ctx = new HashMap<String, Object>();
		ctx.put("defaultValue", "ca roule(context)");
	}

	public void testSimple() throws ISOException, ParseException {
		msg1.set(2, "babebibobu");
		action.process(new ISOMsg[] { msg0, msg1 }, ctx);
		assertEquals("ca roule(msg)", msg0.getString(4));
	}

	public void testSimpleBis() throws ISOException, ParseException {
		msg1.set(2, "gkjdfhgsdmghd");
		action.process(new ISOMsg[] { msg0, msg1 }, ctx);
		assertEquals("ca roule(context)", msg0.getString(4));
	}

	public void testInclusionScript() throws ISOException, ParseException {
		String[] includedScript1Lines = new String[] { "String includedString1=\"chaineDuScript1\";" };
		StringBuffer includedBshScript1 = new StringBuffer();
		for (String currentInstruction : includedScript1Lines) {
			includedBshScript1.append(currentInstruction);
		}
		ISOMsgActionBshScript incBshScript1Action = new ISOMsgActionBshScript();
		incBshScript1Action.setScriptId("commonScript1");
		incBshScript1Action.setBshScript(includedBshScript1.toString());
		incBshScript1Action.process(new ISOMsg[] {  }, null);
		
		String[] includedScript2Lines = new String[] { "String includedString2=\"chaineDuScript2\";" };
		StringBuffer includedBshScript2 = new StringBuffer();
		for (String currentInstruction : includedScript2Lines) {
			includedBshScript2.append(currentInstruction);
		}
		ISOMsgActionBshScript incBshScript2Action = new ISOMsgActionBshScript();
		incBshScript2Action.setScriptId("commonScript2");
		incBshScript2Action.setBshScript(includedBshScript2.toString());
		incBshScript2Action.process(new ISOMsg[] {  }, null);
		
		String[] mainScriptLines = new String[] { "entrypoint() {",
				"org.jpos.iso.ISOMsg destMsg = messages[0];",
				"org.jpos.iso.ISOMsg srcMsg = messages[1];",
				"destMsg.set(4, includedString1+\" // \"+ includedString2);",
				"}" };

		StringBuffer mainBshScript = new StringBuffer();
		for (String currentInstruction : mainScriptLines) {
			mainBshScript.append(currentInstruction);
		}

		action.setBshScript(mainBshScript.toString());
		action.setIncludes("commonScript1,commonScript2");
		
		action.process(new ISOMsg[] { msg0, msg1 }, ctx);
		assertEquals("chaineDuScript1 // chaineDuScript2", msg0.getString(4));
		
	}

}
