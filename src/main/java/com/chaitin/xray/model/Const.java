package com.chaitin.xray.model;

import java.io.File;

public interface Const {
    String ApplicationName = "SuperXray";
    String MinXrayVersion = "支持xray最低版本：1.9.4";
    String SuperXrayVersion = "当前super-xray版本：1.2";
    String GithubTip = "由于前往Github验证，所以可能连接不通，请耐心等待，如果报错可以多次尝试";
    String CurVersion = "1.2";
    String MacNeedAgree = "Mac OS 请设置 系统偏好设置->安全性与隐私->通用->仍要打开";
    String MacNeedAgreeEn = "Please set System Preferences -> Security and Privacy -> General -> Still Open";
    String ModuleXrayYaml = String.format(".%s%s", File.separator, "module.xray.yaml");
    String XrayYaml = String.format(".%s%s", File.separator, "xray.yaml");
    String PluginXrayYaml = String.format(".%s%s", File.separator, "plugin.xray.yaml");
    String ConfigYaml = String.format(".%s%s", File.separator, "config.yaml");
    String flatLaf = "FlatLaf";
    String nimbus = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
    String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    String winClassic = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
    String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    String aqua = "com.apple.laf.AquaLookAndFeel";
}
