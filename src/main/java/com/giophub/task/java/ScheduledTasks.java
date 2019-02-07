package com.giophub.task.java;

import com.giophub.commons.utils.file.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private Timer timer = new Timer();
    private Integer executionNumber = 0;

    public void Task() {
        Properties properties = PropertiesLoader.loadProperties("app.properties");
        long delay =  Long.parseLong(properties.getProperty("java.task.delay"));
        long period = Long.parseLong(properties.getProperty("java.task.period"));


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                executionNumber += 1;
                LOGGER.info("Execution number {}", executionNumber);
//                new ApacheSoapClient(Main.URI, Main.SOAP_ACTION, Main.REQUEST).doCall();
            }
        };

        try {
            timer.schedule(task, delay, period);
        } catch (Exception e){
            timer.cancel();

            LOGGER.error("Problems occurred. " +
                    "Starting to terminate the application.", e.getMessage());
            System.exit(1);
        }
    }
}
