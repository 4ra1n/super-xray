package com.chaitin.xray.form;

import com.chaitin.xray.utils.CmdUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncodeUtilForm {
    private JTextField baseEncodeText;
    private JButton baseEncodeButton;
    private JTextField baseDecodeText;
    private JButton baseDecodeButton;
    private JTextField urlEncodeText;
    private JButton urlEncodeButton;
    private JTextField urlDecodeText;
    private JButton urlDecodeButton;
    private JTextField md5EncodeText;
    private JButton md5EncodeButton;
    private JTextField md5ResultText;
    private JTextField cmdBashEncodeText;
    private JButton cmdBashEncodeButton;
    private JTextField cmdBashResultText;
    private JTextField cmdPwEncodeText;
    private JButton cmdPwEncodeButton;
    private JTextField cmdPwResultText;
    public JPanel encodeUtilPanel;
    private JPanel basePanel;
    private JPanel urlPanel;
    private JPanel md5Panel;
    private JPanel cmdBashPanel;
    private JLabel bashLabel;
    private JPanel cmdPwPanel;
    private JLabel pwLabel;
    private JTextField stringCmdText;
    private JButton stringCmdButton;
    private JTextField stringCmdResultText;
    private JPanel stringCmdPanel;
    private JLabel stringCmdLabel;

    private void initBase64() {
        baseEncodeButton.addActionListener(e -> {
            String source = baseEncodeText.getText();
            String result = Base64.getEncoder().encodeToString(source.getBytes());
            baseDecodeText.setText(result);
        });
        baseDecodeButton.addActionListener(e -> {
            String source = baseDecodeText.getText();
            String result = new String(Base64.getDecoder().decode(source));
            baseEncodeText.setText(result);
        });
    }

    private void initUrl() {
        urlEncodeButton.addActionListener(e -> {
            String source = urlEncodeText.getText();
            String result;
            try {
                result = URLEncoder.encode(source, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            urlDecodeText.setText(result);
        });
        urlDecodeButton.addActionListener(e -> {
            String source = urlDecodeText.getText();
            String result;
            try {
                result = URLDecoder.decode(source, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            urlEncodeText.setText(result);
        });
    }

    private void initMd5() {
        md5EncodeButton.addActionListener(e -> {
            String str = md5EncodeText.getText();
            byte[] digest = null;
            try {
                MessageDigest md5 = MessageDigest.getInstance("md5");
                digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            String md5Str = new BigInteger(1,
                    digest != null ? digest : new byte[0]).toString(16);
            md5ResultText.setText(md5Str);
        });
    }

    private void initBash() {
        cmdBashEncodeButton.addActionListener(e -> {
            String source = cmdBashEncodeText.getText();
            cmdBashResultText.setText(CmdUtil.getBashCommand(source));
        });
    }

    private void initPowershell() {
        cmdPwEncodeButton.addActionListener(e -> {
            String source = cmdPwEncodeText.getText();
            cmdPwResultText.setText(CmdUtil.getPowershellCommand(source));
        });
    }

    private void initStringCmd() {
        stringCmdButton.addActionListener(e -> {
            String source = stringCmdText.getText();
            stringCmdResultText.setText(CmdUtil.getStringCommand(source));
        });
    }

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            bashLabel.setText("  解决 Java 执行命令无法使用重定向和管道符号问题");
            pwLabel.setText("  解决 Java 执行命令无法使用重定向和管道符号问题");
            stringCmdLabel.setText("用于生成可以绕WAF的Java命令");
            stringCmdPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "   特殊命令字符串生成", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
        } else {
            bashLabel.setText("  Java commands cannot use redirection and pipeline symbols");
            pwLabel.setText("  Java commands cannot use redirection and pipeline symbols");
            stringCmdLabel.setText("   Amazing encode");
            stringCmdPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Amazing Encode", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
        }
    }

    public EncodeUtilForm() {
        initLang();
        initBase64();
        initUrl();
        initMd5();
        initBash();
        initPowershell();
        initStringCmd();
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
        encodeUtilPanel = new JPanel();
        encodeUtilPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        encodeUtilPanel.setBackground(new Color(-725535));
        basePanel = new JPanel();
        basePanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePanel.setBackground(new Color(-725535));
        encodeUtilPanel.add(basePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        basePanel.setBorder(BorderFactory.createTitledBorder(null, "Base64", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        baseEncodeText = new JTextField();
        basePanel.add(baseEncodeText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        baseEncodeButton = new JButton();
        baseEncodeButton.setText("编码");
        basePanel.add(baseEncodeButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        baseDecodeText = new JTextField();
        basePanel.add(baseDecodeText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        baseDecodeButton = new JButton();
        baseDecodeButton.setText("解码");
        basePanel.add(baseDecodeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlPanel = new JPanel();
        urlPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        urlPanel.setBackground(new Color(-725535));
        encodeUtilPanel.add(urlPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        urlPanel.setBorder(BorderFactory.createTitledBorder(null, "URL", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        urlEncodeText = new JTextField();
        urlPanel.add(urlEncodeText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        urlEncodeButton = new JButton();
        urlEncodeButton.setText("编码");
        urlPanel.add(urlEncodeButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlDecodeText = new JTextField();
        urlPanel.add(urlDecodeText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        urlDecodeButton = new JButton();
        urlDecodeButton.setText("解码");
        urlPanel.add(urlDecodeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        md5Panel = new JPanel();
        md5Panel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        md5Panel.setBackground(new Color(-725535));
        encodeUtilPanel.add(md5Panel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        md5Panel.setBorder(BorderFactory.createTitledBorder(null, "MD5", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        md5EncodeText = new JTextField();
        md5Panel.add(md5EncodeText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        md5EncodeButton = new JButton();
        md5EncodeButton.setText("加密");
        md5Panel.add(md5EncodeButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        md5ResultText = new JTextField();
        md5Panel.add(md5ResultText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        cmdBashPanel = new JPanel();
        cmdBashPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        cmdBashPanel.setBackground(new Color(-725535));
        encodeUtilPanel.add(cmdBashPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cmdBashPanel.setBorder(BorderFactory.createTitledBorder(null, "Bash Base64 CMD", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        cmdBashEncodeText = new JTextField();
        cmdBashPanel.add(cmdBashEncodeText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        cmdBashEncodeButton = new JButton();
        cmdBashEncodeButton.setText("生成命令");
        cmdBashPanel.add(cmdBashEncodeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmdBashResultText = new JTextField();
        cmdBashResultText.setText("");
        cmdBashPanel.add(cmdBashResultText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        bashLabel = new JLabel();
        bashLabel.setEnabled(true);
        bashLabel.setText("  解决 Java 执行命令无法使用重定向和管道符号问题");
        cmdBashPanel.add(bashLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmdPwPanel = new JPanel();
        cmdPwPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        cmdPwPanel.setBackground(new Color(-725535));
        encodeUtilPanel.add(cmdPwPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cmdPwPanel.setBorder(BorderFactory.createTitledBorder(null, "Powershell Base64 CMD", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        cmdPwEncodeText = new JTextField();
        cmdPwPanel.add(cmdPwEncodeText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        cmdPwEncodeButton = new JButton();
        cmdPwEncodeButton.setText("生成命令");
        cmdPwPanel.add(cmdPwEncodeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmdPwResultText = new JTextField();
        cmdPwPanel.add(cmdPwResultText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        pwLabel = new JLabel();
        pwLabel.setText("  解决 Java 执行命令无法使用重定向和管道符号问题");
        cmdPwPanel.add(pwLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stringCmdPanel = new JPanel();
        stringCmdPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        stringCmdPanel.setBackground(new Color(-725535));
        encodeUtilPanel.add(stringCmdPanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stringCmdPanel.setBorder(BorderFactory.createTitledBorder(null, "特殊命令字符串生成", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        stringCmdText = new JTextField();
        stringCmdPanel.add(stringCmdText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        stringCmdButton = new JButton();
        stringCmdButton.setText("生成命令");
        stringCmdPanel.add(stringCmdButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stringCmdResultText = new JTextField();
        stringCmdPanel.add(stringCmdResultText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), new Dimension(500, -1), new Dimension(500, -1), 0, false));
        stringCmdLabel = new JLabel();
        stringCmdLabel.setText("用于生成可以绕WAF的Java命令");
        stringCmdPanel.add(stringCmdLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return encodeUtilPanel;
    }

}
