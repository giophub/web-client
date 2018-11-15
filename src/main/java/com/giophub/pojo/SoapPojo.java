package com.giophub.pojo;

import groovy.xml.SAXBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Iterator;

public class SoapPojo {
    private static final Logger logger = LoggerFactory.getLogger(SoapPojo.class);

    private String sMessage;

    private SOAPMessage message;
    private SOAPEnvelope envelope;
    private SOAPHeader header;
    private SOAPBody body;
    private SOAPFault fault;


    public SoapPojo() {}

    public void createSoapObject(String message) {
        // the message is a string.
        this.sMessage = message;
        InputStream is = new ByteArrayInputStream(message.getBytes());
        createSoapObject(is);
    }
    public void createSoapObject(InputStream message) {
        // the message is a java stream.
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, message);
            createSoapObject(soapMessage);
        } catch (IOException e) {
            logger.error("IOException: {}", e.getCause());
        } catch (SOAPException e) {
            logger.error("SOAPException: {}", e.getCause());
        }
    }
    public void createSoapObject(SOAPMessage message) {
        // the message is a java SOAPMessage
        this.message = message;
        try {
            envelope = message.getSOAPPart().getEnvelope();
            header = envelope.getHeader();
            body = envelope.getBody();
            logger.debug("\nSOAP Header: {}\nSOAP Body: {}", header.toString(), body.toString());

            fault = body.getFault();
            getSoapFault();

            getAttachment(message);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }


    public void getSoapFault() {
        if (null != fault) {
            String faultCode = fault.getFaultCode();
            String faultString = fault.getFaultString();
            logger.error("\n\tSOAP Fault code: {}\n\tSOAP Fault String: {}", faultCode, faultString);
        }
    }

    // TODO get Attachment
    public void getAttachment(SOAPMessage message) {
        logger.debug("SOAP AttachmentPart");
        int countAttachments = message.countAttachments();
        logger.debug("\t* The number of attachments is {}", countAttachments);

        Iterator $i = message.getAttachments();
        while ($i.hasNext()) {
            // get next attachment
            AttachmentPart ap = (AttachmentPart) $i.next();

            // get content type
            String contentType = ap.getContentType();
            logger.debug("\t* Content type: {}", contentType);

            // get content id
            String contentID = ap.getContentId();
            logger.debug("\t* Content ID: {}", contentID);

            parseAttachmentContent(ap, contentType);
        }
    }


    private void parseAttachmentContent(AttachmentPart ap, String contentType) {
        String content = null;

        //Check to see if this is text
        /*if (contentType.indexOf("text") >= 0) {
            //Get and print string content if it is a text attachment
            try {
                content = (String) ap.getContent();
            } catch (SOAPException e) {
                e.printStackTrace();
            }
        }*/

        // check if it is application/octet-stream
        if (contentType.indexOf("application/octet-stream") >= 0) {
            try {
                InputStream inputStream = ap.getRawContent();
                content = IOUtils.toString(inputStream);
            } catch (SOAPException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("\t*** attachment content: \n{}" + content);
    }


    public SOAPMessage getMessage() {
        return message;
    }

    public String getPrettyPrint() {
        Source source = new StreamSource(new StringReader(sMessage));
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bos);
            t.transform(source, result);
            return bos.toString();
        } catch (Exception e) {
            logger.error("Error while pretty printing SOAP source", e);
            return "Error while pretty printing SOAP source";
        }
    }
}
