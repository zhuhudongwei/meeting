package com.meeting.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 1.1 $ $Date: 2007/10/22 23:52:58 $
 */
public class DateUtil
{
	// ~ Static fields/initializers
	// =============================================
	
	private static Log		log					= LogFactory.getLog(DateUtil.class);
	private static String	defaultDatePattern	= null;
	private static String	timePattern			= "HH:mm";
	
	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern()
	{
		Locale locale = LocaleContextHolder.getLocale();
		try
		{
//			defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("date.format");
		}
		catch (MissingResourceException mse)
		{
			defaultDatePattern = "yyyy-MM-dd";
		}
		
		return defaultDatePattern;
	}
	
	// ~ Methods
	// ================================================================
	
	/**
	 * This method removes the separator of the dateString in the format you
	 * specify on input
	 * 
	 * @param dateString
	 * @return a string afterRemoving
	 */
	public static String removeSeparator(String dateString)
	{
		
		String datePattern = getDatePattern();
		
		if(datePattern.indexOf('-') > 0)
		{
			dateString = dateString.replace("-", "");
		}
		else if(datePattern.indexOf('/') > 0)
		{
			dateString = dateString.replace("/", "");
		}
		
		return dateString;
	}
	
	/**
	 * This method adds the separator to the string in the format you specify on
	 * input
	 * 
	 * @param string
	 * @return a dateString afterAdding
	 */
	public static String addSeparator(String string)
	{
		
		String datePattern = getDatePattern();
		datePattern = "yyyy-MM-dd";
		String separator = "";
		
		if(datePattern.indexOf('-') > 0)
		{
			separator = "-";
		}
		else if(datePattern.indexOf('/') > 0)
		{
			separator = "/";
		}
		String dateString = string;
		int fromIndex = 0;
		int index = datePattern.indexOf(separator, fromIndex);
		while (index > 0)
		{
			dateString = dateString.substring(0, index).concat(separator).concat(dateString.substring(index));
			fromIndex = index + 1;
			index = datePattern.indexOf(separator, fromIndex);
		}
		
		return dateString;
	}
	
	public static String getDateTimePattern()
	{
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}
	
	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate)
	{
		SimpleDateFormat df = null;
		String returnValue = "";
		
		if(aDate != null)
		{
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}
		
		return (returnValue);
	}
	
	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException
	{
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		
		if(log.isDebugEnabled())
		{
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}
		
		try
		{
			date = df.parse(strDate);
		}
		catch (ParseException pe)
		{
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		
		return (date);
	}
	
	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime)
	{
		return getDateTime(timePattern, theTime);
	}
	
	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException
	{
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
		
		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));
		
		return cal;
	}
	
	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate)
	{
		SimpleDateFormat df = null;
		String returnValue = "";
		
		if(aDate == null)
		{
			log.error("aDate is null!");
		}
		else
		{
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		
		return (returnValue);
	}
	
	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate)
	{
		return getDateTime(getDatePattern(), aDate);
	}
	
	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) throws ParseException
	{
		Date aDate = null;
		
		try
		{
			if(log.isDebugEnabled())
			{
				log.debug("converting date with pattern: " + getDatePattern());
			}
			if(strDate == null || strDate.trim().length()==0){
				return aDate;
			}
			aDate = convertStringToDate(getDatePattern(), strDate);
		}
		catch (ParseException pe)
		{
			log.error("Could not convert '" + strDate + "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			
		}
		
		return aDate;
	}
	
	// public static void main(String args[])
	// {
	// addSeparator("19821220");
	// }
	 /**
	 * 将页面上传过来的字符串型时间转化成long类型
	 * 
	 * @param dateInString
	 *            字符串型时间
	 * @return
	 * datecode == "yyyy-MM-dd" or datecode == "yyyy-MM-dd HH:mm:ss"
	 */
	public static Long convertDateInStringToLong(String dateInString, String datecode) {
		if (dateInString == null || dateInString.equals(""))
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(datecode);
		try {
			return new Long(dateFormat.parse(dateInString).getTime());
		} catch (ParseException parseException) {
			return new Long(new Date().getTime());
		}
	}

	/**
	 * 将Long表示的时间转换为日期字符串
	 * 
	 * @param dateTime
	 * @return 转换后的字符串 如果Long为null，则返回null
	 * datecode == "yyyy-MM-dd" or datecode == "yyyy-MM-dd HH:mm:ss"
	 * @throws ParseException
	 */
	public static String convertLongToDateString(Long dateTime, String datecode) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datecode);
		String dateStr = null;
		if (dateTime != null) {
			dateStr = sdf.format(new Date(dateTime));
		}
		return dateStr;
	}
}
