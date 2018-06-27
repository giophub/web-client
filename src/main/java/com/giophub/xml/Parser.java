package com.giophub.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public void Parser() {
    }

    public void readFile(BufferedReader bufferedReader) {
        /*// Get an efficient reader for the file
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error on loading file: " + file + "\n" + e.getMessage());
        }

        BufferedReader bufferedReader = new BufferedReader(reader);*/

        // Read the file and display it's contents
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
}
