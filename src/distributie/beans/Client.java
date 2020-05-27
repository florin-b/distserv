package distributie.beans;

import distributie.enums.EnumTipClient;

public class Client {

	protected int poz;
	protected String codClient;
	protected String numeClient;
	protected EnumTipClient tipClient;
	protected String codAdresa;
	protected boolean isStartBord;
	protected boolean isStopBord;
	protected String localitate;
	protected String strada;
	protected String nrStrada;
	protected String codJudet;

	public int getPoz() {
		return poz;
	}

	public void setPoz(int poz) {
		this.poz = poz;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public EnumTipClient getTipClient() {
		return tipClient;
	}

	public void setTipClient(EnumTipClient tipClient) {
		this.tipClient = tipClient;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public boolean isStartBord() {
		return isStartBord;
	}

	public void setStartBord(boolean isStartBord) {
		this.isStartBord = isStartBord;
	}

	public boolean isStopBord() {
		return isStopBord;
	}

	public void setStopBord(boolean isStopBord) {
		this.isStopBord = isStopBord;
	}

	public String getLocalitate() {
		if (localitate == null)
			return "";
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getStrada() {
		if (strada == null)
			return "";
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public String getNrStrada() {
		if (nrStrada == null)
			return "";
		return nrStrada;
	}

	public void setNrStrada(String nrStrada) {
		this.nrStrada = nrStrada;
	}

	public String getCodJudet() {
		if (codJudet == null)
			return "";
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}



}
