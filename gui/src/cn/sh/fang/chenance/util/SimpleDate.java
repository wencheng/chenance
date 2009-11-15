package cn.sh.fang.chenance.util;

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
	
	public static SimpleDate UTC(Date date) {
		return new SimpleDate(date.getTime() + ONE_DAY_MILI);
	}

	public static SimpleDate yyyyww(String yyyyww) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(yyyyww.substring(0,4)));
		cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(yyyyww.substring(4))+1);
		return new SimpleDate(cal.getTimeInMillis());
	}

	@SuppressWarnings("deprecation")
	public SimpleDate clearHour() {
		this.setHours(0);
		return this;
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

}
