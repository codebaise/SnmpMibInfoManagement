package com.zhi.snmp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装Byte输出结果对应的三个部分
 */
@Data
@AllArgsConstructor
public class HexEntity {
    private String head;
    private String body;
    private String chars;
}
