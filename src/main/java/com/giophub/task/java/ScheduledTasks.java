package com.giophub.task.java;

import com.giophub.commons.utils.PropertiesLoader;
import com.giophub.main.Main;
import com.giophub.web.client.ApacheSoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private Timer timer = new Timer();
    private Integer ApacheSoapClientAcc = 0;

    public void ApacheSoapClientTask() {
        Properties properties = PropertiesLoader.loadProperties("app.properties");
        long delay =  Long.parseLong(properties.getProperty("java.task.delay"));
        long period = Long.parseLong(properties.getProperty("java.task.period"));


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ApacheSoapClientAcc += 1;
                LOGGER.info("Execution number {}", ApacheSoapClientAcc);
                new ApacheSoapClient(Main.URI, Main.SOAP_ACTION, Main.REQUEST).doCall();
            }
        };

        try {
            timer.schedule(task, delay, period);
        } catch (Exception e){
            timer.cancel();

            LOGGER.error("Problems occurred when calling web service. " +
                    "Starting to terminate the application.", e.getMessage());
            System.exit(1);
        }
    }
}
