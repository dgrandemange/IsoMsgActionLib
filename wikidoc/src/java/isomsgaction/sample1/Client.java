package isomsgaction.sample1;

import java.util.HashMap;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class Client {

	/**
	 * @param args
	 * @throws ISOException 
	 */
	public static void main(String[] args) throws ISOException {
		ISOMsg destMsg = new ISOMsg(); 
		MyISOAction action = new MyISOAction();
		Map<String, Object> ctx = new HashMap<String, Object>();
		populateContext(ctx);
		action.process(destMsg, ctx);
		destMsg.dump(System.out, "");
	}

	public static void populateContext(Map<String, Object> ctx) {
		ctx.put(MyISOAction.ACTIONCTX_ATTR_CARDNUMBER, "1234123412341234");
	}

}
