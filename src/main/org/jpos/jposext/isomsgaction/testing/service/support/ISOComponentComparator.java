package org.jpos.jposext.isomsgaction.testing.service.support;

import java.util.Arrays;
import java.util.Comparator;

import org.jpos.iso.ISOBitMap;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;

public class ISOComponentComparator implements Comparator<ISOComponent> {

	private String path;

	private ComparisonContext comparisonContext;

	private String currentId;

	private boolean skipBitmapComparison;

	public ISOComponentComparator(ComparisonContext comparisonContext,
			String path, String id) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
		this.currentId = id;
		this.skipBitmapComparison = true;
	}

	public ISOComponentComparator(ComparisonContext comparisonContext,
			String path, String id, boolean skipBitmapComparison) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
		this.currentId = id;
		this.skipBitmapComparison = skipBitmapComparison;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ISOComponent cmp0, ISOComponent cmp1) {
		boolean oneAtLeastIsNull = false;

		String currentPath = String.format("%s%s%s", path,
				("".equals(path) ? "" : "."), currentId);

		if (comparisonContext.isManualCheck(currentPath)) {
			return 0;
		}

		if (null != cmp0) {
			if (null == cmp1) {
				comparisonContext
						.addDiff(currentPath, String.format(
								"Field %s : Expected=[SET], Current=NULL",
								currentPath));
				oneAtLeastIsNull = true;
			}
		} else {
			if (null == cmp1) {
			} else {
				comparisonContext
						.addDiff(currentPath, String.format(
								"Field %s : Expected=NULL, Current=[SET]",
								currentPath));
				oneAtLeastIsNull = true;
			}
		}

		if (!oneAtLeastIsNull) {
			if (cmp0.getClass() != cmp1.getClass()) {
				comparisonContext
						.addDiff(
								currentPath,
								String
										.format(
												"Field %s : Expected data type=%s, Current data type=%s",
												currentPath, cmp0.getClass(),
												cmp1.getClass()));
				return -1;
			}
		}

		if (cmp0 instanceof ISOMsg) {
			ISOMsgComparator comparator = new ISOMsgComparator(
					comparisonContext, currentPath, skipBitmapComparison);
			return comparator.compare((ISOMsg) cmp0, (ISOMsg) cmp1);
		} else if (cmp0 instanceof ISOField) {
			ISOFieldComparator comparator = new ISOFieldComparator(
					comparisonContext, currentPath);
			return comparator.compare((ISOField) cmp0, (ISOField) cmp1);
		} else if (cmp0 instanceof ISOBitMap) {
			if (!skipBitmapComparison) {
				ISOBitMapComparator comparator = new ISOBitMapComparator(
						comparisonContext, currentPath);
				return comparator.compare((ISOBitMap) cmp0, (ISOBitMap) cmp1);
			} else {
				return 0;
			}
		} else {
			try {
				boolean same = Arrays.equals(cmp0.getBytes(), cmp1.getBytes());
				if (!same) {
					comparisonContext.addDiff(currentPath, String.format(
							"Field %s : Expected(HEXA)=%s, Current(HEXA)=%s",
							currentPath, ISOUtil.hexString(cmp0.getBytes()),
							ISOUtil.hexString(cmp1.getBytes())));
				}
				return same ? 0 : -1;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	public boolean isSkipBitmapComparison() {
		return skipBitmapComparison;
	}

	public void setSkipBitmapComparison(boolean skipBitmapComparison) {
		this.skipBitmapComparison = skipBitmapComparison;
	}

}
