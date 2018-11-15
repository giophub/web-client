package com.giophub.web.client;

import com.giophub.pojo.SoapPojo;
import com.sun.xml.messaging.saaj.client.p2p.HttpSOAPConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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
        SOAPMessage response = null;
        try {
            connection = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage request = this.createMessage(soapXmlRequest);

            request.getSOAPHeader().detachNode();
            Map<String, String> headersMap = new HashMap<>();
//            headersMap.put("Content-Type", "text/xml");
            headersMap.put("Accept", "text/xml");
            soapAction = "\"\"";
            headersMap.put("SOAPAction", soapAction);
            addHttpHeaders(request, headersMap);

            request.saveChanges();

//            this.disassembleMessage(request);
            /* avoid soap connection, to avoid bad behaviour
             ERROR -> SAAJ0008: Bad Response; Not Found*/
            response = connection.call(request, uri);
//            String httpResponse = doHttpConncetion(uri, soapXmlRequest);
//            logger.debug("HTTP Response:\n{}", httpResponse);


            this.disassembleMessage(response);
        }
        catch (SOAPException e) {
            this.disassembleMessage(response);
            logger.error("An error occurred on calling web service. Caused by:", e.getCause());
        }
        /*finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                logger.debug("Connection closed.");
            } catch (SOAPException e1) {
                logger.error("An error occurred on closing connection. Caused by: ", e1.getCause());
            }
        }*/
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

    private void addHttpHeaders(SOAPMessage message, Map<String, String> map) {
        MimeHeaders headers = message.getMimeHeaders();

        if (logger.isDebugEnabled()) {
            logger.debug("Before to add headers");
            printAllHttpHeaders(message);
        }

        for (String key : map.keySet()) {
            headers.addHeader(key, map.get(key));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("After to add headers");
            printAllHttpHeaders(message);
        }
    }

    private void printAllHttpHeaders(SOAPMessage message) {
        Iterator<MimeHeader> $i = message.getMimeHeaders().getAllHeaders();
        while ($i.hasNext()) {
            MimeHeader header = $i.next();
            logger.debug("Header element - key: {} -> value: {}", header.getName(), header.getValue());
        }
    }

    private void disassembleMessage(SOAPMessage message) {
        SoapPojo soapPojo = new SoapPojo();
        soapPojo.createSoapObject(message);
    }

    private String doHttpConncetion(String uri, String request) {
        StringBuilder response = new StringBuilder();
        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            byte[] postData = request.getBytes();

            URLConnection connection = url.openConnection();
            connection.setDoInput( true );
            connection.setDoOutput( true );
//            connection.setInstanceFollowRedirects( false );
//            connection.setRequestMethod( "POST" );
//            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty( "Content-Type", "text/xml");
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postData.length ));
//            connection.setUseCaches( false );

            /*try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                wr.write( postData );
            }*/

            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
            writer.write(request);

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                response.append(line + "\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return response.toString();
    }

}
