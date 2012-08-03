package org.jpos.jposext.isomsgaction.testing.service.support;

import java.util.BitSet;
import java.util.Comparator;

import org.jpos.iso.ISOBitMap;
import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;

public class ISOBitMapComparator implements Comparator<ISOBitMap> {

	private String path;

	private ComparisonContext comparisonContext;

	public ISOBitMapComparator(ComparisonContext comparisonContext, String path) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ISOBitMap bitmap0, ISOBitMap bitmap1) {

		if (null != bitmap0) {
			if (null == bitmap1) {
				comparisonContext.addDiff(path, String.format(
						"Bitmap %s : Expected bit set=%s, Current value=NULL", path,
						((BitSet) bitmap0.getValue()).toString()));
				return -1;
			}
		} else {
			if (null == bitmap1) {
				return 0;
			} else {
				comparisonContext.addDiff(path, String.format(
						"Bitmap %s : Expected value=NULL, Current bit set=%s", path,
						((BitSet) bitmap1.getValue()).toString()));
				return -1;
			}
		}

		boolean same = ((BitSet) bitmap0.getValue()).equals((BitSet) bitmap1
				.getValue());
		if (!same) {
			comparisonContext.addDiff(path, String.format(
					"Bitmap %s : Expected bit set=%s, Current bit set=%s", path,
					((BitSet) bitmap0.getValue()),
					((BitSet) bitmap1.getValue())));
		}
		return same ? 0 : -1;
	}

}
