package com.chaitin.xray;

import com.chaitin.xray.form.MainForm;
import com.chaitin.xray.model.Const;
import com.chaitin.xray.model.DB;
import com.chaitin.xray.utils.StringUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
            Path outLogPath = new File("xray-out.log").toPath();
            Path errLogPath = new File("xray-err.log").toPath();
            System.setOut(new PrintStream(Files.newOutputStream(outLogPath)));
            System.setErr(new PrintStream(Files.newOutputStream(errLogPath)));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.delete(errLogPath);
                } catch (Exception ignored) {
                }
                try {
                    Files.delete(outLogPath);
                } catch (Exception ignored) {
                }
            }));

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
