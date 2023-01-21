package com.chaitin.xray.model;

public class DB {
    private String lastXrayPath;

    public String getLastXrayPath() {
        return lastXrayPath;
    }

    public void setLastXrayPath(String lastXrayPath) {
        this.lastXrayPath = lastXrayPath;
    }

    public String getDB() {
        return String.format("%s=%s;", "last-xray-path", getLastXrayPath());
    }

    public static DB parseDB(byte[] data) {
        DB db = new DB();
        String[] temp = new String(data).split(";");
        db.setLastXrayPath(temp[0].split("=")[1]);
        return db;
    }
}
