package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.exception.ParentMsgDoesNotExistException;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.validation.DataType;
import org.jpos.jposext.isomsgaction.model.validation.PresenceModeEnum;
import org.jpos.jposext.isomsgaction.model.validation.ValidationError;
import org.jpos.jposext.isomsgaction.model.validation.ValidationErrorTypeEnum;
import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;

/**
 * Action that checks one field against a validation rule<BR/>
 * In case validation errors occur, these are listed in a list that one can
 * retrieve in action execution context map. <BR/>
 * Constant ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME indicates the
 * key name of this list in action execution context map.
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionCheckField extends ISOMsgAbstractAction {
	public static final String VALIDATION_ERRORS_LIST_ATTRNAME = "ERRORS_LIST";

	private PresenceModeEnum presenceMode = PresenceModeEnum.MANDATORY;

	private Map<String, ValidationRule> mapValidationRulesByIdPath;

	private String fieldFormatRef;

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {

		List<ValidationError> listeErreurs = null;
		if (null != ctx) {
			listeErreurs = (List<ValidationError>) ctx
					.get(VALIDATION_ERRORS_LIST_ATTRNAME);
		}

		ValidationRule validationRule;
		if (null != fieldFormatRef) {
			validationRule = mapValidationRulesByIdPath.get(this
					.getFieldFormatRef());
			if (null == validationRule) {
				throw new ISOException(String.format("Validation rule not found for fieldFormatRef '%s'",fieldFormatRef));
			}
		} else {
			validationRule = mapValidationRulesByIdPath.get(this.getIdPath());
		}

		ValidationError erreur = null;

		boolean presence = false;

		try {
			ISOComponent component = ISOMsgHelper.getComponent(
					msg[getSrcMsgIndex()], getIdPath());
			presence = (null != component);
		} catch (ParentMsgDoesNotExistException e) {
			presence = false;
		}

		if (PresenceModeEnum.MANDATORY.equals(presenceMode) && (!presence)) {
			erreur = genErreurChampRequis(new ValidationError(), getIdPath());
		} else if (PresenceModeEnum.UNEXPECTED.equals(presenceMode)
				&& (presence)) {
			erreur = genErreurChampRequis(new ValidationError(), getIdPath());
		}

		if (null == erreur) {
			if (presence) {
				if (null != validationRule) {
					// Retrieve current field value
					String strCurrentVal = ISOMsgHelper.getStringValue(
							msg[getSrcMsgIndex()], getIdPath());

					List<DataType> listeTypeDonnee = validationRule
							.getDataType();
					DataType dataType = listeTypeDonnee.get(0);

					if (DataType.DATE.equals(dataType)) {
						if (!isEmpty(strCurrentVal)) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									validationRule.getDatePattern());

							try {
								Date parse = sdf.parse(strCurrentVal);
								if (!(strCurrentVal.equals(sdf.format(parse)))) {
									erreur = genErreurDeType(
											new ValidationError(), getIdPath());
								}
							} catch (ParseException e) {
								erreur = genErreurDeType(new ValidationError(),
										getIdPath());
							}
						}

					} else if (DataType.REGEXP.equals(dataType)) {
						String pattern = validationRule.getPattern();

						if (pattern != null) {
							if (null != strCurrentVal) {
								if (!strCurrentVal.matches(pattern)) {
									erreur = genErreurDeType(
											new ValidationError(), getIdPath());
								}
							}
						}
					} else if (DataType.BINARY.equals(dataType)) {
						int len = strCurrentVal.length();
						erreur = checkLength(validationRule, erreur, len);
					} else {
						String jeuCaracteresAdmis = "";
						for (DataType type : listeTypeDonnee) {
							jeuCaracteresAdmis += type.getAllowedCharSet();
						}

						String pattern = "[" + jeuCaracteresAdmis + "]*";

						if (pattern != null) {
							if (!strCurrentVal.matches(pattern)) {
								erreur = genErreurDeType(new ValidationError(),
										getIdPath());
							}
						}

						int len = strCurrentVal.length();

						erreur = checkLength(validationRule, erreur, len);
					}
				}
			}
		}

		if (null != erreur) {
			if (null != listeErreurs) {
				listeErreurs.add(erreur);
			}
		}

	}

	protected ValidationError checkLength(ValidationRule validationRule,
			ValidationError erreur, int len) {

		if (!(0 == validationRule.getMinLength() && 0 == validationRule
				.getLength())) {

			if (validationRule.isVariableLength()) {
				if (len > validationRule.getLength()) {
					erreur = genErreurDeLongueur(new ValidationError(),
							getIdPath());
				} else {
					if (len > 0) {
						if (len < validationRule.getMinLength()) {
							erreur = genErreurDeLongueur(new ValidationError(),
									getIdPath());
						}
					}
				}
			} else {
				if (len != validationRule.getLength()) {
					erreur = genErreurDeLongueur(new ValidationError(),
							getIdPath());
				}
			}
		}

		return erreur;
	}

	private boolean isEmpty(String value0) {
		boolean empty = false;

		if (value0 == null) {
			empty = true;
		} else if (value0.trim().length() == 0) {
			empty = true;
		}
		return empty;
	}

	private ValidationError genErreurDeType(ValidationError erreur,
			String paramName) {

		erreur.setTypeErreur(ValidationErrorTypeEnum.INVALID_TYPE);
		erreur.setParamName(paramName);

		return erreur;
	}

	private ValidationError genErreurDeLongueur(ValidationError erreur,
			String paramName) {

		erreur.setTypeErreur(ValidationErrorTypeEnum.INVALID_LENGTH);
		erreur.setParamName(paramName);

		return erreur;
	}

	private ValidationError genErreurChampRequis(ValidationError erreur,
			String paramName) {

		erreur.setTypeErreur(ValidationErrorTypeEnum.FIELD_PRESENCE);
		erreur.setParamName(paramName);

		return erreur;
	}

	public PresenceModeEnum getPresenceMode() {
		return presenceMode;
	}

	public void setPresenceMode(PresenceModeEnum presenceMode) {
		this.presenceMode = presenceMode;
	}

	public Map<String, ValidationRule> getMapValidationRulesByIdPath() {
		return mapValidationRulesByIdPath;
	}

	public void setMapValidationRulesByIdPath(
			Map<String, ValidationRule> mapValidationRulesByIdPath) {
		this.mapValidationRulesByIdPath = mapValidationRulesByIdPath;
	}

	/**
	 * @return the fieldFormatRef
	 */
	public String getFieldFormatRef() {
		return fieldFormatRef;
	}

	/**
	 * @param fieldFormatRef
	 *            the fieldFormatRef to set
	 */
	public void setFieldFormatRef(String fieldFormatRef) {
		this.fieldFormatRef = fieldFormatRef;
	}

}
