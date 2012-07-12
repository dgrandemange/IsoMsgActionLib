package org.jpos.jposext.isomsgaction.service.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.TestCase;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import org.jpos.jposext.isomsgaction.model.DateFieldEnum;

public class ISOMsgActionSetStrDateTest extends TestCase {

	private ISOMsgActionSetStrDate action;

	private ISOMsg msg;	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		action = new ISOMsgActionSetStrDate();
		action.setIsoMsgCommonInfoProvider(new ISOMsgCommonInfoProviderImpl());
		
		msg = new ISOMsg();

		ISOMsgTestHelper.populateMsg(msg, new String[][] {
				new String[] { "1", "valeur1_source" },
				new String[] { "2", "valeur2_source" },
				new String[] { "3", "valeur3_source" },
				new String[] { "4", "valeur4_source" } });

	}

	public void testSimple() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String expectedDate = sdf.format(Calendar.getInstance().getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddDay() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.DATE,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.DAY);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddMonth() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.MONTH,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.MONTH);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddYear() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.YEAR,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.YEAR);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddHour() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.HOUR,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.HOUR);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddMinute() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.MINUTE,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.MINUTE);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}
	
	public void testAddSecond() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar calendrier = Calendar.getInstance();
		calendrier.add(Calendar.SECOND,1);
		String expectedDate = sdf.format(calendrier.getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setAddValue(1);
		action.setDateField(DateFieldEnum.SECOND);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
	}

	public void testSimple_Binary() throws ISOException, ParseException {
		String pattern = "ddMMyyyy";		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String expectedDate = sdf.format(Calendar.getInstance().getTime());
		
		action.setIdPath("4");		
		action.setPattern(pattern);
		action.setBinary(true);
		action.process(msg, null);
		
		assertEquals(expectedDate, msg.getString(4));
		assertTrue(msg.getComponent(4) instanceof ISOBinaryField);
	}	
	
}
