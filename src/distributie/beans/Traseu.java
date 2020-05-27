package distributie.beans;

import java.util.TreeSet;

public class Traseu {

	private TreeSet<RezultatTraseu> evenimenteTraseu;
	private SumarTraseu sumarTraseu;

	public TreeSet<RezultatTraseu> getEvenimenteTraseu() {
		return evenimenteTraseu;
	}

	public void setEvenimenteTraseu(TreeSet<RezultatTraseu> evenimenteTraseu) {
		this.evenimenteTraseu = evenimenteTraseu;
	}

	public SumarTraseu getSumarTraseu() {
		return sumarTraseu;
	}

	public void setSumarTraseu(SumarTraseu sumarTraseu) {
		this.sumarTraseu = sumarTraseu;
	}

}
