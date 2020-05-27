package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import distributie.beans.BorderouMasina;
import distributie.beans.PozitieMasina;
import distributie.database.DBManager;
import distributie.utils.MailOperations;

public class Localizare {

	public String getPozitieMasini(String listMasini) {
		String strMasini = listMasini.replace("-", "");

		OperatiiBorderou opBord = new OperatiiBorderou();

		final List<BorderouMasina> listStare = opBord.getStareBordMasini(listMasini);

		String sqlString = " select d.device_id, d.latitude, d.longitude, to_char(d.record_time, 'dd-mon-yyyy hh24:mi:ss','NLS_DATE_LANGUAGE = ENGLISH') datac, "
				+ " m.nr_masina, d.speed from gps_index d, gps_masini m where d.device_id in (select id from gps_masini where nr_masina in (" + strMasini
				+ ")) and  d.device_id = m.id ";
		
		
		
		

		OperatiiSoferi opSoferi = new OperatiiSoferi();
		List<PozitieMasina> listPozitii = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {

				PozitieMasina pozitie = new PozitieMasina();
				pozitie.setDeviceId(String.valueOf(rs.getInt("device_id")));
				pozitie.setLatitudine(String.valueOf(rs.getDouble("latitude")).replace(",", "."));
				pozitie.setLongitudine(String.valueOf(rs.getDouble("longitude")).replace(",", "."));
				pozitie.setData(rs.getString("datac"));
				pozitie.setNrAuto(rs.getString("nr_masina"));
				pozitie.setViteza(String.valueOf(rs.getInt("speed")));
				pozitie.setActual();

				BorderouMasina bord = new BorderouMasina();
				bord.setNrMasina(pozitie.getNrAuto());

				pozitie.setStatus(listStare.get(listStare.indexOf(bord)).getStatus());
				pozitie.setBorderou(opSoferi.getBorderouCurent(conn, pozitie.getNrAuto()));
				pozitie.setTipMasina(opSoferi.getTipMasina(conn, pozitie.getNrAuto()));

				listPozitii.add(pozitie);

			}

		}catch(Exception ex){
			System.out.println(ex.toString());
		}

		return loadLocations(listPozitii);

	}

	private String loadLocations(List<PozitieMasina> listPozitii) {

		String stringGeo = "";

		for (int i = 0; i < listPozitii.size(); i++) {
			if (stringGeo.length() == 0) {
				stringGeo = "" + listPozitii.get(i).getLatitudine() + "," + listPozitii.get(i).getLongitudine() + "," + listPozitii.get(i).getNrAuto() + ","
						+ listPozitii.get(i).getViteza() + "," + listPozitii.get(i).getData() + "," + listPozitii.get(i).getStatus().getCodStatus() + ","
						+ listPozitii.get(i).getBorderou() + "," + listPozitii.get(i).getTipMasina();
			} else {
				stringGeo += "#" + listPozitii.get(i).getLatitudine() + "," + listPozitii.get(i).getLongitudine() + "," + listPozitii.get(i).getNrAuto() + ","
						+ listPozitii.get(i).getViteza() + "," + listPozitii.get(i).getData() + "," + listPozitii.get(i).getStatus().getCodStatus() + ","
						+ listPozitii.get(i).getBorderou() + "," + listPozitii.get(i).getTipMasina() + "";
			}
		}

		
		return stringGeo;
	}	
	
	
	
}
