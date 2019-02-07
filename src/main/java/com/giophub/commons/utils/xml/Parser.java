package com.giophub.commons.utils.xml;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public void Parser() {
    }

    public static Document convertToDOM(String message) {
        return convertToDOM( IOUtils.toInputStream(message, Charsets.UTF_8) );
    }

    public static Document convertToDOM(InputStream is) {
        Document dom = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder builder = dbf.newDocumentBuilder();
            dom = builder.parse(is);
            dom.getDocumentElement().normalize(); // normalize the document

        } catch (ParserConfigurationException e) {
            LOGGER.error("Document builder factory exception: {}", e.getMessage());
        } catch (SAXException e) {
            LOGGER.error("Error on parsing the XML Document from Input Stream: {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("I/O Exception on parsing XML Document: {}", e.getMessage());
        }

        return dom;
    }

    public void asBufferedReader(BufferedReader bufferedReader) {
        LOGGER.debug("Reading file as buffered reader");
        String line = null;
        try {
            if (!bufferedReader.ready())
                LOGGER.warn("The buffer reader is null or it is not ready !");

            while (null != (line = bufferedReader.readLine())) {
                System.out.println(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error on reading buffer\n" + e.getMessage());
        }
    }

    public String asString(BufferedReader bufferedReader) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            if (!bufferedReader.ready())
                LOGGER.warn("The buffer reader is null or it is not ready !");

            while (null != (line = bufferedReader.readLine())) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            LOGGER.error("Error on reading buffer\n" + e.getMessage());
        }

        return sb.toString();
    }

    public void asInputStream(InputStreamReader inputStreamReader) {
//        while (null != (line = FileInputStream(File file)))
    }
}
