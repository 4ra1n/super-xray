package com.chaitin.xray.utils;

public class OSUtil {
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("windows");
    }

    public static boolean isMacOS() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("mac");
    }
}
