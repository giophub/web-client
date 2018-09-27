package com.giophub.main;

import com.giophub.task.java.ScheduledTasks;
import com.giophub.xml.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;


/**
 * TODO 1 :*: get CMD arguments like: java -jar web-client arg1 arg2 argN
 * TODO 2 :*: load the file name and read it
 * TODO 3 :*: call the web service / application
 * TODO 4 :*: get the response / content response
 * TODO 5 :*: save the content response on file / logfile
 * TODO 6 :*: track the response execution time
 * TODO 7 :*: schedule to ApacheSoapClientTask this application N-times with spring scheduler and count the request number
 * */


@EnableScheduling
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    // Input arguments
//    public static String URI = "http://www.acceptance.ec.europa.eu/eurostat/SDMX/diss-ws/NSIStdV20Service";
//    public static String URI = "http://158.166.164.77/eurostat/SDMX/diss-ws/NSIStdV20Service";
//    public static String URI = "http://158.167.133.77/eurostat/SDMX/diss-ws/NSIStdV20Service";
//    public static String URI = "http://ec.europa.eu/eurostat/SDMX/diss-ws/NSIStdV20Service";
    public static String URI = "http://localhost";
    public static String INPUT_FILE = "test/basic-xml-example.xml";
    public static String SOAP_ACTION = "";

    // fields
    public static String REQUEST = "";

    public static void main(String argv[]) throws Exception {
        if (argv.length > 3) {
            System.out.println("Usage: java -classpath <classpath> [-Dorg.apache.commons.logging.simplelog.defaultlog=<loglevel>] PostSOAP <url> <soapaction> <filename>]");
            System.out.println("<classpath> - must contain the commons-httpclient.jar and commons-logging.jar");
            System.out.println("<loglevel> - one of error, warn, info, debug, trace");
            System.out.println("<url> - the URL to post the file to");
            System.out.println("<soapaction> - the SOAP action header value");
            System.out.println("<filename> - file to post to the URL");
            System.out.println();
            System.exit(1);
        }
        URI = argv[0];                      // Get target URI
        INPUT_FILE = argv[1];               // Get file to be posted
        if (!argv[2].trim().isEmpty())
            SOAP_ACTION = "nil or empty";   // Get SOAP action

        LOGGER.info("Summary of arguments\n" +
                "\tURI: {}\n\tSoapAction: {}\n\tInput file: {}", URI, SOAP_ACTION, INPUT_FILE);

        String path = new File(".").getCanonicalPath();
        path = Paths.get(path, INPUT_FILE).toString();
        LOGGER.debug("Path: {}", path);

        try {
            FileReader reader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(reader);
            REQUEST = new Parser().asString(buffer);
            LOGGER.info("HTTP request\n\n {}", REQUEST);
        } catch (Exception e) {
            LOGGER.error("full path file did not find." + e.getMessage());
            LOGGER.info("Exiting from the application...");
            System.exit(1);
        }


//        ApacheSoapClient apacheSoapClient = new ApacheSoapClient(uri, soapAction, INPUT_FILE);






//        BufferedReader buffer = new FileLoader().load(INPUT_FILE).asBufferedReader();
//
//        Parser parser = new Parser();
//        parser.asBufferedReader(buffer);



//        HttpWebClient httpWebClient = new HttpWebClient();
//        httpWebClient.setsURL("http://localhost:8080/pages/readme.html");
//        httpWebClient.setsURL("http://localhost:8080/SampleServletWithParameters");
//        httpWebClient.ApacheGetClient();
//        httpWebClient.ApacheClientWithResponseHandling();
//        httpWebClient.ApacheClientWithManualConnectionRelease();
//        httpWebClient.ApachePostClient();


//        URI = "http://ec.europa.eu/eurostat/SDMX/diss-ws/NSIStdV20Service";
//        URI = "http://www.acceptance.ec.europa.eu/eurostat/SDMX/diss-ws/NSIStdV20Service";
//        INPUT_FILE = parser.asString(buffer);
//        System.out.println(INPUT_FILE);
        /*ApacheSoapClient apacheSoapClient = new ApacheSoapClient(URI, SOAP_ACTION, INPUT_FILE);
        apacheSoapClient.doCall();*/


        ScheduledTasks tasks = new ScheduledTasks();
        tasks.ApacheSoapClientTask();
    }



}
