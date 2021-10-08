package com.zhi.snmp.utils;

import lombok.Data;
import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "snmp.target", ignoreUnknownFields = true)
@Data
public class TargetUtils {
//    @Value("${snmp.target.sendSnmpPort}")
    private int sendSnmpPort;
    private static int timeOut;
    private static int retries;
//    @Value("${snmp.target.targetIP}")
    private String targetIP;
//    @Value("${snmp.target.community}")
    private String community;

    public static CommunityTarget BASE_TARGET_V1;
    public static CommunityTarget BASE_TARGET_V2;
    @Value("${snmp.target.timeOut}")
    public void setTimeOut(int timeOut) {
        TargetUtils.timeOut = timeOut;
    }
    @Value("${snmp.target.retries}")
    public static int getRetries() {
        return retries;
    }

    private void createBaseTarget() {
        TargetUtils.BASE_TARGET_V1 = TargetUtils.createTargetEntityWithVersion(sendSnmpPort, community, targetIP, SnmpConstants.version1);
        TargetUtils.BASE_TARGET_V2 = TargetUtils.createTargetEntityWithVersion(sendSnmpPort, community, targetIP, SnmpConstants.version2c);
    }

    private static CommunityTarget createTargetEntityWithVersion(int sendSnmpPort, String community, String targetIp, int snmpVersion) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setVersion(snmpVersion);
        target.setAddress(new UdpAddress(targetIp + "/" + String.valueOf(sendSnmpPort)));
        target.setTimeout(TargetUtils.timeOut);    //3s
        target.setRetries(TargetUtils.retries);
        return target;
    }
}
