package org.jpos.jposext.isomsgaction.helper;

import java.util.StringTokenizer;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.exception.ISOMsgActionException;
import org.jpos.jposext.isomsgaction.exception.ParentMsgDoesNotExistException;

/**
 * ISOMsg handling helper
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgHelper {

	public static final String DEFAULT_ID_PATH_DELIMITER = ".";

	public interface IFulfillCondition {
		public boolean isConditionFulfilled(ISOMsg msg, int id);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @return retrieved ISOComponent if found, else null
	 */
	public static ISOComponent getComponent(ISOMsg msg, String idPath,
			String idPathDelimiter) {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		return (cmpInfos.getCmpParentMsg()).getComponent(Integer
				.parseInt(cmpInfos.getId()));
	}

	public static ISOComponent getComponent(ISOMsg msg, String idPath) {
		return getComponent(msg, idPath, DEFAULT_ID_PATH_DELIMITER);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @return Field string value
	 */
	public static String getStringValue(ISOMsg msg, String idPath,
			String idPathDelimiter) {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		return (cmpInfos.getCmpParentMsg()).getString(Integer.parseInt(cmpInfos
				.getId()));
	}

	public static String getStringValue(ISOMsg msg, String idPath) {
		return getStringValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @param cmpToSet
	 *            Component to inject
	 * @throws ISOException
	 */
	public static void setComponent(ISOMsg msg, String idPath,
			String idPathDelimiter, ISOComponent cmpToSet) throws ISOException {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		(cmpInfos.getCmpParentMsg()).set(cmpToSet);
	}

	public static void setComponent(ISOMsg msg, String idPath,
			ISOComponent cmpToSet) throws ISOException {
		setComponent(msg, idPath, DEFAULT_ID_PATH_DELIMITER, cmpToSet);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @param valueToSet
	 *            The value to set
	 * @param condition
	 *            Condition to fulfill for the value to be set
	 * @throws ISOException
	 */
	public static void setValue(ISOMsg msg, String idPath,
			String idPathDelimiter, String valueToSet,
			IFulfillCondition condition) throws ISOException {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		ISOMsg cmpParentMsg = cmpInfos.getCmpParentMsg();
		String id = cmpInfos.getId();
		if ((null == condition)
				|| (condition.isConditionFulfilled(cmpParentMsg, Integer
						.parseInt(id)))) {
			cmpParentMsg.set(id, valueToSet);
		}
	}

	public static void setValue(ISOMsg msg, String idPath,
			String idPathDelimiter, String valueToSet) throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, valueToSet, null);
	}

	public static void setValue(ISOMsg msg, String idPath, String valueToSet)
			throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, valueToSet);
	}

	public static void setValue(ISOMsg msg, String idPath, String valueToSet,
			IFulfillCondition condition) throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, valueToSet, condition);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @param bytesToSet
	 *            The bytes to set
	 * @param condition
	 *            Condition to fulfill for the value to be set
	 * @throws ISOException
	 */
	public static void setValue(ISOMsg msg, String idPath,
			String idPathDelimiter, byte[] bytesToSet,
			IFulfillCondition condition) throws ISOException {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		ISOMsg cmpParentMsg = cmpInfos.getCmpParentMsg();
		String id = cmpInfos.getId();
		if ((null == condition)
				|| (condition.isConditionFulfilled(cmpParentMsg, Integer
						.parseInt(id)))) {
			cmpParentMsg.set(id, bytesToSet);
		}
	}

	public static void setValue(ISOMsg msg, String idPath,
			String idPathDelimiter, byte[] bytesToSet) throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, bytesToSet, null);
	}

	public static void setValue(ISOMsg msg, String idPath, byte[] bytesToSet)
			throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, bytesToSet);
	}

	public static void setValue(ISOMsg msg, String idPath, byte[] bytesToSet,
			IFulfillCondition condition) throws ISOException {
		setValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER, bytesToSet, condition);
	}

	/**
	 * @param msg
	 *            ISO message from which to retrieve a field
	 * @param idPath
	 *            Full field path of field to retrieve
	 * @param idPathDelimiter
	 *            Path delimiter
	 * @throws ISOException
	 */
	public static void unsetValue(ISOMsg msg, String idPath,
			String idPathDelimiter) throws ISOException {
		CmpInfoWrapper cmpInfos = findParentMsg(msg, idPath, idPath,
				idPathDelimiter);
		(cmpInfos.getCmpParentMsg()).unset(Integer.parseInt(cmpInfos.getId()));
	}

	public static void unsetValue(ISOMsg msg, String idPath)
			throws ISOException {
		unsetValue(msg, idPath, DEFAULT_ID_PATH_DELIMITER);
	}

	public static String findLastAtomicId(String idPath) {
		String[] split = idPath.split("\\" + DEFAULT_ID_PATH_DELIMITER);
		return split[split.length - 1];
	}

	public static CmpInfoWrapper findParentMsg(ISOMsg msg, String fullIdPath,
			String currentIdPath) {
		return findParentMsg(msg, fullIdPath, currentIdPath,
				DEFAULT_ID_PATH_DELIMITER);
	}

	public static CmpInfoWrapper findParentMsg(ISOMsg msg, String fullIdPath,
			String currentIdPath, String idPathDelimiter) {
		StringTokenizer tokenizer = new StringTokenizer(currentIdPath,
				idPathDelimiter);
		String token = tokenizer.nextToken();

		if ((null == token) || (token.trim().length() == 0)) {
			throw new ISOMsgActionException(String.format(
					"Attempt to retrieve ISOComponent : invalid id path '%s' ",
					fullIdPath));
		}

		ISOComponent isoCmp = msg.getComponent(Integer.parseInt(token));

		if (tokenizer.hasMoreTokens()) {
			if (!(isoCmp instanceof ISOMsg)) {
				throw new ParentMsgDoesNotExistException(
						String
								.format(
										"Attempt to retrieve ISOComponent : can't fully parse '%s', ISOComponent at '%s' is not a message",
										fullIdPath, currentIdPath));
			}
			String tmpPath = currentIdPath.substring(token.length() + 1);
			return findParentMsg((ISOMsg) isoCmp, fullIdPath, tmpPath,
					idPathDelimiter);
		} else {
			return new CmpInfoWrapper(msg, token);
		}

	}

}
