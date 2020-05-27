package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import distributie.beans.Sofer;
import distributie.database.DBManager;
import distributie.utils.MailOperations;

public class Soferi {

	public List<Sofer> getListSoferi(String codFiliala) {

		String sqlString = " select distinct nume, cod from soferi where fili =:codFiliala order by nume ";

		List<Sofer> listSoferi = new ArrayList<Sofer>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codFiliala);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				Sofer sofer = new Sofer();
				sofer.setCodSofer(rs.getString("cod"));
				sofer.setNumeSofer(rs.getString("nume"));
				listSoferi.add(sofer);
			}

		} catch (SQLException ex) {
			MailOperations.sendMail(ex.toString());
		}

		if (!listSoferi.isEmpty()) {
			Sofer sofer = new Sofer();
			sofer.setCodSofer("-1");
			sofer.setNumeSofer("Selectati un sofer");
			listSoferi.add(0, sofer);
		}

		return listSoferi;

	}

}
