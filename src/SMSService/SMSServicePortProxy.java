package SMSService;

public class SMSServicePortProxy implements SMSService.SMSServicePort {
  private String _endpoint = null;
  private SMSService.SMSServicePort sMSServicePort = null;
  
  public SMSServicePortProxy() {
    _initSMSServicePortProxy();
  }
  
  public SMSServicePortProxy(String endpoint) {
    _endpoint = endpoint;
    _initSMSServicePortProxy();
  }
  
  private void _initSMSServicePortProxy() {
    try {
      sMSServicePort = (new SMSService.SMSServiceServiceLocator()).getSMSServicePort();
      if (sMSServicePort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sMSServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sMSServicePort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sMSServicePort != null)
      ((javax.xml.rpc.Stub)sMSServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SMSService.SMSServicePort getSMSServicePort() {
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort;
  }
  
  public java.lang.String send(java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.send(phone, message, date, sender, validity);
  }
  
  public java.lang.String sendSms(java.lang.String username, java.lang.String password, java.lang.String source, java.lang.String destination, java.lang.String body, boolean isUnicode, java.util.Calendar scheduleDate, java.lang.String callbackUrl) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSms(username, password, source, destination, body, isUnicode, scheduleDate, callbackUrl);
  }
  
  public java.lang.String sendSmsAuthKey(java.lang.String username, java.lang.String authKey, java.lang.String sender, java.lang.String recipient, java.lang.String message, java.util.Calendar scheduleDate, int validity, java.lang.String callbackUrl) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSmsAuthKey(username, authKey, sender, recipient, message, scheduleDate, validity, callbackUrl);
  }
  
  public java.lang.String sendWapPush(java.lang.String phone, java.lang.String url, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendWapPush(phone, url, message, date, sender, validity);
  }
  
  public java.lang.String sendVCalendar(java.lang.String recipient, java.lang.String subject, java.lang.String location, java.lang.String description, java.util.Calendar startDatetime, java.util.Calendar endDatetime, java.util.Calendar alarmDatetime, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendVCalendar(recipient, subject, location, description, startDatetime, endDatetime, alarmDatetime, sendDatetime, sender, validity);
  }
  
  public java.lang.String sendVCard(java.lang.String recipient, java.lang.String firstname, java.lang.String lastname, java.lang.String mobilephone, java.lang.String email, java.lang.String organization, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendVCard(recipient, firstname, lastname, mobilephone, email, organization, sendDatetime, sender, validity);
  }
  
  public java.lang.String sendSmsFlash(java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSmsFlash(phone, message, date, sender, validity);
  }
  
  public java.lang.String checkStatus(java.lang.String messageId) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.checkStatus(messageId);
  }
  
  public java.lang.String openSession(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.openSession(username, password);
  }
  
  public boolean closeSession(java.lang.String sessid) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.closeSession(sessid);
  }
  
  public java.lang.String sendSession(java.lang.String sessid, java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSession(sessid, phone, message, date, sender, validity);
  }
  
  public java.lang.String sendSessionWapPush(java.lang.String sessid, java.lang.String phone, java.lang.String url, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSessionWapPush(sessid, phone, url, message, date, sender, validity);
  }
  
  public java.lang.String sendSessionVCalendar(java.lang.String sessid, java.lang.String recipient, java.lang.String subject, java.lang.String location, java.lang.String description, java.util.Calendar startDatetime, java.util.Calendar endDatetime, java.util.Calendar alarmDatetime, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSessionVCalendar(sessid, recipient, subject, location, description, startDatetime, endDatetime, alarmDatetime, sendDatetime, sender, validity);
  }
  
  public java.lang.String sendSessionVCard(java.lang.String sessid, java.lang.String recipient, java.lang.String firstname, java.lang.String lastname, java.lang.String mobilephone, java.lang.String email, java.lang.String organization, java.util.Calendar sendDatetime, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSessionVCard(sessid, recipient, firstname, lastname, mobilephone, email, organization, sendDatetime, sender, validity);
  }
  
  public java.lang.String sendSessionSmsFlash(java.lang.String sessid, java.lang.String phone, java.lang.String message, java.util.Calendar date, java.lang.String sender, int validity) throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.sendSessionSmsFlash(sessid, phone, message, date, sender, validity);
  }
  
  public java.lang.String showIp() throws java.rmi.RemoteException{
    if (sMSServicePort == null)
      _initSMSServicePortProxy();
    return sMSServicePort.showIp();
  }
  
  
}