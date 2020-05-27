package distributie.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import distributie.beans.Address;
import distributie.beans.BeanBoundBord;
import distributie.beans.BeanEvenimentTableta;
import distributie.beans.Client;
import distributie.beans.CoordonateGps;
import distributie.beans.DateBorderou;
import distributie.beans.Oprire;
import distributie.beans.PozitieClient;
import distributie.beans.PozitieGps;
import distributie.beans.RezultatTraseu;
import distributie.beans.SumarTraseu;
import distributie.beans.Traseu;
import distributie.beans.TraseuBorderou;
import distributie.beans.TraseuInterval;
import distributie.comparators.EvTablComparator;
import distributie.database.DBManager;
import distributie.enums.EnumTipClient;
import distributie.enums.EnumTipEveniment;
import distributie.helpers.HelperEvenimente;
import distributie.queries.SqlQueries;
import distributie.utils.Constants;
import distributie.utils.Formatting;
import distributie.utils.MapUtils;
import distributie.utils.Utils;
import distributie.utils.UtilsAdrese;

public class OperatiiTraseu {

	private List<PozitieClient> pozitiiClienti;
	private List<TraseuBorderou> traseuBorderou;
	private List<RezultatTraseu> rezultatTraseu;
	private List<RezultatTraseu> stareTraseu;
	private Set<RezultatTraseu> traseuFinal;
	private String dataStartBorderou;

	private enum EvenimentClient {
		SOSIRE, PLECARE
	};

	public OperatiiTraseu() {
		this.rezultatTraseu = new ArrayList<RezultatTraseu>();
	}

	public List<PozitieClient> getPozitiiClienti() {
		return pozitiiClienti;
	}

	public void setPozitiiClienti(List<PozitieClient> pozitiiClienti) {
		this.pozitiiClienti = pozitiiClienti;
	}

	public List<TraseuBorderou> getTraseuBorderou() {
		return traseuBorderou;
	}

	public void setTraseuBorderou(List<TraseuBorderou> traseuBorderou) {
		this.traseuBorderou = traseuBorderou;
	}

	public void setRezultatTraseu(List<RezultatTraseu> rezultatTraseu) {
		this.rezultatTraseu = rezultatTraseu;
	}

	private void descoperaEvenimente() {

		double distanta = 0;
		for (TraseuBorderou traseu : traseuBorderou) {
			for (PozitieClient pozitieClient : pozitiiClienti) {

				distanta = MapUtils.distanceXtoY(traseu.getLatitudine(), traseu.getLongitudine(),
						pozitieClient.getLatitudine(), pozitieClient.getLongitudine(), "K");

				pozitieClient.setDistantaCamion((int) distanta);

				if (conditiiSosire(traseu, distanta, pozitieClient)) {
					adaugaEveniment(pozitieClient, traseu, EvenimentClient.SOSIRE);

				}

				if (conditiiPlecare(traseu, pozitieClient)) {
					adaugaEveniment(pozitieClient, traseu, EvenimentClient.PLECARE);

				}

			}

		}

	}

	private boolean conditiiSosire(TraseuBorderou traseu, double distanta, PozitieClient pozitieClient) {

		if (pozitieClient.isStopBord()) {
			if (borderouNotStarted(traseu, pozitieClient)) {
				return false;
			}

		}

		if (traseu.getViteza() == 0 && distanta <= getDistanta(pozitieClient.getTipClient())
				&& condSuplimentSosire(traseu))
			return true;

		return false;
	}

	private boolean condSuplimentSosire(TraseuBorderou traseuBorderou) {

		if (dataStartBorderou == null)
			return true;

		try {
			Date dateLow = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).parse(dataStartBorderou);
			Date dateHigh = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
					.parse(traseuBorderou.getDataInreg());

			if (dateLow.compareTo(dateHigh) < 0)
				return true;

		} catch (Exception e) {

		}

