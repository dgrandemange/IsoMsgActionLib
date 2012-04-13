package org.jpos.jposext.isomsgaction.service.support;

import java.util.HashMap;
import java.util.Map;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.objenesis.instantiator.basic.NewInstanceInstantiator;

/**
 * 
 * Loop action
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionLoop extends ISOMsgCompositeAction {

	private String token;

	private int begin;

	private int end;

	private boolean intervalMode;

	protected Map<Long, String> mapCurrIteratorValByThread = new HashMap<Long, String>();

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> map)
			throws ISOException {
		try {
			if (intervalMode) {
				for (int i = begin; i <= end; i++) {
					mapCurrIteratorValByThread.put(Thread.currentThread()
							.getId(), "" + i);
					super.process(msg, map);
				}
			} else {
				ISOComponent isoCmp = ISOMsgHelper.getComponent(
						msg[getMsgIndex()], getIdPath());
				if (isoCmp instanceof ISOMsg) {
					ISOMsg isoMsg = (ISOMsg) isoCmp;
					for (Object obj : isoMsg.getChildren().values()) {
						ISOComponent currChild = (ISOComponent) obj;
						String key = currChild.getKey().toString();
						mapCurrIteratorValByThread.put(Thread.currentThread()
								.getId(), "" + key);
						super.process(msg, map);
					}
				}
			}
		} finally {
			try {
				mapCurrIteratorValByThread.remove(Thread.currentThread()
						.getId());
			} catch (Throwable t) {
				// Safe to ignore
			}
		}
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the intervalMode
	 */
	public boolean isIntervalMode() {
		return intervalMode;
	}

	/**
	 * @param intervalMode
	 *            the intervalMode to set
	 */
	public void setIntervalMode(boolean intervalMode) {
		this.intervalMode = intervalMode;
	}

	/**
	 * @param threadId
	 * @return
	 */
	public String findCurrIteratorValByThread(Long threadId) {
		return mapCurrIteratorValByThread.get(threadId);
	}

}
