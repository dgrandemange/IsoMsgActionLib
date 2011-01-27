package org.jpos.jposext.isomsgaction.testing.service.support;

import java.util.Arrays;
import java.util.Comparator;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;

public class ISOComponentComparator implements Comparator<ISOComponent> {

	private String path;

	private ComparisonContext comparisonContext;

	private String currentId;

	public ISOComponentComparator(ComparisonContext comparisonContext,
			String path, String id) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
		this.currentId = id;
	}

	@Override
	public int compare(ISOComponent cmp0, ISOComponent cmp1) {
		boolean oneAtLeastIsNull = false;

		if (null != cmp0) {
			if (null == cmp1) {
				comparisonContext.getResList().add(
						String.format(
								"Field %s : Expected=[SET], Current=NULL",
								String
										.format("%s%s%s", path, (""
												.equals(path) ? "" : "."),
												currentId)));
				oneAtLeastIsNull = true;
			}
		} else {
			if (null == cmp1) {
			} else {
				comparisonContext.getResList().add(
						String.format(
								"Field %s : Expected=NULL, Current=[SET]",
								String
										.format("%s%s%s", path, (""
												.equals(path) ? "" : "."),
												currentId)));
				oneAtLeastIsNull = true;
			}
		}

		if (!oneAtLeastIsNull) {
			if (cmp0.getClass() != cmp1.getClass()) {
				comparisonContext
						.getResList()
						.add(
								String
										.format(
												"Field %s : Expected data type=%s, Current data type=%s",
												String.format("%s%s%s", path,
														("".equals(path) ? ""
																: "."),
														currentId), cmp0
														.getClass(), cmp1
														.getClass()));
				return -1;
			}
		}

		if (cmp0 instanceof ISOMsg) {
			ISOMsgComparator comparator = new ISOMsgComparator(
					comparisonContext, String.format("%s%s%s", path, (""
							.equals(path) ? "" : "."), currentId));
			return comparator.compare((ISOMsg) cmp0, (ISOMsg) cmp1);
		} else if (cmp0 instanceof ISOField) {
			ISOFieldComparator comparator = new ISOFieldComparator(
					comparisonContext, String.format("%s%s%s", path, (""
							.equals(path) ? "" : "."), currentId));
			return comparator.compare((ISOField) cmp0, (ISOField) cmp1);
		} else {
			try {
				boolean same = Arrays.equals(cmp0.getBytes(), cmp1.getBytes());
				if (!same) {
					comparisonContext.getResList().add(
							String.format("Field %s : Expected(HEXA)=%s, Current(HEXA)=%s",
									String.format("%s%s%s", path,
											("".equals(path) ? ""
													: "."),
											currentId), ISOUtil.hexString(cmp0.getBytes()),
									ISOUtil.hexString(cmp1.getBytes())));
				}
				return same ? 0 : -1;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
