package com.zhi.snmp.mib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zhi.snmp.pojo.MibNode;
import net.percederberg.mibble.*;
import net.percederberg.mibble.snmp.SnmpObjectType;
import org.springframework.stereotype.Component;

/**
 * 加载MIb文件
 */
@Component
public class MibFileParse {
    private static List<MmMib> mibList = new ArrayList<>();
    private static Mib mib = null;
    private static MibNode root;

    public MibFileParse() throws IOException, MibLoaderException {
        char sysSepNotation = File.separatorChar;
        String projectPath = System.getProperty("user.dir");
        String mibFileName = "RFC1213.mib";
        String filePath = projectPath + sysSepNotation + "src" + sysSepNotation + "main" + sysSepNotation + "resources" +
                sysSepNotation + "mib" + sysSepNotation + mibFileName;
        loadMIb(filePath);
        createMibTree();
    }

    /**
     * 载入 并解析mib文件
     */
    private void loadMIb(String filePath) throws IOException, MibLoaderException {
        File file = new File(filePath);
        MibLoader ml = new MibLoader();
        mib = ml.load(file);
    }

    /**
     * 将每一个模块封装为MmMIb实体, 方便调用, 未使用
     */
    @Deprecated
    @SuppressWarnings({"rawtypes"})
    public void wrapMmMibs() {
        String mibName = mib.getName();
        String syntax = "";
        String access = "";
        String status = "";
        Collection allSymbols = mib.getAllSymbols();
        // 遍历解析的mib的对象
        for (Object obj : allSymbols) {
            if (obj instanceof MibValueSymbol) {
                MmMib mo = new MmMib();
                // 将获得的对象转为MibValueSymbol类型
                MibValueSymbol mvs = (MibValueSymbol) obj;
                SnmpObjectType sot = null;
                // 判断当前是否为宏, 为宏时才有syntax
                if (mvs.getType() instanceof SnmpObjectType) {
                    sot = (SnmpObjectType) mvs.getType();
                }
                if (sot != null) {
                    syntax = sot.getSyntax().getName();
                    access = sot.getAccess().toString();
                    status = sot.getStatus().toString();
                }
                //是否为表的列
                boolean isTableColumn = mvs.isTableColumn();
                String name = mvs.getName();
                MibValue value = mvs.getValue();
                MibValueSymbol parent = mvs.getParent();
                String parentValue = "";
//                System.err.println("name==" + name);
//                System.err.println("value==" + value);
//                System.err.println("isTableColumn==" + isTableColumn);
                if (parent != null) {
                    parentValue = parent.getValue().toString();
//                    if (parent.getParent() == null) {
//                        System.err.println("supperParentName======" + mibName);
//                        System.err.println("supperParentValue=====" + parentValue);
//                    }
//                    System.err.println("parentName=" + parent.getName());
//                    System.err.println("parentValue=" + parent.getValue());
                }
//                System.err.println("syntax=" + syntax);
//                System.err.println("access=" + access);
//                System.err.println("status=" + status);
//                System.err.println("-------------------------------------");
                // 封装一个实体
                mo.setName(name);
                mo.setValue(value.toString());
                mo.setParent(parentValue);
                mo.setSyntax(syntax);
                mo.setAccess(access);
                mo.setStatus(status);
                mo.setTableColumn(isTableColumn);
                mibList.add(mo);
            }
        }
    }

    /**
     * 创建好加载的所有mib树
     */
    public void createMibTree() {
        MibValueSymbol rootSymbol = mib.getRootSymbol();
        putNode(root, rootSymbol);
    }

    /**
     * 递归生成mib树
     * @param parentNode 父节点
     * @param curMVS 当前的mvs结点
     */
    public void putNode(MibNode parentNode, MibValueSymbol curMVS){
        MibNode curNode = new MibNode(curMVS.getName(), curMVS.getValue().toString(), curMVS.isScalar(), curMVS.isTableColumn());
        if (parentNode != null)
            parentNode.addChildren(curNode);
        else
            root = curNode;

        for (MibValueSymbol child : curMVS.getChildren()) {
            putNode(curNode, child);
        }
    }

    /**
     * 根据oid找到相应的Symbol
     */
    public MibValueSymbol getMibValueByOID(String oid) {
//        System.err.println("mvs.getName()=" + mvs.getName());
//        System.err.println("mvs.getValue()=" + mvs.getValue());
//        MibValueSymbol parent = mvs.getParent();
//                "1.3.6.1.2.1.10"
        return mib.getSymbolByOid(oid);
    }

    /**
     * @return 根节点
     */
    public static MibNode getRoot() {
        return root;
    }
}