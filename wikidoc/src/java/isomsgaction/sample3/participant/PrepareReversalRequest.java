package isomsgaction.sample3.participant;

import java.io.Serializable;
import java.util.HashMap;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import org.jpos.jposext.isomsgaction.factory.service.IISOMSGActionFactoryService;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 *
 * Use of IISOMSGActionFactoryService in a transaction participant
 * It should have been previously injected
 * 
 * @author dgrandemange
 * 
 */
public class PrepareReversalRequest implements TransactionParticipant, Configurable {

	private Configuration cfg;

	private IISOMSGActionFactoryService actionFactoryService;

	@Override
	public int prepare(long id, Serializable context) {
		Context ctx = (Context) context;

		// Get message to reverse 
		ISOMsg reqToAcquirer = (ISOMsg) ctx.get("REQUEST_SEND_TO_ACQUIRER");

		// Init empty reversal message 
		ISOMsg reversalReqMsg = new ISOMsg();

		// Populate a reversal message using a dedicated iso message action
		
		// First, create an action execution context 
		HashMap<String, Object> actionExecutionContext = new HashMap<String, Object>();
		// Then retrieve the action configured to populate a reversal message from a source message
		IISOMsgAction action = actionFactoryService.create("ANY_TO_Reversal");
		try {
			action
					.process(new ISOMsg[] { reversalReqMsg, reqToAcquirer }, actionExecutionContext);
		} catch (ISOException e) {
			// TODO Something 
			throw new RuntimeException(e);
		}
		
		// Put reversal message into JPos transaction context
		ctx.put("REVERSAL_REQUEST_TO_SEND_TO_ACQUIRER", reversalReqMsg);
		
		return PREPARED | READONLY | NO_JOIN;
	}

	@Override
	public void abort(long id, Serializable context) {
	}

	@Override
	public void commit(long id, Serializable context) {
	}

	@Override
	public void setConfiguration(Configuration cfg)
			throws ConfigurationException {
		this.cfg = cfg;
	}

	public IISOMSGActionFactoryService getActionFactoryService() {
		return actionFactoryService;
	}

	public void setActionFactoryService(
			IISOMSGActionFactoryService actionFactoryService) {
		this.actionFactoryService = actionFactoryService;
	}

}
