package com.zhi.snmp.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * MibTree的每个结点
 */
@Data
@NoArgsConstructor
@ToString
public class MibNode {
    private String OID;
    private String name;
    private boolean isScalar;
    private boolean isTableColumn;
    private List<MibNode> children = new ArrayList<>();

    public MibNode(String name, String OID, boolean isScalar, boolean isTableColumn) {
        this.OID = OID;
        this.name = name;
        this.isScalar = isScalar;
        this.isTableColumn = isTableColumn;
    }

    public void addChildren(MibNode mibNode) {
        children.add(mibNode);
    }
}
