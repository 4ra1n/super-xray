package com.chaitin.xray.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import sun.misc.Unsafe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

public class JNAUtil {

    private static Unsafe getUnsafe() {
        Unsafe unsafe = null;

        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return unsafe;
    }
    public static byte[] readInputStream(InputStream inputStream) {
        byte[] temp = new byte[4096];
        int readOneNum = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((readOneNum = inputStream.read(temp)) != -1) {
                bos.write(temp, 0, readOneNum);
            }
            inputStream.close();
        }catch (Exception e){
        }
        return bos.toByteArray();
    }
    private static void removeClassCache(Unsafe unsafe,Class clazz){
        try {
            Class ClassAnonymousClass = unsafe.defineAnonymousClass(
                    clazz,readInputStream(Class.class.getResourceAsStream("Class.class")),null);
            Field reflectionDataField = ClassAnonymousClass.getDeclaredField("reflectionData");
            unsafe.putObject(clazz,unsafe.objectFieldOffset(reflectionDataField),null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void bypassReflectionFilter(){
        try {
            Unsafe unsafe = getUnsafe();
            Class classClass = Class.class;
            try {
                System.out.println(String.format("没有Reflection Filter ClassLoader :%s", classClass.getDeclaredField("classLoader")));
            }catch (Exception e){
                try {
                    Class reflectionClass=Class.forName("jdk.internal.reflect.Reflection");
                    byte[] classBuffer = readInputStream(reflectionClass.getResourceAsStream("Reflection.class"));
                    Class reflectionAnonymousClass = unsafe.defineAnonymousClass(reflectionClass,classBuffer,null);

                    Field fieldFilterMapField=reflectionAnonymousClass.getDeclaredField("fieldFilterMap");
                    Field methodFilterMapField=reflectionAnonymousClass.getDeclaredField("methodFilterMap");

                    if(fieldFilterMapField.getType().isAssignableFrom(HashMap.class)){
                        unsafe.putObject(reflectionClass,unsafe.staticFieldOffset(fieldFilterMapField),new HashMap());
                    }
                    if(methodFilterMapField.getType().isAssignableFrom(HashMap.class)){
                        unsafe.putObject(reflectionClass,unsafe.staticFieldOffset(methodFilterMapField),new HashMap());
                    }
                    removeClassCache(unsafe,classClass);
                    System.out.println(String.format("Bypass Jdk Reflection Filter Successfully! ClassLoader :%s", classClass.getDeclaredField("classLoader")));

                }catch (ClassNotFoundException e2){
                    try {
                        Class reflectionClass=Class.forName("sun.reflect.Reflection");
                        byte[] classBuffer = readInputStream(reflectionClass.getResourceAsStream("Reflection.class"));
                        Class reflectionAnonymousClass = unsafe.defineAnonymousClass(reflectionClass,classBuffer,null);

                        Field fieldFilterMapField=reflectionAnonymousClass.getDeclaredField("fieldFilterMap");
                        Field methodFilterMapField=reflectionAnonymousClass.getDeclaredField("methodFilterMap");

                        if(fieldFilterMapField.getType().isAssignableFrom(HashMap.class)){
                            unsafe.putObject(reflectionClass,unsafe.staticFieldOffset(fieldFilterMapField),new HashMap());
                        }
                        if(methodFilterMapField.getType().isAssignableFrom(HashMap.class)){
                            unsafe.putObject(reflectionClass,unsafe.staticFieldOffset(methodFilterMapField),new HashMap());
                        }
                        removeClassCache(unsafe,classClass);
                        System.out.println(String.format("Bypass Jdk Reflection Filter Successfully! ClassLoader :%s", classClass.getDeclaredField("classLoader")));

                    }catch (Exception e3){

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static long getProcessID(Process p) {
        String javaVersion = System.getProperty("java.version");
        long result = Integer.MAX_VALUE;
        if(javaVersion.startsWith("1.8")) {
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
        }else{
            try {
                bypassReflectionFilter();
                Method method = Process.class.getDeclaredMethod("pid");
                result = (long) method.invoke(p);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
