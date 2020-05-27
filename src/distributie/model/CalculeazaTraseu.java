package distributie.model;


import java.text.ParseException;
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

import distributie.beans.BeanEvenimentTableta;
import distributie.beans.Client;
import distributie.beans.PozitieClient;
import distributie.beans.PozitieGps;
import distributie.beans.RezultatTraseu;
import distributie.beans.SumarTraseu;
import distributie.beans.Traseu;
import distributie.beans.TraseuBorderou;
import distributie.comparators.EvTablComparator;
import distributie.enums.EnumTipClient;
import distributie.enums.EnumTipEveniment;
import distributie.utils.Constants;
import distributie.utils.Formatting;
import distributie.utils.MailOperations;
import distributie.utils.MapUtils;
import distributie.utils.Utils;

public class CalculeazaTraseu {

	private List<PozitieClient> pozitiiClienti;
	private List<TraseuBorderou> traseuBorderou;
	private List<RezultatTraseu> rezultatTraseu;
	private List<RezultatTraseu> stareTraseu;
	private Set<RezultatTraseu> traseuFinal;
	private String dataStartBorderou;

	private enum EvenimentClient {
		SOSIRE, PLECARE
	};

	public CalculeazaTraseu() {
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

		} catch (ParseException e) {
			MailOperations.sendMail(e.toString());
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
			MailOperations.sendMail(e.toString());
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
				MailOperations.sendMail(e.toString());
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

}
