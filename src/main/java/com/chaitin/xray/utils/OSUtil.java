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

    public static String getArch() {
        return System.getProperty("os.arch") + " (Maybe)";
    }

    public static String getOS() {
        return System.getProperty("os.name");
    }
}
