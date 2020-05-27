package distributie.test;

import java.text.ParseException;
import java.util.Date;

import distributie.model.OperatiiTraseu;

public class Test {

	public static void main(String[] args) throws ParseException{

		
		
		//System.out.println(new OperatiiGps().getGpsInactiv("GL10"));
		
		//Login login = new Login("MTANASESCU", "3mbF2W");
		//System.out.println(new UserDAO().validateUser(login));

		//new OperatiiTablete().gestioneazaCod("356262053100075", "00122873", "aloca");
		
		System.out.println(new OperatiiTraseu().getTraseuInterval("GL08SLE","19-05-2020 00:00","19-05-2020 12:00"));
		

		

		
		
	}
	
	
	public static int dateDiff(Date dateStart, Date dateStop) {

		if (dateStart == null || dateStop == null)
			return -1;

		try {

			long diff = dateStop.getTime() - dateStart.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				return (int)diffDays;
			}
			

		} catch (Exception e) {
			
		}

		return -1;

	}

}
