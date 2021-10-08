package com.zhi.snmp.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * Aop 切片, 为了得到本次请求的Byte[]
 */
@Aspect
@Component
public class BinaryStringAspect {

    @Pointcut("execution(* org.snmp4j.MessageDispatcherImpl.processMessage(..))")
    public void executePackage(){};

    @Before("executePackage()")
    public void getParam(JoinPoint joinPoint) {
        byte[] bytes = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof ByteBuffer)
                bytes = ((ByteBuffer) arg).array();
        }
        if (BytesUtils.curOperation == 0)
            BytesUtils.byteQueue.offer(bytes);
    }
}
