package distributie.model;

import java.rmi.RemoteException;
import java.util.Calendar;

import SMSService.*;

public class SendSmsClient {

	public boolean sendSms(String nrTelefon, String msgText) {

		boolean messageSent = true;

		SMSServicePortProxy proxy = new SMSServicePortProxy();

		try {
			String sessionId = proxy.openSession("arabesque2", "arbsq123");

			proxy.sendSession(sessionId, nrTelefon, msgText, Calendar.getInstance(), "", 0);

			proxy.closeSession(sessionId);

		} catch (RemoteException e) {
			messageSent = false;
		}

		return messageSent;
	}

}
