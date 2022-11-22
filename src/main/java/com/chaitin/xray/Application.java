package com.chaitin.xray;

import com.chaitin.xray.form.MainForm;
import com.chaitin.xray.model.Const;
import com.chaitin.xray.model.DB;
import com.chaitin.xray.utils.StringUtil;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static String globalSkin;

    private static void setNimbus() {
        try {
            UIManager.setLookAndFeel(Const.nimbus);
            globalSkin = Const.nimbus;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Path dbPath = Paths.get("super-xray.db");
            if (Files.exists(dbPath)) {
                DB db = DB.parseDB(Files.readAllBytes(dbPath));
                String defaultSkin = db.getSkin();
                try {
                    Class.forName(defaultSkin);
                } catch (Exception ignored) {
                    setNimbus();
                }
                if (StringUtil.notEmpty(defaultSkin)) {
                    try {
                        UIManager.setLookAndFeel(defaultSkin);
                        globalSkin = defaultSkin;
                    } catch (Exception ignored) {
                        setNimbus();
                    }
                } else {
                    setNimbus();
                }
            } else {
                setNimbus();
            }
            MainForm.startMainForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
