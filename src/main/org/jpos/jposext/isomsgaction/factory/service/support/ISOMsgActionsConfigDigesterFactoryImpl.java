/*
 * Created on 3 avr. 08 by dgrandemange
 *
 */
package org.jpos.jposext.isomsgaction.factory.service.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.jpos.iso.ISOUtil;
import org.jpos.jposext.isomsgaction.factory.service.DigesterFactory;
import org.jpos.jposext.isomsgaction.factory.service.ICustomCondition;
import org.jpos.jposext.isomsgaction.model.DateFieldEnum;
import org.jpos.jposext.isomsgaction.model.PadDirectionEnum;
import org.jpos.jposext.isomsgaction.model.validation.DataType;
import org.jpos.jposext.isomsgaction.model.validation.PresenceModeEnum;
import org.jpos.jposext.isomsgaction.model.validation.ValidationRule;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.service.IISOMsgCommonInfoProvider;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgAbstractAction;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgAbstractIfAction;
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
import org.jpos.jposext.isomsgaction.service.support.ISOMsgCommonInfoProviderImpl;
import org.jpos.jposext.isomsgaction.service.support.ISOMsgCompositeAction;
import org.jpos.jposext.isomsgaction.service.support.proxy.LoopHandler;
import org.xml.sax.Attributes;

public class ISOMsgActionsConfigDigesterFactoryImpl implements DigesterFactory {

