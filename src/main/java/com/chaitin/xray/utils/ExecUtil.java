package com.chaitin.xray.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ExecUtil {
    private static final Logger logger = LogManager.getLogger(ExecUtil.class);

    public static void chmod(String path) {
        String[] chmodCmd = new String[]{"/bin/chmod", "u+x", path};
        exec(chmodCmd);
    }

    public static void execCmdNoRet(String[] cmd) {
        exec(cmd);
    }

    public static Process exec(String[] cmdArray) {
        try {
            String cmd = String.join(" ", cmdArray);
            logger.info(String.format("run cmd: %s", cmd));
            return new ProcessBuilder(cmdArray).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void execOpen(String outputFilePath) {
        if (OSUtil.isWindows()) {
            String cmd = String.format("start %s", outputFilePath);
            String[] xrayCmd = new String[]{"cmd.exe", "/c", String.format("%s", cmd)};
            exec(xrayCmd);
        } else {
            String cmd = String.format("open %s", outputFilePath);
            String[] xrayCmd = new String[]{"/bin/bash", "-c", String.format("%s", cmd)};
            exec(xrayCmd);
        }
    }
}
