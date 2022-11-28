package com.chaitin.xray.model;

import com.chaitin.xray.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RadCmd {
    private String rad;
    private String target;
    private String targetUrl;
    private String proxy;
    private String proxyInfo;

    public String getRad() {
        return rad;
    }

    public void setRad(String rad) {
        this.rad = rad;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getProxyInfo() {
        return proxyInfo;
    }

    public void setProxyInfo(String proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    @SuppressWarnings("all")
    public String[] buildCmd() {
        List<String> list = new ArrayList<>();
        list.add(getRad());
        if (StringUtil.notEmpty(getTargetUrl())) {
            list.add(getTarget());
            list.add(getTargetUrl());
        }
        if (StringUtil.notEmpty(getProxy())) {
            list.add(getProxy());
            list.add(getProxyInfo());
        }
        String[] arr = new String[list.size()];
        list.toArray(arr);
        return arr;
    }
}
