package com.chaitin.xray.model;

import com.chaitin.xray.utils.StringUtil;

import java.util.HashSet;
import java.util.List;

public class Poc {
    private static final HashSet<String> pocList = new HashSet<>();

    public static void addPoc(String[] pocArray) {
        for (String s : pocArray) {
            if(!StringUtil.notEmpty(s.trim())){
                continue;
            }
            if (s.endsWith("\r")) {
                s = s.substring(0, s.length() - 1);
            }
            pocList.add(s);
        }
    }

    public static void clear() {
        pocList.clear();
    }

    public static void addAll(List<String> poc) {
        pocList.addAll(poc);
    }

    public static HashSet<String> getPocList() {
        return pocList;
    }

    public static String getString() {
        StringBuilder sb = new StringBuilder();
        for (String s : pocList) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
