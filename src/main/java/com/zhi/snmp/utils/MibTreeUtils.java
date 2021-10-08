package com.zhi.snmp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhi.snmp.pojo.MibNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MibTreeUtils {
    @Deprecated
    public static String formatJson(MibNode root) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 使用逗号分割string, 获得oidList
     */
    public static List<String> getOidList(String oidString) {
        oidString = oidString.replaceAll(" ", "");
        String[] split = oidString.split(",");
        return new ArrayList<>(Arrays.asList(split));
    }
}
