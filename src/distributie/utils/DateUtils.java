package distributie.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(new Date());
	}

	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("HHmmss");
		return dateFormat.format(new Date());

	}

	public static String formatDate(String strDate) {

		String formatted = "";

		try {
			SimpleDateFormat formatFinal = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatFinal.parse(strDate);

			String pattern = "dd-MMM-yyyy";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, new Locale("ro"));

			formatted = formatInit.format(date);
		} catch (ParseException p) {
			MailOperations.sendMail(Utils.getStackTrace(p));
		}

		return formatted;

	}

	public static int dateDiffMin(Date dateStart, Date dateStop) {

		long diffMinutes = 0;

		if (dateStart == null || dateStop == null)
			return 0;

		try {

			long diff = dateStop.getTime() - dateStart.getTime();

			diffMinutes = diff / (60 * 1000) % 60;

		} catch (Exception e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		return (int) diffMinutes;

	}

	public static boolean hasWeekend(String startInterval, String stopInterval) {

		Date dateStart = DateUtils.getShortDate(startInterval);
		Date dateStop = DateUtils.getShortDate(stopInterval);

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateStart);

		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return true;
		else
			while (cal.getTime().before(dateStop)) {
				cal.add(Calendar.DATE, 1);

				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					return true;

			}

		return false;

	}

	public static String formatDateSap(String strDate) {

		String formatted = "";

		try {
			SimpleDateFormat formatFinal = new SimpleDateFormat("dd-mm-yyyy");
			Date date = formatFinal.parse(strDate);

			String pattern = "yyyymmdd";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, new Locale("ro"));

			formatted = formatInit.format(date);
		} catch (ParseException p) {
			MailOperations.sendMail(Utils.getStackTrace(p));
		}

		return formatted;

	}

	public static String formatDateFromSap(String strDate) {

		String formatted = "";

		try {

			String pattern = "yyyymmdd";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, new Locale("ro"));
			Date date = formatInit.parse(strDate);

			SimpleDateFormat formatFinal = new SimpleDateFormat("dd-mm-yyyy");

			formatted = formatFinal.format(date);
		} catch (ParseException p) {
			MailOperations.sendMail(Utils.getStackTrace(p));
		}

		return formatted;

	}

	public static String formatTime(String strTime) {
		return strTime.substring(0, 2) + ":" + strTime.substring(2, 4);
	}

	public static Date getDate(String stringDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm");
		Date date = new Date();

		try {
			date = dateFormat.parse(stringDate);
		} catch (ParseException e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		return date;
	}

	public static String dateDiff(Date dateStart, Date dateStop) {

		StringBuilder result = new StringBuilder();

		if (dateStart == null || dateStop == null)
			return result.toString();

		try {

			long diff = dateStop.getTime() - dateStart.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				result.append(diffDays);
				result.append(" zile ");
			}

			if (diffHours > 0) {
				result.append(diffHours);
				result.append(" ore ");
			}

			if (diffMinutes > 0) {
				result.append(diffMinutes);

				result.append(" min");

			} else {
				if (diffMinutes != 0) {
					diffMinutes = 60 - Math.abs(diffMinutes);
					result.append(diffMinutes);

					result.append(" min");

				}
			}

		} catch (Exception e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		return result.toString();

	}

	public static Date getShortDate(String stringDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();

		try {
			date = dateFormat.parse(stringDate);
		} catch (ParseException e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		return date;
	}

	public static String addDaysToDate(int nrDays) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, nrDays);

		Date date = calendar.getTime();

		return dateFormat.format(date);

	}

	public static String getStrTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

	}

	public static String getTimeStampEET() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		TimeZone tz = TimeZone.getTimeZone("EET");
		sdf.setTimeZone(tz);

		return sdf.format(Calendar.getInstance().getTime());

	}

}
