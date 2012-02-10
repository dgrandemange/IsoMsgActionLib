package isomsgaction.sample2;

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
		ISOMsg sourceMsg = new ISOMsg();
		populateSourceMessage(sourceMsg);
		action.process(new ISOMsg[] {destMsg, sourceMsg}, ctx);
		destMsg.dump(System.out, "");
	}

	public static void populateSourceMessage(ISOMsg sourceMsg) throws ISOException {
		sourceMsg.set(2, "1234123412341234");
	}

}
