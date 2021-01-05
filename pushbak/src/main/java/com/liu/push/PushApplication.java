package com.sinosoft.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zkr
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableCaching
@EnableScheduling
public class PushApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(PushApplication.class, args);
        System.out.println("======启动成功====");
    }
}


