package distributie.beans;

public class TraseuInterval {

	private SumarTraseu sumarTraseu;
	private String traseu;
	private String opriri;

	public SumarTraseu getSumarTraseu() {
		return sumarTraseu;
	}

	public void setSumarTraseu(SumarTraseu sumarTraseu) {
		this.sumarTraseu = sumarTraseu;
	}

	public String getTraseu() {
		return traseu;
	}

	public void setTraseu(String traseu) {
		this.traseu = traseu;
	}

	public String getOpriri() {
		return opriri;
	}

	public void setOpriri(String opriri) {
		this.opriri = opriri;
	}

	@Override
	public String toString() {
		return "TraseuInterval [sumarTraseu=" + sumarTraseu + ", traseu=" + traseu + ", opriri=" + opriri + "]";
	}

}
