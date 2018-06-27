package com.giophub.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public void Parser() {
    }

    public void readAsBufferedReader(BufferedReader bufferedReader) {
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (null != (line = bufferedReader.readLine())) {
                System.out.println(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error on reading buffer\n" + e.getMessage());
        }
    }

    public void readAsInputStream(InputStream inputStream) {
        // todo
    }
}
