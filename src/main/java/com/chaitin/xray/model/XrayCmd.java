package com.chaitin.xray.model;

import com.chaitin.xray.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class XrayCmd {
    private String xray;
    private String module;
    private String config;
    private String poc;
    private String input;
    private String inputPrefix;
    private String output;
    private String outputPrefix;
    private String others;
    private String othersPrefix;

    public String getInputPrefix() {
        return inputPrefix;
    }

    public void setInputPrefix(String inputPrefix) {
        this.inputPrefix = inputPrefix;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public String getOthersPrefix() {
        return othersPrefix;
    }

    public void setOthersPrefix(String othersPrefix) {
        this.othersPrefix = othersPrefix;
    }

    public String getOthers() {
        return others != null ? others : "";
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getConfig() {
        return config != null ? config : "";
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getXray() {
        return xray != null ? xray : "";
    }

    public void setXray(String xray) {
        this.xray = xray;
    }

    public String getModule() {
        return module != null ? module : "";
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPoc() {
        return poc != null ? poc : "";
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    public String getInput() {
        return input != null ? input : "";
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output != null ? output : "";
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @SuppressWarnings("all")
    public String[] buildCmd() {
        List<String> list = new ArrayList<>();
        list.add(getXray());
        if (StringUtil.notEmpty(getConfig())) {
            list.add("--config");
            list.add(getConfig());
        }
        list.add(getModule());
        if (StringUtil.notEmpty(getPoc())) {
            String temp = getPoc();
            list.add(temp.split(" ")[0]);
            list.add(temp.split(" ")[1]);
        }
        if (StringUtil.notEmpty(getInput())) {
            list.add(getInputPrefix());
            list.add(getInput());
        }
        if (StringUtil.notEmpty(getOutput())) {
            list.add(getOutputPrefix());
            list.add(getOutput());
        }
        if (StringUtil.notEmpty(getOthers())) {
            list.add(getOthersPrefix());
            list.add(getOthers());
        }
        String[] arr = new String[list.size()];
        list.toArray(arr);
        return arr;
    }
}
