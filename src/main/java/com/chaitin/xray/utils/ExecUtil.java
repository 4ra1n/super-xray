package com.chaitin.xray.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.nio.file.Paths;

public class ExecUtil {
    private static final Logger logger = LogManager.getLogger(ExecUtil.class);

    public static void chmod(String path) {
        String[] chmodCmd = new String[]{"/bin/chmod", "777", path};
        exec(chmodCmd);
    }

    public static void cpConfig(String source, String target) {
        if (OSUtil.isWindows()) {
            target = target.substring(0,target.length()-1);
            String[] temp = source.split("\\\\");
            String filename = temp[temp.length-1];
            target = target+"\\"+filename;
            source = source.replace("/","\\");
            target = target.replace("/","\\");

            source = Paths.get(source).toFile().getAbsolutePath();
            target = Paths.get(target).toFile().getAbsolutePath();

            String[] chmodCmd = new String[]{"cmd.exe", "/c",
                    String.format("copy %s %s",source,target)};
            exec(chmodCmd);
        } else {
            String[] chmodCmd = new String[]{"/bin/cp", source, target};
            exec(chmodCmd);
        }
    }

    public static void rmConfig(String file) {
        if (OSUtil.isWindows()) {
            file = file.replace("/","\\");
            String absPath = Paths.get(file).toFile().getAbsolutePath();
            String[] chmodCmd = new String[]{"cmd.exe", "/c",
                    String.format("del %s",absPath)};
            exec(chmodCmd);
        }else{
            String[] chmodCmd = new String[]{"/bin/rm", file};
            exec(chmodCmd);
        }
    }

    public static void execCmdNoRet(String cmd) {
        if (OSUtil.isWindows()) {
            String[] xrayCmd = new String[]{"cmd.exe", "/c", String.format("%s", cmd)};
            exec(xrayCmd);
        } else {
            String[] xrayCmd = new String[]{"/bin/bash", "-c", String.format("%s", cmd)};
            exec(xrayCmd);
        }
    }

    public static InputStream execCmdGetStream(String cmd) {
        if (OSUtil.isWindows()) {
            String[] xrayCmd = new String[]{"cmd.exe", "/c", String.format("%s", cmd)};
            return execGetStream(xrayCmd);
        } else {
            String[] xrayCmd = new String[]{"/bin/bash", "-c", String.format("%s", cmd)};
            return execGetStream(xrayCmd);
        }
    }

    @SuppressWarnings("all")
    public static String exec(String[] cmdArray) {
        try {
            String cmd = String.join(" ", cmdArray);
            logger.info(String.format("run cmd: %s", cmd));
            Process process = new ProcessBuilder(cmdArray).start();
            return IOUtil.readStringFromIs(process.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static InputStream execGetStream(String[] cmdArray) {
        try {
            String cmd = String.join(" ", cmdArray);
            logger.info(String.format("run cmd: %s", cmd));
            Process process = new ProcessBuilder(cmdArray).start();
            return process.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void execOpen(String outputFilePath) {
        if (OSUtil.isWindows()) {
            String cmd = String.format("start %s",outputFilePath);
            String[] xrayCmd = new String[]{"cmd.exe", "/c", String.format("%s", cmd)};
           exec(xrayCmd);
        } else {
            String cmd = String.format("open %s",outputFilePath);
            String[] xrayCmd = new String[]{"/bin/bash", "-c", String.format("%s", cmd)};
            exec(xrayCmd);
        }
    }
}
