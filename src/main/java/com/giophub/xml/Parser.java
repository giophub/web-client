package com.giophub.xml;

import com.sun.istack.Nullable;
import org.apache.commons.io.IOUtils;
import org.jdom.input.DOMBuilder;
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
import java.nio.charset.Charset;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /*public void Parser() {
    }*/

    public static void asBufferedReader(BufferedReader bufferedReader) {
        logger.debug("Reading file as buffered reader");
        String line = null;
        try {
            if (!bufferedReader.ready())
                logger.warn("The buffer reader is null or it is not ready !");

            while (null != (line = bufferedReader.readLine())) {
                System.out.println(line);
            }
        } catch (IOException e) {
            logger.error("Error on reading buffer\n" + e.getMessage());
        }
    }

    public static String asString(BufferedReader bufferedReader) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            if (!bufferedReader.ready())
                logger.warn("The buffer reader is null or it is not ready !");

            while (null != (line = bufferedReader.readLine())) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            logger.error("Error on reading buffer\n" + e.getMessage());
        }

        return sb.toString();
    }

    public static void asInputStream(InputStreamReader inputStreamReader) {
//        while (null != (line = FileInputStream(File file)))
    }


    /**
     * Parse and convert a String to an InputStream object.
     *
     * @param string
     * @param encoding
     * @return
     */
    public static InputStream asInputStream(String string, @Nullable Charset encoding) {
        return IOUtils.toInputStream(string, encoding);
    }


    public static org.jdom.Document asJDomocument(Document document) {
        // use JDOM rather than W3C DOM
        DOMBuilder domBuilder = new DOMBuilder();
        org.jdom.Document jdom = domBuilder.build(document);
        return jdom;
    }


    /**
     * Parse and convert an Input Strem to an W3C DOM Normalized Document object.
     *
     * The normalization is optional, but recommended:
     *             This basically means that the following XML element
     *             <foo>hello
     *             wor
     *             ld</foo>
     *
     *             could be represented like this in a denormalized node:
     *             Element foo
     *                 Text node: ""
     *                 Text node: "Hello "
     *                 Text node: "wor"
     *                 Text node: "ld"
     *
     *            When normalized, the node will look like this:
     *            Element foo
     *                 Text node: "Hello world"
     *
     *            And the same goes for attributes: <foo bar="Hello world"/>, comments, etc.
     *
     * @param is
     *
     * @return XML DOM Document
     */
    public static Document asDocument(InputStream is) {
        Document document = null;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
        } catch (ParserConfigurationException e) {
            logger.error("Document builder factory exception: {}", e.getMessage());
        } catch (SAXException e) {
            logger.error("Error on parsing the XML Document from Input Stream: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("I/O Exception on parsing XML Document: {}", e.getMessage());
        }

        // normalize the document
        assert document != null;
        document.getDocumentElement().normalize();

        return document;
    }
}
