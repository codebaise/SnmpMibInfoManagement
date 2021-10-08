package com.zhi.snmp.mib;


public class MmMib {
    private String name;
    private String value;
    private String parent;
    private String syntax;
    private String access;
    private String status;
    private Boolean isTableColumn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTableColumn() {
        return isTableColumn;
    }

    public void setTableColumn(Boolean tableColumn) {
        isTableColumn = tableColumn;
    }

    @Override
    public String toString() {
        return "MmMib{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", parent='" + parent + '\'' +
                ", syntax='" + syntax + '\'' +
                ", access='" + access + '\'' +
                ", status='" + status + '\'' +
                ", isTableColumn=" + isTableColumn +
                '}';
    }
}