		return false;
	}

	private boolean conditiiPlecare(TraseuBorderou traseu, PozitieClient pozitieClient) {
		if (traseu.getViteza() >= getViteza(pozitieClient.getTipClient())
				&& getCoordSosire(pozitieClient.getCodClient()) != null)
			return true;

		return false;
	}

	private double getDistanta(EnumTipClient tipClient) {
		switch (tipClient) {
		case DISTRIBUTIE:
			return Constants.RAZA_CLIENT_KM;
		case FILIALA:
			return Constants.RAZA_FILIALA_KM;
		case APROVIZIONARE:
			return Constants.RAZA_FURNIZOR_KM;
		default:
			return 0;
		}
	}

	private double getViteza(EnumTipClient tipClient) {
		switch (tipClient) {
		case DISTRIBUTIE:
			return Constants.VITEZA_MINIMA_PLECARE_CLIENT;
		case FILIALA:
			return Constants.VITEZA_MINIMA_PLECARE_FILIALA;
		default:
			return 0;
		}
	}

	private boolean borderouNotStarted(TraseuBorderou traseu, PozitieClient pozitieClient) {

		for (RezultatTraseu rezultat : rezultatTraseu) {

			for (PozitieClient pozitie : pozitiiClienti) {
				if (pozitie.isStartBord()) {
					if (rezultat.getClient().getCodClient().equals(pozitie.getCodClient())) {

						if (rezultat != null && rezultat.getPlecare() == null)
							return true;
						else if (rezultat.getPlecare().getData().equals(traseu.getDataInreg())) {
							return true;
						}

					}
				}

			}

		}

		return false;

	}

	private void adaugaEveniment(PozitieClient pozitieClient, TraseuBorderou traseuBord, EvenimentClient tipEveniment) {

		boolean found = false;

		if (rezultatTraseu.size() == 0) {
			RezultatTraseu evenim = new RezultatTraseu();
			evenim.setPoz(pozitieClient.getPoz());
			evenim.setClient(pozitieClient);
			evenim.setSosire(
					new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(), traseuBord.getLongitudine()));

			rezultatTraseu.add(evenim);
			found = true;
		}

		else {
			for (RezultatTraseu rez : rezultatTraseu) {
				if (rez.getClient().getCodClient().equals(pozitieClient.getCodClient())) {

					if (tipEveniment == EvenimentClient.SOSIRE) {
						if (rez.getSosire() != null)
							found = true;
					}

					if (tipEveniment == EvenimentClient.PLECARE) {
						if (rez.getPlecare() != null)
							found = true;
					}

				}
			}

		}

		if (!found) {

			if (tipEveniment == EvenimentClient.SOSIRE) {
				RezultatTraseu evenim = new RezultatTraseu();
				evenim.setPoz(pozitieClient.getPoz());
				evenim.setClient(pozitieClient);
				evenim.setSosire(new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(),
						traseuBord.getLongitudine()));
				rezultatTraseu.add(evenim);
			}

			if (tipEveniment == EvenimentClient.PLECARE) {
				for (RezultatTraseu traseu : rezultatTraseu) {
					if (traseu.getClient().getCodClient().equals(pozitieClient.getCodClient())) {
						traseu.setPlecare(new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(),
								traseuBord.getLongitudine()));
						break;
					}
				}

				if (pozitieClient.isStartBord()) {
					dataStartBorderou = traseuBord.getDataInreg();
				}

			}

		}

	}

	public List<RezultatTraseu> getRezultatTraseu() {
		return rezultatTraseu;
	}

	public Traseu getEvenimenteTraseu(String codBorderou) {

		Traseu traseu = new Traseu();
		OperatiiBorderou opBorderou = new OperatiiBorderou();
		List<BeanEvenimentTableta> evTableta = opBorderou.getEvenimenteTableta(codBorderou);

		List<Client> listClienti = null;

		listClienti = new OperatiiTraseu().getClientiBorderou(codBorderou);

		Collections.sort(evTableta, new EvTablComparator());

		SumarTraseu sumarTraseu = new SumarTraseu();
		setSumarTraseu(sumarTraseu, evTableta);

		List<RezultatTraseu> listEvsTraseu = new ArrayList<RezultatTraseu>(rezultatTraseu);

		RezultatTraseu rezultatTemp = null;

		for (Client client : listClienti) {
			rezultatTemp = new RezultatTraseu();
			rezultatTemp.setPoz(client.getPoz());
			rezultatTemp.setClient(client);

			for (BeanEvenimentTableta eveniment : evTableta) {
				if (client.isStartBord()) {
					if (eveniment.getClient().equals(codBorderou) && eveniment.getEveniment().equalsIgnoreCase("P")) {

						preiaEveniment(rezultatTemp, eveniment, EnumTipEveniment.PLECARE);

					}
				}
				if (client.isStopBord()) {
					if (eveniment.getClient().equals(codBorderou) && eveniment.getEveniment().equalsIgnoreCase("S")) {

						preiaEveniment(rezultatTemp, eveniment, EnumTipEveniment.SOSIRE);

					}
				}

				if (!client.isStartBord() && !client.isStopBord()) {

					if (client.getCodClient().equals(eveniment.getClient())
							&& client.getCodAdresa().equals(eveniment.getCodAdresa())
							&& eveniment.getEveniment().equalsIgnoreCase("P")) {

						preiaEveniment(rezultatTemp, eveniment, EnumTipEveniment.PLECARE);

					}

					if (client.getCodClient().equals(eveniment.getClient())
							&& client.getCodAdresa().equals(eveniment.getCodAdresa())
							&& eveniment.getEveniment().equalsIgnoreCase("S")) {

						preiaEveniment(rezultatTemp, eveniment, EnumTipEveniment.SOSIRE);

					}

				}

			}

			listEvsTraseu.add(rezultatTemp);
		}

		Set<RezultatTraseu> setStare = new HashSet<RezultatTraseu>(listEvsTraseu);

		TreeSet<RezultatTraseu> evenimenteTraseu = new TreeSet<RezultatTraseu>();
		evenimenteTraseu.addAll(setStare);

		traseu.setSumarTraseu(sumarTraseu);
		traseu.setEvenimenteTraseu(evenimenteTraseu);

		return traseu;
	}

	private void preiaEveniment(RezultatTraseu rezultat, BeanEvenimentTableta eveniment,
			EnumTipEveniment tipEveniment) {

		PozitieGps poz = new PozitieGps();
		poz.setData(Formatting.formatFromSap(eveniment.getData() + " " + eveniment.getOra()));

		String[] coords = eveniment.getGps().split(",");
		poz.setLatitudine(Double.parseDouble(coords[0]));
		poz.setLongitudine(Double.parseDouble(coords[1]));

		if (tipEveniment == EnumTipEveniment.SOSIRE) {
			rezultat.setSosire(poz);
		}

		if (tipEveniment == EnumTipEveniment.PLECARE) {
			rezultat.setPlecare(poz);
		}

	}

	private void setSumarTraseu(SumarTraseu sumarTraseu, List<BeanEvenimentTableta> evTableta) {

		if (evTableta.size() == 0)
			return;

		BeanEvenimentTableta evStart = evTableta.get(0);
		BeanEvenimentTableta evStop = evTableta.get(evTableta.size() - 1);

		sumarTraseu.setDataStart(Formatting.formatFromSap(evStart.getData() + " " + evStart.getOra()));
		sumarTraseu.setDataStop(Formatting.formatFromSap(evStop.getData() + " " + evStop.getOra()));

		sumarTraseu.setDurata(Utils.dateDiff(sumarTraseu.getDataStart(), sumarTraseu.getDataStop()));
		sumarTraseu.setKm(String.valueOf(evStop.getKmBord() - evStart.getKmBord()));

	}

	public Set<RezultatTraseu> getStareTraseu() {

		descoperaEvenimente();

		stareTraseu = new ArrayList<RezultatTraseu>(rezultatTraseu);

		RezultatTraseu tempRez = null;

		for (PozitieClient client : pozitiiClienti) {
			tempRez = new RezultatTraseu();
			tempRez.setPoz(client.getPoz());
			tempRez.setClient(client);
			tempRez.setSosire(null);
			tempRez.setPlecare(null);
			tempRez.setDistantaCamion(client.getDistantaCamion());

			for (RezultatTraseu rezultat : rezultatTraseu) {
				if (rezultat.getClient().getCodClient().equals(client.getCodClient())) {
					tempRez.setSosire(rezultat.getSosire());
					tempRez.setPlecare(rezultat.getPlecare());

				}
			}

			stareTraseu.add(tempRez);
		}

		Set<RezultatTraseu> setStare = new HashSet<RezultatTraseu>(stareTraseu);

		traseuFinal = new TreeSet<RezultatTraseu>();
		traseuFinal.addAll(setStare);

		corecteazaTraseuBorderou(traseuFinal);

		return traseuFinal;

	}

	private void corecteazaTraseuBorderou(Set<RezultatTraseu> traseuBrut) {
		Iterator<TraseuBorderou> iterator = traseuBorderou.iterator();

		String dataStartBorderou = "";
		String dataStopBorderou = "";

		for (RezultatTraseu traseu : traseuBrut) {

			for (PozitieClient client : pozitiiClienti) {
				if (client.getCodClient().equals(traseu.getClient().getCodClient())) {
					if (client.isStartBord()) {
						if (traseu.getPlecare() != null) {
							dataStartBorderou = traseu.getPlecare().getData();
						}

					}
					if (client.isStopBord()) {
						if (traseu.getSosire() != null) {
							dataStopBorderou = traseu.getSosire().getData();
						}
					}
				}
			}

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", new Locale("en"));

		Date dateStart = null;
		Date dateStop = null;

		if (dataStartBorderou.length() == 0 || dataStopBorderou.length() == 0)
			return;

		try {
			dateStart = sdf.parse(dataStartBorderou);
			dateStop = sdf.parse(dataStopBorderou);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		Date dateTraseu = null;

		TraseuBorderou traseu = null;
		while (iterator.hasNext()) {
			traseu = iterator.next();

			try {
				dateTraseu = sdf.parse(traseu.getDataInreg());

				if (dateStart != null && dateTraseu.getTime() < dateStart.getTime())
					iterator.remove();

				if (dateStop != null && dateTraseu.getTime() > dateStop.getTime())
					iterator.remove();

			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}

		}

		System.out.println("Traseu: " + traseuBorderou);

	}

	public PozitieGps getCoordSosire(String codClient) {

		for (RezultatTraseu tras : rezultatTraseu) {
			if (tras.getClient().getCodClient().equals(codClient))
				return tras.getSosire();
		}

		return null;

	}

	public List<Client> getClientiBorderou(String codBorderou) {
		List<Client> listClienti = new ArrayList<>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getClientiBorderou(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codBorderou);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				Client client = new Client();
				client.setPoz(Integer.valueOf(rs.getString("poz")));
				client.setCodClient(rs.getString("cod_client"));
				client.setCodAdresa(rs.getString("cod_adresa"));
				client.setNumeClient(rs.getString("nume"));
				client.setTipClient(EnumTipClient.DISTRIBUTIE);
				client.setLocalitate(rs.getString("city1"));
				client.setStrada(rs.getString("street"));
				client.setNrStrada(rs.getString("house_num1"));
				client.setCodJudet(rs.getString("region"));
				listClienti.add(client);

			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		List<Client> listBorder = null;
		try {
			listBorder = getStartStopBorderou(codBorderou);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		listClienti.add(0, listBorder.get(0));
		listClienti.add(listClienti.size(), listBorder.get(1));

		return listClienti;
	}

	public List<Client> getStartStopBorderou(String codBorderou) throws SQLException {

		Connection conn = new DBManager().getProdDataSource().getConnection();

		String sqlString = SqlQueries.getStartStopBorderou();

		PreparedStatement stmt = conn.prepareStatement(sqlString);

		stmt.setString(1, codBorderou);

		ResultSet rs = stmt.executeQuery();

		List<Client> listClienti = new ArrayList<>();
		Client client = null;
		String adresa;
		while (rs.next()) {

			client = new Client();

			adresa = rs.getString("adr_plecare");

			client.setPoz(0);
			client.setCodClient(rs.getString("plecare"));
			client.setTipClient(getTipClient(client));

			client.setNumeClient("Start borderou " + getBoundBord(adresa).getNumeClient());
			client.setStartBord(true);
			listClienti.add(client);

			client = new PozitieClient();
			adresa = rs.getString("adr_sosire");

			client.setPoz(100);
			client.setCodClient(rs.getString("sosire") + " ");
			client.setTipClient(getTipClient(client));

			client.setNumeClient("Stop borderou " + getBoundBord(adresa).getNumeClient());
			client.setStopBord(true);
			listClienti.add(client);

		}

		if (rs != null)
			rs.close();

		if (conn != null)
			conn.close();

		return listClienti;

	}

	public List<PozitieClient> getCoordClientiBorderou(String codBorderou) {
		List<PozitieClient> listPozitii = new ArrayList<PozitieClient>();

		String sqlString = " select a.poz, c.nume, decode(a.cod_client,'', a.cod_furnizor, a.cod_client) cod_client, "
				+ " decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) cod_adresa, "
				+ " b.city1, b.street, b.house_num1, b.region, "
				+ " nvl(latitude,0) latitude, nvl(longitude,0) longitude "
				+ " from sapprd.zdocumentesms a, sapprd.adrc b, clienti c, sapprd.zcoordcomenzi d where a.nr_bord =:codBorderou and c.cod = a.cod_client "
				+ " and b.client = '900' and b.addrnumber = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) "
				+ " and d.idcomanda(+) = a.idcomanda order by a.poz";

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codBorderou);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				PozitieClient pozitie = new PozitieClient();
				pozitie.setPoz(Integer.valueOf(rs.getString("poz")));
				pozitie.setCodClient(rs.getString("cod_client"));

				setCoordonate(pozitie, rs);
				pozitie.setCodAdresa(rs.getString("cod_adresa"));
				pozitie.setNumeClient(rs.getString("nume"));
				pozitie.setTipClient(EnumTipClient.DISTRIBUTIE);
				listPozitii.add(pozitie);
			}

			List<PozitieClient> listBorder = null;
			try {
				listBorder = getCoordStartStopBorderou(codBorderou);
			} catch (SQLException e) {
				System.out.println(e.getStackTrace());
			}

			listPozitii.add(0, listBorder.get(0));
			listPozitii.add(listPozitii.size(), listBorder.get(1));

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		return listPozitii;

	}

	public List<TraseuBorderou> getTraseuBorderou(String codBorderou, String startBorderou, String stopBorderou) {

		DateBorderou dateBorderou = null;

		try {
			dateBorderou = getDateBorderou(codBorderou);
		} catch (SQLException e) {
			System.out.println(Utils.getStackTrace(e));
		}

		String sqlString = " select to_char(c.record_time,'dd-Mon-yy hh24:mi:ss', 'NLS_DATE_LANGUAGE = AMERICAN') datarec , c.latitude, c.longitude, nvl(c.mileage,0) kilo, "
				+ " nvl(c.speed,0) viteza from gps_masini b, gps_date c  where "
				+ " b.nr_masina = replace(:nrMasina,'-','') and c.device_id = b.id "
				+ " and  c.record_time between to_date(:dataStart,'dd-mm-yy hh24:mi:ss', 'NLS_DATE_LANGUAGE = AMERICAN') "
				+ " and to_date(:dataStop,'dd-mm-yy hh24:mi:ss', 'NLS_DATE_LANGUAGE = AMERICAN') order by c.record_time ";

		List<TraseuBorderou> listTraseu = new ArrayList<TraseuBorderou>();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, dateBorderou.getNrMasina());
			stmt.setString(2, startBorderou);
			stmt.setString(3, stopBorderou);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				TraseuBorderou pozitie = new TraseuBorderou();
				pozitie.setDataInreg(rs.getString("datarec"));
				pozitie.setLatitudine(rs.getDouble("latitude"));
				pozitie.setLongitudine(rs.getDouble("longitude"));
				pozitie.setKm(rs.getInt("kilo"));
				pozitie.setViteza(rs.getInt("viteza"));
				listTraseu.add(pozitie);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listTraseu;

	}

	public DateBorderou getDateBorderou(String codBorderou) throws SQLException {
		Connection conn = new DBManager().getProdDataSource().getConnection();

		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select trunc(to_date(daten,'yyyymmdd')) dataEmitere, masina from ");
		sqlString.append("(select v.tknum as numarb, m.exidv as masina, p.pernr as cod_sofer, ");
		sqlString.append(" (select fili from websap.soferi where cod=p.pernr) fili, v.shtyp, v.daten,v.uaten ");
		sqlString.append(
				"from sapprd.vttk v join sapprd.vekp m on v.mandt = m.mandt and v.tknum = m.vpobjkey and m.vpobj = '04' ");
		sqlString.append(
				"join sapprd.vtpa p on v.mandt = p.mandt and v.tknum = p.vbeln and v.daten != '00000000' and p.parvw = 'ZF' ");
		sqlString.append("where v.mandt = '900') ");
		sqlString.append("where numarb =? ");
		sqlString.append("union ");
		sqlString.append(
				"select a.data_e dataEmitere, a.masina from borderouri a where  numarb =? order by dataEmitere");

		PreparedStatement stmt = conn.prepareStatement(sqlString.toString());

		stmt.setString(1, codBorderou);
		stmt.setString(2, codBorderou);

		ResultSet rs = stmt.executeQuery();
		DateBorderou dateBorderou = new DateBorderou();
		while (rs.next()) {
			dateBorderou.setDataEmitere(rs.getString("dataEmitere"));
			dateBorderou.setNrMasina(rs.getString("masina"));
		}

		if (rs != null)
			rs.close();

		if (conn != null)
			conn.close();

		return dateBorderou;
	}

	private void setCoordonate(PozitieClient pozitieClient, ResultSet rs) {
		try {

			if (Double.valueOf(rs.getString("latitude")) != 0) {
				pozitieClient.setLatitudine(Double.valueOf(rs.getString("latitude")));
				pozitieClient.setLongitudine(Double.valueOf(rs.getString("longitude")));
			} else {
				CoordonateGps coordonate = getCoordonate(rs.getString("region"), rs.getString("city1"),
						rs.getString("house_num1"), rs.getString("street"));
				pozitieClient.setLatitudine(coordonate.getLatitude());
				pozitieClient.setLongitudine(coordonate.getLongitude());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private CoordonateGps getCoordonate(String codJudet, String localitate, String strada, String nrStrada) {
		CoordonateGps coordonate = null;

		Address address = new Address();

		address.setStreet(strada);
		address.setNumber(nrStrada);
		address.setSector(UtilsAdrese.getNumeJudet(codJudet));
		address.setCity(localitate);

		try {
			coordonate = MapUtils.geocodeAddress(address);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		return coordonate;
	}

	public List<PozitieClient> getCoordStartStopBorderou(String codBorderou) throws SQLException {

		Connection conn = new DBManager().getProdDataSource().getConnection();

		String sqlString = SqlQueries.getCoordStartStopBorderou();

		PreparedStatement stmt = conn.prepareStatement(sqlString);

		stmt.setString(1, codBorderou);

		ResultSet rs = stmt.executeQuery();

		List<PozitieClient> listPozitii = new ArrayList<>();
		PozitieClient pozitie = null;
		String adresa;
		while (rs.next()) {

			pozitie = new PozitieClient();

			adresa = rs.getString("adr_plecare");

			pozitie.setPoz(0);
			pozitie.setCodClient(rs.getString("plecare"));
			pozitie.setTipClient(getTipClient(pozitie));

			if (Double.valueOf(rs.getString("plec_lat").replace(",", ".")) > 0) {
				pozitie.setLatitudine(Double.valueOf(rs.getString("plec_lat").replace(",", ".")));
				pozitie.setLongitudine(Double.valueOf(rs.getString("plec_long").replace(",", ".")));
			} else {
				setCoordBound(pozitie, adresa);
			}
			pozitie.setNumeClient("Start borderou " + getBoundBord(adresa).getNumeClient());
			pozitie.setStartBord(true);
			listPozitii.add(pozitie);

			pozitie = new PozitieClient();
			adresa = rs.getString("adr_sosire");

			pozitie.setPoz(100);
			pozitie.setCodClient(rs.getString("sosire") + " ");
			pozitie.setTipClient(getTipClient(pozitie));

			if (Double.valueOf(rs.getString("sosire_lat").replace(",", ".")) > 0) {
				pozitie.setLatitudine(Double.valueOf(rs.getString("sosire_lat").replace(",", ".")));
				pozitie.setLongitudine(Double.valueOf(rs.getString("sosire_long").replace(",", ".")));
			} else {
				setCoordBound(pozitie, adresa);
			}
			pozitie.setNumeClient("Stop borderou " + getBoundBord(adresa).getNumeClient());
			pozitie.setStopBord(true);
			listPozitii.add(pozitie);

		}

		if (rs != null)
			rs.close();

		if (conn != null)
			conn.close();

		return listPozitii;

	}

	public TraseuInterval getTraseuInterval(String nrMasina, String dataStart, String dataStop) {

		String strTraseu = "";

		TraseuInterval tInt = new TraseuInterval();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getTraseuInterval());) {

			stmt.setString(1, nrMasina.replace("-", ""));
			stmt.setString(2, dataStart);
			stmt.setString(3, dataStop);

			stmt.executeQuery();
			ResultSet rs = stmt.getResultSet();

			int kmStart = 0, kmStop = 0, speed = 0, avgSpeed = 0, distanta = 0, maxSpeed = 0;

			List<Oprire> listOpriri = new ArrayList<Oprire>();
			Oprire oprire = null;
			Date dataStartOprire = null, dataStopOprire = null, ultimaInreg = null;

			int i = 0;
			int instantSpeed = 0;
			int countVMedie = 0;

			while (rs.next()) {

				if (i == 0)
					kmStart = rs.getInt("mileage");

				kmStop = rs.getInt("mileage");

				instantSpeed = rs.getInt("speed");

				if (instantSpeed > 0) {
					speed += instantSpeed;
					countVMedie++;
				}

				if (instantSpeed > maxSpeed)
					maxSpeed = rs.getInt("speed");

				if (0 == instantSpeed) {
					if (oprire == null) {
						oprire = new Oprire();
						oprire.setPozitieGps(new PozitieGps(null, rs.getDouble("latitude"), rs.getDouble("longitude")));
						dataStartOprire = Utils.getDate(rs.getString("record_time"));
						oprire.setData(rs.getString("record_time"));

					}

				} else {
					if (dataStartOprire != null) {
						dataStopOprire = Utils.getDate(rs.getString("record_time"));
						oprire.setDurata(Utils.dateDiff(dataStartOprire, dataStopOprire));
						listOpriri.add(oprire);
						oprire = null;
						dataStartOprire = null;
					}
				}

				ultimaInreg = Utils.getDate(rs.getString("record_time"));

				strTraseu += "#" + String.valueOf(rs.getDouble("latitude")) + ","
						+ String.valueOf(rs.getDouble("longitude"));

				i++;

			}

			if (dataStartOprire != null) {
				oprire.setDurata(Utils.dateDiff(dataStartOprire, ultimaInreg));
				listOpriri.add(oprire);
			}

			distanta = kmStop - kmStart;

			if (countVMedie > 0)
				avgSpeed = speed / countVMedie;

			SumarTraseu sumarTraseu = new SumarTraseu();
			sumarTraseu.setKm(String.valueOf(distanta));
			sumarTraseu.setVitezaMedie(String.valueOf(avgSpeed));
			sumarTraseu.setVitezaMaxima(String.valueOf(maxSpeed));

			String opriri = HelperEvenimente.formatOpririTraseu(listOpriri);

			tInt.setSumarTraseu(sumarTraseu);
			tInt.setTraseu(strTraseu);
			tInt.setOpriri(opriri);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return tInt;
	}

	private void setCoordBound(PozitieClient pozitieClient, String dateCoord) {

		if (dateCoord != null && dateCoord.contains("#")) {

			CoordonateGps coord = new CoordonateGps();

			try {
				coord = MapUtils.geocodeAddress(getAddress(dateCoord));
			} catch (Exception e) {
				System.out.println(Utils.getStackTrace(e));
			}

			pozitieClient.setLatitudine(coord.getLatitude());
			pozitieClient.setLongitudine(coord.getLongitude());

		}

	}

	private BeanBoundBord getBoundBord(String adresa) {
		BeanBoundBord boundBord = new BeanBoundBord();

		if (adresa != null && adresa.contains("#")) {
			String[] tokAdresa = adresa.split("#");
			boundBord.setNumeClient(tokAdresa[0]);
			boundBord.setAddress(getAddress(adresa));

		}

		return boundBord;
	}

	private Address getAddress(String valAddress) {
		Address address = new Address();

		if (valAddress != null && valAddress.contains("#")) {
			String[] tokAdresa = valAddress.split("#");

			address.setSector(UtilsAdrese.getNumeJudet(tokAdresa[1]));
			address.setCity(tokAdresa[2]);

			if (tokAdresa.length >= 3)
				address.setStreet(tokAdresa[3]);

			if (tokAdresa.length >= 4)
				address.setNumber(tokAdresa[4]);

		}

		return address;
	}

	private EnumTipClient getTipClient(Client pozitieClient) {
		if (pozitieClient.getCodClient().length() == 4)
			return EnumTipClient.FILIALA;
		else
			return EnumTipClient.DISTRIBUTIE;
	}

}
