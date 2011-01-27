package isomsgaction.sample3.participant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;

import org.jpos.jposext.isomsgaction.factory.service.IISOMSGActionFactoryService;
import org.jpos.jposext.isomsgaction.model.validation.ValidationError;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionCheckField;

/**
 * 
 * @author dgrandemange
 * 
 */
public class CheckMessageValidity implements GroupSelector, Configurable {

	private Configuration cfg;

	private IISOMSGActionFactoryService actionFactoryService;

	@Override
	public int prepare(long id, Serializable context) {
		return PREPARED | READONLY | NO_JOIN;
	}

	@Override
	public String select(long id, Serializable context) {
		Context ctx = (Context) context;

		ISOMsg m = (ISOMsg) ctx.get("ISOMSG__INCOMING_REQUEST");

		IISOMsgAction checkMsgAction = actionFactoryService
				.create("0200_CHECK");

		Map<String, Object> ctxAction = new HashMap<String, Object>();
		try {
			checkMsgAction.process(new ISOMsg[] { null, m }, ctxAction);
		} catch (ISOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		List<ValidationError> erreursValidation = (List<ValidationError>) ctxAction
				.get(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME);


		// Save validation error list in transaction manager's context 
		// (in case you want to use it later in transaction's flow)
		ctx.put("0200_CHECK_VALIDATION_ERRORS",
				erreursValidation);

		String selectedGroup;

		if (erreursValidation.size() > 0) {
			selectedGroup = cfg.get("CHECK__KO");
		} else {
			selectedGroup = cfg.get("CHECK__OK");
		}

		return selectedGroup;
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