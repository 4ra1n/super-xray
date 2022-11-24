package com.chaitin.xray.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

import java.lang.reflect.Field;

public class JNAUtil {
    public static long getProcessID(Process p) {
        long result = Integer.MAX_VALUE;
        try {
            if (OSUtil.isWindows()) {
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handle = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handle));
                result = kernel.GetProcessId(hand);
                f.setAccessible(false);
            } else {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                result = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
