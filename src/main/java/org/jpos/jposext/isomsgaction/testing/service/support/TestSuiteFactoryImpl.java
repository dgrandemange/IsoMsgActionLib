package org.jpos.jposext.isomsgaction.testing.service.support;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import junit.framework.TestSuite;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.betwixt.PatchedXMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.read.BeanCreationChain;
import org.apache.commons.betwixt.io.read.BeanCreationList;
import org.apache.commons.betwixt.io.read.ChainedBeanCreator;
import org.apache.commons.betwixt.io.read.ElementMapping;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.apache.commons.digester.Digester;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.XMLPackager;
import org.jpos.jposext.isomsgaction.factory.service.support.ISOMsgActionsConfigDigesterFactoryImpl;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.validation.ValidationErrorTypeEnum;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.testing.service.ITestSuiteFactory;

/**
 * @author dgrandemange
 *
 */
public class TestSuiteFactoryImpl implements ITestSuiteFactory {

	private static final String DEFAULT_PATH_SEPARATOR = ".";

	private static final String CHECK_REGEXP = "^<check:(.*)>.*$";

	private static final String DEFAULT_MAPPINGS_DIR = "mappings";

	private Boolean manualCheck = null;

	private boolean interactive = Boolean.TRUE;

	private String mappingsDirPath = DEFAULT_MAPPINGS_DIR;

	private ISOPackager xmlPackager;

	public TestSuiteFactoryImpl() {
		super();
	}

	public TestSuiteFactoryImpl(String mappingsDirPath) {
		super();
		this.mappingsDirPath = mappingsDirPath;
		try {
			xmlPackager = new XMLPackager();
		} catch (ISOException e) {
			// Safe to ignore
		}
	}

