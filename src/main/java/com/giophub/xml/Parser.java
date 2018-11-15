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
     * Parse and convert an Input Strem to an XML Document object.
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

        return document;
    }

    /*private static Document asXml2(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());

        return db.parse(new InputSource(is));
    }
    class NullResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    }*/

}
