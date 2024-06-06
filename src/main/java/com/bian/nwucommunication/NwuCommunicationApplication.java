package com.bian.nwucommunication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.bian.nwucommunication.mapper")
@SpringBootApplication
public class NwuCommunicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NwuCommunicationApplication.class, args);
    }

}
