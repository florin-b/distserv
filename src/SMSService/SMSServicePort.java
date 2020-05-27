/**
 * SMSServicePort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMSService;

public interface SMSServicePort extends java.rmi.Remote {

    /**
     * Send a simple SMS using IP based authentication
     */
    public java.lang.String send(java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send a simple SMS using username and password authentication
     */
    public java.lang.String sendSms(java.lang.String username, java.lang.String password, java.lang.String source, java.lang.String destination, java.lang.String body, boolean isUnicode, java.util.Calendar scheduleDate, java.lang.String callbackUrl) throws java.rmi.RemoteException;

    /**
     * Send a SMS using API Key authentication
     */
    public java.lang.String sendSmsAuthKey(java.lang.String username, java.lang.String authKey, java.lang.String sender, java.lang.String recipient, java.lang.String message, java.util.Calendar scheduleDate, int validity, java.lang.String callbackUrl) throws java.rmi.RemoteException;

    /**
     * Send an Wap Push using IP based authentication
     */
    public java.lang.String sendWapPush(java.lang.String phone, java.lang.String url, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send an appoinment using IP based authentication
     */
    public java.lang.String sendVCalendar(java.lang.String recipient, java.lang.String subject, java.lang.String location, java.lang.String description, java.util.Calendar startDatetime, java.util.Calendar endDatetime, java.util.Calendar alarmDatetime, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send a VCARD using IP based authentication
     */
    public java.lang.String sendVCard(java.lang.String recipient, java.lang.String firstname, java.lang.String lastname, java.lang.String mobilephone, java.lang.String email, java.lang.String organization, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send a SMS FLASH using IP based authentication
     */
    public java.lang.String sendSmsFlash(java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Check delivery report status for a SMS
     */
    public java.lang.String checkStatus(java.lang.String messageId) throws java.rmi.RemoteException;

    /**
     * Opens a SMS sending session using username/password authentication
     */
    public java.lang.String openSession(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Closes a SMS sending session previously openned with openSession
     * method
     */
    public boolean closeSession(java.lang.String sessid) throws java.rmi.RemoteException;

    /**
     * Send a simple SMS using session ID authentication (previously
     * openned with openSession method)
     */
    public java.lang.String sendSession(java.lang.String sessid, java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send an Wap Push using session ID authentication (previously
     * openned with openSession method)
     */
    public java.lang.String sendSessionWapPush(java.lang.String sessid, java.lang.String phone, java.lang.String url, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send an appoinment using session ID authentication (previously
     * openned with openSession method)
     */
    public java.lang.String sendSessionVCalendar(java.lang.String sessid, java.lang.String recipient, java.lang.String subject, java.lang.String location, java.lang.String description, java.util.Calendar startDatetime, java.util.Calendar endDatetime, java.util.Calendar alarmDatetime, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send a VCARD using session ID authentication (previously openned
     * with openSession method)
     */
    public java.lang.String sendSessionVCard(java.lang.String sessid, java.lang.String recipient, java.lang.String firstname, java.lang.String lastname, java.lang.String mobilephone, java.lang.String email, java.lang.String organization, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Send a SMS Flash using session ID authentication (previously
     * openned with openSession method)
     */
    public java.lang.String sendSessionSmsFlash(java.lang.String sessid, java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException;

    /**
     * Returns the IP of the calling client
     */
    public java.lang.String showIp() throws java.rmi.RemoteException;
}
