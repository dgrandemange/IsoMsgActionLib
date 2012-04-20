package isomsgaction.model;

import java.beans.IntrospectionException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.SAXException;

public class StoreTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testBetwixWritingtOfByteArrays() throws IOException,
			SAXException, IntrospectionException {
		Store store = new Store();

		byte[] someByteArray = new byte[] { (byte) 0xA0, (byte) 0xB1,
				(byte) 0xC2, (byte) 0xFF };
		store.setSomeByteArray(someByteArray);

		byte[] secondByteArray = new byte[] { (byte) 0xA1, (byte) 0xB2,
				(byte) 0xC3 };
		store.setSecondByteArray(secondByteArray);

		
		BeanWriter beanWriter = new BeanWriter(System.out);		
		beanWriter.write(store);
	}
	
}
