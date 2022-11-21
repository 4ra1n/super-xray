package com.chaitin.xray.model;

import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static DB parseDB(byte[] data) {
        DB db = new DB();
        String[] temp = new String(data).split(";");
        db.setSkin(temp[1].split("=")[1]);
        db.setLastXrayPath(temp[0].split("=")[1]);
        return db;
    }

    public void saveDB() {
        try {
            Files.write(Paths.get("super-xray.db"), getDB().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
