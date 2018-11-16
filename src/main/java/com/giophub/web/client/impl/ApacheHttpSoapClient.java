package com.giophub.web.client.impl;

import com.giophub.web.client.ApacheHttpClientBase;
import com.giophub.xml.Parser;
import com.sun.istack.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ApacheHttpSoapClient extends ApacheHttpClientBase {
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpSoapClient.class);

    private String soapResponse;
    private String soapFaultString;
    private String soapFaultCode;


    public ApacheHttpSoapClient(String uri, String message, boolean chunked, @Nullable Map<String, String> headerElements) {
        super.setRequest(uri, message, chunked, headerElements);
        this.doCall();
        this.processResponse(statusCode, response);
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public String getSoapFaultCode() {
        return soapFaultCode;
    }

    public String getSoapFaultString() {
        return soapFaultString;
    }



    public void printResponse(HttpEntity entity) throws IOException {
        // convert content response to string
        logger.debug("Start to print the content response");
        String sContentResponse = EntityUtils.toString(entity);
        logger.debug(sContentResponse);
    }

    public void getDisposition(CloseableHttpResponse httpResponse) {
        // get disposition from response header
        String disposition = httpResponse.getFirstHeader("Content-Disposition").getValue();
        logger.debug("Response.getFirstHeader name: {}", disposition);
    }


    @Override
    public void doCall() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        try {
            HttpPost httpPost = new HttpPost(super.uri);

            // set the request
            StringEntity request = new StringEntity(super.request, "UTF-8");
            httpPost.setEntity(request);

            // todo : remove all headers and set mine
            // set header elements
            fillHeaders(httpPost);
            logger.info("HTTP Request POST URL: {}", httpPost.toString());

            // start to track the response time
            long startTime = System.nanoTime();

            // Execute and get the response.
            httpResponse = httpClient.execute(httpPost);
            logger.info("HTTP response: {}", httpResponse.toString());

            // Get HTTP status code
            super.statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("Status code: {}", statusCode);

            // Get SOAP response
            HttpEntity entity = httpResponse.getEntity();
            logger.debug("Response Entity: {}", entity.toString());

            // extract the SOAPMessage as stream from POST Body response
            InputStream inputStream = entity.getContent();
            super.response = IOUtils.toString(inputStream);
            logger.debug("HTTP response Body: \n{}", super.response);

            // and ensure it is fully consumed
            EntityUtils.consume(entity);

            // stop to track response time
            long endTime = System.nanoTime();
            long processTime = (endTime - startTime);
            logger.info("Response time: {} nano-seconds that is ~{} seconds", processTime, TimeUnit.NANOSECONDS.toSeconds(processTime));
        }
        catch (IOException | ParseException e) {
            logger.error("The call to Web Service failed. {}", e.getCause());
        }
        finally {
            try {
                assert httpResponse != null;
                httpResponse.close();
                httpClient.close();
            }
            catch (IOException e) {
                logger.error("Error on closing HTTP connection: {}", e.getMessage());
            }
        }
    }

    private void fillHeaders(HttpPost httpPost) {
        for (String key : super.headerElements.keySet()) {
            httpPost.addHeader(key, super.headerElements.get(key));
        }
    }

    private void processResponse(int statusCode, String message) {
        if (statusCode != 200) {
            // looking for SoapFault
            soapResponse = getFault(message);
        } else {
            // looking for content response: attachment, ...
            soapResponse = getAttachementFromHttpSoapResponse(message);
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

    private String getFault(String message) {
        Document xml = Parser.asDocument(IOUtils.toInputStream(message));

        soapFaultCode   = xml.getElementsByTagName("faultcode").item(0).getTextContent();
        soapFaultString = xml.getElementsByTagName("faultstring").item(0).getTextContent();
        logger.debug("SOAPFault - faultcode: {}, faultstring: {}", soapFaultCode, soapFaultString);

        String errorResponse = soapFaultCode + " " + soapFaultString;
        return errorResponse;
    }

}
