package com.chaitin.xray.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.log4j.Logger;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

public class JNAUtil {

    private static final Logger logger = Logger.getLogger(JNAUtil.class);

    private static Unsafe getUnsafe() {
        Unsafe unsafe;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return unsafe;
    }

    private static void removeClassCache(Unsafe unsafe, Class<?> clazz) {
        try {
            Class<?> ClassAnonymousClass = unsafe.defineAnonymousClass(
                    clazz, Objects.requireNonNull(
                            IOUtil.readBytesFromIs(
                                    Class.class.getResourceAsStream("Class.class"))), null);
            Field reflectionDataField = ClassAnonymousClass.getDeclaredField("reflectionData");
            unsafe.putObject(clazz, unsafe.objectFieldOffset(reflectionDataField), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean bypassReflectionFilter() {
        try {
            Unsafe unsafe = getUnsafe();
            Class<?> classClass = Class.class;
            try {
                System.out.println(classClass.getDeclaredField("classLoader"));
                return true;
            } catch (Exception e) {
                try {
                    Class<?> reflectionClass = Class.forName("jdk.internal.reflect.Reflection");
                    byte[] classBuffer = IOUtil.readBytesFromIs(
                            reflectionClass.getResourceAsStream("Reflection.class"));
                    assert classBuffer != null;
                    Class<?> reflectionAnonymousClass = unsafe.defineAnonymousClass(
                            reflectionClass, classBuffer, null);

                    Field fieldFilterMapField =
                            reflectionAnonymousClass.getDeclaredField("fieldFilterMap");
                    Field methodFilterMapField =
                            reflectionAnonymousClass.getDeclaredField("methodFilterMap");

                    if (fieldFilterMapField.getType().isAssignableFrom(HashMap.class)) {
                        unsafe.putObject(reflectionClass, unsafe.staticFieldOffset(
                                fieldFilterMapField), new HashMap<>());
                    }
                    if (methodFilterMapField.getType().isAssignableFrom(HashMap.class)) {
                        unsafe.putObject(reflectionClass, unsafe.staticFieldOffset(
                                methodFilterMapField), new HashMap<>());
                    }
                    removeClassCache(unsafe, classClass);
                    System.out.println(classClass.getDeclaredField("classLoader"));
                    return true;
                } catch (ClassNotFoundException e2) {
                    try {
                        Class<?> reflectionClass = Class.forName("sun.reflect.Reflection");
                        byte[] classBuffer = IOUtil.readBytesFromIs(
                                reflectionClass.getResourceAsStream("Reflection.class"));
                        assert classBuffer != null;
                        Class<?> reflectionAnonymousClass = unsafe.defineAnonymousClass(
                                reflectionClass, classBuffer, null);

                        Field fieldFilterMapField =
                                reflectionAnonymousClass.getDeclaredField("fieldFilterMap");
                        Field methodFilterMapField =
                                reflectionAnonymousClass.getDeclaredField("methodFilterMap");

                        if (fieldFilterMapField.getType().isAssignableFrom(HashMap.class)) {
                            unsafe.putObject(reflectionClass, unsafe.staticFieldOffset(
                                    fieldFilterMapField), new HashMap<>());
                        }
                        if (methodFilterMapField.getType().isAssignableFrom(HashMap.class)) {
                            unsafe.putObject(reflectionClass, unsafe.staticFieldOffset(
                                    methodFilterMapField), new HashMap<>());
                        }
                        removeClassCache(unsafe, classClass);
                        return true;
                    } catch (Exception ignored) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

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
                    if (bypassReflectionFilter()) {
                        logger.info("java 9+ bypass reflect get pid");
                        Method method = Process.class.getDeclaredMethod("pid");
                        result = (long) method.invoke(p);
                    } else {
                        return result;
                    }
                } catch (Exception ignored) {
                    return result;
                }
            }
        }
        return result;
    }
}
