package com.chaitin.xray.model;

import org.apache.log4j.Logger;

import javax.swing.*;

public class XrayCmd {
    private static final Logger logger = Logger.getLogger(XrayCmd.class);
    private String xray;
    private String module;
    private String config;
    private String poc;
    private String input;
    private String output;
    private String others;

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
    public String buildCmd() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getXray());
        sb.append(" ");
        sb.append(this.getConfig());
        sb.append(" ");
        sb.append(this.getModule());
        sb.append(" ");
        sb.append(this.getPoc());
        sb.append(" ");
        sb.append(this.getInput());
        sb.append(" ");
        sb.append(this.getOutput());
        sb.append(" ");
        sb.append(this.getOthers());
        return sb.toString();
    }
}
