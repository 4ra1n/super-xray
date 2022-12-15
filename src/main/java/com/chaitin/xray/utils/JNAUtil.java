package com.chaitin.xray.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JNAUtil {
    private static final Logger logger = Logger.getLogger(JNAUtil.class);

    public static long getProcessID(Process p) {
        String javaVersion = System.getProperty("java.version");
        long result = Integer.MAX_VALUE;
        if (javaVersion.startsWith("1.8")) {
            try {
                if (OSUtil.isWindows()) {
                    logger.info("java 1.8 windows jna get pid");
                    Field f = p.getClass().getDeclaredField("handle");
                    f.setAccessible(true);
                    long handle = f.getLong(p);
                    Kernel32 kernel = Kernel32.INSTANCE;
                    WinNT.HANDLE hand = new WinNT.HANDLE();
                    hand.setPointer(Pointer.createConstant(handle));
                    result = kernel.GetProcessId(hand);
                    f.setAccessible(false);
                } else {
                    logger.info("java 1.8 mac/linux reflect get pid");
                    Field f = p.getClass().getDeclaredField("pid");
                    f.setAccessible(true);
                    result = f.getLong(p);
                    f.setAccessible(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                logger.info("java 9+ reflect get pid");
                Method method = Process.class.getDeclaredMethod("pid");
                result = (long) method.invoke(p);
            } catch (Exception ex) {
                try {
                    logger.info("java 9+ bypass reflect get pid");
                    Method method = Process.class.getDeclaredMethod("pid");
                    result = (long) method.invoke(p);
                } catch (Exception ignored) {
                    return result;
                }
            }
        }
        return result;
    }
}
