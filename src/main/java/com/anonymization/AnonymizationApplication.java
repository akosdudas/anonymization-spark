package com.anonymization;

import com.anonymization.mondrian.Anomyzer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ComponentScan(basePackages = {"com.anonymization.controllers","com.anonymization.services","com.anonymization.configuration"})
@SpringBootApplication
public class AnonymizationApplication {

    public static void main(String[] args) {
        Anomyzer.init();
        SpringApplication.run(AnonymizationApplication.class, args);

    }

    @Override
    protected void finalize() throws Throwable {
        Anomyzer.stop();
    }

}
