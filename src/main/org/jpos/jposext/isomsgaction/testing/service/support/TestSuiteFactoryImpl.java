package org.jpos.jposext.isomsgaction.testing.service.support;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import junit.framework.TestSuite;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.digester.Digester;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import org.jpos.jposext.isomsgaction.factory.service.support.ISOMsgActionsConfigDigesterFactoryImpl;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.validation.ValidationErrorTypeEnum;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;
import org.jpos.jposext.isomsgaction.testing.service.ITestSuiteFactory;

public class TestSuiteFactoryImpl implements ITestSuiteFactory {

	private static final String DEFAULT_MAPPINGS_DIR = "mappings";

	private Boolean manualCheck = null;

	private boolean interactive = Boolean.TRUE;

	private String mappingsDirPath = DEFAULT_MAPPINGS_DIR;

	public TestSuiteFactoryImpl() {
		super();
	}

	public TestSuiteFactoryImpl(String mappingsDirPath) {
		super();
		this.mappingsDirPath = mappingsDirPath;
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(
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
			ByteArrayInputStream bais = new ByteArrayInputStream(agregatedXml
					.getBytes());
			Digester digester = digesterFactory.createDigester();
			Map<String, IISOMsgAction> mapActions = (Map<String, IISOMsgAction>) digester
					.parse(bais);

			TestSuite rootTestSuite = new TestSuite();
			rootTestSuite.setName("testMapping");
			for (File mappingCfgFile : mappingCfgFiles) {
				String mappingCfgName = mappingCfgFile.getName().replaceFirst(
						"(^.*)\\.xml$", "$1");
				File mappingCfgDir = new File(String.format("%s/%s",
						mappingsDirPath, mappingCfgName));
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

						mappingTest.setAction(mapActions.get(mappingCfgName));

						// Constitution des messages ISO sources
						// et injection dans le test de mapping
						final String sourceMsgRegExp = "^isomsg\\.source\\.([0-9]{1,9})\\.properties$";
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
							Properties props = new Properties();
							props.load(new FileInputStream(sourceMsgDefsFile));
							ISOMsg currentSourceMsg = getISOMsgFromProps(props,
									mappingTest);
							mapSrcMsg.put(new Integer(indexMsg),
									currentSourceMsg);
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

						// Constitution du message ISO attendu
						// et injection dans le test de mapping
						Properties expectedMsgProps = new Properties();
						File[] expectedMsgDefsFiles = testSetDir
								.listFiles(new FileFilter() {

									@Override
									public boolean accept(File file) {
										boolean res = false;
										if (file.isFile()) {
											res = "isomsg.expected.properties"
													.equals(file.getName());
										}
										return res;
									}
								});

						for (File expectedMsgDefsFile : expectedMsgDefsFiles) {
							expectedMsgProps.load(new FileInputStream(
									expectedMsgDefsFile));
							ISOMsg expectedMsg = getISOMsgFromProps(
									expectedMsgProps, mappingTest);
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
					File xmlBeanMappingFile = new File(testSetDir
							.getAbsolutePath()
							+ System.getProperty("file.separator")
							+ xmlBeanMappingFilePath);

					BeanReader beanReader = new BeanReader();

					beanReader.getXMLIntrospector().getConfiguration()
							.setAttributesForPrimitives(false);
					beanReader.getBindingConfiguration().setMapIDs(false);

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

			}
		}
	}

	protected ISOMsg getISOMsgFromProps(Properties props,
			MappingTest mappingTest) {
		ISOMsg res = new ISOMsg();

		// Déterminer les champs composites et les créer
		for (Entry entry : props.entrySet()) {
			String idPath = (String) entry.getKey();
			String lastAtomicId = ISOMsgHelper.findLastAtomicId(idPath);

			if (!(lastAtomicId.equals(idPath))) {
				int lastSepIndex = idPath.lastIndexOf(".");

				String pathOnly;
				if (lastSepIndex > 0) {
					pathOnly = idPath.substring(0, lastSepIndex);
				} else {
					pathOnly = idPath;
				}

				StringTokenizer tokenizer = new StringTokenizer(pathOnly, ".");
				ISOMsg inter = res;
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					int currentId = Integer.parseInt(token);
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

				setFieldValue(inter, Integer.parseInt(lastAtomicId),
						(String) entry.getValue(), idPath, mappingTest);

			} else {
				boolean setted = false;

				String entryValue = (String) entry.getValue();
				Pattern pattern = Pattern.compile("^<hexa:(.*)>$");
				Matcher m = pattern.matcher(entryValue);
				boolean matches = m.matches();
				if (matches) {
					String hexWithSpace = m.replaceFirst("$1");
					String hexNoSpace = hexWithSpace.replace(" ", "");
					byte[] hex2byte = ISOUtil.hex2byte(hexNoSpace);
					try {
						res.set(Integer.parseInt(lastAtomicId), hex2byte);
						setted = true;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				if (!setted) {
					setFieldValue(res, Integer.parseInt(lastAtomicId),
							(String) entry.getValue(), idPath, mappingTest);
				}
			}

		}

		return res;
	}

	protected void setFieldValue(ISOMsg msg, int id, String value,
			String idPath, MappingTest mappingTest) {
		try {
			String checkRegExp = "^<check:(.*)>.*$";

			if (value.matches(checkRegExp)) {
				if (interactive) {
					if (null == manualCheck) {
						Object[] options = { "Yes", "No" };
						int n = JOptionPane
								.showOptionDialog(
										null,
										"Some tests require manual checks.\n"
												+ "Do you want a pop reminder per test to check ?",
										"Manual checks",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										options, options[1]);
						manualCheck = new Boolean(n == 0 ? true : false);
					}
				}
				mappingTest
						.setShowManualCheckReminder((null == manualCheck) ? false
								: manualCheck.booleanValue());
				mappingTest.addManualCheck(idPath, value.replaceFirst(
						checkRegExp, "$1"));
			} else {
				msg.set(id, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
