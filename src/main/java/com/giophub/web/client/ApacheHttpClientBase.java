package com.giophub.web.client;

import com.giophub.main.Main;
import com.sun.istack.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public abstract class ApacheHttpClientBase implements ApacheHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClientBase.class);

    /*
            Part of the HTTP Request
     */

    /**
     * Protocol HTTP request and response. */
    protected String request, response;

    /**
     * The URI of HTTP connection. */
    protected String uri;

    protected boolean chunked = false;

    /**
     * Part of Protocol HTTP message. */
    protected Map<String, String> headerElements;


    /*
            Part of the HTTP Response
     */
    protected int statusCode = 0;



    /**
     * Set the applicative message (SOAP XML, REST, ...).
     *
     * @param message the message
     * @param setChunked true or false
     * @return
     */
    public void setRequest(String uri, String message, boolean chunked, @Nullable Map<String, String> headerElements) {
        this.uri = uri;
        this.request = message;
        this.chunked = chunked;
        this.headerElements = headerElements;
        logger.debug("Request arguments \n\tURI: {} \n\tChunked: {} \n\tRequest: \n\t\t{}", uri, chunked, request);
    }

    @Override
    public String getRequest() {
        return null;
    }

    @Override
    public String getResponse() {
        return null;
    }

    public void writeOnDisk() throws IOException {
        String fileName;
        // get file name from response
//                fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
        fileName = "content-response.xml";
        fileName = Paths.get(Main.RUNTIME_PATH, fileName).toString();
        logger.debug("Output filename: {}", fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(response.getBytes());
        fileOutputStream.close();
    }
}
