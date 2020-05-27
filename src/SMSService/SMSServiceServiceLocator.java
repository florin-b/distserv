/**
 * SMSServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMSService;

public class SMSServiceServiceLocator extends org.apache.axis.client.Service implements SMSService.SMSServiceService {

    public SMSServiceServiceLocator() {
    }


    public SMSServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SMSServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SMSServicePort
    private java.lang.String SMSServicePort_address = "http://wsi.bizpack.ro/service.php";

    public java.lang.String getSMSServicePortAddress() {
        return SMSServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SMSServicePortWSDDServiceName = "SMSServicePort";

    public java.lang.String getSMSServicePortWSDDServiceName() {
        return SMSServicePortWSDDServiceName;
    }

    public void setSMSServicePortWSDDServiceName(java.lang.String name) {
        SMSServicePortWSDDServiceName = name;
    }

    public SMSService.SMSServicePort getSMSServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SMSServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSMSServicePort(endpoint);
    }

    public SMSService.SMSServicePort getSMSServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SMSService.SMSServiceBindingStub _stub = new SMSService.SMSServiceBindingStub(portAddress, this);
            _stub.setPortName(getSMSServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSMSServicePortEndpointAddress(java.lang.String address) {
        SMSServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SMSService.SMSServicePort.class.isAssignableFrom(serviceEndpointInterface)) {
                SMSService.SMSServiceBindingStub _stub = new SMSService.SMSServiceBindingStub(new java.net.URL(SMSServicePort_address), this);
                _stub.setPortName(getSMSServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SMSServicePort".equals(inputPortName)) {
            return getSMSServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:SMSService", "SMSServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:SMSService", "SMSServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SMSServicePort".equals(portName)) {
            setSMSServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
