package com.chaitin.xray.model;

public class DB {
    private String lastXrayPath;
    private String skin;

    public String getLastXrayPath() {
        return lastXrayPath;
    }

    public void setLastXrayPath(String lastXrayPath) {
        this.lastXrayPath = lastXrayPath;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getDB() {
        return String.format("%s=%s;%s=%s", "last-xray-path",
                getLastXrayPath(), "skin", getSkin());
    }
}
