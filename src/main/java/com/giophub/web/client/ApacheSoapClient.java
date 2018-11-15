package com.giophub.web.client;

import com.giophub.main.Main;
import com.giophub.pojo.SoapPojo;
import com.giophub.xml.Parser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ApacheSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(ApacheSoapClient.class);
    private String uri;
    private String soapAction;
    private String soapXmlRequest;

    public ApacheSoapClient(String uri, String soapAction, String soapXmlRequest) {
        this.uri = uri;
        this.soapAction = soapAction;
        this.soapXmlRequest = soapXmlRequest;
    }

    public void doCall() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // Create a StringEntity for the SOAP XML.
//        String body ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://example.com/v1.0/Records\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body>"+soapEnvBody+"</SOAP-ENV:Body></SOAP-ENV:Envelope>";

            StringEntity stringEntity = new StringEntity(soapXmlRequest, "UTF-8");
            stringEntity.setChunked(true);

            // Request parameters and other properties.
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(stringEntity);
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.addHeader("Accept", "text/xml");
            httpPost.addHeader("SOAPAction", soapAction);
//            httpPost.addHeader("Content-Disposition", "attachment; filename=\"export.xml\"");
            logger.debug("HTTP POST URL: {}", httpPost.toString());


            // start to track the response time
            long startTime = System.nanoTime();

            // Execute and get the response.
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            logger.debug("Complete HTTP response: \n{}", httpResponse.toString());

            // HTTP response
            int statusCode = 0;
            // Get SOAP response
            HttpEntity entity;
            String body = null;
            InputStream inputStream;
            try {
                statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.debug("Status code: {}", statusCode);

                entity = httpResponse.getEntity();
                logger.debug("Entity: {}", entity.toString());

                // extract the SOAPMessage as stream from POST Body response
                inputStream = entity.getContent();
                body = IOUtils.toString(inputStream);
                logger.debug("HTTP response Body: \n{}", body);

                // and ensure it is fully consumed
                EntityUtils.consume(entity);
            }
            catch (IOException | ParseException  e) {
                logger.error("The call to Web Service failed. {}", e.getCause());
            }
            finally {
                httpResponse.close();
            }

            // parse the soap message
            /*logger.info("PARSE SOAP MESSAGE POJO");
            SoapPojo soapPojo = new SoapPojo();
            soapPojo.createSoapObject(body);*/
//            soapPojo.getAttachment(soapPojo.getMessage());
//            logger.debug("Formatted SOAPMessage: \n{}", soapPojo.getPrettyPrint());


            // write the response on filesystem
            writeOnDisk(body);

            processResponse(statusCode, body);

            // stop to track response time
            long endTime = System.nanoTime();
            long processTime =  (endTime - startTime);
            logger.info("Response time: {} nano-seconds that is ~{} seconds", processTime, TimeUnit.NANOSECONDS.toSeconds(processTime));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeOnDisk(String message) throws IOException {
        String fileName;
        // get file name from response
//                fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
        fileName = "content-response.xml";
        fileName = Paths.get(Main.RUNTIME_PATH, fileName).toString();
        logger.debug("Output filename: {}", fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(message.getBytes());
//                entity.writeTo(fileOutputStream);
        fileOutputStream.close();
    }

    private void printResponse(HttpEntity entity) throws IOException {
        // convert content response to string
        logger.debug("Start to print the content response");
        String sContentResponse = EntityUtils.toString(entity);
        logger.debug(sContentResponse);
    }

    private void getDisposition(CloseableHttpResponse httpResponse) {
        // get disposition from response header
        String disposition = httpResponse.getFirstHeader("Content-Disposition").getValue();
        logger.debug("Response.getFirstHeader name: {}", disposition);
    }


    private void processResponse(int statusCode, String message) {
        if (statusCode != 200) {
            // looking for SoapFault
            getFault(message);
        } else {
            // looking for content response: attachment, ...
            getAttachementFromHttpSoapResponse(message);
        }
    }

    private String getAttachementFromHttpSoapResponse(String message) {
        long start = System.currentTimeMillis();
        String attachment = null;

        StringReader reader = new StringReader(message);
        List<String> lines;
        String line;
        try {
            lines = IOUtils.readLines(reader);
            Iterator<String> $i = lines.iterator();
            while ($i.hasNext()) {
                line = $i.next();
                if (line.startsWith("{") && line.endsWith("}")) {
                    attachment = line;
                    logger.debug("Attachement: {}", attachment);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (attachment == null) {
            logger.debug("No Attachement found !");
        }
        long execTime = System.currentTimeMillis() - start;
        logger.info("Attachment processed in {} milli-sec", execTime);
        return attachment;
    }

    private void getFault(String message) {
        org.jdom.Document xml = Parser.asJDomocument(Parser.asDocument(IOUtils.toInputStream(message)));

        String faultCode = null;
        String faultString = null;

        Iterator $i = xml.getRootElement().getDescendants();
        while ($i.hasNext()) {
            Element node = (Element) $i.next();
            if (node.getName().equalsIgnoreCase("Fault")) {
                faultCode = node.getChild("faultcode").getText();
                faultString = node.getChild("faultctring").getText();
                logger.info("Fault code {}, Fault string: {}", faultCode, faultString);
                break;
            }
        }
    }

}
