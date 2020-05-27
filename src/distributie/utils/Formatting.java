package distributie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Formatting {

	public static String simpleDateFormat(String date) {
		String formattedDate = "";

		if (date.contains(".")) {
			String[] dateArray = date.split("\\.");
			formattedDate = dateArray[2] + dateArray[1] + dateArray[0];

		}

		return formattedDate;
	}

	public static String formatFromSap(String target) {

		String formattedDate = "";

		try {
			SimpleDateFormat formatFinal = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);
			Date date = formatFinal.parse(target);

			String pattern = "dd-MMM-yy HH:mm:ss";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, Locale.US);

			formattedDate = formatInit.format(date);

		} catch (java.text.ParseException e) {
			System.out.println(Utils.getStackTrace(e));
		}
		return formattedDate;

	}

	public static long dateDiffMin(String date1, String date2) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = sdf.parse(date1);
			d2 = sdf.parse(date2);
		} catch (ParseException ex) {
			System.out.println(ex.toString());
		}

		long diff = d2.getTime() - d1.getTime();

		long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

		return minutes;

	}

}
