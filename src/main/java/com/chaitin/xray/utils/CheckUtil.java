package com.chaitin.xray.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CheckUtil {
    public static boolean checkValid(String absPath) {
        Path path = Paths.get(absPath);
        String filename = path.getFileName().toString().toLowerCase();
        // super-xray Super Xray
        boolean superCondition = filename.contains("super");
        boolean xrayCondition = filename.contains("xray");
        if (superCondition && xrayCondition) {
            return false;
        }
        if (!Files.exists(path)) {
            return false;
        }
        try {
            long size = Files.size(path);
            int mb = (int) (size / 1024 / 1024);
            return mb > 10;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean checkRadValid(String absPath) {
        Path path = Paths.get(absPath);
        String filename = path.getFileName().toString().toLowerCase();
        boolean radCondition = filename.contains("rad");
        if (!radCondition) {
            return false;
        }
        if (!Files.exists(path)) {
            return false;
        }
        try {
            long size = Files.size(path);
            int mb = (int) (size / 1024 / 1024);
            return mb > 10;
        } catch (IOException e) {
            return false;
        }
    }
}
