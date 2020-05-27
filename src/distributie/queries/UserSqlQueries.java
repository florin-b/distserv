package distributie.queries;

public class UserSqlQueries {

	public static String getFullName() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select nume from personal where cod =? ");

		return sqlString.toString();
	}

	public static String getTipAngajat() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select functie from personal where cod = ?");

		return sqlString.toString();
	}
	
	public static String getCodDepart() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select divizie from agenti where cod =? ");

		return sqlString.toString();
	}
	
	public static String getAgenti()
	{
		StringBuilder sqlString = new StringBuilder();
		
		sqlString.append(" select cod, nume from agenti where filiala in ('BU10','BU11') and divizie = ? order by nume");
		return sqlString.toString();
	}

}
