package isomsgaction.sample2;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.service.IISOMsgAction;


public class MyISOAction implements IISOMsgAction {

	public static final String ACTIONCTX_ATTR_CARDNUMBER = "CARDNUMBER";

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		ISOMsg targetMsg = msg[0];
		ISOMsg sourceMsg = msg[1];
		
		String cardNumber = sourceMsg.getString(2);		
		targetMsg.set(2, cardNumber);
	}

	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		throw new ISOException("We need two messages here");
	}

}
