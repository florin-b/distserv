package distributie.comparators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import distributie.beans.BeanEvenimentTableta;
import distributie.utils.Formatting;

public class EvTablComparator implements Comparator<BeanEvenimentTableta> {

	@Override
	public int compare(BeanEvenimentTableta ev1, BeanEvenimentTableta ev2) {

		String strDate1 = Formatting.formatFromSap(ev1.getData() + " " + ev1.getOra());
		String strDate2 = Formatting.formatFromSap(ev2.getData() + " " + ev2.getOra());

		Date date1 = new Date();
		Date date2 = new Date();

		String pattern = "dd-MMM-yy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);

		try {
			date1 = sdf.parse(strDate1);
			date2 = sdf.parse(strDate2);
		} catch (ParseException p) {
			System.out.println(p.toString());
		}

		return date1.compareTo(date2);
	}

}
