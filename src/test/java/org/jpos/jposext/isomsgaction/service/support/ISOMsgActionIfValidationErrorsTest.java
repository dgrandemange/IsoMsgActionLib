package org.jpos.jposext.isomsgaction.service.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.model.validation.ValidationError;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

public class ISOMsgActionIfValidationErrorsTest extends TestCase {

	private ISOMsgActionIfValidationErrors action;
	
	private Map<String, Object> ctx;
	
	private List<ValidationError> validationErrors;
	
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionIfValidationErrors();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		action.setChilds(new ArrayList<IISOMsgAction>());

		action.add(new IISOMsgAction() {

			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
			 */
			public void process(ISOMsg[] msg, Map<String, Object> ctx)
					throws ISOException {
				putSomeFlag(ctx);
			}

			/* (non-Javadoc)
			 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg, java.util.Map)
			 */
			public void process(ISOMsg msg, Map<String, Object> ctx)
					throws ISOException {
				putSomeFlag(ctx);
			}

			private void putSomeFlag(Map<String, Object> ctx) {
				ctx.put("conditionFulfilled", "conditionFulfilled");
			}

		});
		
		ctx = new HashMap<String, Object>();		
	}

	public void testProcess_OneValidationErrorsArePresent() throws ISOException {
		validationErrors = new ArrayList<ValidationError>();
		validationErrors.add(new ValidationError());
		ctx.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME, validationErrors);
		action.process((ISOMsg) null, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}

	public void testProcess_ManyValidationErrorsArePresent() throws ISOException {
		validationErrors = new ArrayList<ValidationError>();
		validationErrors.add(new ValidationError());
		validationErrors.add(new ValidationError());
		validationErrors.add(new ValidationError());
		validationErrors.add(new ValidationError());
		ctx.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME, validationErrors);
		action.process((ISOMsg) null, ctx);
		assertNotNull(ctx.get("conditionFulfilled"));
	}
	
	public void testProcess_ValidationErrorsListPresentButEmpty() throws ISOException {
		validationErrors = new ArrayList<ValidationError>();
		ctx.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME, validationErrors);
		action.process((ISOMsg) null, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

	public void testProcess_ValidationErrorsListPresentButNull() throws ISOException {
		validationErrors = null;
		ctx.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME, validationErrors);
		action.process((ISOMsg) null, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

	public void testProcess_ValidationErrorsListNotPresent() throws ISOException {
		action.process((ISOMsg) null, ctx);
		assertNull(ctx.get("conditionFulfilled"));
	}

}
