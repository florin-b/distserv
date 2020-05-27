package distributie.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import distributie.enums.EnumDateDiff;
import distributie.enums.EnumStatusMasina;
import distributie.utils.Utils;

public class PozitieMasina implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String deviceId;
	private String codSofer;
	private String nrAuto;
	private String latitudine;
	private String longitudine;
	private String data;
	private String viteza;
	private boolean isActual;
	private EnumStatusMasina status;
	private String borderou;
	private String tipMasina;

	public PozitieMasina() {

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCodSofer() {
		return codSofer;
	}

	public void setCodSofer(String codSofer) {
		this.codSofer = codSofer;
	}

	public String getNrAuto() {
		return nrAuto;
	}

	public void setNrAuto(String nrAuto) {
		this.nrAuto = nrAuto;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getViteza() {
		return viteza;
	}

	public void setViteza(String viteza) {
		this.viteza = viteza;
	}

	public void setActual() {
		this.isActual = isActual();
	}

	public EnumStatusMasina getStatus() {
		return status;
	}

	public void setStatus(EnumStatusMasina status) {
		this.status = status;
	}

	public boolean isActual() {
		Calendar currentDate = Calendar.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss", new Locale("en"));
		if (Utils.dateDiff(dateFormat.format(currentDate.getTime()).toString(), data, EnumDateDiff.HOURS) > 1)
			return false;

		return true;
	}

	public String getBorderou() {
		return borderou;
	}

	public void setBorderou(String borderou) {
		this.borderou = borderou;
	}

	public String getTipMasina() {
		return tipMasina;
	}

	public void setTipMasina(String tipMasina) {
		this.tipMasina = tipMasina;
	}

	@Override
	public String toString() {
		return "PozitieMasina [deviceId=" + deviceId + ", codSofer=" + codSofer + ", nrAuto=" + nrAuto + ", latitudine=" + latitudine + ", longitudine="
				+ longitudine + ", data=" + data + ", viteza=" + viteza + ", isActual=" + isActual + ", status=" + status + ", borderou=" + borderou
				+ ", tipMasina=" + tipMasina + "]";
	}

}
