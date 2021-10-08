package com.zhi.snmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SnmpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnmpApplication.class, args);
    }

}
