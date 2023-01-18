package com.chaitin.xray;

import com.chaitin.xray.form.MainForm;
import com.formdev.flatlaf.FlatDarkLaf;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) {
        try {
            Path outLogPath = new File("xray-out.log").toPath();
            Path errLogPath = new File("xray-err.log").toPath();
            Path caCerPath = new File("ca.crt").toPath();
            Path caKeyPath = new File("ca.key").toPath();
            System.setOut(new PrintStream(Files.newOutputStream(outLogPath)));
            System.setErr(new PrintStream(Files.newOutputStream(errLogPath)));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (MainForm.instance.delLogCheckBox.isSelected()) {
                    try {
                        Files.delete(errLogPath);
                    } catch (Exception ignored) {
                    }
                    try {
                        Files.delete(outLogPath);
                    } catch (Exception ignored) {
                    }
                    if (MainForm.instance.delCaCheckBox.isSelected()) {
                        try {
                            Files.delete(caCerPath);
                        } catch (Exception ignored) {
                        }
                        try {
                            Files.delete(caKeyPath);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }));
            FlatDarkLaf.setup();
            MainForm.startMainForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
