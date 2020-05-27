package distributie.beans;

public class Sofer {

	private String codSofer;
	private String numeSofer;

	public String getCodSofer() {
		return codSofer;
	}

	public void setCodSofer(String codSofer) {
		this.codSofer = codSofer;
	}

	public String getNumeSofer() {
		return numeSofer;
	}

	public void setNumeSofer(String numeSofer) {
		this.numeSofer = numeSofer;
	}

	@Override
	public String toString() {
		return "Sofer [codSofer=" + codSofer + ", numeSofer=" + numeSofer + "]";
	}

	
}
