package com.chaitin.xray;

import com.chaitin.xray.form.MainForm;
import com.chaitin.xray.model.DB;
import com.chaitin.xray.utils.StringUtil;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {
    private static final String SKIN = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

    public static void main(String[] args) {
        try {
            DB db = DB.parseDB(Files.readAllBytes(Paths.get("super-xray.db")));
            String defaultSkin = db.getSkin();

            try {
                Class.forName(defaultSkin);
            } catch (Exception ignored) {
                UIManager.setLookAndFeel(SKIN);
            }
            if (StringUtil.notEmpty(defaultSkin)) {
                try {
                    UIManager.setLookAndFeel(defaultSkin);
                } catch (Exception ignored) {
                    UIManager.setLookAndFeel(SKIN);
                }
            }
            MainForm.startMainForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
