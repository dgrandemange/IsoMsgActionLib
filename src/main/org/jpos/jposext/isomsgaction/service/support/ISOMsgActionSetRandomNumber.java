package org.jpos.jposext.isomsgaction.service.support;

import java.util.Map;
import java.util.Random;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 * 
 * Value set action : in dest message, dest field is valued with a random number thans to a random number generator<BR/>
 * Number digits count must be specified
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetRandomNumber extends ISOMsgAbstractAction implements
		IISOMsgAction {

	/**
	 * Nombre de digits du nombre aléatoire
	 */
	private int nbDigits;	
	
	public ISOMsgActionSetRandomNumber() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		StringBuffer res = new StringBuffer("");
		Random randomizer = new Random();
		for (int i = 0; i < nbDigits; i++) {
			res.append(randomizer.nextInt(10));
		}
		ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), res.toString());
	}

	public int getNbDigits() {
		return nbDigits;
	}

	public void setNbDigits(int nbDigits) {
		this.nbDigits = nbDigits;
	}

}
