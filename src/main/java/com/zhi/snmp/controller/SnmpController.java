package com.zhi.snmp.controller;

import com.zhi.snmp.pojo.MibNode;
import com.zhi.snmp.bean.Msg;
import com.zhi.snmp.bean.Session;
import com.zhi.snmp.mib.MibFileParse;
import com.zhi.snmp.service.SnmpService;
import com.zhi.snmp.utils.BytesUtils;
import com.zhi.snmp.utils.MibTreeUtils;
import com.zhi.snmp.utils.PDUUtils;
import com.zhi.snmp.utils.TargetUtils;
import net.percederberg.mibble.MibValueSymbol;
import org.snmp4j.PDU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class SnmpController {
    @Autowired
    private SnmpService snmpService;
    @Autowired
    private MibFileParse mibFileParse;
    @Autowired
    private PDUUtils pduUtils;

    /**
     * 获取mib树结构
     */
    @GetMapping("/mibTree")
    public Msg getMibTree() {
        return Msg.success().add("tree", MibFileParse.getRoot());
    }

    /**
     * 请求oid的getNext
     */
    @GetMapping("/getRequest")
    public Msg getRequest(@RequestParam("oidString") String oidString) throws IOException {
        PDU getPdu = PDUUtils.createGetPdu(MibTreeUtils.getOidList(oidString));
        return getRequestWithOptional(getPdu);
    }

    /**
     * getNextRequest
     */
    @GetMapping("/getNextRequest")
    public Msg getNextRequest(@RequestParam("oidString") String oidString) throws IOException {
        PDU getNextPdu = PDUUtils.createGetNextPdu(MibTreeUtils.getOidList(oidString));
        return getRequestWithOptional(getNextPdu);
    }

    /**
     * getRequest 和 getNextRequest中相同部分 抽象出来的方法
     *
     * @param pdu 传进来代表不同的请求方式代表的put
     * @return 返回结果给两个接口方法
     */
    private Msg getRequestWithOptional(PDU pdu) throws IOException {
        Session session = snmpService.sendRequest(pdu, TargetUtils.BASE_TARGET_V1);
        Msg success = Msg.success();
        success.add("symbols", pduUtils.getResults(session.getResponse()));
        success.add("hexStrings", BytesUtils.getHexString());
        return success;
    }

    /**
     * 根据前端的oid去mib树中获取相应的结点, 并返回
     */
    @GetMapping("/getSymbol")
    public Msg getSymbolByOid(@RequestParam("oidString") String oidString) {
        MibValueSymbol mvs = mibFileParse.getMibValueByOID(oidString);
        return Msg.success().add("symbol", new MibNode(mvs.getName(), mvs.getValue().toString(), mvs.isScalar(), mvs.isTableColumn()));
    }

    /**
     * 获取表对象的所有索引
     */
    @GetMapping("/getAllIndex")
    public Msg getAllIndexByBaseOid(@RequestParam("baseOid") String baseOid) throws IOException {
        BytesUtils.curOperation = 1;
        List<String> curNodeAllIndex = snmpService.getCurNodeAllIndex(baseOid);
        BytesUtils.curOperation = 0;
        return Msg.success().add("indexList", curNodeAllIndex);
    }

}
