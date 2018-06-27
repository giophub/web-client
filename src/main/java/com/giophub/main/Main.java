package com.giophub.main;

import com.giophub.commons.utils.Loader;
import com.giophub.xml.Parser;

import java.io.BufferedReader;

public class Main {

    public static void main(String argv[]) {



        Loader loader = new Loader();
        BufferedReader bufferedReader = loader.loadAsBufferedReader("files/xml/basic/basic-xml-example.xml");

        Parser parser = new Parser();
        parser.readAsBufferedReader(bufferedReader);
    }


}
