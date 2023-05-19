package com.chaitin.xray.form;

import com.chaitin.xray.model.XrayCmd;
import com.chaitin.xray.utils.ExecUtil;
import com.chaitin.xray.utils.JNAUtil;
import com.chaitin.xray.utils.OSUtil;
import com.chaitin.xray.utils.StringUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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

public class SubdomainForm {

    public volatile boolean stop = false;
    private static final Logger logger = LogManager.getLogger(SubdomainForm.class);

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

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            targetLabel.setText("   目标");
            outputLabel.setText("   输出文件名");
            startButton.setText("开始扫描");
            optionLabel.setText("   选项");
            bruteForceCheckBox.setText("暴力扫描");
            onlyIpCheckbox.setText("只输出成功解析IP的");
            onlyWebCheckbox.setText("只输出web应用子域名");
            openResultButton.setText("打开输出文件");
            generateButton.setText("随机文件名");
            delCheckbox.setText("关闭时删除报告");
        } else {
            targetLabel.setText("   Target");
            outputLabel.setText("   Output File");
            startButton.setText("Start");
            optionLabel.setText("   Option");
            bruteForceCheckBox.setText("brute force");
            onlyIpCheckbox.setText("only resolved ip");
            onlyWebCheckbox.setText("only web apps");
            openResultButton.setText("Open Result");
            generateButton.setText("Generate");
            delCheckbox.setText("delete reports when exit");
        }
    }

    public JPanel subdomainPanel;
    private JScrollPane outputPanel;
    private JTextArea outputTextArea;
    private JTextField targetText;
    private JButton startButton;
    private JTextField outputFileText;
    private JPanel configPanel;
    private JPanel confPanel;
    private JLabel targetLabel;
    private JLabel outputLabel;
    private JCheckBox bruteForceCheckBox;
    private JLabel optionLabel;
    private JCheckBox onlyIpCheckbox;
    private JCheckBox onlyWebCheckbox;
    private JButton openResultButton;
    private JButton generateButton;
    private JPanel optionPanel;
    private JCheckBox delCheckbox;
    private static String xray;
    private static boolean running = false;
    public static String outputFilePath;
    public static ArrayList<String> outputFilePathList = new ArrayList<>();

    public void initOpenOutput() {
        openResultButton.addActionListener(e -> {
            if (outputFilePath == null) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "目前没有输出文件");
                } else {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "No output file");
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
                        JOptionPane.showMessageDialog(this.subdomainPanel, "目前没有输出文件");
                    } else {
                        JOptionPane.showMessageDialog(this.subdomainPanel, "No output file");
                    }
                }
            }
        });
    }

    public SubdomainForm(XrayCmd xrayCmd) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (delCheckbox.isSelected()) {
                for (String path : outputFilePathList) {
                    try {
                        Files.delete(Paths.get(path));
                    } catch (Exception ignored) {
                    }
                }
            }
        }));

        delCheckbox.setSelected(true);
        onlyIpCheckbox.setSelected(false);
        onlyIpCheckbox.setEnabled(false);
        xray = xrayCmd.getXray();
        initLang();
        initOpenOutput();
        generateButton.addActionListener(e -> outputFileText.setText(UUID.randomUUID() + ".html"));

        startButton.addActionListener(e -> {
            if (running) {
                stop = true;
                if (MainForm.LANG == MainForm.CHINESE) {
                    startButton.setText("开始扫描");
                } else {
                    startButton.setText("Start");
                }
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "已停止");
                } else {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "Stop");
                }
                running = false;
                outputTextArea.setText(null);
                return;
            }
            if (!StringUtil.notEmpty(targetText.getText())) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "目标不能为空");
                } else {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "Target is null");
                }
                return;
            }
            if (!StringUtil.notEmpty(outputFileText.getText())) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "输出不能为空");
                } else {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "Output file is null");
                }
                return;
            } else {
                Path outPath = Paths.get(outputFileText.getText());
                if (Files.exists(outPath)) {
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(this.subdomainPanel, "输出文件存在");
                    } else {
                        JOptionPane.showMessageDialog(this.subdomainPanel, "Output file exist");
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

            String target = targetText.getText();
            if (target.startsWith("http")) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "请直接输入域名");
                } else {
                    JOptionPane.showMessageDialog(this.subdomainPanel, "Input domain directly");
                }
                return;
            }

            String[] finalCmd = new String[]{
                    xray,
                    "subdomain",
                    "--target",
                    target,
                    "--html-output",
                    outputFilePath,
                    bruteForceCheckBox.isSelected() ? "" : "--no-brute",
                    onlyIpCheckbox.isSelected() ? "--ip-only" : "",
                    onlyWebCheckbox.isSelected() ? "--web-only" : "",
            };
            if (MainForm.LANG == MainForm.CHINESE) {
                startButton.setText("停止扫描");
            } else {
                startButton.setText("Stop");
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
        subdomainPanel = new JPanel();
        subdomainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        subdomainPanel.setBackground(new Color(-12828863));
        configPanel = new JPanel();
        configPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.setBackground(new Color(-12828863));
        subdomainPanel.add(configPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        confPanel = new JPanel();
        confPanel.setLayout(new GridLayoutManager(3, 7, new Insets(10, 10, 10, 10), -1, -1));
        confPanel.setBackground(new Color(-12828863));
        configPanel.add(confPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        confPanel.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        targetLabel = new JLabel();
        targetLabel.setText("   Target");
        confPanel.add(targetLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        targetText = new JTextField();
        confPanel.add(targetText, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        startButton = new JButton();
        startButton.setText("Start");
        confPanel.add(startButton, new GridConstraints(0, 5, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputLabel = new JLabel();
        outputLabel.setText("   Output File");
        confPanel.add(outputLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputFileText = new JTextField();
        outputFileText.setText("");
        confPanel.add(outputFileText, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        optionLabel = new JLabel();
        optionLabel.setText("   Option");
        confPanel.add(optionLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openResultButton = new JButton();
        openResultButton.setText("Open Result");
        confPanel.add(openResultButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate");
        confPanel.add(generateButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        optionPanel.setBackground(new Color(-12828863));
        confPanel.add(optionPanel, new GridConstraints(2, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bruteForceCheckBox = new JCheckBox();
        bruteForceCheckBox.setBackground(new Color(-12828863));
        bruteForceCheckBox.setText("brute force");
        optionPanel.add(bruteForceCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        onlyIpCheckbox = new JCheckBox();
        onlyIpCheckbox.setBackground(new Color(-12828863));
        onlyIpCheckbox.setText("only resolved ip");
        optionPanel.add(onlyIpCheckbox, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        onlyWebCheckbox = new JCheckBox();
        onlyWebCheckbox.setBackground(new Color(-12828863));
        onlyWebCheckbox.setText("only web apps");
        optionPanel.add(onlyWebCheckbox, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delCheckbox = new JCheckBox();
        delCheckbox.setBackground(new Color(-12828863));
        delCheckbox.setText("delete reports when exit");
        optionPanel.add(delCheckbox, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        configPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 5), null, null, 0, false));
        outputPanel = new JScrollPane();
        outputPanel.setBackground(new Color(-12828863));
        outputPanel.setForeground(new Color(-12828863));
        subdomainPanel.add(outputPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(800, 200), null, null, 0, false));
        outputPanel.setBorder(BorderFactory.createTitledBorder(null, "output", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputTextArea = new JTextArea();
        outputTextArea.setBackground(new Color(-12828863));
        outputTextArea.setEditable(false);
        Font outputTextAreaFont = this.$$$getFont$$$("Consolas", -1, -1, outputTextArea.getFont());
        if (outputTextAreaFont != null) outputTextArea.setFont(outputTextAreaFont);
        outputTextArea.setForeground(new Color(-16711895));
        outputTextArea.setLineWrap(true);
        outputPanel.setViewportView(outputTextArea);
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
        return subdomainPanel;
    }

}
