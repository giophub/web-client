package com.giophub.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.*;

public class SoapPojo {
    private static final Logger logger = LoggerFactory.getLogger(SoapPojo.class);

    private SOAPEnvelope envelope;
    private SOAPHeader header;
    private SOAPBody body;
    private SOAPFault fault;


    public SoapPojo (SOAPMessage message) {
        try {
            envelope = message.getSOAPPart().getEnvelope();
            header = envelope.getHeader();
            body = envelope.getBody();
            fault = body.getFault();
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        logger.debug("SOAP Header: {}\n\"SOAP Body: {}", header.toString(), body.toString());
    }


    public void getSoapFault() {
        if (null != fault) {
            String faultCode = fault.getFaultCode();
            String faultString = fault.getFaultString();
            logger.error("SOAP Fault code: {}\nSOAP Fault String: {}", faultCode, faultString);
        }
    }

    // TODO get Attachment
}
