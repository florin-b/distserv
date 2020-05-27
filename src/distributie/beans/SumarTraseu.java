package distributie.beans;

public class SumarTraseu {

	private String dataStart;
	private String dataStop;
	private String durata;
	private String km;
	private String vitezaMaxima;
	private String vitezaMedie;

	public String getDurata() {
		return durata;
	}

	public void setDurata(String durata) {
		this.durata = durata;
	}

	public String getKm() {
		return km;
	}

	public void setKm(String km) {
		this.km = km;
	}

	public String getDataStart() {
		return dataStart;
	}

	public void setDataStart(String dataStart) {
		this.dataStart = dataStart;
	}

	public String getDataStop() {
		return dataStop;
	}

	public void setDataStop(String dataStop) {
		this.dataStop = dataStop;
	}

	public String getVitezaMaxima() {
		return vitezaMaxima;
	}

	public void setVitezaMaxima(String vitezaMaxima) {
		this.vitezaMaxima = vitezaMaxima;
	}

	public String getVitezaMedie() {
		return vitezaMedie;
	}

	public void setVitezaMedie(String vitezaMedie) {
		this.vitezaMedie = vitezaMedie;
	}

	@Override
	public String toString() {
		return "SumarTraseu [dataStart=" + dataStart + ", dataStop=" + dataStop + ", durata=" + durata + ", km=" + km
				+ ", vitezaMaxima=" + vitezaMaxima + ", vitezaMedie=" + vitezaMedie + "]";
	}



}
