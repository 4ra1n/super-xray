package com.chaitin.xray.test;


import com.chaitin.xray.utils.JNAUtil;

public class Main {
    public static void main(String[] args)throws Exception {
        Process p = Runtime.getRuntime().exec("ls");
        System.out.println(JNAUtil.getProcessID(p));
    }
}
