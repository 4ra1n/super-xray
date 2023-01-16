package com.chaitin.xray.utils;

import com.chaitin.xray.model.Const;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XrayUtil {
    private static final Logger logger = Logger.getLogger(XrayUtil.class);

    public static void rmAllConfig(String targetDir) {
        logger.info("delete all config");
        try {
            Files.delete(Paths.get(targetDir + Const.ModuleXrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Files.delete(Paths.get(targetDir + Const.XrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Files.delete(Paths.get(targetDir + Const.PluginXrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Files.delete(Paths.get(Const.ModuleXrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Files.delete(Paths.get(Const.XrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Files.delete(Paths.get(Const.PluginXrayYaml));
        } catch (Exception ignored) {
        }
        try {
            Path thisPath = Paths.get(Const.ConfigYaml);
            Path xrayPath = Paths.get(targetDir + Const.ConfigYaml);
            // do not delete current config.yaml
            if (!thisPath.toFile().getAbsolutePath().equals(
                    xrayPath.toFile().getAbsolutePath())) {
                Files.delete(xrayPath);
            }
        } catch (Exception ignored) {
        }
    }

    public static void cpAllConfig(String targetDir) {
        logger.info("copy all config");
        try {
            Files.write(Paths.get(targetDir + Const.ConfigYaml),
                    Files.readAllBytes(Paths.get(Const.ConfigYaml)));
        } catch (Exception ignored) {
        }
        try {
            Files.write(Paths.get(targetDir + Const.ModuleXrayYaml),
                    Files.readAllBytes(Paths.get(Const.ModuleXrayYaml)));
        } catch (Exception ignored) {
        }
        try {
            Files.write(Paths.get(targetDir + Const.XrayYaml),
                    Files.readAllBytes(Paths.get(Const.XrayYaml)));
        } catch (Exception ignored) {
        }
        try {
            Files.write(Paths.get(targetDir + Const.PluginXrayYaml),
                    Files.readAllBytes(Paths.get(Const.PluginXrayYaml)));
        } catch (Exception ignored) {
        }
    }
}
