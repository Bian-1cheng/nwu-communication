package com.bian.nwucommunication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = {"com.bian.nwucommunication.mapper"})
public class NwuCommunicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NwuCommunicationApplication.class, args);
    }

}
