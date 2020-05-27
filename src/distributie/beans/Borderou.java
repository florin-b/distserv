package distributie.beans;

public class Borderou {

	private String cod;
	private String dataEmitere;
	private boolean isActiv;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDataEmitere() {
		return dataEmitere;
	}

	public void setDataEmitere(String dataEmitere) {
		this.dataEmitere = dataEmitere;
	}

	public boolean isActiv() {
		return isActiv;
	}

	public void setActiv(boolean isActiv) {
		this.isActiv = isActiv;
	}

	@Override
	public String toString() {
		return "Borderou [cod=" + cod + ", dataEmitere=" + dataEmitere + ", isActiv=" + isActiv + "]";
	}
	
	

}
