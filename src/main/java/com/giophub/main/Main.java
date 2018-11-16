package com.giophub.main;

import com.giophub.task.java.ScheduledTasks;
import com.giophub.web.client.JaxWsClient;
import com.giophub.web.client.impl.ApacheHttpSoapClient;
import com.giophub.xml.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 *
 * TODO 4 :: get the response / content response
 * TODO 5 :: save the content response on file / logfile
 * TODO 6 :: track the response execution time
 * TODO 7 :: schedule to ApacheSoapClientTask this application N-times with spring scheduler and count the request number
 * */


@EnableScheduling
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    // Input arguments
    public static String URI = "http://localhost";
    public static String INPUT_FILE = "";
    public static String SOAP_ACTION = "";

    // fields
    public static String RUNTIME_PATH;
    public static String REQUEST = "";

    public static void main(String argv[]) throws Exception {
        // check input arguments
        if (argv.length > 3) {
            System.out.println("Usage: java -jar <uri> <filename> <soapaction>");
            System.out.println("<uri> - the URI where is located the web service");
            System.out.println("<filename> - file containing the SOAP request");
            System.out.println("<soapaction> - the SOAP action header value");
            System.out.println();
            System.exit(1);
        }
        URI = argv[0];                      // Get target URI
        INPUT_FILE = argv[1];               // Get file to be posted
        if (!argv[2].trim().isEmpty())
            SOAP_ACTION = "nil or empty";   // Get SOAP action

        LOGGER.info("Summary of arguments\n" +
                "\tURI: {}\n\tSoapAction: {}\n\tInput file: {}", URI, SOAP_ACTION, INPUT_FILE);

        // get the application path and the build full file path
        RUNTIME_PATH = new File(".").getCanonicalPath();
        String path = Paths.get(RUNTIME_PATH, INPUT_FILE).toString();
        LOGGER.debug("Request Path: {}", path);

        // read the content of file, that is the request
        try {
            FileReader reader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(reader);
            REQUEST = new Parser().asString(buffer);
            LOGGER.info("HTTP String request\n\n {}", REQUEST);
        } catch (Exception e) {
            LOGGER.error("full path file did not find." + e.getMessage());
            LOGGER.info("Exiting from the application...");
            System.exit(1);
        }

//        new JaxWsClient(URI, SOAP_ACTION, REQUEST).doCall();

        // schedule web client tasks
//        ScheduledTasks tasks = new ScheduledTasks();
//        tasks.ApacheSoapClientTask();


        Map<String, String> headerElements = new HashMap<>();
        headerElements.put("Content-Type", "text/xml");
        headerElements.put("Accept", "text/xml");
        headerElements.put("SOAPAction", SOAP_ACTION);
//            headerElements.put("Content-Disposition", "attachment; filename=\"export.xml\"");

        ApacheHttpSoapClient soapClient = new ApacheHttpSoapClient(URI, REQUEST, true, headerElements);
        String soapResponse = soapClient.getSoapResponse();
        System.out.println("SOAP Response: " + soapResponse);
        soapClient.writeOnDisk();
    }

}