	public TestSuite core(String mappingId) {
		try {
			final String finalMappingId = mappingId;

			File mappingsDir = new File(mappingsDirPath);

			File[] mappingCfgFiles = mappingsDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					boolean res = false;

					if (file.isFile()) {
						if (null == finalMappingId) {
							if (file.getName().endsWith(".xml")) {
								return true;
							}
						} else {
							if (file.getName().equals(
									String.format("%s.xml", finalMappingId))) {
								return true;
							}
						}
					}

					return res;
				}

			});

			ISOMsgActionsConfigDigesterFactoryImpl digesterFactory = new ISOMsgActionsConfigDigesterFactoryImpl();
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							getClass()
									.getResourceAsStream(
											"/org/jpos/jposext/isomsgaction/template/isoaction-main-template.xml")));

			StringBuffer inclusionsBuf = new StringBuffer();
			for (File mappingCfgFile : mappingCfgFiles) {
				inclusionsBuf.append(String.format(
						"<xi:include href=\"%s/%s\" />\n", mappingsDirPath,
						mappingCfgFile.getName()));
			}

			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				String replacedLine = line.replaceFirst(
						"(^.*)\\#INCLUSIONS_TOKEN\\#(.*$)", "$1"
								+ inclusionsBuf.toString() + "$2");
				strBuf.append(replacedLine);
			}

			String agregatedXml = strBuf.toString();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					agregatedXml.getBytes());
			Digester digester = digesterFactory.createDigester();
			Map<String, IISOMsgAction> mapActions = (Map<String, IISOMsgAction>) digester
					.parse(bais);

			TestSuite rootTestSuite = new TestSuite();
			rootTestSuite.setName("testMapping");
			for (File mappingCfgFile : mappingCfgFiles) {
				String mappingCfgName = mappingCfgFile.getName().replaceFirst(
						"(^.*)\\.xml$", "$1");
				String mappingCfgDirPath = String.format("%s/%s",
						mappingsDirPath, mappingCfgName);
				File mappingCfgDir = new File(mappingCfgDirPath);
				if (mappingCfgDir.isDirectory()) {
					TestSuite mappingTestSuite = new TestSuite();
					mappingTestSuite.setName(String
							.format("%s", mappingCfgName));
					final String testSetRegExp = "^test.*$";

					File[] testSetDirs = mappingCfgDir
							.listFiles(new FileFilter() {
								@Override
								public boolean accept(File file) {
									boolean res = false;
									if (file.isDirectory()) {
										res = file.getName().matches(
												testSetRegExp);
									}
									return res;
								}
							});

					for (File testSetDir : testSetDirs) {
						MappingTest mappingTest = new MappingTest();
						mappingTest.setName(String.format("%s.%s",
								mappingCfgName, testSetDir.getName()));

						IISOMsgAction action = mapActions.get(mappingCfgName);

						if (null == action) {
							throw new Exception(
									String.format(
											"No iso action found with id '%s'. Please check mapping config '%s/%s' : attribute 'id', in the <iso-action> element, should be set to '%s'",
											mappingCfgName, mappingsDirPath,
											mappingCfgFile.getName(),
											mappingCfgName));
						}

						mappingTest.setAction(action);

						// Constitution des messages ISO sources
						// et injection dans le test de mapping
						final String sourceMsgRegExp = "^isomsg\\.source\\.([0-9]{1,9})\\.(properties|xml)$";
						File[] sourceMsgDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;

										if (file.isFile()) {

											res = (file.getName()
													.matches(sourceMsgRegExp));
										}

										return res;
									}
								});

						SortedMap<Integer, ISOMsg> mapSrcMsg = new TreeMap<Integer, ISOMsg>();
						for (File sourceMsgDefsFile : sourceMsgDefsFiles) {
							int indexMsg = Integer.parseInt(sourceMsgDefsFile
									.getName().replaceFirst(sourceMsgRegExp,
											"$1"));
							String msgFileExt = sourceMsgDefsFile.getName()
									.replaceFirst(sourceMsgRegExp, "$2");

							ISOMsg currentSourceMsg = null;

							if ("properties".equalsIgnoreCase(msgFileExt)) {
								Properties props = new Properties();
								props.load(new FileInputStream(
										sourceMsgDefsFile));
								currentSourceMsg = getISOMsgFromProps(props,
										mappingTest);
							} else if ("xml".equalsIgnoreCase(msgFileExt)) {
								BufferedReader xmlMsgReader = null;
								try {
									currentSourceMsg = new ISOMsg();
									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									xmlMsgReader = new BufferedReader(
											new FileReader(sourceMsgDefsFile));
									String xmlLine = "";
									while (null != xmlLine) {
										bos.write(xmlLine.getBytes());
										xmlLine = xmlMsgReader.readLine();
									}
									currentSourceMsg.setPackager(xmlPackager);
									currentSourceMsg.unpack(bos.toByteArray());
								} catch (Exception e) {
									if (null != xmlMsgReader) {
										xmlMsgReader.close();
									}
								}
							}

							if (null != currentSourceMsg) {
								mapSrcMsg.put(new Integer(indexMsg),
										currentSourceMsg);
							}
						}

						for (ISOMsg srcIsoMsg : mapSrcMsg.values()) {
							mappingTest.addSrcISOMsg(srcIsoMsg);
						}

						// Constitution du contexte de mapping
						// et injection dans le test de mapping
						Properties contextProps = new Properties();
						File[] mappingContextDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;
										if (file.isFile()) {
											res = "context.properties"
													.equals(file.getName());
										}
										return res;
									}
								});

						for (File mappingContextDefsFile : mappingContextDefsFiles) {
							contextProps.load(new FileInputStream(
									mappingContextDefsFile));
							resolveContextMappedBeans(testSetDir, contextProps);
							mappingTest.setContext(new HashMap<String, Object>(
									(Map) contextProps));
						}

						// Constitution du contexte ATTENDU de mapping
						// et injection dans le test de mapping
						Properties expectedContextProps = new Properties();
						File[] mappingExpectedContextDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;
										if (file.isFile()) {
											res = "context.expected.properties"
													.equals(file.getName());
										}
										return res;
									}
								});

						for (File mappingExpectedContextDefsFile : mappingExpectedContextDefsFiles) {
							expectedContextProps.load(new FileInputStream(
									mappingExpectedContextDefsFile));
							resolveContextMappedBeans(testSetDir,
									expectedContextProps);
							HashMap<String, Object> expectedContextMap = new HashMap<String, Object>(
									(Map) expectedContextProps);
							HashMap<String, Object> finalExpectedContextMap = new HashMap<String, Object>();

							List<String> binaryAttrs = new ArrayList<String>();
							Pattern patternHexa = Pattern
									.compile("^<hexa:(.*)>.*$");
							Pattern patternCheck = Pattern
									.compile(CHECK_REGEXP);
							for (Entry<String, Object> entry : expectedContextMap
									.entrySet()) {
								String entryKey = (String) entry.getKey();
								String entryValue = (String) entry.getValue();

								boolean matches;

								Matcher hexaMatcher = patternHexa
										.matcher(entryValue);
								matches = hexaMatcher.matches();
								if (matches) {
									String hexWithSpace = hexaMatcher
											.replaceFirst("$1");
									String hexNoSpace = hexWithSpace.replace(
											" ", "");
									entry.setValue(hexNoSpace);
									binaryAttrs.add(entryKey);
								}

								Matcher checkMatcher = patternCheck
										.matcher(entryValue);
								matches = checkMatcher.matches();
								if (matches) {
									manageManualChecks(entryValue,
											"context entry [" + entryKey + "]",
											mappingTest, CHECK_REGEXP);
								} else {
									finalExpectedContextMap.put(entryKey,
											entryValue);
								}

							}

							mappingTest
									.setExpectedContext(finalExpectedContextMap);
							mappingTest
									.setExpectedContextBinaryAttrs(binaryAttrs);
						}

						// Constitution du message ISO attendu
						// et injection dans le test de mapping
						final String expectedMsgRegExp = "^isomsg\\.expected\\.(properties|xml)$";
						File[] expectedMsgDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;

										if (file.isFile()) {

											res = (file.getName()
													.matches(expectedMsgRegExp));
										}

										return res;
									}
								});

						for (File expectedMsgDefsFile : expectedMsgDefsFiles) {
							String msgFileExt = expectedMsgDefsFile.getName()
									.replaceFirst(expectedMsgRegExp, "$1");

							ISOMsg expectedMsg = null;

							if ("properties".equalsIgnoreCase(msgFileExt)) {
								Properties expectedMsgProps = new Properties();
								expectedMsgProps.load(new FileInputStream(
										expectedMsgDefsFile));
								expectedMsg = getISOMsgFromProps(
										expectedMsgProps, mappingTest);
							} else if ("xml".equalsIgnoreCase(msgFileExt)) {
								BufferedReader xmlMsgReader = null;
								try {
									expectedMsg = new ISOMsg();
									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									xmlMsgReader = new BufferedReader(
											new FileReader(expectedMsgDefsFile));
									String xmlLine = "";
									while (null != xmlLine) {
										bos.write(xmlLine.getBytes());
										xmlLine = xmlMsgReader.readLine();
									}
									expectedMsg.setPackager(xmlPackager);
									expectedMsg.unpack(bos.toByteArray());
								} catch (Exception e) {
									if (null != xmlMsgReader) {
										xmlMsgReader.close();
									}
								}
							}

							mappingTest.setExpectedISOMsg(expectedMsg);
						}

						// Constitution de la liste des erreurs attendues
						// de validation, et injection dans le test de mapping
						Properties validationErrorsProps = new Properties();
						File[] validationErrorsDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;
										if (file.isFile()) {
											res = "errors.expected.properties"
													.equals(file.getName());
										}
										return res;
									}
								});

						Map<String, List<ValidationErrorTypeEnum>> mapExpectedErrsByIdPath = new HashMap<String, List<ValidationErrorTypeEnum>>();
						for (File validationErrorsDefsFile : validationErrorsDefsFiles) {
							validationErrorsProps.load(new FileInputStream(
									validationErrorsDefsFile));
							for (Entry<Object, Object> entry : validationErrorsProps
									.entrySet()) {
								String sTypeErreurValidation = (String) entry
										.getKey();
								String idPathSepList = (String) entry
										.getValue();
								ValidationErrorTypeEnum validationErrorTypeEnum = ValidationErrorTypeEnum
										.valueOf(sTypeErreurValidation);
								StringTokenizer tokenizer = new StringTokenizer(
										idPathSepList, ",");
								while (tokenizer.hasMoreTokens()) {
									String currentIdPath = tokenizer
											.nextToken().trim();
									if (!("".equals(currentIdPath))) {
										List<ValidationErrorTypeEnum> listErrsForIdPath = mapExpectedErrsByIdPath
												.get(currentIdPath);
										if (null == listErrsForIdPath) {
											listErrsForIdPath = new ArrayList<ValidationErrorTypeEnum>();
											mapExpectedErrsByIdPath.put(
													currentIdPath,
													listErrsForIdPath);
										}
										listErrsForIdPath
												.add(validationErrorTypeEnum);
									}
								}
							}
						}
						mappingTest
								.setMapExpectedErrsByIdPath(mapExpectedErrsByIdPath);

						mappingTestSuite.addTest(mappingTest);
					}
					rootTestSuite.addTest(mappingTestSuite);
				}
			}

			return rootTestSuite;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void resolveContextMappedBeans(File testSetDir,
			Properties contextProps) {
		String xmlBeanRegExp = "^<xmlbean:(xmlpath=.*)(.*=.*[ ]*)*[ ]*>$";
		String refBeanRegExp = "^<refbean:(.*)[ ]*>$";
		Map<String, String> mapRefBean = new HashMap<String, String>();
		for (Entry<Object, Object> entry : contextProps.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (value.matches(xmlBeanRegExp)) {
				String mappingInfos = value.replaceFirst(xmlBeanRegExp, "$1");
				String xmlBeanMappingFilePath = null;

				Map<String, String> eltClassMap = new HashMap<String, String>();
				StringTokenizer tokenizer = new StringTokenizer(mappingInfos,
						" ");
				while (tokenizer.hasMoreTokens()) {
					String keyValuePair = tokenizer.nextToken();
					String keyValuePairRegExp = "^(.*)=(.*)$";
					if (keyValuePair.matches(keyValuePairRegExp)) {
						String xmlElt = keyValuePair.replaceFirst(
								keyValuePairRegExp, "$1");
						String beanClassname = keyValuePair.replaceFirst(
								keyValuePairRegExp, "$2");
						if ("xmlpath".equalsIgnoreCase(xmlElt)) {
							xmlBeanMappingFilePath = beanClassname;
						} else {
							eltClassMap.put(xmlElt, beanClassname);
						}
					}
				}

				try {
					File xmlBeanMappingFile = new File(
							testSetDir.getAbsolutePath()
									+ System.getProperty("file.separator")
									+ xmlBeanMappingFilePath);

					BeanReader beanReader = new BeanReader();

					PatchedXMLIntrospector patchedXmlIntrospector = new PatchedXMLIntrospector();
					beanReader.setXMLIntrospector(patchedXmlIntrospector);

					patchedXmlIntrospector.getConfiguration()
							.setAttributesForPrimitives(false);

					beanReader.getBindingConfiguration().setMapIDs(false);

					ChainedBeanCreator chainedBeanCreator = new ChainedBeanCreator() {

						public Object create(ElementMapping mapping,
								ReadContext context, BeanCreationChain chain) {
							if (byte.class.equals(mapping.getType())) {
								String hexByteValue = mapping.getAttributes()
										.getValue("hexa");
								return new Byte(
										ISOUtil.hex2byte(hexByteValue)[0]);
							}
							return chain.create(mapping, context);
						}

					};

					BeanCreationList chain = BeanCreationList
							.createStandardChain();
					chain.insertBeanCreator(1, chainedBeanCreator);

					beanReader.getReadConfiguration().setBeanCreationChain(
							chain);

					for (Entry<String, String> eltClassEntry : eltClassMap
							.entrySet()) {
						beanReader.registerBeanClass(eltClassEntry.getKey(),
								Class.forName(eltClassEntry.getValue()));
					}

					Object obj = (Object) beanReader.parse(xmlBeanMappingFile);
					contextProps.put(key, obj);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			} else if (value.matches(refBeanRegExp)) {
				mapRefBean.put(key, value);
			}
		}

		for (Entry<String, String> entry : mapRefBean.entrySet()) {
			String beanPath = entry.getValue()
					.replaceFirst(refBeanRegExp, "$1");
			String[] splitStr = beanPath.split("\\.", 2);
			try {
				Object obj = PropertyUtils.getProperty(
						contextProps.get(splitStr[0]), splitStr[1]);
				contextProps.put(entry.getKey(), obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	protected ISOMsg getISOMsgFromProps(Properties props,
			MappingTest mappingTest) {
		ISOMsg res = new ISOMsg();

		// D�terminer les champs composites et les cr�er
		for (Entry entry : props.entrySet()) {
			String rawIdPath = (String) entry.getKey();
			String idPath = hexPathToDecPath(rawIdPath, DEFAULT_PATH_SEPARATOR);
			String strLastAtomicId = ISOMsgHelper.findLastAtomicId(idPath);
			int lastAtomicId = ISOMsgHelper
					.getIntIdFromStringId(strLastAtomicId);
			ISOMsg inter = res;

			if (!(strLastAtomicId.equals(idPath))) {
				int lastSepIndex = idPath.lastIndexOf(DEFAULT_PATH_SEPARATOR);

				String pathOnly;
				if (lastSepIndex > 0) {
					pathOnly = idPath.substring(0, lastSepIndex);
				} else {
					pathOnly = idPath;
				}

				StringTokenizer tokenizer = new StringTokenizer(pathOnly,
						DEFAULT_PATH_SEPARATOR);
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					int currentId = ISOMsgHelper.getIntIdFromStringId(token);
					boolean cmpFieldExists = inter.hasField(currentId);
					if (!cmpFieldExists) {
						ISOMsg cmpField = new ISOMsg(currentId);
						try {
							inter.set(cmpField);
						} catch (ISOException e) {
							throw new RuntimeException(e);
						}
					}
					inter = (ISOMsg) inter.getComponent(currentId);
				}

			}

			boolean setted = false;

			String entryValue = (String) entry.getValue();
			Pattern pattern = Pattern.compile("^<hexa:(.*)>.*$");
			Matcher m = pattern.matcher(entryValue);
			boolean matches = m.matches();
			if (matches) {
				String hexWithSpace = m.replaceFirst("$1");
				String hexNoSpace = hexWithSpace.replace(" ", "");
				byte[] hex2byte = ISOUtil.hex2byte(hexNoSpace);
				try {
					inter.set(lastAtomicId, hex2byte);
					setted = true;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			if (!setted) {
				setFieldValue(inter, lastAtomicId, (String) entry.getValue(),
						idPath, mappingTest);
			}

		}

		return res;
	}

	protected String hexPathToDecPath(String rawIdPath,
			String defaultPathSeparator) {
		StringBuffer res = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(rawIdPath,
				defaultPathSeparator);
		String sep = "";
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			int currentId = ISOMsgHelper.getIntIdFromStringId(token);
			res.append(sep);
			res.append(currentId);
			sep = defaultPathSeparator;
		}

		return res.toString();
	}

	protected void setFieldValue(ISOMsg msg, int id, String value,
			String idPath, MappingTest mappingTest) {
		try {
			String checkRegExp = CHECK_REGEXP;

			if (value.matches(checkRegExp)) {
				manageManualChecks(value, idPath, mappingTest, checkRegExp);
			} else {
				msg.set(id, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void manageManualChecks(String value, String idPath,
			MappingTest mappingTest, String checkRegExp) {
		if (interactive) {
			if (null == manualCheck) {
				Object[] options = { "Yes", "No" };
				int n = JOptionPane
						.showOptionDialog(
								null,
								"Some tests require manual checks.\n"
										+ "Do you want a pop reminder per test to check ?",
								"Manual checks", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);
				manualCheck = new Boolean(n == 0 ? true : false);
			}
		}
		mappingTest.setShowManualCheckReminder((null == manualCheck) ? false
				: manualCheck.booleanValue());
		mappingTest.addManualCheck(idPath,
				value.replaceFirst(checkRegExp, "$1"));
	}

	@Override
	public TestSuite create() {
		return core(null);
	}

	@Override
	public TestSuite createByMappingId(String id) {
		return core(id);
	}

	public String getMappingsDirPath() {
		return mappingsDirPath;
	}

	public void setMappingsDirPath(String mappingsDirPath) {
		this.mappingsDirPath = mappingsDirPath;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

}
