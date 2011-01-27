package org.jpos.jposext.isomsgaction.service.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.model.validation.ValidationError;
import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;


/**
 * 
 * Validation context creation action
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionValidate extends ISOMsgCompositeAction {

	private Map<String, ValidationRule> mapValidationRulesByIdPath = new HashMap<String, ValidationRule>();

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> map)
			throws ISOException {
		if (null != map) {
			map.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME,
					new ArrayList<ValidationError>());
		}
		super.process(msg, map);
	}

	public Map<String, ValidationRule> getMapValidationRulesByIdPath() {
		return mapValidationRulesByIdPath;
	}

	public void addValidationRule(String idPath, ValidationRule rule) {
		mapValidationRulesByIdPath.put(idPath, rule);
	}

	public void removeValidationRule(String idPath) {
		mapValidationRulesByIdPath.remove(idPath);
	}

}
