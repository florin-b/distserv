package distributie.database;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

public class DBManager {

	public DataSource getProdDataSource() {

		OracleDataSource oracleDS = null;
		try {

			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.76:1521/PRD");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oracleDS;
	}
	
	
	public DataSource getTestDataSource() {

		OracleDataSource oracleDS = null;
		try {

			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.89:1527/TES");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oracleDS;
	}

}
