package org.jpos.jposext.isomsgaction.service.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.helper.ISOMsgHelper;
import org.jpos.jposext.isomsgaction.model.DateFieldEnum;
import org.jpos.jposext.isomsgaction.service.IISOMsgAction;

/**
 *
 * Set value action : in dest message, dest field is valued with current date using a specified date format pattern<BR/>
 * 
 * @author dgrandemange
 * 
 */
public class ISOMsgActionSetStrDate extends ISOMsgAbstractAction implements
		IISOMsgAction {

	/**
	 * Date format pattern (see java.util.SimpleDateFormat)
	 */
	private String pattern;	
	
	private int addValue=0;
	
	private DateFieldEnum dateField;

	public ISOMsgActionSetStrDate() {
		super();
	}

	@Override
	public void process(ISOMsg[] msg, Map<String, Object> ctx)
			throws ISOException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		Calendar calendrier = Calendar.getInstance();
		
		if (addValue!=0){
			switch (dateField) {
			case DAY : calendrier.add(Calendar.DATE, addValue); break; 
			case MONTH : calendrier.add(Calendar.MONTH, addValue); break; 
			case YEAR : calendrier.add(Calendar.YEAR, addValue); break; 
			case HOUR : calendrier.add(Calendar.HOUR, addValue); break; 
			case MINUTE : calendrier.add(Calendar.MINUTE, addValue); break; 
			case SECOND : calendrier.add(Calendar.SECOND, addValue); break; 
			default : calendrier.add(Calendar.DATE, addValue); break; 
			}
		}
		
		String fmttedDate = sdf.format(calendrier.getTime());
		ISOMsgHelper.setValue(msg[getMsgIndex()], getIdPath(), fmttedDate, isBinary());
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

	public DateFieldEnum getDateField() {
		return dateField;
	}

	public void setDateField(DateFieldEnum dateField) {
		this.dateField = dateField;
	}
	
}
