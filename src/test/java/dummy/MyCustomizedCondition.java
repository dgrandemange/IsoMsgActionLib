package dummy;

import java.util.Map;

import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.factory.service.ICustomCondition;

/**
 * Dummy custom condition for test purpose only
 * 
 * @author dgrandemange
 *
 */
public class MyCustomizedCondition implements ICustomCondition {

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.factory.service.ICustomCondition#isConditionFulfilled(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public boolean isConditionFulfilled(ISOMsg[] msgTab, Map<String, Object> ctx) {
		return false;
	}

}
