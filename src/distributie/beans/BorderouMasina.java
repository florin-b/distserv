package distributie.beans;

import distributie.enums.EnumStatusMasina;

public class BorderouMasina {

	private String nrMasina;
	private String nrBorderou;
	private EnumStatusMasina status;

	public String getNrMasina() {
		return nrMasina;
	}

	public void setNrMasina(String nrMasina) {
		this.nrMasina = nrMasina;
	}

	public String getNrBorderou() {
		return nrBorderou;
	}

	public void setNrBorderou(String nrBorderou) {
		this.nrBorderou = nrBorderou;
	}

	public EnumStatusMasina getStatus() {
		return status;
	}

	public void setStatus(EnumStatusMasina status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nrMasina == null) ? 0 : nrMasina.hashCode());
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
		BorderouMasina other = (BorderouMasina) obj;
		if (nrMasina == null) {
			if (other.nrMasina != null)
				return false;
		} else if (!nrMasina.equals(other.nrMasina))
			return false;
		return true;
	}


}
