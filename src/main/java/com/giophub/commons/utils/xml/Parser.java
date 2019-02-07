package com.giophub.commons.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public void Parser() {
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
