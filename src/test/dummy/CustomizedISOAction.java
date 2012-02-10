package dummy;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * A customized iso action for unit test purpose only
 * 
 * @author dgrandemange
 *
 */
public class CustomizedISOAction implements IISOMsgAction {

	private int propTypeInt;
	
	private String propTypeString;
	
	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Nothing special
	}

	@Override
	public void process(ISOMsg msg, Map<String, Object> ctx)
			throws ISOException {
		this.process(new ISOMsg[] {msg}, ctx);
	}

	public int getPropTypeInt() {
		return propTypeInt;
	}

	public void setPropTypeInt(int propTypeInt) {
		this.propTypeInt = propTypeInt;
	}

	public String getPropTypeString() {
		return propTypeString;
	}

	public void setPropTypeString(String propTypeString) {
		this.propTypeString = propTypeString;
	}

}
