package org.jpos.jposext.isomsgaction.testing.service.support;

import java.util.Arrays;
import java.util.Comparator;

import org.jpos.iso.ISOField;

import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;

public class ISOFieldComparator implements Comparator<ISOField> {

	private String path;

	private ComparisonContext comparisonContext;

	public ISOFieldComparator(ComparisonContext comparisonContext, String path) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
	}

	@Override
	public int compare(ISOField field0, ISOField field1) {

		if (null != field0) {
			if (null == field1) {
				comparisonContext.getResList().add(
						String.format("Field %s : Expected=%s, Current=NULL",
								path, new String(field0.getBytes())));
				return -1;
			}
		} else {
			if (null == field1) {
				return 0;
			} else {
				comparisonContext.getResList().add(
						String.format("Field %s : Expected=NULL, Current=%s",
								path, new String(field1.getBytes())));
				return -1;
			}
		}

		boolean same = Arrays.equals(field0.getBytes(), field1.getBytes());
		if (!same) {
			comparisonContext.getResList().add(
					String.format("Field %s : Expected=%s, Current=%s", path,
							new String(field0.getBytes()), new String(field1
									.getBytes())));
		}
		return same ? 0 : -1;
	}

}
