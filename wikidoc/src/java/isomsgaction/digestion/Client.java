package isomsgaction.digestion;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.jpos.jposext.isomsgaction.factory.service.support.IISOMSGActionFactoryServiceImpl;
import org.jpos.jposext.isomsgaction.factory.service.support.ISOMsgActionsConfigDigesterFactoryImpl;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class Client {

	/**
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, SAXException {
		// Instanciates the digester implementation
		ISOMsgActionsConfigDigesterFactoryImpl digesterFactory = new ISOMsgActionsConfigDigesterFactoryImpl();
		Digester digester = digesterFactory.createDigester();

		// Digests the XML config and get identified actions map in return
		Map<String, IISOMsgAction> mapActions = (Map<String, IISOMsgAction>) digester
				.parse(Client.class
						.getResourceAsStream("/dummy/config/actions-config.xml"));

		// Instanciates an action factory and inject actions map into it
		IISOMSGActionFactoryServiceImpl actionFactory = new IISOMSGActionFactoryServiceImpl();
		actionFactory.setMapActions(mapActions);

		// One should then inject the action factory instance into any client
		// instance that needs it.
		// Client may then use this action factory like this :
		//
		// ISOMsg[] msgArray = ...;
		// Map<String, Object> ctx = ...;
		// IISOMsgAction action = actionFactory.create("action_ID_as_declared_in_XML_config");
		// action.process(msgArray, ctx);
	}

}
