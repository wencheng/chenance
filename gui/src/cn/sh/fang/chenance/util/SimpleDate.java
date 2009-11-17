package cn.sh.fang.chenance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDate extends Date {

	private static final long serialVersionUID = 5133340837481489661L;

	public static final int ONE_DAY_MILI = 86400000;

	public static final int ONE_DAY = 86400;

	public SimpleDate(long millis) {
		super(toCalendar(millis/ONE_DAY_MILI*ONE_DAY_MILI).getTimeInMillis());
	}

	public SimpleDate(Date date) {
		super(toCalendar(date.getTime()/ONE_DAY_MILI*ONE_DAY_MILI).getTimeInMillis());
	}

	private static Calendar toCalendar(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}
	
	/**
	 * presume the date is right about the local time,
	 * but not right about the timezone.
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static SimpleDate UTC(Date date) {
		date.setHours(-date.getTimezoneOffset()/60);
		return new SimpleDate(date);
	}

	public static SimpleDate UTC(String string) {
		try {
			return UTC(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SimpleDate yyyyww(String yyyyww) {
		Calendar cal = Calendar.getInstance();
		cal.setMinimalDaysInFirstWeek(7);
		cal.set(Calendar.YEAR, Integer.parseInt(yyyyww.substring(0,4)));
		cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(yyyyww.substring(4)));
		cal.set(Calendar.DAY_OF_WEEK, 1);
		return SimpleDate.UTC(cal.getTime());
	}

	/**
	 * ex: 2009/09/15 ~ 2009/10/14 => 30
	 * ex: 2009/10/15 ~ 2009/11/14 => 31
	 * ex: 2009/10/15 ~ 2009/11/15 => 32
	 * 
	 * @param from
	 * @return
	 */
	public int dayDiff(Date from) {
		return (int) ((this.getTime() - from.getTime()) / ONE_DAY_MILI) + 1;
	}

	public SimpleDate nextDay() {
		return new SimpleDate(this.getTime() + ONE_DAY_MILI);
	}

	public SimpleDate nextWeek() {
		return new SimpleDate(this.getTime() + ONE_DAY_MILI * 7);
	}

	public SimpleDate nextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		cal.add(Calendar.MONTH, 1);
		return new SimpleDate(cal.getTime());
	}

	/**
	 * make a range of date to query bDate <= x < eDate 
	 * 
	 * @param bDate
	 * @param eDate
	 * @param dateRange
	 * @param closingDay
	 */
	public static void resetDateRange(Date bDate, Date eDate, int dateRange, Integer closingDay) {
		if ( dateRange != Calendar.DATE 
				&& dateRange != Calendar.MONTH
				&& dateRange != Calendar.WEEK_OF_YEAR
				&& dateRange != Calendar.YEAR
				) {
			throw new IllegalArgumentException("dateRange only accepts: date, month, week_of_year, year");
		}
		
		if (closingDay == null) {
			closingDay = 31;
		}
		
		Calendar bgn = Calendar.getInstance();
		bgn.setTime(bDate);
		Calendar end = Calendar.getInstance();
		end.setTime(eDate);

		if ( dateRange == Calendar.DATE ) {
			end.add( Calendar.DATE, 1 );
		} else if ( dateRange == Calendar.MONTH ) {
			if (closingDay == 31) {
				bgn.set( Calendar.DATE, bgn.getActualMinimum(Calendar.DATE) );
				end.set( Calendar.DATE, end.getActualMaximum(Calendar.DATE) );
			} else {
				if ( bgn.get( Calendar.DATE ) > closingDay ) {
					bgn.set( Calendar.DATE, closingDay+1 );
				} else {
					bgn.add( Calendar.MONTH, -1 );
					bgn.set( Calendar.DATE, closingDay+1 );
				}

				if ( end.get( Calendar.DATE ) < closingDay ) {
					end.set( Calendar.DATE, closingDay+1 );
				} else {
					bgn.add( Calendar.MONTH, 1 );
					end.set( Calendar.DATE, closingDay+1 );
				}
			}
		} else if ( dateRange == Calendar.WEEK_OF_YEAR ) {
			bgn.add( Calendar.DATE, -(bgn.get(Calendar.DAY_OF_WEEK)-bgn.getMinimalDaysInFirstWeek()) );

			end.add( Calendar.DATE, 7 );
			end.add( Calendar.DATE, -(bgn.get(Calendar.DAY_OF_WEEK)-bgn.getMinimalDaysInFirstWeek()) );
		} else if ( dateRange == Calendar.YEAR ) {
			bgn.set( Calendar.MONTH, 0 );
			bgn.set( Calendar.DATE, 1 );

			end.set( Calendar.MONTH, 11 );
			end.set( Calendar.DATE, 31 );
			end.add( Calendar.DATE, 1 );
		}
		
		bDate.setTime(bgn.getTimeInMillis());
		eDate.setTime(end.getTimeInMillis());

	}

	public static SimpleDate yyyymm(String yyyymm) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(yyyymm.substring(0,4)));
		cal.set(Calendar.MONTH, Integer.parseInt(yyyymm.substring(4))-1);
		cal.set(Calendar.DATE, 1);
		return new SimpleDate(cal.getTimeInMillis());
	}

	public static void main(String[] s) {
		/*
select strftime('%Y%W', '2007-12-31')
union all
select strftime('%Y%W', '2008-01-01')
union all
select strftime('%Y%W', '2008-01-05', 'localtime')
union all
select strftime('%Y%W', '2008-01-06','localtime')
union all
select strftime('%Y%W', '2008-01-07','localtime');
		 */
		Calendar cal = Calendar.getInstance();
		cal.setMinimalDaysInFirstWeek(7);
		cal.setFirstDayOfWeek(1);
		cal.set(Calendar.YEAR, 2009);
		cal.set(Calendar.MONTH, 10);
		// sqlite3: 45
		cal.set(Calendar.DATE, 14);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));
		System.err.println(SimpleDate.yyyyww("200945"));
		// s: 46
		cal.set(Calendar.DATE, 16);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));
		// s: 46
		cal.set(Calendar.DATE, 17);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));
		cal.set(Calendar.MONTH, 0);
		// s: 0
		cal.set(Calendar.DATE, 1);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));
		// s: 0
		cal.set(Calendar.DATE, 4);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));
		// s: 1
		cal.set(Calendar.DATE, 5);
		System.err.println(cal.get(Calendar.WEEK_OF_YEAR));

		System.err.println(cal.getMinimalDaysInFirstWeek());
		cal.set(Calendar.YEAR, 2010);
		System.err.println(cal.getMinimalDaysInFirstWeek());
		cal.set(Calendar.YEAR, 2008);
		System.err.println(cal.getMinimalDaysInFirstWeek());

	}

}
