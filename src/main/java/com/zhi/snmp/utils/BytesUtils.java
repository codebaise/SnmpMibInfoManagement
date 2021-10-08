package com.zhi.snmp.utils;

import com.zhi.snmp.pojo.HexEntity;

import java.util.*;

public class BytesUtils {
    public static Queue<byte[]> byteQueue = new LinkedList<>();
    public static int curOperation = 0;
    // 输出16进制形式SNMP包
    public static void showHex() {
        StringBuilder hexString = new StringBuilder();
        StringBuilder charString = new StringBuilder();
        // 出队列
        byte[] bytes = byteQueue.poll();
        for (int i = 0; i < Objects.requireNonNull(bytes).length; i++) {
            if (i % 16 == 0) {
                System.out.println();
                System.out.printf("%05x ", i);
            }

//            if (bytes[i] == '\n' || bytes[i] == '\t' || bytes[i] == '\r')
            if (bytes[i] >= 33 && bytes[i] <= 126)
                charString.append(String.format("%c", Math.abs(bytes[i])));
            else
                charString.append(".");

            hexString.append(String.format("%02x ", Math.abs(bytes[i])));
            // 换行
            if (i % 16 == 15 || i == bytes.length - 1) {
                System.out.printf("%-48s %-32s", hexString.toString(), charString.toString());
                hexString = new StringBuilder();
                charString = new StringBuilder();
            }
        }
        System.out.println();
    }

    /**
     * 拆分HexString, [0x ..][十六进制表示][ascii码]封装到HexEntity中
     * @return
     */
    public static ArrayList<HexEntity> getHexString() {
        ArrayList<HexEntity> strings = new ArrayList<>();
        StringBuilder hexString = new StringBuilder();
        StringBuilder charString = new StringBuilder();
        String lineHead = "";
        // 出队列
        byte[] bytes = byteQueue.poll();
        for (int i = 0; i < Objects.requireNonNull(bytes).length; i++) {
            if (i % 16 == 0) {
//                System.out.println();
                lineHead = String.format("%05x ", i);
            }

//            if (bytes[i] == '\n' || bytes[i] == '\t' || bytes[i] == '\r')
            if (bytes[i] >= 33 && bytes[i] <= 126)
                charString.append(String.format("%c", Math.abs(bytes[i])));
            else
                charString.append(".");

            hexString.append(String.format("%02x ", Math.abs(bytes[i])));
            // 换行
            if (i % 16 == 15 || i == bytes.length - 1) {
                strings.add(new HexEntity(lineHead, hexString.toString(), charString.toString()));
                hexString = new StringBuilder();
                charString = new StringBuilder();
            }
        }
//        byteQueue = new LinkedList<byte[]>();
        return strings;
    }
//    public static void showHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        StringBuilder charString = new StringBuilder();
//        for (int i=0; i < bytes.length; i++){
//            if (i % 16 == 0){
//                System.out.println();
//                System.out.printf("%05x ", i);
//            }
//
//            hexString.append(String.format("%02x ", Math.abs(bytes[i])));
//            charString.append(String.format("%c", Math.abs(bytes[i])));
//            if (i % 16 == 15 || i == bytes.length-1) {
//                System.out.printf("%-48s %-32s", hexString.toString(), charString.toString());
//                hexString = new StringBuilder();
//                charString = new StringBuilder();
//                System.out.println();
//            }
//        }
//        System.out.println();
//    }

    public static void main(String[] args) {
//        showHex(new byte[]{112});
    }
}
