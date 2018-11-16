package com.giophub.web.client;

public interface ApacheHttpClient {

    String request = null;
    String response = null;

    void doCall();
    String getRequest();
    String getResponse();
}
