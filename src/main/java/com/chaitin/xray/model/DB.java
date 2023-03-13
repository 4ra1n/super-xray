package com.chaitin.xray.model;

public class DB {
    private String lastXrayPath;

    private String lastRadPath;

    public String getLastXrayPath() {
        return lastXrayPath;
    }

    public String getLastRadPath() {
        return lastRadPath;
    }

    public void setLastXrayPath(String lastXrayPath) {
        this.lastXrayPath = lastXrayPath;
    }

    public void setLastRadPath(String lastRadPath) {
        this.lastRadPath = lastRadPath;
    }

    public String getDB() {
        return String.format("%s=%s;%s=%s", "last-xray-path",
                getLastXrayPath(), "last-rad-path", getLastRadPath());
    }

    public static DB parseDB(byte[] data) {
        DB db = new DB();
        String[] temp = new String(data).split(";");
        db.setLastXrayPath(temp[0].split("=")[1]);
        if (temp.length > 1) {
            db.setLastRadPath(temp[1].split("=")[1]);
        }
        return db;
    }
}
