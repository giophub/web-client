package com.giophub.main;

import com.giophub.commons.utils.FileLoader;
import com.giophub.web.client.ApacheSoapClient;
import com.giophub.web.client.HttpWebClient;
import com.giophub.xml.Parser;

import java.io.*;

public class Main {

    public static void main(String argv[]) throws Exception {
        /*if (argv.length != 3) {
            System.out.println("Usage: java -classpath <classpath> [-Dorg.apache.commons.logging.simplelog.defaultlog=<loglevel>] PostSOAP <url> <soapaction> <filename>]");
            System.out.println("<classpath> - must contain the commons-httpclient.jar and commons-logging.jar");
            System.out.println("<loglevel> - one of error, warn, info, debug, trace");
            System.out.println("<url> - the URL to post the file to");
            System.out.println("<soapaction> - the SOAP action header value");
            System.out.println("<filename> - file to post to the URL");
            System.out.println();
            System.exit(1);
        }
        String uri = argv[0];           // Get target URI
        String soapAction = argv[1]; // Get SOAP action
        String fileName = argv[2];      // Get file to be posted

        BufferedReader buffer = new FileLoader().load(fileName).asBufferedReader();
        String soapXmlFile = new Parser().asString(buffer);
        ApacheSoapClient apacheSoapClient = new ApacheSoapClient(uri, soapAction, soapXmlFile);
*/



        /**
         * TODO 1 :*: get CMD arguments like: java -jar web-client arg1 arg2 argN
         * TODO 2 :*: load the file name and read it
         * TODO 3 :*: call the web service / application
         * TODO 4 :*: get the response / content response
         * TODO 5 :*: save the content response on file / logfile
         * TODO 6 :: track the response execution time
         * TODO 7 :: schedule to run this application N-times with spring scheduler
         * */


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
