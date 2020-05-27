package distributie.enums;

public enum EnumStatusMasina {
	BORDEROU_ACTIV(0), BORDEROU_TERMINAT(1), FARA_BORDEROU(2);

	int codStatus;

	EnumStatusMasina(int codStatus) {
		this.codStatus = codStatus;
	}

	public int getCodStatus() {
		return codStatus;
	}

}
