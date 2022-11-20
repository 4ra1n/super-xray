package com.chaitin.xray;

import com.chaitin.xray.form.MainForm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.*;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    private static final String SKIN = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(SKIN);
        }catch (Exception e){
            logger.error(String.format("nimbus look and feel not found: %s", e));
        }finally {
            logger.info("start main form");
            MainForm.startMainForm();
        }
    }
}
