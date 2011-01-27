package org.jpos.jposext.isomsgaction.factory.service.support;

import java.util.Map;

import org.jpos.jposext.isomsgaction.factory.service.IISOMSGActionFactoryService;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * @author dgrandemange
 *
 */
public class IISOMSGActionFactoryServiceImpl implements
		IISOMSGActionFactoryService {

	private Map<String, IISOMsgAction> mapActions;
	
	/* (non-Javadoc)
	 * @see com.mbs.kdo.service.IISOMSGActionFactoryService#create(java.lang.String)
	 */
	@Override
	public IISOMsgAction create(String id) {
		return mapActions.get(id);
	}

	public Map<String, IISOMsgAction> getMapActions() {
		return mapActions;
	}

	public void setMapActions(Map<String, IISOMsgAction> mapActions) {
		this.mapActions = mapActions;
	}

	public void addAction(String id, IISOMsgAction action) {
		mapActions.put(id, action);
	}
}
