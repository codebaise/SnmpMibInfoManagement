package com.zhi.snmp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zhi.snmp.exception.ServiceErrorException;
import com.zhi.snmp.utils.PDUUtils;
import com.zhi.snmp.bean.Session;
import com.zhi.snmp.utils.TargetUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Snmp 请求调用
 */
@Component
public class SnmpService {
    private Snmp snmp;

    public SnmpService() {
    }

    @Autowired
    public SnmpService(Snmp snmp) {
        this.snmp = snmp;
    }

    /**
     * 发送请求的对外接口
     * @param pdu 不同类型的PDU
     * @param target target可能变更, 所以传进来, 且有些请求需要更换target类型
     * @return Session对象, Request 和 Response
     */
    public Session sendRequest(PDU pdu, CommunityTarget target)
            throws IOException {
        ResponseEvent responseEvent = snmp.send(pdu, target);
        PDU response = responseEvent.getResponse();
        if (response == null)
            throw new RuntimeException("TimeOut...");

        if (response.getErrorStatus() != PDU.noError)
            throw new RuntimeException("Error:" + response.getErrorStatusText());

        return new Session(responseEvent.getRequest(), responseEvent.getResponse());
    }

    /**
     * 发送异步请求
     */
    public static void sendAsyncRequest(Snmp snmp, PDU pdu, CommunityTarget target)
            throws IOException {
        snmp.send(pdu, target, null, new ResponseListener() {

            @Override
            public void onResponse(ResponseEvent event) {
                PDU response = event.getResponse();
                System.out.println("Got response from " + event.getPeerAddress());
                if (response == null) {
                    System.out.println("TimeOut...");
                } else {
                    if (response.getErrorStatus() == PDU.noError) {
                        List<? extends VariableBinding> vbs = response.getVariableBindings();
                        for (VariableBinding vb : vbs) {
                            System.out.println(vb + " ," + vb.getVariable().getSyntaxString());
                        }
                    } else {
                        System.out.println("Error:" + response.getErrorStatusText());
                    }
                }
            }
        });
    }

    /**
     * 根据baseOid获取子树
     */
    public List<String> getCurNodeAllIndex(String baseOid) throws IOException {
        CommunityTarget target = TargetUtils.BASE_TARGET_V2;
        baseOid += '.';
        return getCurNodeAllIndex(baseOid, target);
    }

    private List<String> getCurNodeAllIndex(String baseOid, CommunityTarget target) throws IOException {
        List<String> result = new ArrayList<>();
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(baseOid));
        if (events == null || events.size() == 0) {
            throw new ServiceErrorException(400, "Error: Unable to read table...");
        }

        for (TreeEvent event : events) {
            if (event == null)
                continue;

            if (event.isError()) {
//                throw new ServiceErrorException(400, "Error: table OID [" + baseOid + "] " + event.getErrorMessage());
                System.out.println("Error: table OID [" + baseOid + "] " + event.getErrorMessage());
                continue;
            }

            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null || varBindings.length == 0) {
                continue;
            }
            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null)
                    continue;

                result.add(varBinding.getOid().toString());
            }
        }
        return result;
    }

}