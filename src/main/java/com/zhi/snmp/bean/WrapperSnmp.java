package com.zhi.snmp.bean;

import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * 封装原始Snmp对象, 为了初始化 messageDispatcherImpl
 * 虽然原始的snmp也有setMessageDispatcher方法, 但是还需要执行
 *         protected initMessageDispatcher();
 *         addTransportMapping(new DefaultUdpTransportMapping());
 *         这两步, 则自行封装
 */
//@Component
public class WrapperSnmp extends Snmp {
    public WrapperSnmp() {}
    public WrapperSnmp(MessageDispatcherImpl messageDispatcherImpl) throws IOException {
        super();
        setMessageDispatcher(messageDispatcherImpl);
        initMessageDispatcher();
        addTransportMapping(new DefaultUdpTransportMapping());
    }
}
