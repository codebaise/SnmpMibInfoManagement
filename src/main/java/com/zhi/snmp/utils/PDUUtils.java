package com.zhi.snmp.utils;

import com.zhi.snmp.pojo.Result;
import com.zhi.snmp.mib.MibFileParse;
import net.percederberg.mibble.MibValueSymbol;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PDUUtils {

    private MibFileParse mibFileParse;

    public PDUUtils() {
    }

    @Autowired
    public PDUUtils(MibFileParse mibFileParse) {
        this.mibFileParse = mibFileParse;
    }

    public static PDU createGetPdu(List<String> oidList) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        for (String oid : oidList) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        return pdu;
    }

    public static PDU createGetNextPdu(List<String> oidList) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        for (String oid : oidList) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        return pdu;
    }

    public static PDU createGetBulkPdu(List<String> oidList) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(10);    //must set it, default is 0
        pdu.setNonRepeaters(0);
        for (String oid : oidList) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        return pdu;
    }

    public static PDU createSetPdu(Map<String, String> setMap) {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        for (Map.Entry<String, String> stringStringEntry : setMap.entrySet()) {
            pdu.add(new VariableBinding(new OID(stringStringEntry.getKey()), new OctetString(stringStringEntry.getValue())));
        }
        return pdu;
    }

    /**
     * 打印结果集
     * @param response
     */
    public void showResponseVbs(PDU response) {
        List<? extends VariableBinding> vbs = response.getVariableBindings();
        for (VariableBinding vb : vbs) {
            MibValueSymbol entity = mibFileParse.getMibValueByOID(vb.getOid().toString());
            System.out.println("Name: " + entity.getName() + "\nOID: " + vb + " >> Type: " + vb.getVariable().getSyntaxString());
        }
    }

    /**
     * 根据获得的请求体解析结果, 封装到Result对象中
     * @param response
     * @return
     */
    public List<Result> getResults(PDU response) {
        List<? extends VariableBinding> allVariable = response.getVariableBindings();
        ArrayList<Result> results = new ArrayList<>();
        for (VariableBinding per : allVariable) {
            MibValueSymbol entity = mibFileParse.getMibValueByOID(per.getOid().toString());
            results.add(new Result(entity.getName(),per.getOid().toString(), per.getVariable().toString(), per.getVariable().getSyntaxString()));
        }
        return results;
    }

    public void showResponseVbsWithHex(PDU response) {
        showResponseVbs(response);
        BytesUtils.showHex();
    }
}
