package com.zhi.snmp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 封装结果对象
 */
@Data
@AllArgsConstructor
@ToString
public class Result {
    private String name;
    private String oid;
    private String value;
    private String type;
}
