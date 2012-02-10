package isomsgaction.sample1;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;


public class MyISOAction implements IISOMsgAction {

	public static final String ACTIONCTX_ATTR_CARDNUMBER = "CARDNUMBER";

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		String cardNumber = (String) ctx.get(ACTIONCTX_ATTR_CARDNUMBER);
		ISOMsg targetMsg = msg[0];
		targetMsg.set(2, cardNumber);
	}

	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		process(new ISOMsg[] {msg}, ctx);
	}

}
