package distributie.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import distributie.beans.Login;
import distributie.beans.User;
import distributie.helpers.HelperUser;
import distributie.queries.SqlQueries;
import distributie.queries.UserSqlQueries;
import distributie.utils.Utils;

public class UserDAO {

	public User validateUser(Login login) {

		User user = new User();

		String storedProcedure = "{ call web_pkg.wlogin(?,?,?,?,?,?,?,?,?,?) }";
		int logonStatus = 0;

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				CallableStatement callableStatement = conn.prepareCall(storedProcedure);) {

			callableStatement.setString(1, login.getUsername().trim());
			callableStatement.setString(2, login.getPassword().trim());

			callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.NUMERIC);

			callableStatement.execute();
			logonStatus = callableStatement.getInt(3);

			if (logonStatus == 3) {

				user.setFiliala(callableStatement.getString(5));

				String codAgent = callableStatement.getString(8);

				for (int i = 0; i < 8 - callableStatement.getString(8).length(); i++) {
					codAgent = "0" + codAgent;
				}

				user.setCodPers(codAgent);
				user.setNume(getNumeAngajat(conn, codAgent));
				user.setTipAcces(callableStatement.getString(6));
				user.setUnitLog(getUnitLogAngajat(conn, codAgent));
				user.setTipAngajat(getTipAngajat(conn, codAgent));

				if (user.getTipAcces().equals("4")) {
					user.setDepozit2(true);
					user.setUnitLog(user.getUnitLog().substring(0, 2) + "02");
				}

				if (isAccesPermited(user.getTipAcces())) {
					user.setSuccessLogon(true);
				} else {
					user.setSuccessLogon(false);
					user.setLogonMessage("Acces interzis");
				}

			} else {
				user.setSuccessLogon(false);
				user.setLogonMessage(HelperUser.getLogonStatus(logonStatus));
			}
		} catch (SQLException e) {
			System.out.println(Utils.getStackTrace(e));

			user.setSuccessLogon(false);
			user.setLogonMessage(HelperUser.getLogonStatus(logonStatus));

			return user;

		} catch (Exception e) {
			System.out.println(Utils.getStackTrace(e));
		}

		return user;
	}

	private static String getNumeAngajat(Connection conn, String angajatId) {

		String fullName = null;

		try (PreparedStatement stmt = conn.prepareStatement(UserSqlQueries.getFullName())) {

			stmt.setString(1, angajatId);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				fullName = rs.getString("nume");
			}

		} catch (Exception ex) {
			System.out.println(Utils.getStackTrace(ex));

		}

		return fullName;
	}

	private static String getTipAngajat(Connection conn, String angajatId) {

		String tipPers = null;

		try (PreparedStatement stmt = conn.prepareStatement(UserSqlQueries.getTipAngajat(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, angajatId);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				tipPers = rs.getString("functie");
			}

		} catch (Exception ex) {
			System.out.println(Utils.getStackTrace(ex));
		}

		return tipPers;
	}

	private static String getUnitLogAngajat(Connection conn, String angajatId) {

		String unitLog = null;

		try (PreparedStatement stmt = conn.prepareStatement(SqlQueries.getUnitLogAngajat())) {

			stmt.setString(1, angajatId);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				unitLog = rs.getString("filiala");
			}

		} catch (Exception ex) {
			System.out.println(Utils.getStackTrace(ex));
		}

		return unitLog;
	}

	private boolean isAccesPermited(String codAcces) {
		if (codAcces.equals("11") || codAcces.equals("16") || codAcces.equals("65") || codAcces.equals("20")
				|| codAcces.equals("13") || codAcces.equals("19") || codAcces.equals("140") || codAcces.equals("47")
				|| codAcces.equals("8") || codAcces.equals("89"))
			return true;
		else
			return false;
	}

}
