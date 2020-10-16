package com.gunbi.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created on 2020/10/16.
 *
 * @author hanzhong
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("server started successfully.\n");
    }
}
