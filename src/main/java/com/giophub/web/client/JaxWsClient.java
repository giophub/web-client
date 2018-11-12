package com.giophub.web.client;

import com.giophub.main.Main;
import com.giophub.pojo.SoapPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class JaxWsClient {
    private static final Logger logger = LoggerFactory.getLogger(JaxWsClient.class);

    private String uri;
    private String soapAction;
    private String soapXmlRequest;

    public JaxWsClient(String uri, String soapAction, String soapXmlRequest) {
        this.uri = uri;
        this.soapAction = soapAction;
        this.soapXmlRequest = soapXmlRequest;
    }

    public void doCall() {
        SOAPConnection connection = null;
        try {
            connection = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage request = this.createMessage(soapXmlRequest);
            SOAPMessage response = connection.call(request, uri);

            this.disassembleMessage(response);
        } catch (SOAPException e) {
            logger.error("An error occurred on calling web service. Caused by: \n", e.getCause());
        }
        finally {
            // close connection
            try {
                if (connection != null) {
                    connection.close();
                }
                logger.debug("Connection closed.");
            } catch (SOAPException e1) {
                logger.error("An error occurred on closing connection. Caused by: ", e1.getCause());
            }
        }
    }

    private SOAPMessage createMessage(String request) throws SOAPException {
        SOAPMessage message = null;
        if (null == request || request.isEmpty()) {
            message = MessageFactory.newInstance().createMessage();
        } else {
            InputStream is = new ByteArrayInputStream(request.getBytes());
            try {
                message = MessageFactory.newInstance().createMessage(null, is);
            } catch (IOException e) {
                logger.error("Create message error. The conversion from String to Input Stream failed: {} \nCaused by: {}", e.getMessage(), e.getCause());
            }
        }
        return message;
    }

    private void disassembleMessage(SOAPMessage message) {
        SoapPojo soapPojo = new SoapPojo(message);
    }


}
