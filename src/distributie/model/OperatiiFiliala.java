package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import distributie.beans.Masina;
import distributie.database.DBManager;

public class OperatiiFiliala {

	public List<Masina> getMasiniFiliala(String codFiliala) {

		String sqlString = " select distinct a.ktext masina, nvl(b.id,-1) deviceid from sapprd.coas a, gps_masini b where  a.phas3<>'X' and "
				+ " a.werks !=' ' and a.mandt = '900' and a.auart = '1001' and "
				+ " replace(a.ktext, '-') = b.nr_masina and a.werks=:werks order by a.ktext";

		List<Masina> listMasini = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString);) {

			stmt.setString(1, codFiliala);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				Masina masina = new Masina();
				masina.setNrAuto(rs.getString("masina"));
				masina.setDeviceId(rs.getString("deviceid"));
				listMasini.add(masina);

			}

		} catch (SQLException ex) {
			System.out.println(ex.toString());
		}

		return listMasini;

	}

}
