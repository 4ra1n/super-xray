package com.chaitin.xray.form;

import com.chaitin.xray.utils.StringUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvanceConfigForm {
    public JPanel advanceConfigPanel;
    private JPanel leftPanel;
    private JPanel midPanel;
    private JCheckBox detectCorsHeaderConfigCheckBox;
    private JCheckBox detectServerErrorPageCheckBox;
    private JCheckBox detectPhpinfoCheckBox;
    private JCheckBox detectSystemPathLeakCheckBox;
    private JCheckBox detectOutdatedSslVersionCheckBox;
    private JCheckBox detectHttpHeaderConfigCheckBox;
    private JCheckBox detectCookieHttponlyCheckBox;
    private JCheckBox detectChinaIdCardNumberCheckBox;
    private JCheckBox detectChinaPhoneNumberCheckBox;
    private JCheckBox detectChinaBankCardCheckBox;
    private JCheckBox detectPrivateIpCheckBox;
    private JButton refreshBaselineButton;
    private JButton saveBaselineButton;
    private JTextField usernameDicText;
    private JButton usernameButton;
    private JButton refreshBruteButton;
    private JButton saveBruteButton;
    private JTextField pwdText;
    private JButton pwdDicButton;
    private JTextField dirDepthText;
    private JTextField dirDicText;
    private JButton dirDicButton;
    private JButton dirRefreshButton;
    private JButton dirSaveButton;
    private JTextField shiroCookieText;
    private JTextField shiroAesKeyText;
    private JButton shiroRefreshButton;
    private JButton shiroSaveButton;
    private JCheckBox booleanBasedDetectionCheckBox;
    private JCheckBox errorBasedDetectionCheckBox;
    private JCheckBox timeBasedDetectionCheckBox;
    private JCheckBox useCommentInPayloadCheckBox;
    private JCheckBox detectSqliInCookieCheckBox;
    private JButton refreshSqlButton;
    private JButton saveSqlButton;
    private JTextField shiroKeyFileText;
    private JButton shiroKeyFileButton;
    private JCheckBox detectXssInCookieCheckBox;
    private JCheckBox ieFeatureCheckBox;
    private JButton xssRefreshButton;
    private JButton xssSaveButton;
    private JButton allDefaultButton;
    private JButton allRefreshButton;
    private JButton selectBaselineButton;
    private JButton selectXssButton;
    private JButton selectSqlButton;
    private JPanel baselinePanel;
    private JLabel corsLabel;
    private JLabel serverLabel;
    private JLabel phpinfoLabel;
    private JLabel leakLabel;
    private JLabel sslLabel;
    private JLabel headerLabel;
    private JLabel setCookieLabel;
    private JLabel idLabel;
    private JLabel phoneLabel;
    private JLabel bankLabel;
    private JLabel privateLabel;
    private JPanel sqldetPanel;
    private JLabel boolLabel;
    private JLabel errorLabel;
    private JLabel timeLabel;
    private JLabel commentLabel;
    private JLabel sqlCookieLabel;
    private JPanel configCenterPanel;
    private JLabel showLabel;
    private JPanel bruteForcePanel;
    private JPanel dirscanPanel;
    private JPanel shiroPanel;
    private JPanel xssPanel;
    private JTextField phantasmDepthText;
    private JTextArea phExcludeArea;
    private JButton phRefreshButton;
    private JButton phSaveButton;
    private JCheckBox detectThinkphpSqliCheckBox;
    private JButton thinkphpRefreshButton;
    private JButton thinkphpSaveButton;
    private JTextArea excludeArea;
    private JPanel thinkphpPanel;
    private JLabel thinkphpSqlLabel;
    private JLabel excludeLabel;
    private JLabel phExcludeLabel;
    private JPanel phantasmPanel;
    private JLabel xssCookieLabel;
    private JLabel xssIeLabel;
    private JLabel phDepthLabel;
    private JLabel dirDepthLabel;
    private JLabel shiroCookieLabel;
    private JLabel shiroAesKeyLabel;
    private JScrollPane phScroll;
    private JScrollPane dirScroll;
    private JButton applyAllButton;
    private JPanel rightPanel;
    private JPanel xstreamPanel;
    private JCheckBox useFullModeCheckBox;
    private JLabel useFullModeLabel;
    private JButton xstreamRefreshButton;
    private JButton xstreamSaveButton;

    private final List<JCheckBox> baselineCheckBoxList = new ArrayList<>();
    private final List<JCheckBox> sqlCheckBoxList = new ArrayList<>();
    private final List<JCheckBox> xssCheckBoxList = new ArrayList<>();
    private static boolean baselineAll = false;
    private static boolean sqlAll = false;
    private static boolean xssAll = false;

    private void init() {
        baselineCheckBoxList.add(detectCorsHeaderConfigCheckBox);
        baselineCheckBoxList.add(detectServerErrorPageCheckBox);
        baselineCheckBoxList.add(detectPhpinfoCheckBox);
        baselineCheckBoxList.add(detectSystemPathLeakCheckBox);
        baselineCheckBoxList.add(detectOutdatedSslVersionCheckBox);
        baselineCheckBoxList.add(detectHttpHeaderConfigCheckBox);
        baselineCheckBoxList.add(detectCookieHttponlyCheckBox);
        baselineCheckBoxList.add(detectChinaIdCardNumberCheckBox);
        baselineCheckBoxList.add(detectChinaPhoneNumberCheckBox);
        baselineCheckBoxList.add(detectChinaBankCardCheckBox);
        baselineCheckBoxList.add(detectPrivateIpCheckBox);

        sqlCheckBoxList.add(booleanBasedDetectionCheckBox);
        sqlCheckBoxList.add(errorBasedDetectionCheckBox);
        sqlCheckBoxList.add(timeBasedDetectionCheckBox);
        sqlCheckBoxList.add(useCommentInPayloadCheckBox);
        sqlCheckBoxList.add(detectSqliInCookieCheckBox);

        xssCheckBoxList.add(detectXssInCookieCheckBox);
        xssCheckBoxList.add(ieFeatureCheckBox);

        refreshAll();
    }

    private void refreshAll() {
        refreshBaseline();
        refreshThinkphp();
        refreshSql();
        refreshXss();
        refreshPhantasm();
        refreshDirScan();
        refreshBrute();
        refreshShiro();
    }


    private void initConfigCenter() {
        applyAllButton.addActionListener(e -> {
            applyBaseline();
            applyThinkphp();
            applySql();
            applyXss();
            applyPhantasm();
            applyDirScan();
            applyBruteForce();
            applyShiro();
            applyXStream();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
        allDefaultButton.addActionListener(e -> {
            MainForm.instance.reloadConfig(true, true);
            refreshAll();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
        allRefreshButton.addActionListener(e -> {
            refreshAll();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
        selectBaselineButton.addActionListener(e -> {
            if (baselineAll) {
                for (JCheckBox box : baselineCheckBoxList) {
                    box.setSelected(false);
                }
                baselineAll = false;
            } else {
                for (JCheckBox box : baselineCheckBoxList) {
                    box.setSelected(true);
                }
                baselineAll = true;
            }
        });
        selectSqlButton.addActionListener(e -> {
            if (sqlAll) {
                for (JCheckBox box : sqlCheckBoxList) {
                    box.setSelected(false);
                }
                sqlAll = false;
            } else {
                for (JCheckBox box : sqlCheckBoxList) {
                    box.setSelected(true);
                }
                sqlAll = true;
            }
        });
        selectXssButton.addActionListener(e -> {
            if (xssAll) {
                for (JCheckBox box : xssCheckBoxList) {
                    box.setSelected(false);
                }
                xssAll = false;
            } else {
                for (JCheckBox box : xssCheckBoxList) {
                    box.setSelected(true);
                }
                xssAll = true;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void refreshBaseline() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> baselineObj = (Map<String, Object>) plugins.get("baseline");
                detectCorsHeaderConfigCheckBox.setSelected((boolean) baselineObj.get("detect_cors_header_config"));
                detectServerErrorPageCheckBox.setSelected((boolean) baselineObj.get("detect_server_error_page"));
                detectPhpinfoCheckBox.setSelected((boolean) baselineObj.get("detect_phpinfo"));
                detectSystemPathLeakCheckBox.setSelected((boolean) baselineObj.get("detect_system_path_leak"));
                detectOutdatedSslVersionCheckBox.setSelected((boolean) baselineObj.get("detect_outdated_ssl_version"));
                detectHttpHeaderConfigCheckBox.setSelected((boolean) baselineObj.get("detect_http_header_config"));
                detectCookieHttponlyCheckBox.setSelected((boolean) baselineObj.get("detect_cookie_httponly"));
                detectChinaIdCardNumberCheckBox.setSelected((boolean) baselineObj.get("detect_china_id_card_number"));
                detectChinaPhoneNumberCheckBox.setSelected((boolean) baselineObj.get("detect_china_phone_number"));
                detectChinaBankCardCheckBox.setSelected((boolean) baselineObj.get("detect_china_bank_card"));
                detectPrivateIpCheckBox.setSelected((boolean) baselineObj.get("detect_private_ip"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshThinkphp() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("thinkphp");
                detectThinkphpSqliCheckBox.setSelected((boolean) obj.get("detect_thinkphp_sqli"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshSql() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("sqldet");
                booleanBasedDetectionCheckBox.setSelected((boolean) obj.get("boolean_based_detection"));
                errorBasedDetectionCheckBox.setSelected((boolean) obj.get("error_based_detection"));
                timeBasedDetectionCheckBox.setSelected((boolean) obj.get("time_based_detection"));
                useCommentInPayloadCheckBox.setSelected((boolean) obj.get("use_comment_in_payload"));
                detectSqliInCookieCheckBox.setSelected((boolean) obj.get("detect_sqli_in_cookie"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshXss() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("xss");
                detectXssInCookieCheckBox.setSelected((boolean) obj.get("detect_xss_in_cookie"));
                ieFeatureCheckBox.setSelected((boolean) obj.get("ie_feature"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshPhantasm() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("phantasm");
                int depth = (int) obj.get("depth");
                phantasmDepthText.setText(String.valueOf(depth));
                ArrayList<String> exArray = (ArrayList<String>) obj.get("exclude_poc");
                StringBuilder sb = new StringBuilder();
                for (String ex : exArray) {
                    sb.append(ex);
                    sb.append("\n");
                }
                phExcludeArea.setText(sb.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshBrute() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("brute-force");
                String userDic = (String) obj.get("username_dictionary");
                String pwdDic = (String) obj.get("password_dictionary");
                if (StringUtil.notEmpty(userDic)) {
                    usernameDicText.setText(userDic);
                }
                if (StringUtil.notEmpty(pwdDic)) {
                    pwdText.setText(pwdDic);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshDirScan() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("dirscan");
                int depth = (int) obj.get("depth");
                dirDepthText.setText(String.valueOf(depth));
                dirDicText.setText((String) obj.get("dictionary"));
                ArrayList<String> exArray = (ArrayList<String>) obj.get("exclude_dir");
                StringBuilder sb = new StringBuilder();
                for (String ex : exArray) {
                    sb.append(ex);
                    sb.append("\n");
                }
                excludeArea.setText(sb.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshShiro() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("shiro");
                String cookieName = (String) obj.get("cookie_name");
                shiroCookieText.setText(cookieName);
                ArrayList<String> aesKey = (ArrayList<String>) obj.get("ase_key");
                if (aesKey != null && aesKey.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String k : aesKey) {
                        sb.append(k);
                        sb.append(",");
                    }
                    String finalStr = sb.toString();
                    shiroAesKeyText.setText(finalStr.substring(0, finalStr.length() - 1));
                }
                String aesKeyFile = (String) obj.get("aes_key_file");
                shiroKeyFileText.setText(aesKeyFile);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void refreshXStream() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("xstream");
                useFullModeCheckBox.setSelected((boolean) obj.get("full"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void applyBaseline() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> baselineObj = (Map<String, Object>) plugins.get("baseline");
                baselineObj.put("detect_cors_header_config", detectCorsHeaderConfigCheckBox.isSelected());
                baselineObj.put("detect_server_error_page", detectServerErrorPageCheckBox.isSelected());
                baselineObj.put("detect_phpinfo", detectPhpinfoCheckBox.isSelected());
                baselineObj.put("detect_system_path_leak", detectSystemPathLeakCheckBox.isSelected());
                baselineObj.put("detect_outdated_ssl_version", detectOutdatedSslVersionCheckBox.isSelected());
                baselineObj.put("detect_http_header_config", detectHttpHeaderConfigCheckBox.isSelected());
                baselineObj.put("detect_cookie_httponly", detectCookieHttponlyCheckBox.isSelected());
                baselineObj.put("detect_china_id_card_number", detectChinaIdCardNumberCheckBox.isSelected());
                baselineObj.put("detect_china_phone_number", detectChinaPhoneNumberCheckBox.isSelected());
                baselineObj.put("detect_china_bank_card", detectChinaBankCardCheckBox.isSelected());
                baselineObj.put("detect_private_ip", detectPrivateIpCheckBox.isSelected());
            }
        }
        MainForm.instance.refreshConfig();
    }


    private void initBaseline() {
        refreshBaselineButton.addActionListener(e -> refreshBaseline());
        saveBaselineButton.addActionListener(e -> {
            applyBaseline();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyXStream() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("xstream");
                obj.put("full", useFullModeCheckBox.isSelected());
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initXStream() {
        xstreamRefreshButton.addActionListener(e -> refreshXStream());
        xstreamSaveButton.addActionListener(e -> {
            applyXStream();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyThinkphp() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("thinkphp");
                obj.put("detect_thinkphp_sqli", detectThinkphpSqliCheckBox.isSelected());
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initThinkphp() {
        thinkphpRefreshButton.addActionListener(e -> refreshThinkphp());
        thinkphpSaveButton.addActionListener(e -> {
            applyThinkphp();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applySql() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("sqldet");
                obj.put("boolean_based_detection", booleanBasedDetectionCheckBox.isSelected());
                obj.put("error_based_detection", errorBasedDetectionCheckBox.isSelected());
                obj.put("time_based_detection", timeBasedDetectionCheckBox.isSelected());
                obj.put("use_comment_in_payload", useCommentInPayloadCheckBox.isSelected());
                obj.put("detect_sqli_in_cookie", detectSqliInCookieCheckBox.isSelected());
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initSqldet() {
        refreshSqlButton.addActionListener(e -> refreshSql());
        saveSqlButton.addActionListener(e -> {
            applySql();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyXss() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("xss");
                obj.put("detect_xss_in_cookie", detectXssInCookieCheckBox.isSelected());
                obj.put("ie_feature", ieFeatureCheckBox.isSelected());
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initXss() {
        xssRefreshButton.addActionListener(e -> refreshXss());
        xssSaveButton.addActionListener(e -> {
            applyXss();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyPhantasm() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("phantasm");
                if (StringUtil.notEmpty(phantasmDepthText.getText().trim())) {
                    obj.put("depth", Integer.parseInt(phantasmDepthText.getText().trim()));
                }
                String phText = phExcludeArea.getText();
                String[] lines = phText.split("\n");
                ArrayList<String> list = new ArrayList<>();
                for (String line : lines) {
                    if (!StringUtil.notEmpty(line.trim())) {
                        continue;
                    }
                    if (line.endsWith("\r")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    list.add(line.trim());
                }
                obj.put("exclude_poc", list);
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initPhantasm() {
        phRefreshButton.addActionListener(e -> refreshPhantasm());
        phSaveButton.addActionListener(e -> {
            applyPhantasm();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyBruteForce() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("brute-force");
                if (StringUtil.notEmpty(usernameDicText.getText().trim())) {
                    obj.put("username_dictionary", usernameDicText.getText().trim());
                }
                if (StringUtil.notEmpty(pwdText.getText())) {
                    obj.put("password_dictionary", pwdText.getText().trim());
                }
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void getFile(JTextField field) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = fileChooser.showOpenDialog(new JFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String absPath = file.getAbsolutePath();
            field.setText(absPath);
        }
    }

    public void initBruteForce() {
        usernameButton.addActionListener(e -> getFile(usernameDicText));
        pwdDicButton.addActionListener(e -> getFile(pwdText));
        refreshBruteButton.addActionListener(e -> refreshBrute());
        saveBruteButton.addActionListener(e -> {
            applyBruteForce();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyDirScan() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("dirscan");
                obj.put("depth", Integer.parseInt(dirDepthText.getText()));
                if (StringUtil.notEmpty(dirDicText.getText().trim())) {
                    obj.put("dictionary", dirDicText.getText().trim());
                }
                String exText = excludeArea.getText();
                String[] lines = exText.split("\n");
                ArrayList<String> list = new ArrayList<>();
                for (String line : lines) {
                    if (!StringUtil.notEmpty(line.trim())) {
                        continue;
                    }
                    if (line.endsWith("\r")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    list.add(line.trim());
                }
                obj.put("exclude_dir", list);
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initDirScan() {
        dirDicButton.addActionListener(e -> getFile(dirDicText));
        dirRefreshButton.addActionListener(e -> refreshDirScan());
        dirSaveButton.addActionListener(e -> {
            applyDirScan();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void applyShiro() {
        for (Map.Entry<String, Object> entry : MainForm.configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                Map<String, Object> obj = (Map<String, Object>) plugins.get("shiro");
                obj.put("cookie_name", shiroCookieText.getText());
                ArrayList<String> keys = new ArrayList<>();
                String text = shiroAesKeyText.getText();
                String[] temp = text.split(",");
                for (String s : temp) {
                    if (StringUtil.notEmpty(s.trim())) {
                        keys.add(s.trim());
                    }
                }
                obj.put("aes_key", keys);
                obj.put("aes_key_file", shiroKeyFileText.getText());
            }
        }
        MainForm.instance.refreshConfig();
    }

    private void initShiro() {
        shiroKeyFileButton.addActionListener(e -> getFile(shiroKeyFileText));
        shiroRefreshButton.addActionListener(e -> refreshShiro());
        shiroSaveButton.addActionListener(e -> {
            applyShiro();
            if (MainForm.LANG == MainForm.CHINESE) {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "????????????");
            } else {
                JOptionPane.showMessageDialog(this.advanceConfigPanel, "Success");
            }
        });
    }

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            configCenterPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "????????????", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            applyAllButton.setText("????????????????????????");
            allDefaultButton.setText("????????????");
            selectBaselineButton.setText("baseline????????????");
            selectSqlButton.setText("sqldet????????????");
            refreshBaselineButton.setText("????????????");
            saveBaselineButton.setText("????????????");
            refreshSqlButton.setText("????????????");
            saveSqlButton.setText("????????????");
            usernameButton.setText("???????????????????????????");
            pwdDicButton.setText("????????????????????????");
            refreshBruteButton.setText("????????????");
            saveBruteButton.setText("????????????");
            dirDepthLabel.setText("?????????????????????1-10???");
            dirDicButton.setText("?????????????????????");
            excludeLabel.setText("<html> ?????????????????? " +
                    "<br> ???/admin???/system/key <br>" +
                    " ???????????????????????????????????? </html>");
            dirRefreshButton.setText("????????????");
            dirSaveButton.setText("????????????");
            xssRefreshButton.setText("????????????");
            xssSaveButton.setText("????????????");
            phDepthLabel.setText("?????????????????????1-10???");
            phExcludeLabel.setText("<html> ?????????????????? PoC <br>" +
                    " ?????????????????????poc-yaml-tomcat*??? <br>" +
                    " ???????????????????????????????????? </html>");
            phRefreshButton.setText("????????????");
            phSaveButton.setText("????????????");
            shiroCookieLabel.setText("??????Cookie??????");
            shiroAesKeyLabel.setText("??????AES Key");
            shiroKeyFileButton.setText("??????Key??????");
            shiroRefreshButton.setText("????????????");
            shiroSaveButton.setText("????????????");
            thinkphpSqlLabel.setText("?????? thinkphp ??? sql ??????");
            thinkphpRefreshButton.setText("????????????");
            thinkphpSaveButton.setText("????????????");
            showLabel.setText("<html> ????????????????????????????????? " +
                    "<br> ?????????????????? ?????? ???????????? ?????? </html>");
            xssCookieLabel.setText("???????????????????????? cookie ?????? xss");
            xssIeLabel.setText("????????????????????? ie ???????????? xss");
            boolLabel.setText("????????????????????????");
            errorLabel.setText("????????????????????????");
            timeLabel.setText("????????????????????????");
            commentLabel.setText("??? payload ????????? or (??????????????????????????????)");
            sqlCookieLabel.setText("??????????????? cookie ????????????");
            corsLabel.setText("?????? cors ????????????");
            serverLabel.setText("???????????????????????????");
            phpinfoLabel.setText("????????????????????????phpinfo");
            leakLabel.setText("??????????????????????????????????????????");
            sslLabel.setText("?????? ssl ????????????");
            headerLabel.setText("?????? http ???????????? header ????????????");
            setCookieLabel.setText("?????? set-cookie ??????????????? http only");
            idLabel.setText("????????????????????????????????????");
            phoneLabel.setText("????????????????????????????????????");
            bankLabel.setText("????????????????????????????????????");
            privateLabel.setText("?????????????????????????????? ip");
            allRefreshButton.setText("????????????");
            selectXssButton.setText("xss????????????");
            useFullModeLabel.setText("??????????????????????????????");
            xstreamRefreshButton.setText("????????????");
            xstreamSaveButton.setText("????????????");
        } else {
            configCenterPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Config Center", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            applyAllButton.setText("All Confirm");
            allDefaultButton.setText("Reset Default");
            selectBaselineButton.setText("baseline all");
            selectSqlButton.setText("sqldet all");
            refreshBaselineButton.setText("Refresh");
            saveBaselineButton.setText("Save");
            refreshSqlButton.setText("Refresh");
            saveSqlButton.setText("Save");
            usernameButton.setText("Username Dict");
            pwdDicButton.setText("Password Dict");
            refreshBruteButton.setText("Refresh");
            saveBruteButton.setText("Save");
            dirDepthLabel.setText("Depth (1-10)");
            dirDicButton.setText("Your Dict");
            excludeLabel.setText("<html> Exclude Dir " +
                    "<br> /admin or /system/key <br>" +
                    " one rule per line </html>");
            dirRefreshButton.setText("Refresh");
            dirSaveButton.setText("Save");
            xssRefreshButton.setText("Refresh");
            xssSaveButton.setText("Save");
            phDepthLabel.setText("Depth (1-10)");
            phExcludeLabel.setText("<html> Exclude PoC <br>" +
                    " poc-yaml-tomcat* <br>" +
                    " one rule per line </html>");
            phRefreshButton.setText("Refresh");
            phSaveButton.setText("Save");
            shiroCookieLabel.setText("Cookie Name");
            shiroAesKeyLabel.setText("AES Key");
            shiroKeyFileButton.setText("Key File");
            shiroRefreshButton.setText("Refresh");
            shiroSaveButton.setText("Save");
            thinkphpSqlLabel.setText("thinkphp sql injection");
            thinkphpRefreshButton.setText("Refresh");
            thinkphpSaveButton.setText("Save");
            showLabel.setText("<html> Advanced configuration does not activate plug-ins " +
                    "<br> Check the corresponding plug-in and click to confirm the plug-in takes effect </html>");
            xssCookieLabel.setText("detect xss in cookie");
            xssIeLabel.setText("detect xss in IE");
            boolLabel.setText("boolean based");
            errorLabel.setText("error based");
            timeLabel.setText("time based");
            commentLabel.setText("use or (dangerous!)");
            sqlCookieLabel.setText("check cookie injection");
            corsLabel.setText("check cors");
            serverLabel.setText("check error page");
            phpinfoLabel.setText("check phpinfo");
            leakLabel.setText("check path leak");
            sslLabel.setText("check ssl version");
            headerLabel.setText("check security http header");
            setCookieLabel.setText("check http-only cookie");
            idLabel.setText("check id number");
            phoneLabel.setText("check phone number");
            bankLabel.setText("check bank number");
            privateLabel.setText("check private ip");
            allRefreshButton.setText("Refresh All");
            selectXssButton.setText("xss all");
            useFullModeLabel.setText("use full mode");
            xstreamRefreshButton.setText("Refresh");
            xstreamSaveButton.setText("Save");
        }
    }

    public AdvanceConfigForm() {
        initLang();
        init();
        initConfigCenter();
        initBaseline();
        initThinkphp();
        initSqldet();
        initXss();
        initPhantasm();
        initBruteForce();
        initDirScan();
        initShiro();
        initXStream();
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
        advanceConfigPanel = new JPanel();
        advanceConfigPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        advanceConfigPanel.setBackground(new Color(-725535));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.setBackground(new Color(-725535));
        advanceConfigPanel.add(leftPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        baselinePanel = new JPanel();
        baselinePanel.setLayout(new GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), -1, -1));
        baselinePanel.setBackground(new Color(-725535));
        leftPanel.add(baselinePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        baselinePanel.setBorder(BorderFactory.createTitledBorder(null, "baseline", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        detectCorsHeaderConfigCheckBox = new JCheckBox();
        detectCorsHeaderConfigCheckBox.setBackground(new Color(-725535));
        detectCorsHeaderConfigCheckBox.setText("detect_cors_header_config");
        baselinePanel.add(detectCorsHeaderConfigCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        corsLabel = new JLabel();
        corsLabel.setText("?????? cors ????????????");
        baselinePanel.add(corsLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectServerErrorPageCheckBox = new JCheckBox();
        detectServerErrorPageCheckBox.setBackground(new Color(-725535));
        detectServerErrorPageCheckBox.setText("detect_server_error_page");
        baselinePanel.add(detectServerErrorPageCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectPhpinfoCheckBox = new JCheckBox();
        detectPhpinfoCheckBox.setBackground(new Color(-725535));
        detectPhpinfoCheckBox.setText("detect_phpinfo");
        baselinePanel.add(detectPhpinfoCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectSystemPathLeakCheckBox = new JCheckBox();
        detectSystemPathLeakCheckBox.setBackground(new Color(-725535));
        detectSystemPathLeakCheckBox.setText("detect_system_path_leak");
        baselinePanel.add(detectSystemPathLeakCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectOutdatedSslVersionCheckBox = new JCheckBox();
        detectOutdatedSslVersionCheckBox.setBackground(new Color(-725535));
        detectOutdatedSslVersionCheckBox.setText("detect_outdated_ssl_version");
        baselinePanel.add(detectOutdatedSslVersionCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectHttpHeaderConfigCheckBox = new JCheckBox();
        detectHttpHeaderConfigCheckBox.setBackground(new Color(-725535));
        detectHttpHeaderConfigCheckBox.setText("detect_http_header_config");
        baselinePanel.add(detectHttpHeaderConfigCheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectCookieHttponlyCheckBox = new JCheckBox();
        detectCookieHttponlyCheckBox.setBackground(new Color(-725535));
        detectCookieHttponlyCheckBox.setText("detect_cookie_httponly");
        baselinePanel.add(detectCookieHttponlyCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectChinaIdCardNumberCheckBox = new JCheckBox();
        detectChinaIdCardNumberCheckBox.setBackground(new Color(-725535));
        detectChinaIdCardNumberCheckBox.setText("detect_china_id_card_number");
        baselinePanel.add(detectChinaIdCardNumberCheckBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectChinaPhoneNumberCheckBox = new JCheckBox();
        detectChinaPhoneNumberCheckBox.setBackground(new Color(-725535));
        detectChinaPhoneNumberCheckBox.setText("detect_china_phone_number");
        baselinePanel.add(detectChinaPhoneNumberCheckBox, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectChinaBankCardCheckBox = new JCheckBox();
        detectChinaBankCardCheckBox.setBackground(new Color(-725535));
        detectChinaBankCardCheckBox.setText("detect_china_bank_card");
        baselinePanel.add(detectChinaBankCardCheckBox, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectPrivateIpCheckBox = new JCheckBox();
        detectPrivateIpCheckBox.setBackground(new Color(-725535));
        detectPrivateIpCheckBox.setText("detect_private_ip");
        baselinePanel.add(detectPrivateIpCheckBox, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        serverLabel = new JLabel();
        serverLabel.setText("???????????????????????????");
        baselinePanel.add(serverLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phpinfoLabel = new JLabel();
        phpinfoLabel.setText("????????????????????????phpinfo");
        baselinePanel.add(phpinfoLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leakLabel = new JLabel();
        leakLabel.setText("??????????????????????????????????????????");
        baselinePanel.add(leakLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sslLabel = new JLabel();
        sslLabel.setText("?????? ssl ????????????");
        baselinePanel.add(sslLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        headerLabel = new JLabel();
        headerLabel.setText("?????? http ???????????? header ????????????");
        baselinePanel.add(headerLabel, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setCookieLabel = new JLabel();
        setCookieLabel.setText("?????? set-cookie ??????????????? http only");
        baselinePanel.add(setCookieLabel, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idLabel = new JLabel();
        idLabel.setText("????????????????????????????????????");
        baselinePanel.add(idLabel, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phoneLabel = new JLabel();
        phoneLabel.setText("????????????????????????????????????");
        baselinePanel.add(phoneLabel, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankLabel = new JLabel();
        bankLabel.setText("????????????????????????????????????");
        baselinePanel.add(bankLabel, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        privateLabel = new JLabel();
        privateLabel.setText("?????????????????????????????? ip");
        baselinePanel.add(privateLabel, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshBaselineButton = new JButton();
        refreshBaselineButton.setText("????????????");
        baselinePanel.add(refreshBaselineButton, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveBaselineButton = new JButton();
        saveBaselineButton.setText("????????????");
        baselinePanel.add(saveBaselineButton, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqldetPanel = new JPanel();
        sqldetPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        sqldetPanel.setBackground(new Color(-725535));
        leftPanel.add(sqldetPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sqldetPanel.setBorder(BorderFactory.createTitledBorder(null, "sqldet", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        booleanBasedDetectionCheckBox = new JCheckBox();
        booleanBasedDetectionCheckBox.setBackground(new Color(-725535));
        booleanBasedDetectionCheckBox.setText("boolean_based_detection");
        sqldetPanel.add(booleanBasedDetectionCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boolLabel = new JLabel();
        boolLabel.setText("????????????????????????");
        sqldetPanel.add(boolLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorBasedDetectionCheckBox = new JCheckBox();
        errorBasedDetectionCheckBox.setBackground(new Color(-725535));
        errorBasedDetectionCheckBox.setText("error_based_detection");
        sqldetPanel.add(errorBasedDetectionCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeBasedDetectionCheckBox = new JCheckBox();
        timeBasedDetectionCheckBox.setBackground(new Color(-725535));
        timeBasedDetectionCheckBox.setText("time_based_detection");
        sqldetPanel.add(timeBasedDetectionCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useCommentInPayloadCheckBox = new JCheckBox();
        useCommentInPayloadCheckBox.setBackground(new Color(-725535));
        useCommentInPayloadCheckBox.setText("use_comment_in_payload");
        sqldetPanel.add(useCommentInPayloadCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detectSqliInCookieCheckBox = new JCheckBox();
        detectSqliInCookieCheckBox.setBackground(new Color(-725535));
        detectSqliInCookieCheckBox.setText("detect_sqli_in_cookie");
        sqldetPanel.add(detectSqliInCookieCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setText("????????????????????????");
        sqldetPanel.add(errorLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeLabel = new JLabel();
        timeLabel.setText("????????????????????????");
        sqldetPanel.add(timeLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentLabel = new JLabel();
        commentLabel.setText("??? payload ????????? or (??????????????????????????????)");
        sqldetPanel.add(commentLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqlCookieLabel = new JLabel();
        sqlCookieLabel.setText("??????????????? cookie ????????????");
        sqldetPanel.add(sqlCookieLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshSqlButton = new JButton();
        refreshSqlButton.setText("????????????");
        sqldetPanel.add(refreshSqlButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveSqlButton = new JButton();
        saveSqlButton.setText("????????????");
        sqldetPanel.add(saveSqlButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        midPanel.setBackground(new Color(-725535));
        advanceConfigPanel.add(midPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bruteForcePanel = new JPanel();
        bruteForcePanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        bruteForcePanel.setBackground(new Color(-725535));
        midPanel.add(bruteForcePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bruteForcePanel.setBorder(BorderFactory.createTitledBorder(null, "brute-force", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        usernameDicText = new JTextField();
        usernameDicText.setEditable(false);
        usernameDicText.setEnabled(false);
        bruteForcePanel.add(usernameDicText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        usernameButton = new JButton();
        usernameButton.setText("???????????????????????????");
        bruteForcePanel.add(usernameButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveBruteButton = new JButton();
        saveBruteButton.setText("????????????");
        bruteForcePanel.add(saveBruteButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshBruteButton = new JButton();
        refreshBruteButton.setText("????????????");
        bruteForcePanel.add(refreshBruteButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pwdText = new JTextField();
        pwdText.setEditable(false);
        pwdText.setEnabled(false);
        bruteForcePanel.add(pwdText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pwdDicButton = new JButton();
        pwdDicButton.setText("????????????????????????");
        bruteForcePanel.add(pwdDicButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirscanPanel = new JPanel();
        dirscanPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        dirscanPanel.setBackground(new Color(-725535));
        midPanel.add(dirscanPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dirscanPanel.setBorder(BorderFactory.createTitledBorder(null, "dirscan", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        dirDepthText = new JTextField();
        dirscanPanel.add(dirDepthText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dirDicText = new JTextField();
        dirDicText.setEditable(false);
        dirDicText.setEnabled(false);
        dirscanPanel.add(dirDicText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dirDicButton = new JButton();
        dirDicButton.setText("?????????????????????");
        dirscanPanel.add(dirDicButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirRefreshButton = new JButton();
        dirRefreshButton.setText("????????????");
        dirscanPanel.add(dirRefreshButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirSaveButton = new JButton();
        dirSaveButton.setText("????????????");
        dirscanPanel.add(dirSaveButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirDepthLabel = new JLabel();
        dirDepthLabel.setText("?????????????????????1-10???");
        dirscanPanel.add(dirDepthLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirScroll = new JScrollPane();
        dirscanPanel.add(dirScroll, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        excludeArea = new JTextArea();
        dirScroll.setViewportView(excludeArea);
        excludeLabel = new JLabel();
        excludeLabel.setText("<html>\n??????????????????\n<br>\n???/admin???/system/key\n<br>\n????????????????????????????????????\n</html>");
        dirscanPanel.add(excludeLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssPanel = new JPanel();
        xssPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        xssPanel.setBackground(new Color(-725535));
        midPanel.add(xssPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xssPanel.setBorder(BorderFactory.createTitledBorder(null, "xss", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        detectXssInCookieCheckBox = new JCheckBox();
        detectXssInCookieCheckBox.setBackground(new Color(-725535));
        detectXssInCookieCheckBox.setText("detect_xss_in_cookie");
        xssPanel.add(detectXssInCookieCheckBox, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssCookieLabel = new JLabel();
        xssCookieLabel.setText("???????????????????????? cookie ?????? xss");
        xssPanel.add(xssCookieLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ieFeatureCheckBox = new JCheckBox();
        ieFeatureCheckBox.setBackground(new Color(-725535));
        ieFeatureCheckBox.setText("ie_feature");
        xssPanel.add(ieFeatureCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssIeLabel = new JLabel();
        xssIeLabel.setText("????????????????????? ie ???????????? xss");
        xssPanel.add(xssIeLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssRefreshButton = new JButton();
        xssRefreshButton.setText("????????????");
        xssPanel.add(xssRefreshButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssSaveButton = new JButton();
        xssSaveButton.setText("????????????");
        xssPanel.add(xssSaveButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configCenterPanel = new JPanel();
        configCenterPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        configCenterPanel.setBackground(new Color(-725535));
        advanceConfigPanel.add(configCenterPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        configCenterPanel.setBorder(BorderFactory.createTitledBorder(null, "????????????", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        allDefaultButton = new JButton();
        allDefaultButton.setText("????????????");
        configCenterPanel.add(allDefaultButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allRefreshButton = new JButton();
        allRefreshButton.setText("????????????");
        configCenterPanel.add(allRefreshButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applyAllButton = new JButton();
        applyAllButton.setText("????????????????????????");
        configCenterPanel.add(applyAllButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectBaselineButton = new JButton();
        selectBaselineButton.setText("baseline????????????");
        configCenterPanel.add(selectBaselineButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectSqlButton = new JButton();
        selectSqlButton.setText("sqldet????????????");
        configCenterPanel.add(selectSqlButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectXssButton = new JButton();
        selectXssButton.setText("xss????????????");
        configCenterPanel.add(selectXssButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showLabel = new JLabel();
        showLabel.setText("<html>\n?????????????????????????????????\n<br>\n?????????????????? ?????? ???????????? ??????\n</html>");
        advanceConfigPanel.add(showLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightPanel.setBackground(new Color(-725535));
        advanceConfigPanel.add(rightPanel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        phantasmPanel = new JPanel();
        phantasmPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        phantasmPanel.setBackground(new Color(-725535));
        rightPanel.add(phantasmPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        phantasmPanel.setBorder(BorderFactory.createTitledBorder(null, "phantasm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        phantasmDepthText = new JTextField();
        phantasmPanel.add(phantasmDepthText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phRefreshButton = new JButton();
        phRefreshButton.setText("????????????");
        phantasmPanel.add(phRefreshButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phSaveButton = new JButton();
        phSaveButton.setText("????????????");
        phantasmPanel.add(phSaveButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phDepthLabel = new JLabel();
        phDepthLabel.setText("?????????????????????1-10???");
        phantasmPanel.add(phDepthLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phScroll = new JScrollPane();
        phantasmPanel.add(phScroll, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        phExcludeArea = new JTextArea();
        phScroll.setViewportView(phExcludeArea);
        phExcludeLabel = new JLabel();
        phExcludeLabel.setText("<html>\n?????????????????? PoC\n<br>\n?????????????????????poc-yaml-tomcat*???\n<br>\n????????????????????????????????????\n</html>");
        phantasmPanel.add(phExcludeLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroPanel = new JPanel();
        shiroPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        shiroPanel.setBackground(new Color(-725535));
        rightPanel.add(shiroPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        shiroPanel.setBorder(BorderFactory.createTitledBorder(null, "shiro", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        shiroCookieText = new JTextField();
        shiroPanel.add(shiroCookieText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        shiroAesKeyText = new JTextField();
        shiroPanel.add(shiroAesKeyText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        shiroRefreshButton = new JButton();
        shiroRefreshButton.setText("????????????");
        shiroPanel.add(shiroRefreshButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroSaveButton = new JButton();
        shiroSaveButton.setText("????????????");
        shiroPanel.add(shiroSaveButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroKeyFileText = new JTextField();
        shiroKeyFileText.setEditable(false);
        shiroKeyFileText.setEnabled(false);
        shiroPanel.add(shiroKeyFileText, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        shiroKeyFileButton = new JButton();
        shiroKeyFileButton.setText("??????Key??????");
        shiroPanel.add(shiroKeyFileButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroCookieLabel = new JLabel();
        shiroCookieLabel.setText("??????Cookie??????");
        shiroPanel.add(shiroCookieLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroAesKeyLabel = new JLabel();
        shiroAesKeyLabel.setText("??????AES Key");
        shiroPanel.add(shiroAesKeyLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpPanel = new JPanel();
        thinkphpPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        thinkphpPanel.setBackground(new Color(-725535));
        rightPanel.add(thinkphpPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        thinkphpPanel.setBorder(BorderFactory.createTitledBorder(null, "thinkphp", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        detectThinkphpSqliCheckBox = new JCheckBox();
        detectThinkphpSqliCheckBox.setBackground(new Color(-725535));
        detectThinkphpSqliCheckBox.setText("detect_thinkphp_sqli");
        thinkphpPanel.add(detectThinkphpSqliCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpSqlLabel = new JLabel();
        thinkphpSqlLabel.setText("?????? thinkphp ??? sql ??????");
        thinkphpPanel.add(thinkphpSqlLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpRefreshButton = new JButton();
        thinkphpRefreshButton.setText("????????????");
        thinkphpPanel.add(thinkphpRefreshButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpSaveButton = new JButton();
        thinkphpSaveButton.setText("????????????");
        thinkphpPanel.add(thinkphpSaveButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xstreamPanel = new JPanel();
        xstreamPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        xstreamPanel.setBackground(new Color(-528927));
        rightPanel.add(xstreamPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xstreamPanel.setBorder(BorderFactory.createTitledBorder(null, "xstream", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        useFullModeCheckBox = new JCheckBox();
        useFullModeCheckBox.setBackground(new Color(-528927));
        useFullModeCheckBox.setText("use full mode");
        xstreamPanel.add(useFullModeCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useFullModeLabel = new JLabel();
        useFullModeLabel.setText("??????????????????????????????");
        xstreamPanel.add(useFullModeLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xstreamRefreshButton = new JButton();
        xstreamRefreshButton.setText("????????????");
        xstreamPanel.add(xstreamRefreshButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xstreamSaveButton = new JButton();
        xstreamSaveButton.setText("????????????");
        xstreamPanel.add(xstreamSaveButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return advanceConfigPanel;
    }

}
