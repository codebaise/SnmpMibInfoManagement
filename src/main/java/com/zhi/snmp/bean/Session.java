package com.zhi.snmp.bean;

import org.snmp4j.PDU;

/**
 * 封装request 和 response
 */
public class Session {
    private final PDU request;
    private final PDU response;

    public Session(PDU request, PDU response) {
        this.request = request;
        this.response = response;
    }

    public PDU getRequest() {
        return request;
    }

    public PDU getResponse() {
        return response;
    }


}
