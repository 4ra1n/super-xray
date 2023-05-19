package com.chaitin.xray.form;

import com.chaitin.xray.model.XrayCmd;
import com.chaitin.xray.utils.ExecUtil;
import com.chaitin.xray.utils.JNAUtil;
import com.chaitin.xray.utils.OSUtil;
import com.chaitin.xray.utils.StringUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class AJPScanForm {
    public volatile boolean stop = false;
    private static final Logger logger = LogManager.getLogger(AJPScanForm.class);

    @SuppressWarnings("all")
    private void execAndFresh(String[] finalCmd) {
        outputTextArea.setText(null);
        Thread thread = new Thread(() -> {
            try {
                Process process = ExecUtil.exec(finalCmd);
                if (process == null) {
                    return;
                }
                InputStream inputStream = process.getInputStream();
                if (inputStream == null) {
                    return;
                }
                BufferedReader isReader;
                if (OSUtil.isWindows()) {
                    InputStreamReader isr = new InputStreamReader(inputStream,
                            StandardCharsets.UTF_8);
                    isReader = new BufferedReader(isr);
                } else {
                    isReader = new BufferedReader(new InputStreamReader(inputStream));
                }
                String thisLine;
                new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (stop) {
                            long pid = JNAUtil.getProcessID(process);
                            logger.info(String.format("stop pid: %d", pid));
                            try {
                                if (!OSUtil.isWindows()) {
                                    new ProcessBuilder("kill", "-9",
                                            Long.toString(pid)).start();
                                } else {
                                    new ProcessBuilder("cmd.exe", "/c",
                                            String.format("taskkill /f /pid %d", pid)).start();
                                }
                                return;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).start();
                while ((!stop) && (thisLine = isReader.readLine()) != null) {
                    outputTextArea.append(thisLine);
                    outputTextArea.append("\n");
                    outputTextArea.setCaretPosition(outputTextArea.getText().length());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    public JPanel ajpPanel;
    private JScrollPane outputPanel;
    private JTextArea outputTextArea;
    private JPanel rootPanel;
    private JPanel ajpConfigPanel;
    private JTextField targetIPText;
    private JTextField portText;
    private JTextField outFileText;
    private JButton startScanButton;
    private JButton generateButton;
    private JButton openButton;
    private JCheckBox deleteCheckBox;
    private JLabel targetIPLabel;
    private JLabel targetPortLabel;
    private JLabel outputFileLabel;
    private JLabel payloadLabel;
    private JRadioButton tomcatAJPPotentialRCERadioButton;
    private JRadioButton weblogicRCECVE2023RadioButton;

    private static String xray;
    private static boolean running = false;
    public static String outputFilePath;
    public static ArrayList<String> outputFilePathList = new ArrayList<>();

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            targetIPLabel.setText("目标IP");
            targetPortLabel.setText("目标端口");
            startScanButton.setText("开始扫描");
            outputFileLabel.setText("输出文件");
            generateButton.setText("随机文件名");
            openButton.setText("打开输出文件");
            deleteCheckBox.setText("退出时删除报告");
        } else {
            targetIPLabel.setText("Target IP");
            targetPortLabel.setText("Target Port");
            startScanButton.setText("Start Scan");
            outputFileLabel.setText("Output File");
            generateButton.setText("Generate");
            openButton.setText("Open");
            deleteCheckBox.setText("delete when output");
        }
    }

    public void initOpenOutput() {
        openButton.addActionListener(e -> {
            if (outputFilePath == null) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.ajpPanel, "目前没有输出文件");
                } else {
                    JOptionPane.showMessageDialog(this.ajpPanel, "No output file");
                }
                return;
            }
            if (StringUtil.notEmpty(outputFilePath.trim())) {
                if (Files.exists(Paths.get(outputFilePath))) {
                    String tempOutput = outputFilePath.replace(".html",
                            "copy.html");
                    try {
                        Files.write(Paths.get(tempOutput),
                                Files.readAllBytes(Paths.get(outputFilePath)));
                    } catch (Exception ignored) {
                    }
                    new Thread(() -> ExecUtil.execOpen(tempOutput)).start();
                } else {
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(this.ajpPanel, "目前没有输出文件");
                    } else {
                        JOptionPane.showMessageDialog(this.ajpPanel, "No output file");
                    }
                }
            }
        });
    }

    public AJPScanForm(XrayCmd xrayCmd) {

        weblogicRCECVE2023RadioButton.setSelected(true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (deleteCheckBox.isSelected()) {
                for (String path : outputFilePathList) {
                    try {
                        Files.delete(Paths.get(path));
                    } catch (Exception ignored) {
                    }
                }
            }
        }));

        deleteCheckBox.setSelected(true);
        xray = xrayCmd.getXray();
        initLang();
        initOpenOutput();
        generateButton.addActionListener(e -> outFileText.setText(UUID.randomUUID() + ".html"));

        startScanButton.addActionListener(e -> {
            if (running) {
                stop = true;
                if (MainForm.LANG == MainForm.CHINESE) {
                    startScanButton.setText("开始扫描");
                } else {
                    startScanButton.setText("Start Scan");
                }
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.ajpPanel, "已停止");
                } else {
                    JOptionPane.showMessageDialog(this.ajpPanel, "Stop");
                }
                running = false;
                outputTextArea.setText(null);
                return;
            }
            if (!StringUtil.notEmpty(targetIPText.getText())) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.ajpPanel, "目标IP不能为空");
                } else {
                    JOptionPane.showMessageDialog(this.ajpPanel, "Target IP is null");
                }
                return;
            } else {
                if (!targetIPText.getText().contains(".") || targetIPText.getText().startsWith("http")) {
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(this.ajpPanel, "请输入正确的IP");
                    } else {
                        JOptionPane.showMessageDialog(this.ajpPanel, "Please input IP");
                    }
                    return;
                }
            }
            if (!StringUtil.notEmpty(portText.getText())) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.ajpPanel, "目标Port不能为空");
                } else {
                    JOptionPane.showMessageDialog(this.ajpPanel, "Target port is null");
                }
                return;
            } else {
                try {
                    Integer.parseInt(portText.getText());
                } catch (Exception ex) {
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(this.ajpPanel, "请输入正确的端口");
                    } else {
                        JOptionPane.showMessageDialog(this.ajpPanel, "Please input port");
                    }
                    return;
                }
            }
            if (!StringUtil.notEmpty(outFileText.getText())) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.ajpPanel, "输出不能为空");
                } else {
                    JOptionPane.showMessageDialog(this.ajpPanel, "Output file is null");
                }
                return;
            } else {
                Path outPath = Paths.get(outFileText.getText());
                if (Files.exists(outPath)) {
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(this.ajpPanel, "输出文件存在");
                    } else {
                        JOptionPane.showMessageDialog(this.ajpPanel, "Output file exist");
                    }
                    return;
                }
                outputFilePath = outPath.toAbsolutePath().toString();
                if (StringUtil.notEmpty(outputFilePath)) {
                    outputFilePathList.add(outputFilePath);
                    String copyName = outputFilePath.split("\\.html")[0] + "copy.html";
                    outputFilePathList.add(copyName);
                }
            }

            String targetIP = targetIPText.getText();
            String targetPort = portText.getText();
            String target = String.format("%s:%s", targetIP, targetPort);

            String p;
            if (tomcatAJPPotentialRCERadioButton.isSelected()) {
                p = "tomcat";
            } else if (weblogicRCECVE2023RadioButton.isSelected()) {
                p = "weblogic";
            } else {
                return;
            }


            String[] finalCmd = new String[]{
                    xray,
                    "servicescan",
                    "-m",
                    p,
                    "--target",
                    target,
                    "--html-output",
                    outputFilePath
            };
            if (MainForm.LANG == MainForm.CHINESE) {
                startScanButton.setText("停止扫描");
            } else {
                startScanButton.setText("Stop Scan");
            }
            running = true;
            stop = false;
            outputTextArea.setText(null);
            execAndFresh(finalCmd);
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        ajpPanel = new JPanel();
        ajpPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setBackground(new Color(-12828863));
        ajpPanel.add(rootPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        outputPanel = new JScrollPane();
        outputPanel.setBackground(new Color(-12828863));
        outputPanel.setForeground(new Color(-12828863));
        rootPanel.add(outputPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(800, 200), null, null, 0, false));
        outputPanel.setBorder(BorderFactory.createTitledBorder(null, "output", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputTextArea = new JTextArea();
        outputTextArea.setBackground(new Color(-12828863));
        outputTextArea.setEditable(false);
        Font outputTextAreaFont = this.$$$getFont$$$("Consolas", -1, -1, outputTextArea.getFont());
        if (outputTextAreaFont != null) outputTextArea.setFont(outputTextAreaFont);
        outputTextArea.setForeground(new Color(-16711895));
        outputTextArea.setLineWrap(true);
        outputPanel.setViewportView(outputTextArea);
        ajpConfigPanel = new JPanel();
        ajpConfigPanel.setLayout(new GridLayoutManager(3, 7, new Insets(0, 0, 0, 0), -1, -1));
        ajpConfigPanel.setBackground(new Color(-12828863));
        rootPanel.add(ajpConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ajpConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "AJP / IIOP Service Scan", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        targetIPLabel = new JLabel();
        targetIPLabel.setText("Target IP");
        ajpConfigPanel.add(targetIPLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        targetIPText = new JTextField();
        ajpConfigPanel.add(targetIPText, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        targetPortLabel = new JLabel();
        targetPortLabel.setText("Target Port");
        ajpConfigPanel.add(targetPortLabel, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portText = new JTextField();
        ajpConfigPanel.add(portText, new GridConstraints(1, 4, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        outputFileLabel = new JLabel();
        outputFileLabel.setText("Output File");
        ajpConfigPanel.add(outputFileLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outFileText = new JTextField();
        ajpConfigPanel.add(outFileText, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate");
        ajpConfigPanel.add(generateButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openButton = new JButton();
        openButton.setText("Open");
        ajpConfigPanel.add(openButton, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startScanButton = new JButton();
        startScanButton.setText("Start Scan");
        ajpConfigPanel.add(startScanButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteCheckBox = new JCheckBox();
        deleteCheckBox.setBackground(new Color(-12828863));
        deleteCheckBox.setText("delete when output");
        ajpConfigPanel.add(deleteCheckBox, new GridConstraints(2, 5, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        payloadLabel = new JLabel();
        payloadLabel.setText("Payload");
        ajpConfigPanel.add(payloadLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tomcatAJPPotentialRCERadioButton = new JRadioButton();
        tomcatAJPPotentialRCERadioButton.setText("Tomcat AJP Potential RCE (CVE-2020-1938)");
        ajpConfigPanel.add(tomcatAJPPotentialRCERadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        weblogicRCECVE2023RadioButton = new JRadioButton();
        weblogicRCECVE2023RadioButton.setText("Weblogic RCE (CVE-2023-21839/21931/21979)");
        ajpConfigPanel.add(weblogicRCECVE2023RadioButton, new GridConstraints(0, 2, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(tomcatAJPPotentialRCERadioButton);
        buttonGroup.add(weblogicRCECVE2023RadioButton);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return ajpPanel;
    }

}
