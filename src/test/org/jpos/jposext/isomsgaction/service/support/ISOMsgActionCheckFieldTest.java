package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.model.validation.ValidationError;
import org.jpos.jposext.isomsgaction.model.validation.PresenceModeEnum;
import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;
import org.jpos.jposext.isomsgaction.model.validation.DataType;
import org.jpos.jposext.isomsgaction.model.validation.ValidationErrorTypeEnum;

public class ISOMsgActionCheckFieldTest extends TestCase {

	private ISOMsgActionCheckField action;

	private ISOMsg msg;
	
	private ISOMsg submsg1;

	private Map<String, Object> ctx;

	private ArrayList<ValidationError> errorsList;

	private ValidationRule validationRule;

	private List<DataType> typesDonnee;

	private Map<String, ValidationRule> mapValidationRulesByIdPath;	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionCheckField();

		ctx = new HashMap<String, Object>();
		errorsList = new ArrayList<ValidationError>();
		ctx.put(ISOMsgActionCheckField.VALIDATION_ERRORS_LIST_ATTRNAME,
				errorsList);

		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "abcdefABCDEF" },
				new String[] { "2", "abcdefABCDEF123" },
				new String[] { "3", "abcdefABCDEF123 " },
				new String[] { "4", "abcdefABCDEF123 *_;&#" },
				new String[] { "5", "abcdefABCDEF123 *_;&# יטאש" },
				new String[] { "6", "19/03/2010 14:36:00" },
				new String[] { "7", "bachibouzouk" },
				new String[] { "8", "bouzoukbachi" } });

		submsg1 = new ISOMsg(9);
		ISOMsgTestHelper.populateMsg(submsg1, new String[][] {
				new String[] { "1", "sub1_valeur1" },
				new String[] { "2", "sub1_valeur2" },
				new String[] { "3", "sub1_valeur3" } });

		msg.set(submsg1);
		msg.set(10, new byte[] {0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01});
		
		action.setIdPath("4");

		validationRule = new ValidationRule();
		typesDonnee = new ArrayList<DataType>();
		validationRule.setDataType(typesDonnee);
		validationRule.setVariableLength(true);
		validationRule.setLength(100);		
		
		mapValidationRulesByIdPath = new HashMap<String, ValidationRule>();
		action.setMapValidationRulesByIdPath(mapValidationRulesByIdPath);
	}

	public void testSimpleSansErreur() throws ISOException, ParseException {
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("1");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());

		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}
	
	public void testPresenceRequiseDuChamp() throws ISOException, ParseException {
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("100");		
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.FIELD_PRESENCE, err.getTypeErreur());
	}

	public void testPresenceRequiseDuSousChamp_ChampParentInexistant() throws ISOException, ParseException {
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("200.1");		
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.FIELD_PRESENCE, err.getTypeErreur());
	}	
	
	public void testPresenceRequiseChampComposite_PasDeRegleValidationAssociee() throws ISOException, ParseException {
		action.setIdPath("9");
		action.process(msg, ctx);		

		assertEquals(0, errorsList.size());
	}	

	public void testAbsenceRequiseChampComposite_PasDeRegleValidationAssociee() throws ISOException, ParseException {
		action.setIdPath("50");
		action.setPresenceMode(PresenceModeEnum.UNEXPECTED);
		action.process(msg, ctx);				
		
		assertEquals(0, errorsList.size());
	}		

	public void testPresenceOptionelleChampComposite_PasDeRegleValidationAssociee() throws ISOException, ParseException {
		action.setPresenceMode(PresenceModeEnum.OPTIONAL);
		
		action.setIdPath("9");		
		action.process(msg, ctx);				
		assertEquals(0, errorsList.size());
		
		action.setIdPath("50");		
		action.process(msg, ctx);						
		assertEquals(0, errorsList.size());		
	}	
	
	public void testAbsenceObligatoireDunChamp_ChampAbsent() throws ISOException, ParseException {
		action.setPresenceMode(PresenceModeEnum.UNEXPECTED);
		
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("100");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(0, errorsList.size());
	}	
	
	public void testAbsenceObligatoireDunChamp_ChampPresent() throws ISOException, ParseException {
		action.setPresenceMode(PresenceModeEnum.UNEXPECTED);
		
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("1");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.FIELD_PRESENCE, err.getTypeErreur());
	}
	
	public void testPresenceOptionnelleDunChamp_ChampPresent() throws ISOException, ParseException {
		action.setPresenceMode(PresenceModeEnum.OPTIONAL);
		
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("1");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(0, errorsList.size());
	}		

	public void testPresenceOptionnelleDunChamp_ChampAbsent() throws ISOException, ParseException {
		action.setPresenceMode(PresenceModeEnum.OPTIONAL);
		
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("100");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);
		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(0, errorsList.size());
	}		
	
	public void testTypeChampAlpha() throws ISOException, ParseException {
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("2");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_TYPE, err.getTypeErreur());

		errorsList.clear();

		validationRule.setName("1");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeChampAlphaNum() throws ISOException, ParseException {
		typesDonnee.add(DataType.ALPHA);
		typesDonnee.add(DataType.NUM);

		validationRule.setName("3");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_TYPE, err.getTypeErreur());

		errorsList.clear();

		validationRule.setName("2");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeChampAlphaNumEspaces() throws ISOException,
			ParseException {
		typesDonnee.add(DataType.ALPHA);
		typesDonnee.add(DataType.NUM);
		typesDonnee.add(DataType.SPACES);

		validationRule.setName("4");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_TYPE, err.getTypeErreur());

		errorsList.clear();

		validationRule.setName("3");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeChampAlphaNumEspacesSpeciaux() throws ISOException,
			ParseException {
		typesDonnee.add(DataType.ALPHA);
		typesDonnee.add(DataType.NUM);
		typesDonnee.add(DataType.SPACES);
		typesDonnee.add(DataType.SPECIAL);

		validationRule.setName("5");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(0, errorsList.size());

		errorsList.clear();

		validationRule.setName("4");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeChampAlphaNumEspacesAccents() throws ISOException,
			ParseException {
		typesDonnee.add(DataType.ALPHA);
		typesDonnee.add(DataType.NUM);
		typesDonnee.add(DataType.SPACES);
		typesDonnee.add(DataType.SPECIAL);

		validationRule.setName("5");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeDate() throws ISOException, ParseException {
		typesDonnee.add(DataType.DATE);

		validationRule.setName("6");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		validationRule.setDatePattern("dd/MM/yyyy");
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_TYPE, err.getTypeErreur());

		errorsList.clear();

		validationRule.setDatePattern("dd/MM/yyyy HH:mm:ss");
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testTypeRegExp() throws ISOException, ParseException {
		typesDonnee.add(DataType.REGEXP);
		String regexpPattern;

		regexpPattern = "^maxibouze$";
		validationRule.setName("7");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		validationRule.setPattern(regexpPattern);
		action.setIdPath(validationRule.getName());
		
		action.process(msg, ctx);

		assertEquals(1, errorsList.size());
		ValidationError err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_TYPE, err.getTypeErreur());

		errorsList.clear();

		regexpPattern = "^(b(achi|ouzouk)){2}$";

		validationRule.setPattern(regexpPattern);
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());

		errorsList.clear();

		validationRule.setName("8");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		validationRule.setPattern(regexpPattern);
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());
	}

	public void testLongueur() throws ISOException, ParseException {
		ValidationError err;
		
		typesDonnee.add(DataType.ALPHA);

		validationRule.setName("1");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		validationRule.setMinLength(0);
		validationRule.setLength(100);
		validationRule.setVariableLength(true);

		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);

		assertEquals(0, errorsList.size());
		
		errorsList.clear();

		validationRule.setMinLength(50);
		validationRule.setLength(100);
		validationRule.setVariableLength(true);
		action.process(msg, ctx);
		assertEquals(1, errorsList.size());
		err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_LENGTH, err.getTypeErreur());

		errorsList.clear();

		validationRule.setMinLength(0);
		validationRule.setLength(100);
		validationRule.setVariableLength(false);
		action.process(msg, ctx);
		assertEquals(1, errorsList.size());
		err = errorsList.get(0);
		assertEquals(ValidationErrorTypeEnum.INVALID_LENGTH, err.getTypeErreur());		
	}	
	
	public void testTypeChampBinary() throws ISOException, ParseException {
		typesDonnee.add(DataType.BINARY);

		validationRule.setName("10");
		mapValidationRulesByIdPath.put(validationRule.getName(), validationRule);		
		action.setIdPath(validationRule.getName());
		action.process(msg, ctx);
		assertEquals(0, errorsList.size());		
	}	
	
}
