package com.giophub.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String argv[]) throws Exception {
        // check input arguments
        LOGGER.info("Starting main");
    }

}
