package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class ISOMsgActionUserCustomizedTest extends TestCase {

	private ISOMsgActionUserCustomized isoAction;
	
	public void testSimple() throws ISOException {
		isoAction = new ISOMsgActionUserCustomized();
		final AtomicBoolean called = new AtomicBoolean();
		
		isoAction.setIsoAction(new IISOMsgAction() {
			
			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg, java.util.Map)
			 */
			public void process(ISOMsg msg, Map<String, Object> ctx)
					throws ISOException {
				called.set(true);
			}
			
			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
			 */
			public void process(ISOMsg[] msg, Map<String, Object> ctx)
					throws ISOException {
				called.set(true);
			}
		});
		
		called.set(false);
		isoAction.process((ISOMsg) null, null);
		assertTrue(called.get());
		
		called.set(false);
		isoAction.process((ISOMsg[]) null, null);
		assertTrue(called.get());
	}
	
}
