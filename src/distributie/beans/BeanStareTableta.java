package distributie.beans;

public class BeanStareTableta {

	private String id;
	private String dataInreg;
	private String stare;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataInreg() {
		return dataInreg;
	}

	public void setDataInreg(String dataInreg) {
		this.dataInreg = dataInreg;
	}

	public String getStare() {
		return stare;
	}

	public void setStare(String stare) {
		this.stare = stare;
	}

	@Override
	public String toString() {
		return "BeanStareTableta [id=" + id + ", dataInreg=" + dataInreg + ", stare=" + stare + "]";
	}

}
