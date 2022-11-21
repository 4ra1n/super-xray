package com.chaitin.xray.model;

import java.io.File;

public interface Const {
    String ApplicationName = "SuperXray";
    String MacNeedAgree = "Mac OS 请设置 系统偏好设置->安全性与隐私->通用->仍要打开";
    String ModuleXrayYaml = String.format(".%s%s", File.separator, "module.xray.yaml");
    String XrayYaml = String.format(".%s%s", File.separator, "xray.yaml");
    String PluginXrayYaml = String.format(".%s%s", File.separator, "plugin.xray.yaml");
    String ConfigYaml = String.format(".%s%s", File.separator, "config.yaml");
}
