package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import distributie.beans.BeanStareTableta;

import distributie.database.DBManager;
import distributie.queries.SqlQueries;
import distributie.utils.DateUtils;

public class OperatiiTablete {

	public List<BeanStareTableta> getTableteSoferi(String codSofer) {

		List<BeanStareTableta> listTablete = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getTableteSofer(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codSofer);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				BeanStareTableta tableta = new BeanStareTableta();

				tableta.setId(rs.getString("codtableta"));
				tableta.setDataInreg(rs.getString("datai"));
				tableta.setStare(rs.getString("stare"));

				listTablete.add(tableta);
			}

		} catch (SQLException ex) {
			System.out.println(ex.toString());
		}

		return listTablete;
	}

	public String gestioneazaCod(String codTableta, String codSofer, String operatie)  {

		if (operatie.equalsIgnoreCase("aloca"))
			adaugaCod(codTableta, codSofer);
		if (operatie.equalsIgnoreCase("sterge"))
			invalideazaTablete(codSofer);
		
		return "";

	}

	public String adaugaCod(String codTableta, String codSofer) {

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.invalidateAllCodes(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codSofer);
			stmt.executeQuery();

			PreparedStatement innerStmt = conn.prepareCall(SqlQueries.addCodTableta());

			innerStmt.setString(1, codSofer);
			innerStmt.setString(2, codTableta);
			innerStmt.setString(3, DateUtils.getCurrentDate());
			innerStmt.setString(4, "1");
			innerStmt.setString(5, "1");
			innerStmt.setString(6, DateUtils.getCurrentTime());

			innerStmt.execute();

			innerStmt.close();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return null;
	}

	private String invalideazaTablete(String codSofer) {

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.invalidateAllCodes(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codSofer);
			stmt.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return null;
	}

}
