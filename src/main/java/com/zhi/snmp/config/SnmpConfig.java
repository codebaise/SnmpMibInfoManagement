package com.zhi.snmp.config;

import com.zhi.snmp.bean.WrapperSnmp;
import com.zhi.snmp.utils.TargetUtils;
import org.snmp4j.MessageDispatcherImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(TargetUtils.class)
//@ComponentScan("com.zhi.snmp")
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SnmpConfig {

    @Bean
    public MessageDispatcherImpl messageDispatcherImpl() {
        return new MessageDispatcherImpl();
    }

    @Bean
    @DependsOn("messageDispatcherImpl")
    public WrapperSnmp wrapperSnmp() throws IOException {
        WrapperSnmp wrapperSnmp = new WrapperSnmp(messageDispatcherImpl());
        wrapperSnmp.listen();
        return wrapperSnmp;
    }

    @Bean(initMethod = "createBaseTarget")
    public TargetUtils targetUtils() {
        return new TargetUtils();
    }
}