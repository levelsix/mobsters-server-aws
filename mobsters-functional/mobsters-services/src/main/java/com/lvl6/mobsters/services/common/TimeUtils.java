package com.lvl6.mobsters.services.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils
{

	private static Logger LOG = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	private static String[] DAYS = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY",
		"SATURDAY", "SUNDAY", };

	public static int NUM_MINUTES_LEEWAY_FOR_CLIENT_TIME = 5;

	public static DateTimeZone PST =
		DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
	
	private TimeUtils() {
		throw new AssertionError("TimeUtils is a static utility class");
	}

	public static boolean isSynchronizedWithServerTime( Date maybeNow )
	{
		if (null == maybeNow) { return false; }
		DateTime possiblyNow = new DateTime(maybeNow);
		DateTime now = new DateTime();
		Period interim = new Period(possiblyNow, now);

		int minutesApart = interim.getMinutes();
		if (minutesApart > NUM_MINUTES_LEEWAY_FOR_CLIENT_TIME) {
			// client time is unsynchronized with server time
			return false;
		} else {
			return true;
		}
	}

	public static int numMinutesDifference( Date d1, Date d2 )
	{
		// log.info("numMinutesDifference() d1=" + d1);
		// log.info("numMinutesDifference() d2=" + d2);

		MutableDateTime mdtOne = new DateTime(d1).toMutableDateTime();
		mdtOne.setSecondOfMinute(0);
		DateTime dOne = mdtOne.toDateTime();

		MutableDateTime mdtTwo = new DateTime(d2).toMutableDateTime();
		mdtTwo.setSecondOfMinute(0);
		DateTime dTwo = mdtTwo.toDateTime();

		Period interim = new Period(dOne, dTwo);

		return interim.getMinutes();
	}

	// not sure if DateTime works, as well.
	public static boolean isFirstEarlierThanSecond( Date one, Date two )
	{
		if (null == one
			&& null == two) {
			LOG.info("both dates null");
			return false;
		} else if (null == one) {
			LOG.info("first date null");
			return true;
		} else if (null == two) {
			LOG.info("second date null");
			return false;
		}

		LocalDateTime ldOne = new LocalDateTime(one);
		LocalDateTime ldTwo = new LocalDateTime(two);

		// log.info("ldOne=" + ldOne);
		// log.info("ldTwo=" + ldTwo);

		return ldOne.isBefore(ldTwo);
	}

	public static int getDayOfWeek( String dayOfWeekName )
	{
		for (int i = 0; i < DAYS.length; i++ ) {

			String dow = DAYS[i];
			if (dow.equals(dayOfWeekName)) { return i + 1; }
		}
		return 0;
	}

	public static int getDayOfWeekPst( Date d )
	{
		DateTime dt = new DateTime(d, PST);
		return dt.getDayOfWeek();
	}

	public static int getDayOfMonthPst( Date d )
	{
		DateTime dt = new DateTime(d, PST);
		return dt.getDayOfMonth();
	}

    public static Date createNow() {
    	return new Date();
    }

	// dayOffset is most likely negative (called from ClanEventPersistentRetrieveUtils.java)
	public static Date createPstDate( Date curDate, int dayOffset, int hour, int minutesAddend )
	{
		DateTime dt = new DateTime(curDate, PST);
		// log.info("nowish in pst (Date form) " + dt.toDate() + "\t (DateTime form) " + dt);
		MutableDateTime mdt = dt.withTimeAtStartOfDay()
			.toMutableDateTime();
		mdt.addDays(dayOffset);
		mdt.setHourOfDay(hour);
		mdt.addMinutes(minutesAddend);

		// log.info("pstDate created: " + mdt.toDateTime());
		Date createdDate = mdt.toDate();
		// log.info("date with hour set: (Date form) " + createdDate);
		return createdDate;
	}

	/**
	 * 
	 * @param curDate
	 * @param minutesAddend
	 *        Can be negative.
	 * @return
	 */
	public static Date createPstDateAddMinutes( Date curDate, int minutesAddend )
	{
		DateTime dt = new DateTime(curDate, PST);
		// log.info("nowish in pst (Date form) " + dt.toDate() + "\t (DateTime form) " + dt +
		// "\t originally=" + curDate);

		MutableDateTime mdt = dt.toMutableDateTime();
		mdt.addMinutes(minutesAddend);
		// log.info("pstDate created2: " + mdt.toDateTime());
		Date createdDate = mdt.toDate();
		// log.info("date advanced " + minutesAddend + " minutes. date=" + createdDate);
		return createdDate;
	}

	/**
	 * 
	 * @param curDate
	 * @param daysAddend
	 *        Can be negative.
	 * @return
	 */
	public static Date createDateAddDays( Date curDate, int daysAddend )
	{
		DateTime dt = new DateTime(curDate);

		MutableDateTime mdt = dt.toMutableDateTime();
		mdt.addDays(daysAddend);
		Date createdDate = mdt.toDate();

		return createdDate;
	}

	/**
	 * 
	 * @param curDate
	 * @param hoursAddend
	 *        Can be negative.
	 * @return
	 */
	public static Date createDateAddHours( Date curDate, int hoursAddend )
	{
		DateTime dt = new DateTime(curDate);

		MutableDateTime mdt = dt.toMutableDateTime();
		mdt.addHours(hoursAddend);
		Date createdDate = mdt.toDate();

		return createdDate;
	}

	public static Date createDateTruncateMillis( Date curDate )
	{
		DateTime dt = new DateTime(curDate);

		MutableDateTime mdt = dt.toMutableDateTime();
		mdt.setMillisOfSecond(0);
		Date createdDate = mdt.toDate();

		return createdDate;
	}
    
    public static Calendar createToday() {
    	return new GregorianCalendar();
    }
    
    public static long currentTimeMillis() {
    	return System.currentTimeMillis();
    }
}