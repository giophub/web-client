package com.giophub.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoader.class);

    private FileReader reader;


    public FileLoader load(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        name = classLoader.getResource(name).getFile();

//        FileReader reader = null;
        try {
            reader = new FileReader(name);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error on loading file: " + name + "\n" + e.getMessage());
        }
        return this;
    }

    public void flush() {

    }

    public void close() {
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    TODO write the content of file in memory to re-use it n-times.
    public void inMemory() {

    }

    public BufferedReader asBufferedReader() {
        return new BufferedReader(reader);
    }


}
