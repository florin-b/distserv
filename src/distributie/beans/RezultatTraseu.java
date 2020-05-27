package distributie.beans;

public class RezultatTraseu implements Comparable<RezultatTraseu> {

	private int poz;
	private Client client;
	private PozitieGps sosire;
	private PozitieGps plecare;
	private int distantaCamion;

	public PozitieGps getSosire() {

		return sosire;
	}

	public void setSosire(PozitieGps sosire) {
		this.sosire = sosire;
	}

	public PozitieGps getPlecare() {

		return plecare;
	}

	public void setPlecare(PozitieGps plecare) {
		this.plecare = plecare;
	}

	public int getDistantaCamion() {
		return distantaCamion;
	}

	public void setDistantaCamion(int distantaCamion) {
		this.distantaCamion = distantaCamion;
	}

	public int getPoz() {
		return poz;
	}

	public void setPoz(int poz) {
		this.poz = poz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client.getCodClient() == null) ? 0 : client.getCodClient().hashCode());
		result = prime * result + ((client.getCodAdresa() == null) ? 0 : client.getCodAdresa().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RezultatTraseu other = (RezultatTraseu) obj;
		if (client.getCodClient() == null) {
			if (other.client.getCodClient() != null)
				return false;
		} else if (!client.getCodClient().equals(other.client.getCodClient()))
			return false;
		if (client.getCodAdresa() == null) {
			if (other.client.getCodAdresa() != null)
				return false;
		} else if (!client.getCodAdresa().equals(other.client.getCodAdresa()))
			return false;
		return true;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "RezultatTraseu [poz=" + poz + ", client=" + client + ", sosire=" + sosire + ", plecare=" + plecare + ", distantaCamion=" + distantaCamion + "]";
	}

	public int compareTo(RezultatTraseu that) {
		return this.poz < that.poz ? -1 : this.poz > that.poz ? 1 : 0;
	}

}