	public Digester createDigester() {
		final Digester digester = new Digester();
		digester.setXIncludeAware(true);
		digester.setNamespaceAware(true);

		digester.addObjectCreate("iso-actions", HashMap.class);

		digester.addObjectCreate("iso-actions/iso-action",
				ISOMsgCompositeAction.class);

		digester.addRule("iso-actions/iso-action", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				HashMap<String, IISOMsgAction> mapActions = (HashMap<String, IISOMsgAction>) digester
						.peek(1);
				ISOMsgCompositeAction cmpAction = (ISOMsgCompositeAction) digester
						.peek(0);
				String cmpActionId = attr.getValue("id");
				mapActions.put(cmpActionId, cmpAction);
			}

		});

		digester.addObjectCreate("*/set", ISOMsgActionSetStringValue.class);
		digester.addRule("*/set", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionSetStringValue action = (ISOMsgActionSetStringValue) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				action.setValue(attr.getValue("value"));

				action.setValueBeanPath(attr.getValue("ctxBeanPath"));

				String sfixedLength = attr.getValue("fixedLength");
				int fixedLength = -1;
				if (null != sfixedLength) {
					try {
						fixedLength = Integer.parseInt(sfixedLength);
					} catch (Exception e) {
					}
				}
				action.setFixedLength(fixedLength);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/ifMatchesList",
				ISOMsgActionIfMatchesDelimConsts.class);
		digester.addRule("*/ifMatchesList", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionIfMatchesDelimConsts action = (ISOMsgActionIfMatchesDelimConsts) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));
				populateCommonIfActionProperties(action, attr);

				action.setValuesToMatch(attr.getValue("matchlist"));
				action.setCaseSensitive(!convertStrToBoolean(attr
						.getValue("ignoreCase")));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/ifMatchesRegExp",
				ISOMsgActionIfMatchesRegExp.class);
		digester.addRule("*/ifMatchesRegExp", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionIfMatchesRegExp action = (ISOMsgActionIfMatchesRegExp) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));
				populateCommonIfActionProperties(action, attr);

				action.setRegexp(attr.getValue("pattern"));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/ifPresent", ISOMsgActionIfPresent.class);
		digester.addRule("*/ifPresent", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionIfPresent action = (ISOMsgActionIfPresent) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));
				populateCommonIfActionProperties(action, attr);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/else", ISOMsgCompositeAction.class);
		digester.addRule("*/else", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgAbstractIfAction parentIfAction = (ISOMsgAbstractIfAction) digester
						.peek(1);
				ISOMsgCompositeAction elseAction = (ISOMsgCompositeAction) digester
						.peek(0);

				parentIfAction.setElseAction(elseAction);
			}

		});

		digester.addObjectCreate("*/setDate", ISOMsgActionSetStrDate.class);
		digester.addRule("*/setDate", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionSetStrDate action = (ISOMsgActionSetStrDate) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				action.setPattern(attr.getValue("format"));

				String saddValue = attr.getValue("addValue");
				int addValue = 0;
				if (null != saddValue) {
					try {
						addValue = Integer.parseInt(saddValue);
					} catch (Exception e) {
					}
				}

				action.setAddValue(addValue);

				String saddInterval = attr.getValue("addInterval");
				DateFieldEnum addInterval;
				if (saddInterval != null) {
					try {
						addInterval = DateFieldEnum.valueOf(saddInterval);
					} catch (IllegalArgumentException ex) {
						addInterval = DateFieldEnum.valueOf("DAY");
					}
				} else {
					addInterval = DateFieldEnum.valueOf("DAY");
				}

				action.setDateField(addInterval);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/setRandomNumber",
				ISOMsgActionSetRandomNumber.class);
		digester.addRule("*/setRandomNumber", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionSetRandomNumber action = (ISOMsgActionSetRandomNumber) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				action.setNbDigits(Integer.parseInt(attr.getValue("digits")));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/stringCopy", ISOMsgActionStrValCopy.class);
		digester.addRule("*/stringCopy", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionStrValCopy action = (ISOMsgActionStrValCopy) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				String concat = attr.getValue("concat");
				if (null != concat) {
					action.setConcat("true".equalsIgnoreCase(concat));
				}

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/removeField", ISOMsgActionRemoveField.class);
		digester.addRule("*/removeField", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionRemoveField action = (ISOMsgActionRemoveField) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/fieldCopyByRef",
				ISOMsgActionCopyFieldByRef.class);
		digester.addRule("*/fieldCopyByRef", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionCopyFieldByRef action = (ISOMsgActionCopyFieldByRef) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/stringRegExpReplace",
				ISOMsgActionStrValRegExpReplace.class);
		digester.addRule("*/stringRegExpReplace", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionStrValRegExpReplace action = (ISOMsgActionStrValRegExpReplace) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				action.setRegexpPattern(attr.getValue("pattern"));
				action.setRegexpReplace(attr.getValue("replace"));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/createCmpField",
				ISOMsgActionCreateCompositeField.class);
		digester.addRule("*/createCmpField", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionCreateCompositeField action = (ISOMsgActionCreateCompositeField) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/bshScript", ISOMsgActionBshScript.class);
		digester.addRule("*/bshScript", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionBshScript action = (ISOMsgActionBshScript) digester
						.peek(0);

				action.setScriptId(attr.getValue("scriptId"));
				action.setIncludes(attr.getValue("includes"));

				addActionToParent(digester, parentAction, action);
			}

			@Override
			public void body(String namespace, String name, String text)
					throws Exception {
				ISOMsgActionBshScript action = (ISOMsgActionBshScript) digester
						.peek(0);
				action.setBshScript(text.trim());
			}

		});

		digester.addObjectCreate("*/stringPadding",
				ISOMsgActionStrValPadding.class);
		digester.addRule("*/stringPadding", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionStrValPadding action = (ISOMsgActionStrValPadding) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				String padChar = attr.getValue("padChar");
				if (null != padChar) {
					action.setPadChar(padChar);
				} else {
					action.setPadChar(" ");
				}

				String padDir = attr.getValue("padDir");
				PadDirectionEnum defaultPadDir = PadDirectionEnum.LEFT;
				if (null != padDir) {
					action.setPadDir("right".equalsIgnoreCase(padDir) ? PadDirectionEnum.RIGHT
							: defaultPadDir);
				} else {
					action.setPadDir(defaultPadDir);
				}

				String sexpLen = attr.getValue("expLen");
				int expLen = -1;
				if (null != sexpLen) {
					try {
						expLen = Integer.parseInt(sexpLen);
					} catch (Exception e) {
					}
				}
				action.setExpLen(expLen);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/merge", ISOMsgActionMergeMsg.class);
		digester.addRule("*/merge", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionMergeMsg action = (ISOMsgActionMergeMsg) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/validate", ISOMsgActionValidate.class);
		digester.addRule("*/validate", new Rule() {
			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionValidate action = (ISOMsgActionValidate) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}
		});

		digester.addObjectCreate("*/fieldFormat",
				ISOMsgActionDeclFieldFormat.class);
		digester.addRule("*/fieldFormat", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionDeclFieldFormat action = (ISOMsgActionDeclFieldFormat) digester
						.peek(0);

				ISOMsgActionValidate parentValidateAction = findParentValidateAction(digester);
				if (null == parentValidateAction) {
					throw new RuntimeException(
							"A 'fieldFormat' declaration must have a 'validate' element as ancestor.");
				}

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				ValidationRule regle = new ValidationRule();
				regle.setName(action.getIdPath());
				regle.setMinLength(0);

				String longueur = attr.getValue("lg");
				try {
					regle.setLength(Integer.parseInt(longueur));
				} catch (NumberFormatException e) {
				}

				String lgVariable = attr.getValue("lgVariable");
				regle.setVariableLength(!("false".equalsIgnoreCase(lgVariable)));

				String lgMin = attr.getValue("lgMin");
				try {
					regle.setMinLength(Integer.parseInt(lgMin));
				} catch (NumberFormatException e) {
				}

				// Type de données
				Vector<DataType> listeTypes = new Vector<DataType>();
				String strListeTypes = attr.getValue("type");
				StringTokenizer tokenizer = new StringTokenizer(strListeTypes,
						"+");
				while (tokenizer.hasMoreElements()) {
					String token = tokenizer.nextToken();
					DataType dataType;
					try {
						dataType = DataType.valueOf(token.toUpperCase());
						if ((DataType.DATE == dataType)
								|| (DataType.REGEXP == dataType)) {
							listeTypes.clear();
							listeTypes.add(dataType);
							break;
						} else {
							listeTypes.add(dataType);
						}
					} catch (Exception e) {
					}
				}

				if (listeTypes.size() == 0) {
					listeTypes.add(DataType.UNDEFINED);
				}

				regle.setDataType(listeTypes);

				String pattern = attr.getValue("pattern");
				if (DataType.DATE == listeTypes.get(0)) {
					// Vérifier la validité du pattern
					// une IllegalArgumentException devrait être balancée si
					// invalide
					new SimpleDateFormat(pattern);

					// Si on est là, c'est que le pattern date est valide
					regle.setDatePattern(pattern);
				} else {
					regle.setPattern(pattern);
				}

				action.setRegleValidation(regle);

				parentValidateAction.addValidationRule(action.getIdPath(),
						regle);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/checkField", ISOMsgActionCheckField.class);
		digester.addRule("*/checkField", new Rule() {
			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionCheckField action = (ISOMsgActionCheckField) digester
						.peek(0);

				ISOMsgActionValidate parentValidateAction = findParentValidateAction(digester);
				if (null == parentValidateAction) {
					throw new RuntimeException(
							"A 'checkField' declaration must have a 'validate' element as ancestor.");
				}

				action.setMapValidationRulesByIdPath(parentValidateAction
						.getMapValidationRulesByIdPath());

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				PresenceModeEnum presenceMode;
				String sPresenceMode = attr.getValue("presence");
				if (null == sPresenceMode) {
					presenceMode = PresenceModeEnum.MANDATORY;
				} else {
					presenceMode = PresenceModeEnum.valueOf(sPresenceMode
							.trim().toUpperCase());
				}

				String fieldFormatRef = attr.getValue("fieldFormatRef");
				action.setFieldFormatRef(fieldFormatRef);
				
				action.setPresenceMode(presenceMode);

				addActionToParent(digester, parentAction, action);
			}
		});

		digester.addObjectCreate("*/group", ISOMsgCompositeAction.class);
		digester.addRule("*/group", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgCompositeAction action = (ISOMsgCompositeAction) digester
						.peek(0);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/setResponseMTI",
				ISOMsgActionSetResponseMTI.class);
		digester.addRule("*/setResponseMTI", new Rule() {
			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionSetResponseMTI action = (ISOMsgActionSetResponseMTI) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				String value = attr.getValue("default");
				action.setDefaultResponseMTI(value);

				addActionToParent(digester, parentAction, action);
			}
		});

		digester.addObjectCreate("*/binaryCopy", ISOMsgActionBinaryCopy.class);
		digester.addRule("*/binaryCopy", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionBinaryCopy action = (ISOMsgActionBinaryCopy) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/updateContext",
				ISOMsgActionUpdateExecutionContext.class);
		digester.addRule("*/updateContext", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionUpdateExecutionContext action = (ISOMsgActionUpdateExecutionContext) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				action.setValueBeanPath(attr.getValue("ctxBeanPath"));

				String sfixedLength = attr.getValue("fixedLength");
				int fixedLength = -1;
				if (null != sfixedLength) {
					try {
						fixedLength = Integer.parseInt(sfixedLength);
					} catch (Exception e) {
					}
				}
				action.setFixedLength(fixedLength);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/customAction",
				ISOMsgActionUserCustomized.class);
		digester.addRule("*/customAction", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionUserCustomized action = (ISOMsgActionUserCustomized) digester
						.peek(0);

				String customISOActionClazzName = attr.getValue("class");
				action.setIsoActionClazzName(customISOActionClazzName);

				addActionToParent(digester, parentAction, action);
			}

			@Override
			public void body(String namespace, String name, String text)
					throws Exception {
				ISOMsgActionUserCustomized action = (ISOMsgActionUserCustomized) digester
						.peek(0);

				BeanReader beanReader = new BeanReader();

				beanReader.getXMLIntrospector().getConfiguration()
						.setAttributesForPrimitives(false);
				beanReader.getBindingConfiguration().setMapIDs(false);

				beanReader.registerBeanClass("customISOAction",
						Class.forName(action.getIsoActionClazzName()));

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(bos);
				writer.write("<customISOAction>");
				writer.write(text);
				writer.write("</customISOAction>");
				writer.flush();
				writer.close();

				ByteArrayInputStream bis = new ByteArrayInputStream(bos
						.toByteArray());

				Object obj = (Object) beanReader.parse(bis);
				action.setIsoAction((IISOMsgAction) obj);
			}
		});

		digester.addObjectCreate("*/setBinary", ISOMsgActionSetBinary.class);
		digester.addRule("*/setBinary", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionSetBinary action = (ISOMsgActionSetBinary) digester
						.peek(0);

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));
				String hexValue = attr.getValue("value");
				byte[] bytes = ISOUtil.hex2byte(hexValue);
				action.setBytes(bytes);
				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/ifCustomCondition",
				ISOMsgActionIfCustomCondition.class);
		digester.addRule("*/ifCustomCondition", new Rule() {

			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionIfCustomCondition action = (ISOMsgActionIfCustomCondition) digester
						.peek(0);

				String customConditionClazzName = attr.getValue("class");
				action.setCustomConditionClazzName(customConditionClazzName);

				ICustomCondition customCondition = (ICustomCondition) Class
						.forName(customConditionClazzName).newInstance();
				action.setCustomCondition(customCondition);

				addActionToParent(digester, parentAction, action);
			}

		});

		digester.addObjectCreate("*/loop", ISOMsgActionLoop.class);
		digester.addRule("*/loop", new Rule() {
			@Override
			public void begin(String namespace, String name, Attributes attr)
					throws Exception {
				ISOMsgCompositeAction parentAction = (ISOMsgCompositeAction) digester
						.peek(1);
				ISOMsgActionLoop action = (ISOMsgActionLoop) digester.peek(0);

				String token = attr.getValue("token");
				action.setToken(token);

				String begin = attr.getValue("begin");
				if (null != begin) {
					action.setIntervalMode(true);
					action.setBegin(Integer.parseInt(begin));

					String end = attr.getValue("end");
					action.setEnd(Integer.parseInt(end));
				} else {
					action.setIntervalMode(false);
				}

				action.setIsoMsgCommonInfoProvider(populateCommonActionProperties(attr));

				addActionToParent(digester, parentAction, action);
			}
		});

		return digester;
	}

	private ISOMsgActionValidate findParentValidateAction(Digester digester) {
		ISOMsgActionValidate res = null;

		for (int i = 0; i < digester.getCount(); i++) {
			Object obj = digester.peek(i);
			if (obj instanceof ISOMsgActionValidate) {
				res = (ISOMsgActionValidate) obj;
				break;
			}
		}

		return res;
	}

	private ISOMsgActionLoop[] findParentLoopAction(Digester digester) {
		ISOMsgActionLoop[] res;
		List<ISOMsgActionLoop> lstLoopActions = new ArrayList<ISOMsgActionLoop>();

		for (int i = 1; i < digester.getCount(); i++) {
			Object obj = digester.peek(i);
			if (obj instanceof ISOMsgActionLoop) {
				lstLoopActions.add((ISOMsgActionLoop) obj);
			}
		}

		res = new ISOMsgActionLoop[lstLoopActions.size()];
		res = lstLoopActions.toArray(res);

		return res;
	}

	private void addActionToParent(Digester digester,
			ISOMsgCompositeAction parent, IISOMsgAction action) {

		if (action instanceof ISOMsgAbstractAction) {
			ISOMsgActionLoop[] parentLoopActions = findParentLoopAction(digester);

			if (parentLoopActions.length > 0) {
				IISOMsgCommonInfoProvider commonInfoProvider = ((ISOMsgAbstractAction) action)
						.getIsoMsgCommonInfoProvider();
				IISOMsgCommonInfoProvider surrogate = (IISOMsgCommonInfoProvider) Proxy
						.newProxyInstance(
								action.getClass().getClassLoader(),
								new Class[] { IISOMsgCommonInfoProvider.class },
								new LoopHandler(parentLoopActions,
										commonInfoProvider));
				((ISOMsgAbstractAction) action).setIsoMsgCommonInfoProvider(surrogate);

			}
		}

		parent.add(action);

	}

	private ISOMsgCommonInfoProviderImpl populateCommonActionProperties(
			Attributes attr) {
		ISOMsgCommonInfoProviderImpl commonInfoProvider = new ISOMsgCommonInfoProviderImpl();

		String id = attr.getValue("id");
		commonInfoProvider.setIdPath(id);

		String sourceId = attr.getValue("sourceId");
		if (null == sourceId) {
			sourceId = id;
		}
		commonInfoProvider.setSrcIdPath(sourceId);

		int msgIndex = 0;
		String smsgIndex = attr.getValue("msg");
		if (null != smsgIndex) {
			msgIndex = Integer.parseInt(smsgIndex);
		}
		commonInfoProvider.setMsgIndex(msgIndex);

		int sourceMsgIndex = 1;
		String ssourceMsgIndex = attr.getValue("sourceMsg");
		if (null != ssourceMsgIndex) {
			sourceMsgIndex = Integer.parseInt(ssourceMsgIndex);
		}
		commonInfoProvider.setSrcMsgIndex(sourceMsgIndex);

		boolean binary = false;
		String sBinary = attr.getValue("binary");
		if (null != sBinary) {
			binary = "true".equalsIgnoreCase(sBinary);
		}
		commonInfoProvider.setBinary(binary);

		return commonInfoProvider;
	}

	private void populateCommonIfActionProperties(
			ISOMsgAbstractIfAction action, Attributes attr) {
		String sApplyNotOperator = attr.getValue("applyNotOp");
		boolean applyNotOperator = false;
		if (null != sApplyNotOperator) {
			applyNotOperator = "true"
					.equalsIgnoreCase(sApplyNotOperator.trim());
		}
		action.setApplyNotOperator(applyNotOperator);
	}

	private boolean convertStrToBoolean(String strBool) {
		boolean res = false;

		if (null == strBool) {
			return res;
		}

		res = strBool.equalsIgnoreCase("true");

		return res;
	}

}
