package com.giophub.web.client;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class ApacheSoapClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheSoapClient.class);

    public ApacheSoapClient(String uri, String soapAction, String soapXmlRequest) {

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

            // start to track the response time
            long startTime = System.nanoTime();

            // Execute and get the response.
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            try {
                HttpEntity entity = httpResponse.getEntity();

                // convert content response to string
//                LOGGER.debug("Start to print the content response");
//                String sContentResponse = EntityUtils.toString(entity);
//                System.out.println(sContentResponse);

                // get disposition from response header
                /*String disposition = httpResponse.getFirstHeader("Content-Disposition").getValue();
                LOGGER.debug("Response.getFirstHeader name: {}", disposition);*/

                // get file name from response
                String fileName;
//                fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
                fileName = "content-response.xml";
                fileName = "C:\\_logs\\myDIR\\file\\" + fileName;
                LOGGER.debug("Output filename: {}", fileName);

                // write the response on filesystem
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                entity.writeTo(fileOutputStream);
                fileOutputStream.close();

                // and ensure it is fully consumed
                EntityUtils.consume(entity);
            }
            catch (IOException | ParseException  e) {
                e.printStackTrace();
            }
            finally {
                httpResponse.close();
            }

            // stop to track response time
            long endTime = System.nanoTime();
            long processTime =  (endTime - startTime);
            LOGGER.info("Response time: {} nano-seconds that is ~{} seconds", processTime, TimeUnit.NANOSECONDS.toSeconds(processTime));

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

}
