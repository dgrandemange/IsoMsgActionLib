package org.jpos.jposext.isomsgaction.factory.service.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.digester.Digester;
import org.jpos.jposext.isomsgaction.model.DateFieldEnum;
import org.jpos.jposext.isomsgaction.model.PadDirectionEnum;
import org.jpos.jposext.isomsgaction.model.validation.DataType;
import org.jpos.jposext.isomsgaction.model.validation.PresenceModeEnum;
import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionBinaryCopy;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionBshScript;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionCheckField;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionCopyFieldByRef;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionCreateCompositeField;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionDeclFieldFormat;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfCustomCondition;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfMatchesDelimConsts;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfMatchesRegExp;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfPresent;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfSetInContext;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionIfValidationErrors;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionLoop;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionMergeMsg;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionRemoveField;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionSetBinary;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionSetRandomNumber;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionSetResponseMTI;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionSetStrDate;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionSetStringValue;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionStrValCopy;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionStrValPadding;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionStrValRegExpReplace;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionUpdateExecutionContext;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionUserCustomized;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgActionValidate;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgCompositeAction;
import org.xml.sax.SAXException;

import dummy.CustomizedISOAction;

/**
 * Iso msg actions configuration digester factory test class
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionsConfigDigesterFactoryImplTest extends TestCase {

	private ISOMsgActionsConfigDigesterFactoryImpl digesterFactory = new ISOMsgActionsConfigDigesterFactoryImpl();

	private Map<String, IISOMsgAction> mapActions;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@SuppressWarnings("unchecked")
	public void testDigestion() throws FileNotFoundException, IOException,
			SAXException {
		Digester digester = digesterFactory.createDigester();
		mapActions = (Map<String, IISOMsgAction>) digester.parse(this
				.getClass().getResourceAsStream(
						"/dummy/config/actions-config.xml"));

		assertTrue(mapActions.containsKey("ISO_ACTIONS_SET_1"));
		ISOMsgCompositeAction cmpAction0100F = (ISOMsgCompositeAction) mapActions
				.get("ISO_ACTIONS_SET_1");

		ISOMsgActionSetStringValue action0100F_0 = (ISOMsgActionSetStringValue) cmpAction0100F
				.get(0);
		assertEquals("0", action0100F_0.getIdPath());
		assertEquals(action0100F_0.getIdPath(), action0100F_0.getSrcIdPath());
		assertEquals(0, action0100F_0.getMsgIndex());
		assertEquals(1, action0100F_0
				.getSrcMsgIndex());
		assertEquals("106", action0100F_0.getValue());
		assertNull(action0100F_0.getValueBeanPath());
		assertFalse(action0100F_0.isBinary());

		ISOMsgActionSetStringValue action0100F_1 = (ISOMsgActionSetStringValue) cmpAction0100F
				.get(1);
		assertEquals("1", action0100F_1.getIdPath());
		assertEquals(action0100F_1.getIdPath(), action0100F_1.getSrcIdPath());
		assertEquals(0, action0100F_1.getMsgIndex());
		assertEquals(1, action0100F_1
				.getSrcMsgIndex());
		assertNull(action0100F_1.getValue());
		assertEquals("ctx(onePropertyPath)", action0100F_1.getValueBeanPath());

		ISOMsgActionIfMatchesDelimConsts action0100F_2 = (ISOMsgActionIfMatchesDelimConsts) cmpAction0100F
				.get(2);
		assertEquals("2", action0100F_2.getIdPath());
		assertEquals(action0100F_2.getIdPath(), action0100F_2.getSrcIdPath());
		assertEquals(0, action0100F_2.getMsgIndex());
		assertEquals(1, action0100F_2
				.getSrcMsgIndex());
		assertEquals("tata tete titi toto", action0100F_2.getValuesToMatch());
		assertTrue(action0100F_2.isCaseSensitive());

		IISOMsgAction action0100F_2_subaction_1 = action0100F_2.get(0);
		assertNotNull(action0100F_2_subaction_1);
		assertTrue(action0100F_2_subaction_1 instanceof ISOMsgActionSetStringValue);

		IISOMsgAction action0100F_2_elseaction = action0100F_2.getElseAction();
		assertNotNull(action0100F_2_elseaction);
		assertTrue(action0100F_2_elseaction instanceof ISOMsgCompositeAction);
		IISOMsgAction action0100F_2_elseaction_subaction1 = ((ISOMsgCompositeAction) action0100F_2_elseaction)
				.get(0);
		assertNotNull(action0100F_2_elseaction_subaction1);
		assertTrue(action0100F_2_elseaction_subaction1 instanceof ISOMsgActionRemoveField);

		ISOMsgActionIfMatchesDelimConsts action0100F_3 = (ISOMsgActionIfMatchesDelimConsts) cmpAction0100F
				.get(3);
		assertEquals("3", action0100F_3.getIdPath());
		assertEquals(action0100F_3.getIdPath(), action0100F_3.getSrcIdPath());
		assertEquals(0, action0100F_3.getMsgIndex());
		assertEquals(1, action0100F_3
				.getSrcMsgIndex());
		assertEquals("baba bebe bibi bobo", action0100F_3.getValuesToMatch());
		assertFalse(action0100F_3.isCaseSensitive());
		IISOMsgAction action0100F_3_subaction_1 = action0100F_3.get(0);
		assertNotNull(action0100F_3_subaction_1);
		assertTrue(action0100F_3_subaction_1 instanceof ISOMsgActionSetStringValue);

		ISOMsgActionIfMatchesRegExp action0100F_4 = (ISOMsgActionIfMatchesRegExp) cmpAction0100F
				.get(4);
		assertEquals("4", action0100F_4.getIdPath());
		assertEquals("^([tT][aeiouy]){2}$", action0100F_4.getRegexp());
		IISOMsgAction action0100F_4_subaction_1 = action0100F_4.get(0);
		assertNotNull(action0100F_4_subaction_1);
		assertTrue(action0100F_4_subaction_1 instanceof ISOMsgActionSetStringValue);

		ISOMsgActionIfPresent action0100F_5 = (ISOMsgActionIfPresent) cmpAction0100F
				.get(5);
		assertEquals("5", action0100F_5.getIdPath());
		assertFalse(action0100F_5.isApplyNotOperator());
		IISOMsgAction action0100F_5_subaction_1 = action0100F_5.get(0);
		assertNotNull(action0100F_5_subaction_1);
		assertTrue(action0100F_5_subaction_1 instanceof ISOMsgActionSetStringValue);

		assertTrue(mapActions.containsKey("ISO_ACTIONS_SET_2"));
		ISOMsgCompositeAction cmpAction0100D = (ISOMsgCompositeAction) mapActions
				.get("ISO_ACTIONS_SET_2");

		ISOMsgActionSetStrDate action0100F_6 = (ISOMsgActionSetStrDate) cmpAction0100D
				.get(0);
		assertEquals("6", action0100F_6.getIdPath());
		assertEquals("MMyyyy", action0100F_6.getPattern());

		ISOMsgActionSetRandomNumber action0100F_7 = (ISOMsgActionSetRandomNumber) cmpAction0100D
				.get(1);
		assertEquals("7", action0100F_7.getIdPath());
		assertEquals(6, action0100F_7.getNbDigits());

		ISOMsgActionStrValCopy action0100F_8 = (ISOMsgActionStrValCopy) cmpAction0100D
				.get(2);
		assertEquals("8", action0100F_8.getIdPath());
		assertEquals("7", action0100F_8.getSrcIdPath());
		assertEquals(0, action0100F_8.getMsgIndex());
		assertEquals(1, action0100F_8
				.getSrcMsgIndex());

		ISOMsgActionRemoveField action0100F_10 = (ISOMsgActionRemoveField) cmpAction0100D
				.get(3);
		assertEquals("10", action0100F_10.getIdPath());
		assertEquals(0, action0100F_10.getMsgIndex());

		ISOMsgActionCopyFieldByRef action0100F_11 = (ISOMsgActionCopyFieldByRef) cmpAction0100D
				.get(4);
		assertEquals("11", action0100F_11.getIdPath());
		assertEquals(0, action0100F_11.getMsgIndex());
		assertEquals("2", action0100F_11.getSrcIdPath());
		assertEquals(1, action0100F_11.getSrcMsgIndex());

		ISOMsgActionStrValRegExpReplace action0100F_12 = (ISOMsgActionStrValRegExpReplace) cmpAction0100D
				.get(5);
		assertEquals("12", action0100F_12.getIdPath());
		assertEquals("11", action0100F_12.getSrcIdPath());
		assertEquals("(^.*$)", action0100F_12.getRegexpPattern());
		assertEquals("$1", action0100F_12.getRegexpReplace());

		ISOMsgActionCreateCompositeField action0100F_13 = (ISOMsgActionCreateCompositeField) cmpAction0100D
				.get(6);
		assertEquals("13.1", action0100F_13.getIdPath());

		ISOMsgActionStrValCopy action0100F_14 = (ISOMsgActionStrValCopy) cmpAction0100D
				.get(7);
		assertEquals("14", action0100F_14.getIdPath());
		assertEquals("7", action0100F_14.getSrcIdPath());
		assertEquals(0, action0100F_14.getMsgIndex());
		assertEquals(1, action0100F_14.getSrcMsgIndex());

		ISOMsgActionBshScript action0100F_15 = (ISOMsgActionBshScript) cmpAction0100D
				.get(8);		
		assertEquals(
				"// Here, two messages in array\n\t\t\t\t// Our goal here is to update field 4 of first message in array (index 0) from\n\t\t\t\t// - either the vlaue of field 1 of second message in array (index 1),\n\t\t\t\t// - either the value of property 'defaultValue' in action execution context,\n\t\t\t\t// Decision is made on the value of field 2 in the target message. See ?\n\n\t\t\t\tentrypoint() {\n\t\t\t\t\torg.jpos.iso.ISOMsg destMsg = messages[0];\n\t\t\t\t\torg.jpos.iso.ISOMsg srcMsg = messages[1];\n\t\n\t\t\t\t\tString valChamp2MsgDest = destMsg.getString(2);\n\t\n\t\t\t\t\tif (\"babebibobu\".equals(valChamp2MsgDest)) {\n\t\t\t\t\t\tdestMsg.set(4, srcMsg.getString(1));\n\t\t\t\t\t} else {\n\t\t\t\t\t\tdestMsg.set(4, context.get(\"defaultValue\"));\n\t\t\t\t\t}\n\t\t\t\t}",
				action0100F_15.getBshScript());
		assertEquals("unIdUniqueDeSCriptBSH", action0100F_15.getScriptId());
		assertEquals("commonScript1Id, commonScript2", action0100F_15.getIncludes());

		ISOMsgActionIfPresent action0100F_16 = (ISOMsgActionIfPresent) cmpAction0100D
				.get(9);
		assertEquals("5", action0100F_16.getIdPath());
		assertEquals(true, action0100F_16.isApplyNotOperator());
		
		ISOMsgActionStrValCopy action0100F_17 = (ISOMsgActionStrValCopy) cmpAction0100D
		.get(10);
		assertNotNull(action0100F_17);
		assertTrue(action0100F_17.isConcat());
		
		ISOMsgActionStrValPadding action0100F_18 = (ISOMsgActionStrValPadding) cmpAction0100D
		.get(11);
		assertNotNull(action0100F_18);
		assertEquals("0", action0100F_18.getPadChar());
		assertEquals(10, action0100F_18.getExpLen());
		assertEquals(PadDirectionEnum.LEFT, action0100F_18.getPadDir());
		
		ISOMsgActionMergeMsg action0100F_19 = (ISOMsgActionMergeMsg) cmpAction0100D
		.get(12);
		assertNotNull(action0100F_19);		
		assertEquals(0, action0100F_19.getMsgIndex());
		assertEquals(1, action0100F_19.getSrcMsgIndex());
		assertNull(action0100F_19.getIdPath());
		assertNull(action0100F_19.getSrcIdPath());	
		
		ISOMsgActionSetStrDate action0100F_20 = (ISOMsgActionSetStrDate) cmpAction0100D.get(13);
		assertNotNull(action0100F_20);		
		assertEquals(1, action0100F_20.getAddValue());
		assertEquals(DateFieldEnum.DAY, action0100F_20.getDateField());
		
		ISOMsgActionSetStrDate action0100F_21 = (ISOMsgActionSetStrDate) cmpAction0100D.get(14);
		assertNotNull(action0100F_21);		
		assertEquals(1, action0100F_21.getAddValue());
		assertEquals(DateFieldEnum.MONTH, action0100F_21.getDateField());
		
		ISOMsgActionSetStrDate action0100F_22 = (ISOMsgActionSetStrDate) cmpAction0100D.get(15);
		assertNotNull(action0100F_22);		
		assertEquals(1, action0100F_22.getAddValue());
		assertEquals(DateFieldEnum.YEAR, action0100F_22.getDateField());
		
		ISOMsgActionSetStrDate action0100F_23 = (ISOMsgActionSetStrDate) cmpAction0100D.get(16);
		assertNotNull(action0100F_23);		
		assertEquals(1, action0100F_23.getAddValue());
		assertEquals(DateFieldEnum.HOUR, action0100F_23.getDateField());
		
		ISOMsgActionSetStrDate action0100F_24 = (ISOMsgActionSetStrDate) cmpAction0100D.get(17);
		assertNotNull(action0100F_24);		
		assertEquals(1, action0100F_24.getAddValue());
		assertEquals(DateFieldEnum.MINUTE, action0100F_24.getDateField());
		
		ISOMsgActionSetStrDate action0100F_25 = (ISOMsgActionSetStrDate) cmpAction0100D.get(18);
		assertNotNull(action0100F_25);		
		assertEquals(10, action0100F_25.getAddValue());
		assertEquals(DateFieldEnum.SECOND, action0100F_25.getDateField());
		
		ISOMsgActionSetStringValue action0100F_26 = (ISOMsgActionSetStringValue) cmpAction0100D.get(19);
		assertNotNull(action0100F_26);		
		assertEquals("C'EST SUPER LES FILTRES", action0100F_26.getValue());
		assertEquals(12, action0100F_26.getFixedLength());
		
		ISOMsgActionSetStringValue action0100F_27 = (ISOMsgActionSetStringValue) cmpAction0100D.get(20);
		assertNotNull(action0100F_27);		
		assertEquals("ctx(onePropertyPath)", action0100F_27.getValueBeanPath());
		assertEquals(18, action0100F_27.getFixedLength());
		
		ISOMsgActionValidate cmpAction0100F_28 = (ISOMsgActionValidate) cmpAction0100D.get(21);
		assertNotNull(cmpAction0100F_28);		
		Map<String, ValidationRule> mapValidationRulesByIdPath = cmpAction0100F_28.getMapValidationRulesByIdPath();
		assertNotNull(mapValidationRulesByIdPath);
				
		ISOMsgActionDeclFieldFormat action0100F_28_1 = (ISOMsgActionDeclFieldFormat) cmpAction0100F_28.get(0);
		assertNotNull(action0100F_28_1);
		assertEquals("15", action0100F_28_1.getIdPath());
		ValidationRule regleValidation_0100F_28_1 = action0100F_28_1.getRegleValidation();
		assertNotNull(regleValidation_0100F_28_1);
		assertEquals("15", regleValidation_0100F_28_1.getName());
		assertEquals(6, regleValidation_0100F_28_1.getMinLength());
		assertEquals(15, regleValidation_0100F_28_1.getLength());
		assertTrue(regleValidation_0100F_28_1.isVariableLength());		
		assertNull(regleValidation_0100F_28_1.getDatePattern());
		assertNull(regleValidation_0100F_28_1.getPattern());
		List<DataType> typeDonnee_0100F_28_1 = regleValidation_0100F_28_1.getDataType();
		assertNotNull(typeDonnee_0100F_28_1);
		assertTrue(typeDonnee_0100F_28_1.contains(DataType.ALPHA));
		assertTrue(typeDonnee_0100F_28_1.contains(DataType.NUM));
		assertTrue(typeDonnee_0100F_28_1.contains(DataType.SPECIAL));
		assertTrue(typeDonnee_0100F_28_1.contains(DataType.SPACES));
		assertFalse(typeDonnee_0100F_28_1.contains(DataType.DATE));
		assertFalse(typeDonnee_0100F_28_1.contains(DataType.REGEXP));
		assertEquals(regleValidation_0100F_28_1, mapValidationRulesByIdPath.get(action0100F_28_1.getIdPath()));

		ISOMsgActionDeclFieldFormat action0100F_28_2 = (ISOMsgActionDeclFieldFormat) cmpAction0100F_28.get(1);
		assertNotNull(action0100F_28_2);
		assertEquals("16", action0100F_28_2.getIdPath());
		ValidationRule regleValidation_0100F_28_2 = action0100F_28_2.getRegleValidation();
		assertNotNull(regleValidation_0100F_28_2);
		assertEquals("16", regleValidation_0100F_28_2.getName());
		assertEquals(0, regleValidation_0100F_28_2.getMinLength());
		assertEquals(10, regleValidation_0100F_28_2.getLength());
		assertFalse(regleValidation_0100F_28_2.isVariableLength());		
		assertNull(regleValidation_0100F_28_2.getDatePattern());
		assertNull(regleValidation_0100F_28_2.getPattern());
		List<DataType> typeDonnee_0100F_28_2 = regleValidation_0100F_28_2.getDataType();
		assertNotNull(typeDonnee_0100F_28_2);
		assertTrue(typeDonnee_0100F_28_2.contains(DataType.ALPHA));
		assertTrue(typeDonnee_0100F_28_2.contains(DataType.NUM));
		assertFalse(typeDonnee_0100F_28_2.contains(DataType.SPECIAL));
		assertFalse(typeDonnee_0100F_28_2.contains(DataType.SPACES));
		assertFalse(typeDonnee_0100F_28_2.contains(DataType.DATE));
		assertFalse(typeDonnee_0100F_28_2.contains(DataType.REGEXP));
		assertEquals(regleValidation_0100F_28_2, mapValidationRulesByIdPath.get(action0100F_28_2.getIdPath()));
		
		ISOMsgActionDeclFieldFormat action0100F_28_3 = (ISOMsgActionDeclFieldFormat) cmpAction0100F_28.get(2);
		assertNotNull(action0100F_28_3);
		assertEquals("17", action0100F_28_3.getIdPath());
		ValidationRule regleValidation_0100F_28_3 = action0100F_28_3.getRegleValidation();
		assertNotNull(regleValidation_0100F_28_3);
		assertEquals("17", regleValidation_0100F_28_3.getName());		
		assertEquals("dd/MM/yyyy:HH:mm:ss", regleValidation_0100F_28_3.getDatePattern());
		assertNull(regleValidation_0100F_28_3.getPattern());
		List<DataType> typeDonnee_0100F_28_3 = regleValidation_0100F_28_3.getDataType();
		assertNotNull(typeDonnee_0100F_28_3);
		assertFalse(typeDonnee_0100F_28_3.contains(DataType.ALPHA));
		assertFalse(typeDonnee_0100F_28_3.contains(DataType.NUM));
		assertFalse(typeDonnee_0100F_28_3.contains(DataType.SPECIAL));
		assertFalse(typeDonnee_0100F_28_3.contains(DataType.SPACES));
		assertTrue(typeDonnee_0100F_28_3.contains(DataType.DATE));
		assertFalse(typeDonnee_0100F_28_3.contains(DataType.REGEXP));
		assertEquals(regleValidation_0100F_28_3, mapValidationRulesByIdPath.get(action0100F_28_3.getIdPath()));
		
		ISOMsgActionDeclFieldFormat action0100F_28_4 = (ISOMsgActionDeclFieldFormat) cmpAction0100F_28.get(3);
		assertNotNull(action0100F_28_4);
		assertEquals("myFormatForField18", action0100F_28_4.getIdPath());
		ValidationRule regleValidation_0100F_28_4 = action0100F_28_4.getRegleValidation();
		assertNotNull(regleValidation_0100F_28_4);
		assertEquals("myFormatForField18", regleValidation_0100F_28_4.getName());
		assertNull(regleValidation_0100F_28_4.getDatePattern());
		assertEquals("[0-9]{1,10}[.][0-9]{2}[A-Z]{3}", regleValidation_0100F_28_4.getPattern());
		List<DataType> typeDonnee_0100F_28_4 = regleValidation_0100F_28_4.getDataType();
		assertNotNull(typeDonnee_0100F_28_4);
		assertFalse(typeDonnee_0100F_28_4.contains(DataType.ALPHA));
		assertFalse(typeDonnee_0100F_28_4.contains(DataType.NUM));
		assertFalse(typeDonnee_0100F_28_4.contains(DataType.SPECIAL));
		assertFalse(typeDonnee_0100F_28_4.contains(DataType.SPACES));
		assertFalse(typeDonnee_0100F_28_4.contains(DataType.DATE));
		assertTrue(typeDonnee_0100F_28_4.contains(DataType.REGEXP));
		assertEquals(regleValidation_0100F_28_4, mapValidationRulesByIdPath.get(action0100F_28_4.getIdPath()));
		
		ISOMsgActionCheckField action0100F_28_5 = (ISOMsgActionCheckField) cmpAction0100F_28.get(4);
		assertNotNull(action0100F_28_5);
		assertEquals("15", action0100F_28_5.getIdPath());
		assertEquals(PresenceModeEnum.MANDATORY, action0100F_28_5.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_5.getMapValidationRulesByIdPath());
		assertEquals(false, action0100F_28_5.isCompareToMessageField());
		
		ISOMsgActionCheckField action0100F_28_6 = (ISOMsgActionCheckField) cmpAction0100F_28.get(5);
		assertNotNull(action0100F_28_6);
		assertEquals("16", action0100F_28_6.getIdPath());
		assertEquals(PresenceModeEnum.MANDATORY, action0100F_28_6.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_6.getMapValidationRulesByIdPath());
		assertEquals(false, action0100F_28_6.isCompareToMessageField());

		ISOMsgActionCheckField action0100F_28_7 = (ISOMsgActionCheckField) cmpAction0100F_28.get(6);
		assertNotNull(action0100F_28_7);
		assertEquals("17", action0100F_28_7.getIdPath());
		assertEquals(PresenceModeEnum.OPTIONAL, action0100F_28_7.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_7.getMapValidationRulesByIdPath());
		assertEquals(false, action0100F_28_7.isCompareToMessageField());

		ISOMsgActionCheckField action0100F_28_8 = (ISOMsgActionCheckField) cmpAction0100F_28.get(7);
		assertNotNull(action0100F_28_8);
		assertEquals("18", action0100F_28_8.getIdPath());
		assertEquals("myFormatForField18", action0100F_28_8.getFieldFormatRef());  
		assertEquals(PresenceModeEnum.UNEXPECTED, action0100F_28_8.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_8.getMapValidationRulesByIdPath());		
		assertEquals(false, action0100F_28_8.isCompareToMessageField());
		
		ISOMsgActionCheckField action0100F_28_9 = (ISOMsgActionCheckField) cmpAction0100F_28.get(8);
		assertNotNull(action0100F_28_9);
		assertEquals("19", action0100F_28_9.getIdPath());
		assertEquals(PresenceModeEnum.OPTIONAL, action0100F_28_9.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_9.getMapValidationRulesByIdPath());
		assertEquals("69", action0100F_28_9.getCompareToIdPath());
		assertEquals(2, action0100F_28_9.getCompareToMsgIndex());
		assertEquals(true, action0100F_28_9.isCompareToMessageField());

		ISOMsgActionCheckField action0100F_28_10 = (ISOMsgActionCheckField) cmpAction0100F_28.get(9);
		assertNotNull(action0100F_28_10);
		assertEquals("19", action0100F_28_10.getIdPath());
		assertEquals(PresenceModeEnum.OPTIONAL, action0100F_28_10.getPresenceMode());
		assertEquals(mapValidationRulesByIdPath, action0100F_28_10.getMapValidationRulesByIdPath());
		assertEquals("19", action0100F_28_10.getCompareToIdPath());
		assertEquals(2, action0100F_28_10.getCompareToMsgIndex());
		assertEquals(true, action0100F_28_10.isCompareToMessageField());
		
		ISOMsgCompositeAction cmpAction0100F_29 = (ISOMsgCompositeAction) cmpAction0100D.get(22);
		assertNotNull(cmpAction0100F_29);
		assertNotNull(cmpAction0100F_29.get(0));
		assertTrue(cmpAction0100F_29.get(0) instanceof ISOMsgActionSetStringValue);
		assertNotNull(cmpAction0100F_29.get(1));
		assertTrue(cmpAction0100F_29.get(1) instanceof ISOMsgActionSetStrDate);
		
		ISOMsgActionSetResponseMTI action0100F_30 = (ISOMsgActionSetResponseMTI) cmpAction0100D.get(23);
		assertNotNull(action0100F_30);
		assertEquals(3, action0100F_30.getMsgIndex());
		assertEquals("9919", action0100F_30.getDefaultResponseMTI());
		
		ISOMsgActionBinaryCopy action0100F_31 = (ISOMsgActionBinaryCopy) cmpAction0100D.get(24);
		assertNotNull(action0100F_31);
		assertEquals("8", action0100F_31.getIdPath());
		assertEquals("7", action0100F_31.getSrcIdPath());
		
		ISOMsgActionUpdateExecutionContext action0100F_32 = (ISOMsgActionUpdateExecutionContext) cmpAction0100D.get(25);
		assertNotNull(action0100F_32);
		assertEquals(1, action0100F_32.getSrcMsgIndex());
		assertEquals("7", action0100F_32.getSrcIdPath());
		assertEquals(20, action0100F_32.getFixedLength());
		
		ISOMsgActionUserCustomized action0100F_33 = (ISOMsgActionUserCustomized) cmpAction0100D.get(26);
		assertTrue(action0100F_33.getIsoAction() instanceof CustomizedISOAction);
		CustomizedISOAction customizedAction = (CustomizedISOAction) action0100F_33.getIsoAction();
		assertEquals(12345, customizedAction.getPropTypeInt());
		assertEquals("a dummy string", customizedAction.getPropTypeString());
		
		ISOMsgActionSetBinary action0100F_34 = (ISOMsgActionSetBinary) cmpAction0100D.get(27);
		assertEquals("123", action0100F_34.getIdPath());
		assertTrue(Arrays.equals(new byte[] {0x00, (byte) 0xFF, (byte) 0xA0}, action0100F_34.getBytes()));
		
		ISOMsgActionSetStringValue action0100F_35 = (ISOMsgActionSetStringValue) cmpAction0100D.get(28);
		assertTrue(action0100F_35.isBinary());

		ISOMsgActionIfCustomCondition action0100F_36 = (ISOMsgActionIfCustomCondition) cmpAction0100D.get(29);
		assertEquals("dummy.MyCustomizedCondition", action0100F_36.getCustomConditionClazzName());
		assertTrue(action0100F_36.getCustomCondition() instanceof dummy.MyCustomizedCondition);
		
		ISOMsgActionLoop action0100F_37 = (ISOMsgActionLoop) cmpAction0100D.get(30);
		assertFalse(action0100F_37.isIntervalMode());
		assertEquals("i", action0100F_37.getToken());
		assertEquals("6", action0100F_37.getIdPath());
		assertEquals(1, action0100F_37.getMsgIndex());
		
		IISOMsgAction iisoMsgAction_37_1 = action0100F_37.get(0);
		assertNotNull(iisoMsgAction_37_1);		
		assertTrue(iisoMsgAction_37_1 instanceof ISOMsgActionCreateCompositeField);
		
		IISOMsgAction iisoMsgAction_37_2 = action0100F_37.get(1);
		assertNotNull(iisoMsgAction_37_2);		
		assertTrue(iisoMsgAction_37_2 instanceof ISOMsgActionStrValCopy);
		
		ISOMsgActionLoop action0100F_38 = (ISOMsgActionLoop) cmpAction0100D.get(31);
		assertTrue(action0100F_38.isIntervalMode());
		assertEquals("j", action0100F_38.getToken());
		assertEquals(1, action0100F_38.getBegin());
		assertEquals(4, action0100F_38.getEnd());
		assertNotNull(action0100F_38.get(0));
		
		ISOMsgActionIfValidationErrors action0100F_39 = (ISOMsgActionIfValidationErrors) cmpAction0100D.get(32);
		IISOMsgAction action0100F_39_subaction_1 = action0100F_39.get(0);
		assertNotNull(action0100F_39_subaction_1);
		assertTrue(action0100F_39_subaction_1 instanceof ISOMsgActionSetStrDate);

		ISOMsgActionIfSetInContext action0100F_40 = (ISOMsgActionIfSetInContext) cmpAction0100D.get(33);
		assertEquals("incomingRequestBean", action0100F_40.getKey());
		IISOMsgAction action0100F_40_subaction_1 = action0100F_40.get(0);
		assertNotNull(action0100F_40_subaction_1);
		assertTrue(action0100F_40_subaction_1 instanceof ISOMsgActionSetStrDate);
	}

}
