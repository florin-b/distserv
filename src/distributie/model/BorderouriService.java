package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import distributie.beans.Borderou;
import distributie.database.DBManager;
import distributie.queries.SqlQueries;
import distributie.utils.Formatting;
import distributie.utils.MailOperations;

public class BorderouriService {

	public List<Borderou> getBorderouri(String codSofer, String dataStart, String dataStop) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		List<Borderou> listBorderouri = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getBorderouri(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codSofer);
			stmt.setString(2, Formatting.simpleDateFormat(dataStart));
			stmt.setString(3, Formatting.simpleDateFormat(dataStop));
			stmt.setString(4, codSofer);
			stmt.setString(5, Formatting.simpleDateFormat(dataStart));
			stmt.setString(6, Formatting.simpleDateFormat(dataStop));

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				Borderou borderou = new Borderou();
				borderou.setCod(rs.getString("numarb"));
				borderou.setDataEmitere(rs.getDate("dataEmitere").toLocalDate().format(formatter));
				borderou.setActiv(Boolean.valueOf(rs.getString("activ")));
				listBorderouri.add(borderou);
			}

		} catch (SQLException ex) {
			MailOperations.sendMail(ex.toString());
		}

		if (listBorderouri.isEmpty()) {
			Borderou bord = new Borderou();
			bord.setCod("Nu exista borderouri");
			bord.setDataEmitere("");
			bord.setActiv(false);
			listBorderouri.add(bord);
		} else {
			Borderou bord = new Borderou();
			bord.setCod("Selectati un borderou");
			bord.setDataEmitere("-1");
			bord.setActiv(false);
			listBorderouri.add(0, bord);
		}

		return listBorderouri;

	}

}
