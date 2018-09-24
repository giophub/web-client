package com.giophub.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.locale.provider.LocaleServiceProviderPool;

import java.io.*;

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

    public void asInputStream(InputStreamReader inputStreamReader) {
//        while (null != (line = FileInputStream(File file)))
    }
}
