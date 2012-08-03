package org.jpos.jposext.isomsgaction.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * @author dgrandemange
 * 
 */
public class IsoMsgActionHelper {

	/**
	 * @param mappingsDirPath
	 *            The ISO mapping configs directory path
	 * @param mappingId
	 *            An ISO mapping id, or null to process all ISO mapping configs
	 * @return A byte array input stream containing a global iso actions
	 *         configuration including the mappingId configuration file (or all
	 *         mapping configs if mappingId is null)
	 * @throws IOException
	 */
	public static InputStream getMainISOActionConfigInputStream(String mappingsDirPath,
			String mappingId, List<File> lstMappingCfgFiles) throws IOException {

		final String finalMappingId = mappingId;

		File mappingsDir = new File(mappingsDirPath);

		File[] mappingCfgFiles = mappingsDir.listFiles(new FileFilter() {

			/* (non-Javadoc)
			 * @see java.io.FileFilter#accept(java.io.File)
			 */
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
		
		Collections.addAll(lstMappingCfgFiles, mappingCfgFiles);
		
		StringBuffer strBuf = new StringBuffer();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						IsoMsgActionHelper.class
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
					"(^.*)\\#INCLUSIONS_TOKEN\\#(.*$)",
					"$1" + inclusionsBuf.toString() + "$2");
			strBuf.append(replacedLine);
		}
		
		String agregatedXml = strBuf.toString();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(
				agregatedXml.getBytes());
		
		return bais;
	}

}
