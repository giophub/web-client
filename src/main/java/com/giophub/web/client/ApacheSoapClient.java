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

import java.io.IOException;

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

            // Execute and get the response.
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            try {
                HttpEntity entity = httpResponse.getEntity();

                // convert content response to string
                LOGGER.debug("Start to print the content response");
                String sContentResponse = EntityUtils.toString(entity);
                System.out.println(sContentResponse);

                // and ensure it is fully consumed
                EntityUtils.consume(entity);
            }
            catch (IOException | ParseException  e) {
                e.printStackTrace();
            }
            finally {
                httpResponse.close();
            }

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
