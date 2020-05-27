package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import distributie.beans.BeanEvenimentTableta;
import distributie.beans.BorderouMasina;
import distributie.database.DBManager;
import distributie.enums.EnumStatusMasina;
import distributie.queries.SqlQueries;
import distributie.utils.Utils;

public class OperatiiBorderou {

	public List<BeanEvenimentTableta> getEvenimenteTableta(String codBorderou) {

		List<BeanEvenimentTableta> listEvenimente = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getEvenimenteTableta(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codBorderou);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			BeanEvenimentTableta eveniment = null;
			while (rs.next()) {
				eveniment = new BeanEvenimentTableta();
				eveniment.setClient(rs.getString("client"));
				eveniment.setCodAdresa(rs.getString("codadresa"));
				eveniment.setEveniment(rs.getString("eveniment"));
				eveniment.setData(rs.getString("data"));
				eveniment.setOra(rs.getString("ora"));
				eveniment.setGps(rs.getString("gps"));
				eveniment.setKmBord(Integer.valueOf(rs.getString("fms")));
				listEvenimente.add(eveniment);

			}

		} catch (SQLException e) {

		}

		return listEvenimente;
	}

	private EnumStatusMasina getStareMasina(String codBorderou) throws SQLException {

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement prep = conn.prepareStatement(SqlQueries.getStareMasina())) {

			prep.setString(1, codBorderou);
			ResultSet rs = prep.executeQuery();

			rs.next();
			int livrExist = rs.getInt(1);

			if (livrExist > 0)
				return EnumStatusMasina.BORDEROU_TERMINAT;
			else
				return EnumStatusMasina.BORDEROU_ACTIV;

		}

	}

	public List<BorderouMasina> getStareBordMasini(String strMasini) {

		List<BorderouMasina> listBordMasini = new ArrayList<>();
		List<String> listMasini = Arrays.asList(strMasini.replace("'", "").replace("-", "").split(","));
		copyBord(listMasini, listBordMasini);

		String sqlString = " select numarb, masina from websap.borderouri where sttrg >= 2 " + " and masina in ("
				+ strMasini + ") " + " order by masina, sttrg desc, data_e asc ";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				Statement stmt = conn.createStatement()) {

			ResultSet rs = stmt.executeQuery(sqlString);

			while (rs.next()) {

				BorderouMasina bord = new BorderouMasina();
				bord.setNrMasina(rs.getString("masina").replace("-", ""));

				if (listBordMasini.get(listBordMasini.indexOf(bord)).getNrBorderou() == null) {
					listBordMasini.get(listBordMasini.indexOf(bord)).setNrBorderou(rs.getString("numarb"));
					listBordMasini.get(listBordMasini.indexOf(bord)).setStatus(getStareMasina(rs.getString("numarb")));
				}

			}

		} catch (SQLException ex) {
			System.out.println(ex.toString());
		}

		return listBordMasini;
	}

	private void copyBord(List<String> listMasini, List<BorderouMasina> listBordMasini) {
		for (String masina : listMasini) {

			BorderouMasina bord = new BorderouMasina();
			bord.setNrMasina(masina);
			bord.setStatus(EnumStatusMasina.FARA_BORDEROU);
			listBordMasini.add(bord);
		}
	}

}
