package com.giophub.web.client;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpWebClient.class);

    private String sURL = "localhost:8080";

    public void setsURL(String sURL) {
        this.sURL = sURL;
    }

    public URI setParams() {
        URI uri = null;
        try {
            uri = new URIBuilder(sURL)
                    .addParameter("name", "Pippo")
                    .addParameter("city", "Roma")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }

    public void ApacheGetClient() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
//            HttpGet httpGet = new HttpGet("http://httpbin.org/get");
//            HttpGet httpGet = new HttpGet(sURL);
            HttpGet httpGet = new HttpGet(setParams());
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
                LOGGER.debug("Get request line: {}", httpGet.getRequestLine());
                LOGGER.debug("Get status line: {}", response1.getStatusLine());
//                System.out.println(response1.getStatusLine());

                // print the repsonse
                System.out.println(response1.toString());

                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body

                // print content response
                String sContent = EntityUtils.toString(entity1);
                System.out.println(sContent);

                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ApachePostClient() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "vip"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body

                String sContentResponse = EntityUtils.toString(entity2);
                System.out.println(sContentResponse);

                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void ApacheClientWithResponseHandling() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
//            HttpGet httpget = new HttpGet("http://httpbin.org/");
            HttpGet httpget = new HttpGet(sURL);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler without lambda expression
            /*ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };*/

            // Create a custom response handler with lambda expression
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
//                    return entity != null ? "Ciao" : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void ApacheClientWithManualConnectionRelease() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
//            HttpGet httpget = new HttpGet("http://httpbin.org/get");
            HttpGet httpget = new HttpGet(sURL);

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        instream.read();
                        // do something useful with the response

                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}