package org.jpos.jposext.isomsgaction.testing.service.support;

import java.util.Comparator;
import java.util.Map.Entry;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOMsg;
import org.jpos.jposext.isomsgaction.testing.model.ComparisonContext;

public class ISOMsgComparator implements Comparator<ISOMsg> {

	private String path;

	private ComparisonContext comparisonContext;

	private boolean skipBitmapComparison;
	
	public ISOMsgComparator(ComparisonContext comparisonContext, String path, boolean skipBitmapComparison) {
		super();
		this.comparisonContext = comparisonContext;
		this.path = path;
		this.skipBitmapComparison = skipBitmapComparison;
	}

	@Override
	public int compare(ISOMsg isomsg0, ISOMsg isomsg1) {
		int res = 0;
				
		if (null != isomsg0) {
			if (null == isomsg1) {
				comparisonContext.addDiff(path, String.format(
						"Composite field %s : Expected=[SET], Current=NULL",
						path));
				return -1;
			}
		} else {
			if (null == isomsg1) {
				return 0;
			} else {
				comparisonContext.addDiff(path, String.format(
						"Composite field %s : Expected=NULL, Current=[SET]",
						path));
				return -1;
			}
		}

		boolean allOk = true;

		for (Object obj : isomsg0.getChildren().entrySet()) {
			Entry<Integer, ISOComponent> entry = (Entry<Integer, ISOComponent>) obj;

			ISOComponent child0 = entry.getValue();
			ISOComponent child1 = isomsg1.getComponent(entry.getKey());

			ISOComponentComparator isoCmpComparator = new ISOComponentComparator(
					comparisonContext, path, entry.getKey().toString(), skipBitmapComparison);
			int compare = isoCmpComparator.compare(child0, child1);
			allOk = allOk && (compare == 0);
		}

		// Lister les composants de isomsg1 et vérifier si leur présence n'était
		// pas attendue
		for (Object obj : isomsg1.getChildren().entrySet()) {
			Entry<Integer, ISOComponent> entry = (Entry<Integer, ISOComponent>) obj;
			if (!(isomsg0.hasField(entry.getKey()))) {
				
				String idPath = String.format("%s%s%s", path,
						("".equals(path) ? "" : "."), entry.getKey());
				
				if (comparisonContext.getMapManualChecks().containsKey(idPath)) {
					// OK
				} else {
					comparisonContext.addDiff(idPath, String.format(
							"Composite field %s : subfield %s is not expected",
							path, entry.getKey()));
					allOk = false;
				}
			}
		}

		res = allOk ? 0 : -1;

		return res;
	}

}
