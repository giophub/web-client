package com.giophub.main;

import com.giophub.commons.utils.FileLoader;
import com.giophub.web.client.ApacheSoapClient;
import com.giophub.web.client.HttpWebClient;
import com.giophub.xml.Parser;

import java.io.*;

public class Main {

    public static void main(String argv[]) {

        BufferedReader buffer = new FileLoader().load("test/basic-xml-example.xml").asBufferedReader();

        Parser parser = new Parser();
//        parser.asBufferedReader(buffer);


//        parser.asBufferedReader(buffer);


//        HttpWebClient httpWebClient = new HttpWebClient();
//        httpWebClient.setsURL("http://localhost:8080/pages/readme.html");
//        httpWebClient.setsURL("http://localhost:8080/SampleServletWithParameters");
//        httpWebClient.ApacheGetClient();
//        httpWebClient.ApacheClientWithResponseHandling();
//        httpWebClient.ApacheClientWithManualConnectionRelease();
//        httpWebClient.ApachePostClient();


        String uri = "http://ec.europa.eu/eurostat/SDMX/diss-ws/NSIStdV20Service";
        String soapXmlFile = parser.asString(buffer);
//        System.out.println(soapXmlFile);
        ApacheSoapClient apacheSoapClient = new ApacheSoapClient(uri, "", soapXmlFile);
    }


}
