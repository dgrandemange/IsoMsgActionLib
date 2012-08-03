package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;

/**
 * 
 * Action that actually only wraps a field format validation rule 
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionDeclFieldFormat extends ISOMsgAbstractAction {

	private ValidationRule validationRule;

	/* (non-Javadoc)
	 * @see org.jpos.jposext.isomsgaction.service.IISOMsgAction#process(org.jpos.iso.ISOMsg[], java.util.Map)
	 */
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		// Well, nothing special to do here
	}

	public ValidationRule getRegleValidation() {
		return validationRule;
	}

	public void setRegleValidation(ValidationRule validationRule) {
		this.validationRule = validationRule;
	}

}
