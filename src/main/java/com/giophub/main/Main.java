package com.giophub.main;

import com.giophub.commons.utils.FileLoader;
import com.giophub.xml.Parser;

import java.io.*;

public class Main {

    public static void main(String argv[]) {

        BufferedReader buffer = new FileLoader().load("test/basic-xml-example.xml").asBufferedReader();

        Parser parser = new Parser();
        parser.asBufferedReader(buffer);

//        parser.asBufferedReader(buffer);
    }


}
