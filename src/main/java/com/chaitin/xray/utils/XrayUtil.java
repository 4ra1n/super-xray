package com.chaitin.xray.utils;

import com.chaitin.xray.model.Const;

public class XrayUtil {
    public static void rmAllConfig(String targetDir){
        ExecUtil.rmConfig(targetDir + Const.ModuleXrayYaml);
        ExecUtil.rmConfig(targetDir + Const.XrayYaml);
        ExecUtil.rmConfig(targetDir + Const.PluginXrayYaml);
        ExecUtil.rmConfig(targetDir + Const.ConfigYaml);
        ExecUtil.rmConfig(Const.ModuleXrayYaml);
        ExecUtil.rmConfig(Const.XrayYaml);
        ExecUtil.rmConfig(Const.PluginXrayYaml);
        ExecUtil.rmConfig(Const.ConfigYaml);
    }

    public static void cpAllConfig(String targetDir){
        ExecUtil.cpConfig(Const.ConfigYaml, targetDir);
        ExecUtil.cpConfig(Const.ModuleXrayYaml, targetDir);
        ExecUtil.cpConfig(Const.XrayYaml, targetDir);
        ExecUtil.cpConfig(Const.PluginXrayYaml, targetDir);
    }
}
