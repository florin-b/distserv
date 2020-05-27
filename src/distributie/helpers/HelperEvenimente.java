package distributie.helpers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import distributie.beans.Client;
import distributie.beans.ClientNelivrat;
import distributie.beans.GpsInactiv;
import distributie.beans.Oprire;
import distributie.beans.RezultatTraseu;
import distributie.beans.SumarTraseu;
import distributie.utils.Utils;
import distributie.utils.UtilsAdrese;

public class HelperEvenimente {

	public static String formatEvenimente(Set<RezultatTraseu> rezultat) {

		StringBuilder strResult = new StringBuilder();

		try {

			strResult.append("<table class='imagetable'><tr>");
			strResult.append("<th>Nr</th>");
			strResult.append("<th>Client</th>");
			strResult.append("<th>Cod</th>");
			strResult.append("<th>Sosire</th>");
			strResult.append("<th>Plecare</th>");
			strResult.append("<th>Stationare</th></tr>");

			int cont = 1;
			for (RezultatTraseu traseu : rezultat) {

				strResult.append("<tr><td align='center'>");
				strResult.append(String.valueOf(cont) + ".");
				strResult.append("</td>");

				strResult.append("<td>");
				strResult.append(traseu.getClient().getNumeClient() + "<br>");
				strResult.append(getFormattedAddress(traseu.getClient()));
				strResult.append("</td>");

				strResult.append("<td>");
				strResult.append(traseu.getClient().getCodClient());
				strResult.append("</td>");

				strResult.append("<td>");
				strResult.append(validareSosire(traseu));
				strResult.append("</td>");

				strResult.append("<td>");
				strResult.append(validarePlecare(traseu));
				strResult.append("</td>");

				strResult.append("<td>");
				strResult.append(Utils.dateDiff(validareSosire(traseu), validarePlecare(traseu)));
				strResult.append("</td></tr>");

				cont++;

			}

			strResult.append("</table>");

		} catch (Exception ex) {
			System.out.println("displayFormattedResults : " + rezultat + " : " + Arrays.toString(ex.getStackTrace()));
		}
		return strResult.toString();

	}

	public static String formatSumarBorderou(SumarTraseu sumar) {
		StringBuilder strResult = new StringBuilder();

		strResult.append("<table class='imagetable'>");
		strResult.append("<th colspan='2' align='left'>Sumar</th>");

		strResult.append("<tr><td>");
		strResult.append("Start cursa");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getDataStart() == null ? "" : sumar.getDataStart());
		strResult.append("</td></tr>");

		strResult.append("<tr><td>");
		strResult.append("Stop cursa");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getDataStop() == null ? "" : sumar.getDataStop());
		strResult.append("</td></tr>");

		strResult.append("<tr><td>");
		strResult.append("Durata");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getDurata() == null ? "" : sumar.getDurata());
		strResult.append("</td></tr>");

		strResult.append("<tr><td>");
		strResult.append("Km");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getKm() == null ? "" : sumar.getKm());
		strResult.append("</td></tr>");

		strResult.append("</table>");

		return strResult.toString();
	}

	public static String formatSumarInterval(SumarTraseu sumar) {
		StringBuilder strResult = new StringBuilder();

		strResult.append("<table class='imagetable'>");
		strResult.append("<th colspan='2' align='left'>Sumar</th>");

		strResult.append("<tr><td width='30%'>");
		strResult.append("Distanta parcursa");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getKm() == null ? "" : sumar.getKm());
		strResult.append(" km</td></tr>");

		strResult.append("<tr><td>");
		strResult.append("Viteza medie");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getVitezaMedie() == null ? "" : sumar.getVitezaMedie());
		strResult.append(" km/h</td></tr>");

		strResult.append("<tr><td>");
		strResult.append("Viteza maxima");
		strResult.append("</td>");

		strResult.append("<td>");
		strResult.append(sumar.getVitezaMaxima() == null ? "" : sumar.getVitezaMaxima());
		strResult.append(" km/h</td></tr>");

		strResult.append("</table>");

		return strResult.toString();
	}

	public static String formatGpsInactiv(List<GpsInactiv> listGps) {

		StringBuilder strResult = new StringBuilder();

		strResult.append("<table class='imagetable'><tr>");
		strResult.append("<th>Nr</th>");
		strResult.append("<th>Numar auto</th>");
		strResult.append("<th>Zile inactivitate</th>");

		int cont = 1;
		for (GpsInactiv gpsInactiv : listGps) {
			strResult.append("<tr><td align='center'>");
			strResult.append(String.valueOf(cont) + ".");
			strResult.append("</td>");

			strResult.append("<td align='center'>");
			strResult.append(gpsInactiv.getNrAuto());
			strResult.append("</td>");

			strResult.append("<td align='center'>");
			strResult.append(gpsInactiv.getNrZileInact());
			strResult.append("</td>");

			cont++;

		}

		return strResult.toString();

	}

	public static String formatClientiNelivrati(List<ClientNelivrat> listClienti) {

		StringBuilder strResult = new StringBuilder();

		if (listClienti != null && listClienti.size() > 0) {

			strResult.append("<b>Marfa nu a fost livrata la urmatorii clienti:</b><p></p>");

			strResult.append("<table class='imagetable'><tr>");
			strResult.append("<th>Nr</th>");
			strResult.append("<th>Cod client</th>");
			strResult.append("<th>Nume client</th>");
			strResult.append("<th>Adresa</th>");

			int pos = 1;
			for (ClientNelivrat client : listClienti) {

				strResult.append("<tr><td align='center'>");
				strResult.append(String.valueOf(pos) + ".");
				strResult.append("</td>");

				strResult.append("<td align='center'>");
				strResult.append(client.getCodClient());
				strResult.append("</td>");

				strResult.append("<td align='left'>");
				strResult.append(client.getNumeClient());
				strResult.append("</td>");

				strResult.append("<td align='left'>");
				strResult.append(client.getAdresa());
				strResult.append("</td>");

				pos++;

			}
		} else {
			strResult.append("<b>Marfa a fost livrata la toti clientii.</b><p></p>");

		}

		return strResult.toString();

	}

	private static String validareSosire(RezultatTraseu traseu) {
		return traseu.getSosire() == null ? "" : traseu.getSosire().getData();

	}

	private static String validarePlecare(RezultatTraseu traseu) {
		if (traseu.getPlecare() == null)
			return "";
		else
			return traseu.getPlecare().getData();

	}

	private static String getFormattedAddress(Client client) {
		StringBuilder str = new StringBuilder();

		if (client.getCodJudet() != "") {
			str.append(UtilsAdrese.getNumeJudet(client.getCodJudet()));
		}

		if (client.getLocalitate() != "") {
			str.append(", ");
			str.append(client.getLocalitate());
		}

		if (client.getStrada() != "") {
			str.append(", ");
			str.append(client.getStrada());
		}

		if (client.getNrStrada().trim().length() != 0) {
			str.append(", ");
			str.append(client.getNrStrada());
		}

		return str.toString();
	}

	public static String formatOpririTraseu(List<Oprire> listOpriri) {

		StringBuilder str = new StringBuilder();

		for (Oprire oprire : listOpriri) {
			str.append(oprire.getData());
			str.append("-");
			str.append(oprire.getDurata());
			str.append("-");
			str.append(oprire.getPozitieGps().getLatitudine());
			str.append("-");
			str.append(oprire.getPozitieGps().getLongitudine());
			str.append("!");
		}

		return str.toString();
	}

}
