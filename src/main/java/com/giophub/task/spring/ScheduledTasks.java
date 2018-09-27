/*
package com.giophub.task.spring;

import com.giophub.main.Main;
import com.giophub.web.client.ApacheSoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private Integer ApacheSoapClientAcc = 0;

    @Scheduled(fixedRate = 60000)
    public void ApacheSoapClientTask() {
        ApacheSoapClientAcc = +1;
        LOGGER.info("Execution number {}", ApacheSoapClientAcc);
        new ApacheSoapClient(Main.URI, Main.SOAP_ACTION, Main.INPUT_FILE).doCall();
    }
}
*/
