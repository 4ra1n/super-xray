package com.chaitin.xray.form;

import com.chaitin.xray.model.Const;
import com.chaitin.xray.model.DB;
import com.chaitin.xray.model.Poc;
import com.chaitin.xray.model.XrayCmd;
import com.chaitin.xray.utils.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class MainForm {
    private static final Logger logger = LogManager.getLogger(MainForm.class);

    public static MainForm instance;
    private XrayCmd xrayCmd;
    public static String configStr;
    public static String configTemplate;
    public static String configPath;
    public static Map<String, Object> configObj;
    private static ArrayList<JCheckBox> checkBoxList;
    private static boolean pluginAll = false;

    private static String outputFilePath;
    private JButton choseDirButton;
    private JPanel SuperXray;
    private JPanel showPathPanel;
    private JPanel pathButtonPanel;
    private JLabel xrayPathLabel;
    private JTextField xrayPathTextField;
    private JLabel noteLabel;
    private JPanel configPanel;
    private JPanel bottomPanel;
    private JLabel authorLabel;
    private JPanel leftConfigPanel;
    private JPanel rightConfigPanel;
    private JPanel catConfigPanel;
    private JLabel catConfigLabel;
    private JLabel choseXrayLabel;
    private JPanel loadXrayPanel;
    private JCheckBox bruteForceCheckBox;
    private JCheckBox baselineCheckBox;
    private JCheckBox cmdInjectionCheckBox;
    private JCheckBox crlfInjectionCheckBox;
    private JCheckBox dirscanCheckBox;
    private JCheckBox fastjsonCheckBox;
    private JCheckBox jsonpCheckBox;
    private JCheckBox pathTraversalCheckBox;
    private JCheckBox phantasmCheckBox;
    private JCheckBox redirectCheckBox;
    private JCheckBox shiroCheckBox;
    private JCheckBox sqldetCheckBox;
    private JCheckBox ssrfCheckBox;
    private JCheckBox strutsCheckBox;
    private JCheckBox thinkphpCheckBox;
    private JCheckBox uploadCheckBox;
    private JCheckBox xxeCheckBox;
    private JCheckBox xssCheckBox;
    private JButton enableAllButton;
    private JButton advanceButton;
    private JButton reverseConfigButton;
    private JButton pocButton;
    private JButton activeScanButton;
    private JButton mitmScanButton;
    private JButton outputConfigButton;
    private JButton proxyConfigButton;
    private JPanel outputConfigPanel;
    private JScrollPane outputPanel;
    private JTextArea outputTextArea;
    private JPanel scanOutConfigPanel;
    private JRadioButton htmlRadioButton;
    private JRadioButton cliRadioButton;
    private JRadioButton jsonRadioButton;
    private JButton urlFileButton;
    private JTextField urlFileField;
    private JTextField rawFileField;
    private JButton rawFileButton;
    private JTextField urlField;
    private JButton urlButton;
    private JButton lookupConfigButton;
    private JButton lookupCmdButton;
    private JButton confirmPluginButton;
    private JPanel pocPanel;
    private JButton allPoCButton;
    private JTextField usePoCText;
    private JPanel scanTargetPanel;
    private JPanel proxyConfigPanel;
    private JPanel reverseConfigPanel;
    private JPanel startScanPanel;
    private JPanel otherPanel;
    private JPanel midConfigPanel;
    private JLabel pocNumLabel;
    private JTextField portText;
    private JLabel portLabel;
    private JPanel mitmPanel;
    private JTextField proxyText;
    private JLabel proxyLabel;
    private JPanel proxyPanel;
    private JTextField dnsText;
    private JTextField httpReverseText;
    private JPanel reverseUrlPanel;
    private JLabel httpReverseLabel;
    private JLabel dnsReverseLabel;
    private JButton stopButton;
    private JButton resetConfigButton;
    private JButton openResultButton;
    private JLabel stopLabel;
    private JLabel openResultLabel;
    private JLabel resetConfigLabel;
    private JPanel resetPanel;
    private JPanel openResultPanel;
    private JPanel stopPanel;
    private JPanel otherButton;
    private JPanel utilPanel;
    private JButton httpUtilButton;
    private JButton encodeUtilButton;
    private JButton listenUtilButton;
    private JButton updatePocButton;
    private JTextField localPoCText;
    private JButton localPoCButton;

    public void init() {
        logger.info("init main form");
        checkBoxList = new ArrayList<>();
        xrayCmd = new XrayCmd();

        reloadConfig(true);

        logger.info("init look up config button");
        lookupConfigButton.addActionListener(e -> {
            JFrame frame = new JFrame("查看配置文件");
            frame.setContentPane(new LookupConfigForm().lookupConfigPanel);
            frame.pack();
            frame.setVisible(true);
        });

        logger.info("init look up cmd button");
        lookupCmdButton.addActionListener(e ->
                JOptionPane.showMessageDialog(null, xrayCmd.buildCmd()));
    }

    @SuppressWarnings("unchecked")
    public void reloadConfig(boolean init) {
        if (init) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
            configStr = IOUtil.readStringFromIs(is);
            configTemplate = configStr;
        }

        Yaml yaml = new Yaml();
        configObj = yaml.load(configStr);

        try {
            if (StringUtil.notEmpty(configPath)) {
                Files.write(Paths.get(configPath),
                        configStr.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        for (Map.Entry<String, Object> entry : configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> plugin : plugins.entrySet()) {
                    if (plugin.getKey().equals("baseline")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        baselineCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("brute-force")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        bruteForceCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("cmd-injection")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        cmdInjectionCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("crlf-injection")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        crlfInjectionCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("dirscan")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        dirscanCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("fastjson")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        fastjsonCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("jsonp")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        jsonpCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("path-traversal")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        pathTraversalCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("phantasm")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        phantasmCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("redirect")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        redirectCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("shiro")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        shiroCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("sqldet")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        sqldetCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("ssrf")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        ssrfCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("struts")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        strutsCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("thinkphp")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        thinkphpCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("upload")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        uploadCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("xss")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        xssCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                    if (plugin.getKey().equals("xxe")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        xxeCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                }
            }
        }
    }

    private volatile boolean stop = false;

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
                        if (stop) {
                            logger.info(String.format("stop pid: %d", process.pid()));
                            try {
                                if (!OSUtil.isWindows()) {
                                    new ProcessBuilder("kill", "-9",
                                            Long.toString(process.pid())).start();
                                } else {
                                    new ProcessBuilder("cmd.exe", "/c",
                                            String.format("taskkill /f /pid %d", process.pid())).start();
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

    public void initLoadXray() {
        logger.info("init load xray module");
        choseDirButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                logger.info(String.format("user chose file: %s", absPath));
                String targetDir = Paths.get(absPath).toFile().getParent() + File.separator;
                XrayUtil.rmAllConfig(targetDir);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> XrayUtil.rmAllConfig(targetDir)));

                xrayPathTextField.setText(absPath);
                if (!OSUtil.isWindows()) {
                    ExecUtil.chmod(absPath);
                }

                String[] cmd = new String[]{absPath};
                Thread t = new Thread(() -> ExecUtil.execCmdNoRet(cmd));
                t.start();
                if (OSUtil.isMacOS()) {
                    JOptionPane.showMessageDialog(null, Const.MacNeedAgree);
                }

                JOptionPane.showMessageDialog(null, "请等待初始化");
                try {
                    Thread.sleep(1000);
                    t.interrupt();
                } catch (Exception ex) {
                    logger.error(ex);
                }
                XrayUtil.cpAllConfig(targetDir);

                try {
                    Path configPathPath = Paths.get(targetDir + Const.ConfigYaml);
                    configPath = configPathPath.toFile().getAbsolutePath();
                    Files.write(configPathPath,
                            configStr.getBytes(StandardCharsets.UTF_8));
                } catch (Exception ex) {
                    logger.error(ex);
                }

                xrayCmd.setXray(absPath);

                stop = false;
                execAndFresh(cmd);

                // todo: global
                DB data = new DB();
                data.setLastXrayPath(absPath);

                try{
                    Files.write(Paths.get("super-xray.db"), data.getDB().getBytes());
                }catch (Exception ex){
                    logger.error(ex);
                }

            } else {
                xrayPathTextField.setText("你取消了选择");
            }
        });
    }

    public void initPluginCheckBox() {
        logger.info("init all plugins");
        pluginAll = true;
        checkBoxList.add(bruteForceCheckBox);
        checkBoxList.add(baselineCheckBox);
        checkBoxList.add(cmdInjectionCheckBox);
        checkBoxList.add(jsonpCheckBox);
        checkBoxList.add(phantasmCheckBox);
        checkBoxList.add(shiroCheckBox);
        checkBoxList.add(ssrfCheckBox);
        checkBoxList.add(thinkphpCheckBox);
        checkBoxList.add(xssCheckBox);
        checkBoxList.add(crlfInjectionCheckBox);
        checkBoxList.add(dirscanCheckBox);
        checkBoxList.add(fastjsonCheckBox);
        checkBoxList.add(pathTraversalCheckBox);
        checkBoxList.add(redirectCheckBox);
        checkBoxList.add(sqldetCheckBox);
        checkBoxList.add(strutsCheckBox);
        checkBoxList.add(uploadCheckBox);
        checkBoxList.add(xxeCheckBox);
        enableAllButton.addActionListener(e -> {
            if (!pluginAll) {
                for (JCheckBox checkBox : checkBoxList) {
                    checkBox.setSelected(true);
                }
                pluginAll = true;
            } else {
                for (JCheckBox checkBox : checkBoxList) {
                    checkBox.setSelected(false);
                }
                pluginAll = false;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void initPluginSave() {
        confirmPluginButton.addActionListener(e -> {
            for (Map.Entry<String, Object> entry : configObj.entrySet()) {
                if (entry.getKey().equals("plugins")) {
                    Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                    for (Map.Entry<String, Object> plugin : plugins.entrySet()) {
                        if (plugin.getKey().equals("baseline")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (baselineCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("brute-force")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (bruteForceCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("cmd-injection")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (cmdInjectionCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("crlf-injection")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (crlfInjectionCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("dirscan")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (dirscanCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("fastjson")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (fastjsonCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("jsonp")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (jsonpCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("path-traversal")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (pathTraversalCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("phantasm")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (phantasmCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("redirect")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (redirectCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("shiro")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (shiroCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("sqldet")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (sqldetCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("ssrf")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (ssrfCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("struts")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (strutsCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("thinkphp")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (thinkphpCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("upload")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (uploadCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("xss")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (xssCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                        if (plugin.getKey().equals("xxe")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (xxeCheckBox.isSelected()) {
                                items.put("enabled", true);
                            } else {
                                items.put("enabled", false);
                            }
                        }
                    }
                }
            }
            refreshConfig();
            xrayCmd.setPoc(null);
            JOptionPane.showMessageDialog(null, "设置完成");
        });
    }

    public void refreshConfig() {
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(configObj, writer);
        configStr = writer.toString();
        try {
            Files.write(Paths.get(configPath),
                    configStr.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    public void initAdvanceConfig() {
        logger.info("init advance config button");
        advanceButton.addActionListener(e -> {
            JFrame frame = new JFrame("高级配置");
            frame.setContentPane(new AdvanceConfigForm().advanceConfigPanel);
            frame.setResizable(true);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initOutputConfig() {
        logger.info("init output config module");
        htmlRadioButton.setSelected(true);
        jsonRadioButton.setSelected(false);
        cliRadioButton.setSelected(false);
        outputConfigButton.addActionListener(e -> {
            refreshOutput();
            JOptionPane.showMessageDialog(null, "设置输出成功");
        });
    }

    public void refreshOutput() {
        String uuid = UUID.randomUUID().toString();
        if (htmlRadioButton.isSelected()) {
            outputFilePath = Paths.get(String.format("./xray-%s.html", uuid)).toFile().getAbsolutePath();
            xrayCmd.setOutputPrefix("--html-output");
            xrayCmd.setOutput(outputFilePath);
        }
        if (jsonRadioButton.isSelected()) {
            outputFilePath = Paths.get(String.format("./xray-%s.txt", uuid)).toFile().getAbsolutePath();
            xrayCmd.setOutputPrefix("--json-output");
            xrayCmd.setOutput(outputFilePath);
        }
        if (cliRadioButton.isSelected()) {
            xrayCmd.setOutput(null);
        }
    }

    public void initTargetUrlConfig() {
        urlButton.addActionListener(e -> {
            urlFileField.setText(null);
            rawFileField.setText(null);
            String url = urlField.getText();
            logger.info(String.format("target url: %s", url));
            xrayCmd.setInputPrefix("--url");
            xrayCmd.setInput(url);
            JOptionPane.showMessageDialog(null, "设置URL成功");
        });
    }

    public void initUrlFileConfig() {
        logger.info("init url file module");
        urlFileButton.addActionListener(e -> {
            urlField.setText(null);
            rawFileField.setText(null);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                urlFileField.setText(absPath);
                xrayCmd.setInputPrefix("--url-file");
                xrayCmd.setInput(String.format("%s", absPath));
            } else {
                rawFileField.setText("你取消了选择");
            }
        });
    }

    public void initRawScanConfig() {
        logger.info("init raw scan module");
        rawFileButton.addActionListener(e -> {
            urlField.setText(null);
            urlFileField.setText(null);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                rawFileField.setText(absPath);
                xrayCmd.setInputPrefix("--raw-request");
                xrayCmd.setInput(String.format("%s", absPath));
            } else {
                rawFileField.setText("你取消了选择");
            }
        });
    }

    public void initActiveScan() {
        logger.info("init active scan module");
        activeScanButton.addActionListener(e -> {
            try {
                refreshOutput();
                xrayCmd.setModule("webscan");
                xrayCmd.setConfig(String.format("%s", configPath));
                xrayCmd.setOthers(null);
                String[] finalCmd = xrayCmd.buildCmd();
                outputTextArea.setText(null);
                stop = false;
                execAndFresh(finalCmd);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void initAllPoC() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("poc.list");
        String allPocStr = IOUtil.readStringFromIs(is);
        assert allPocStr != null;
        String[] temp = allPocStr.split("\n");
        Poc.addPoc(temp);
        pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
        allPoCButton.addActionListener(e -> {
            JFrame frame = new JFrame("查看所有PoC");
            frame.setContentPane(new AllPoCForm().searchPoC);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    @SuppressWarnings("unchecked")
    private void onlyUsePhantasm(String poc) {
        xrayCmd.setPoc(String.format("%s", poc));

        for (JCheckBox box : checkBoxList) {
            box.setSelected(false);
        }
        phantasmCheckBox.setSelected(true);

        for (Map.Entry<String, Object> entry : configObj.entrySet()) {
            if (entry.getKey().equals("plugins")) {
                Map<String, Object> plugins = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> plugin : plugins.entrySet()) {
                    Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                    if (plugin.getKey().equals("phantasm")) {
                        items.put("enabled", true);
                    } else {
                        items.put("enabled", false);
                    }
                }
            }
        }

        refreshConfig();
    }

    public void initTargetPoC() {
        localPoCButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                String[] temp = absPath.split("\\.");
                String suffix = temp[temp.length - 1].toLowerCase().trim();
                if (!suffix.equals("yml") && !suffix.equals("yaml")) {
                    JOptionPane.showMessageDialog(null, "你选择的不是合法YAML文件");
                    return;
                }
                localPoCText.setText(absPath);
                onlyUsePhantasm(absPath);
                JOptionPane.showMessageDialog(null, "设置PoC成功");
            }
        });

        updatePocButton.addActionListener(e -> {
            String[] cmd = new String[]{xrayCmd.getXray(), "ws", "--list"};
            InputStream is = Objects.requireNonNull(ExecUtil.exec(cmd)).getInputStream();
            stop = false;
            execAndFresh(cmd);
            List<String> poc = new ArrayList<>();
            String data = IOUtil.readStringFromIs(is);
            assert data != null;
            String[] temp = data.split("\n");
            for (String s : temp) {
                s = s.trim();
                if (s.endsWith("\r")) {
                    s = s.substring(0, s.length() - 1);
                }
                if (s.contains(":")) {
                    continue;
                }
                if (s.contains("test")) {
                    continue;
                }
                if (!s.contains("poc")) {
                    continue;
                }
                s = s.substring(2);
                poc.add(s.trim());
            }
            Poc.clear();
            Poc.addAll(poc);
            pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
        });

        pocButton.addActionListener(e -> {
            String poc = usePoCText.getText();
            logger.info(poc);
            if (!Poc.getPocList().contains(poc.trim())) {
                JOptionPane.showMessageDialog(null, "PoC不存在");
                return;
            }
            onlyUsePhantasm(poc);
            JOptionPane.showMessageDialog(null, "设置PoC成功");
        });
    }

    private static boolean mitmRunning = false;

    public void initMitmScan() {
        mitmScanButton.addActionListener(e -> {
            if (!mitmRunning) {
                String port = portText.getText();
                xrayCmd.setModule("webscan");
                xrayCmd.setConfig(String.format("%s", configPath));
                xrayCmd.setInput(null);
                xrayCmd.setOthersPrefix("--listen");
                xrayCmd.setOthers("127.0.0.1:" + port);
                String[] cmd = xrayCmd.buildCmd();
                stop = false;
                execAndFresh(cmd);
                mitmScanButton.setText("关闭被动监听");
                portText.setEnabled(false);
                mitmRunning = true;
            } else {
                stop = true;
                portText.setEnabled(true);
                mitmScanButton.setText("开启被动扫描");
                mitmRunning = false;
                outputTextArea.setText(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void initHttpProxy() {
        proxyConfigButton.addActionListener(e -> {
            String httpProxy = proxyText.getText();
            for (Map.Entry<String, Object> entry : configObj.entrySet()) {
                if (entry.getKey().equals("http")) {
                    Map<String, Object> httpModule = (Map<String, Object>) entry.getValue();
                    httpModule.put("proxy", httpProxy);
                }
            }
            refreshConfig();
            JOptionPane.showMessageDialog(null, "设置代理成功");
        });
    }

    @SuppressWarnings("unchecked")
    public void initReverse() {
        reverseConfigButton.addActionListener(e -> {
            String http = httpReverseText.getText();
            String dns = dnsText.getText();
            for (Map.Entry<String, Object> entry : configObj.entrySet()) {
                if (entry.getKey().equals("reverse")) {
                    Map<String, Object> reverse = (Map<String, Object>) entry.getValue();
                    Map<String, Object> client = (Map<String, Object>) reverse.get("client");
                    client.put("reverse_server", true);
                    client.put("http_base_url", http);
                    client.put("dns_server_ip", dns);
                }
            }
            refreshConfig();
            JOptionPane.showMessageDialog(null, "设置反连成功");
        });
    }

    public void initForcedStop() {
        stopButton.addActionListener(e -> {
            stop = true;
            outputTextArea.setText(null);
            JOptionPane.showMessageDialog(null, "已强制停止");
        });
    }

    public void initOpenOutput() {
        openResultButton.addActionListener(e -> {
            if (StringUtil.notEmpty(outputFilePath.trim())) {
                if (Files.exists(Paths.get(outputFilePath))) {
                    ExecUtil.execOpen(outputFilePath);
                } else {
                    logger.info("output file not exist");
                }
            } else {
                logger.warn("output file is none");
            }
        });
    }

    public void initReset() {
        resetConfigButton.addActionListener(e -> {
            reloadConfig(true);
            JOptionPane.showMessageDialog(null, "已恢复");
        });
    }

    private void initHttpUtil() {
        httpUtilButton.addActionListener(e -> {
            JFrame frame = new JFrame("Http工具");
            frame.setContentPane(new HttpUtilForm().httpUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initListenUtil() {
        listenUtilButton.addActionListener(e -> {
            JFrame frame = new JFrame("监听端口工具");
            frame.setContentPane(new ListenUtilForm().listenUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initEncodeUtil() {
        encodeUtilButton.addActionListener(e -> {
            JFrame frame = new JFrame("编码工具");
            frame.setContentPane(new EncodeUtilForm().encodeUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public MainForm() {
        init();
        initLoadXray();
        initPluginCheckBox();
        initPluginSave();
        initAdvanceConfig();
        initOutputConfig();
        initRawScanConfig();
        initUrlFileConfig();
        initTargetUrlConfig();
        initAllPoC();
        initHttpProxy();
        initTargetPoC();
        initActiveScan();
        initMitmScan();
        initReverse();
        initForcedStop();
        initOpenOutput();
        initReset();
        initHttpUtil();
        initListenUtil();
        initEncodeUtil();
    }

    public static void startMainForm() {
        JFrame frame = new JFrame(Const.ApplicationName);
        instance = new MainForm();
        frame.setContentPane(instance.SuperXray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        SuperXray = new JPanel();
        SuperXray.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        SuperXray.setBackground(new Color(-725535));
        loadXrayPanel = new JPanel();
        loadXrayPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        loadXrayPanel.setBackground(new Color(-725535));
        SuperXray.add(loadXrayPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadXrayPanel.setBorder(BorderFactory.createTitledBorder(null, "Super Xray", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        choseXrayLabel = new JLabel();
        choseXrayLabel.setText("选择你的xray文件");
        loadXrayPanel.add(choseXrayLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showPathPanel = new JPanel();
        showPathPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        showPathPanel.setBackground(new Color(-725535));
        loadXrayPanel.add(showPathPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xrayPathLabel = new JLabel();
        xrayPathLabel.setText("你选择的xray文件是：");
        showPathPanel.add(xrayPathLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xrayPathTextField = new JTextField();
        xrayPathTextField.setEditable(false);
        xrayPathTextField.setEnabled(false);
        showPathPanel.add(xrayPathTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(500, -1), null, 0, false));
        pathButtonPanel = new JPanel();
        pathButtonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        pathButtonPanel.setBackground(new Color(-725535));
        loadXrayPanel.add(pathButtonPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        choseDirButton = new JButton();
        choseDirButton.setText("点击按钮选择");
        pathButtonPanel.add(choseDirButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        noteLabel = new JLabel();
        noteLabel.setText("注意：在 Mac OS 中请用 control+c/v 复制/粘贴（不是 command 键）");
        pathButtonPanel.add(noteLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), null, null, 0, false));
        configPanel = new JPanel();
        configPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.setBackground(new Color(-725535));
        SuperXray.add(configPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftConfigPanel = new JPanel();
        leftConfigPanel.setLayout(new GridLayoutManager(11, 4, new Insets(0, 0, 0, 0), -1, -1));
        leftConfigPanel.setBackground(new Color(-725535));
        leftConfigPanel.setEnabled(true);
        leftConfigPanel.setForeground(new Color(-4540485));
        leftConfigPanel.setToolTipText("");
        configPanel.add(leftConfigPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "扫描插件配置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        catConfigPanel = new JPanel();
        catConfigPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        catConfigPanel.setBackground(new Color(-725535));
        leftConfigPanel.add(catConfigPanel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
        catConfigLabel = new JLabel();
        catConfigLabel.setText("<html>\n使用的插件：\n<br>\n<b>请配置完成后点击->确认插件</b>\n<p>(部分插件仅高级版支持)</p>\n</html>");
        catConfigPanel.add(catConfigLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirmPluginButton = new JButton();
        confirmPluginButton.setText("确认插件");
        catConfigPanel.add(confirmPluginButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bruteForceCheckBox = new JCheckBox();
        bruteForceCheckBox.setText("bruteforce（暴力破解）");
        leftConfigPanel.add(bruteForceCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        baselineCheckBox = new JCheckBox();
        baselineCheckBox.setText("baseline（基线检查）");
        leftConfigPanel.add(baselineCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmdInjectionCheckBox = new JCheckBox();
        cmdInjectionCheckBox.setText("cmd-injection");
        leftConfigPanel.add(cmdInjectionCheckBox, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crlfInjectionCheckBox = new JCheckBox();
        crlfInjectionCheckBox.setText("crlf-injection");
        leftConfigPanel.add(crlfInjectionCheckBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirscanCheckBox = new JCheckBox();
        dirscanCheckBox.setText("dirscan");
        leftConfigPanel.add(dirscanCheckBox, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fastjsonCheckBox = new JCheckBox();
        fastjsonCheckBox.setText("fastjson");
        leftConfigPanel.add(fastjsonCheckBox, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jsonpCheckBox = new JCheckBox();
        jsonpCheckBox.setText("jsonp");
        leftConfigPanel.add(jsonpCheckBox, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pathTraversalCheckBox = new JCheckBox();
        pathTraversalCheckBox.setText("path-traversal");
        leftConfigPanel.add(pathTraversalCheckBox, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phantasmCheckBox = new JCheckBox();
        phantasmCheckBox.setText("phantasm（PoC合集）");
        leftConfigPanel.add(phantasmCheckBox, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        redirectCheckBox = new JCheckBox();
        redirectCheckBox.setText("redirect");
        leftConfigPanel.add(redirectCheckBox, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroCheckBox = new JCheckBox();
        shiroCheckBox.setText("shiro");
        leftConfigPanel.add(shiroCheckBox, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqldetCheckBox = new JCheckBox();
        sqldetCheckBox.setText("sqldet（sql注入）");
        leftConfigPanel.add(sqldetCheckBox, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ssrfCheckBox = new JCheckBox();
        ssrfCheckBox.setText("ssrf");
        leftConfigPanel.add(ssrfCheckBox, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        strutsCheckBox = new JCheckBox();
        strutsCheckBox.setText("struts");
        leftConfigPanel.add(strutsCheckBox, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpCheckBox = new JCheckBox();
        thinkphpCheckBox.setText("thinkphp");
        leftConfigPanel.add(thinkphpCheckBox, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploadCheckBox = new JCheckBox();
        uploadCheckBox.setText("upload");
        leftConfigPanel.add(uploadCheckBox, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xxeCheckBox = new JCheckBox();
        xxeCheckBox.setText("xxe");
        leftConfigPanel.add(xxeCheckBox, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssCheckBox = new JCheckBox();
        xssCheckBox.setText("xss");
        leftConfigPanel.add(xssCheckBox, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enableAllButton = new JButton();
        enableAllButton.setText("全选 / 取消全选");
        leftConfigPanel.add(enableAllButton, new GridConstraints(10, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        advanceButton = new JButton();
        advanceButton.setText("高级配置");
        leftConfigPanel.add(advanceButton, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightConfigPanel = new JPanel();
        rightConfigPanel.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightConfigPanel.setBackground(new Color(-725535));
        rightConfigPanel.setEnabled(true);
        configPanel.add(rightConfigPanel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        proxyConfigPanel = new JPanel();
        proxyConfigPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        proxyConfigPanel.setBackground(new Color(-725535));
        rightConfigPanel.add(proxyConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        proxyConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "代理配置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        proxyPanel = new JPanel();
        proxyPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        proxyPanel.setBackground(new Color(-725535));
        proxyConfigPanel.add(proxyPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        proxyConfigButton = new JButton();
        proxyConfigButton.setText("确认");
        proxyPanel.add(proxyConfigButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        proxyLabel = new JLabel();
        proxyLabel.setText("输入HTTP代理URL");
        proxyPanel.add(proxyLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        proxyText = new JTextField();
        proxyPanel.add(proxyText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        reverseConfigPanel = new JPanel();
        reverseConfigPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        reverseConfigPanel.setBackground(new Color(-725535));
        rightConfigPanel.add(reverseConfigPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        reverseConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "反连平台", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        reverseUrlPanel = new JPanel();
        reverseUrlPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        reverseUrlPanel.setBackground(new Color(-725535));
        reverseConfigPanel.add(reverseUrlPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        httpReverseLabel = new JLabel();
        httpReverseLabel.setText("请输入HTTP URL（IP形式）");
        reverseUrlPanel.add(httpReverseLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        httpReverseText = new JTextField();
        reverseUrlPanel.add(httpReverseText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dnsReverseLabel = new JLabel();
        dnsReverseLabel.setText("DNS IP（IP形式）");
        reverseUrlPanel.add(dnsReverseLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dnsText = new JTextField();
        reverseUrlPanel.add(dnsText, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        reverseConfigButton = new JButton();
        reverseConfigButton.setText("确认配置");
        reverseConfigPanel.add(reverseConfigButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startScanPanel = new JPanel();
        startScanPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        startScanPanel.setBackground(new Color(-725535));
        rightConfigPanel.add(startScanPanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        startScanPanel.setBorder(BorderFactory.createTitledBorder(null, "启动", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        activeScanButton = new JButton();
        activeScanButton.setText("开启主动扫描");
        startScanPanel.add(activeScanButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mitmPanel = new JPanel();
        mitmPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mitmPanel.setBackground(new Color(-725535));
        startScanPanel.add(mitmPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mitmScanButton = new JButton();
        mitmScanButton.setText("开启被动扫描");
        mitmPanel.add(mitmScanButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portText = new JTextField();
        portText.setText("");
        mitmPanel.add(portText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        portLabel = new JLabel();
        portLabel.setText("被动监听端口:");
        mitmPanel.add(portLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        otherPanel = new JPanel();
        otherPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        otherPanel.setBackground(new Color(-725535));
        rightConfigPanel.add(otherPanel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        otherPanel.setBorder(BorderFactory.createTitledBorder(null, "其他", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lookupCmdButton = new JButton();
        lookupCmdButton.setText("查看当前命令");
        otherPanel.add(lookupCmdButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lookupConfigButton = new JButton();
        lookupConfigButton.setText("查看当前配置文件");
        otherPanel.add(lookupConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        midConfigPanel = new JPanel();
        midConfigPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        midConfigPanel.setBackground(new Color(-725535));
        configPanel.add(midConfigPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pocPanel = new JPanel();
        pocPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        pocPanel.setBackground(new Color(-725535));
        midConfigPanel.add(pocPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
        pocPanel.setBorder(BorderFactory.createTitledBorder(null, "PoC模块", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pocButton = new JButton();
        pocButton.setText("指定PoC");
        pocPanel.add(pocButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allPoCButton = new JButton();
        allPoCButton.setText("查看所有PoC");
        pocPanel.add(allPoCButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usePoCText = new JTextField();
        pocPanel.add(usePoCText, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pocNumLabel = new JLabel();
        pocNumLabel.setText("当前xray的PoC数量：");
        pocPanel.add(pocNumLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updatePocButton = new JButton();
        updatePocButton.setText("同步PoC数据库");
        pocPanel.add(updatePocButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        localPoCText = new JTextField();
        localPoCText.setEditable(false);
        localPoCText.setEnabled(false);
        pocPanel.add(localPoCText, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        localPoCButton = new JButton();
        localPoCButton.setText("选择本地PoC");
        pocPanel.add(localPoCButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scanTargetPanel = new JPanel();
        scanTargetPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        scanTargetPanel.setBackground(new Color(-725535));
        midConfigPanel.add(scanTargetPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scanTargetPanel.setBorder(BorderFactory.createTitledBorder(null, " 扫描目标设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        rawFileField = new JTextField();
        rawFileField.setEditable(false);
        rawFileField.setEnabled(false);
        scanTargetPanel.add(rawFileField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rawFileButton = new JButton();
        rawFileButton.setText("指定request文件");
        scanTargetPanel.add(rawFileButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlField = new JTextField();
        urlField.setEditable(true);
        scanTargetPanel.add(urlField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        urlButton = new JButton();
        urlButton.setText("指定url");
        scanTargetPanel.add(urlButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlFileField = new JTextField();
        urlFileField.setEditable(false);
        urlFileField.setEnabled(false);
        scanTargetPanel.add(urlFileField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        urlFileButton = new JButton();
        urlFileButton.setText("指定url列表文件");
        scanTargetPanel.add(urlFileButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputConfigPanel = new JPanel();
        outputConfigPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        outputConfigPanel.setBackground(new Color(-725535));
        midConfigPanel.add(outputConfigPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        outputConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "输出模块", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputConfigButton = new JButton();
        outputConfigButton.setText("点击确认输出配置");
        outputConfigPanel.add(outputConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scanOutConfigPanel = new JPanel();
        scanOutConfigPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        scanOutConfigPanel.setBackground(new Color(-725535));
        outputConfigPanel.add(scanOutConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        htmlRadioButton = new JRadioButton();
        htmlRadioButton.setText("html");
        scanOutConfigPanel.add(htmlRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cliRadioButton = new JRadioButton();
        cliRadioButton.setText("cli");
        scanOutConfigPanel.add(cliRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jsonRadioButton = new JRadioButton();
        jsonRadioButton.setText("json");
        scanOutConfigPanel.add(jsonRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        utilPanel = new JPanel();
        utilPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        utilPanel.setBackground(new Color(-725535));
        midConfigPanel.add(utilPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        utilPanel.setBorder(BorderFactory.createTitledBorder(null, "小工具", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        httpUtilButton = new JButton();
        httpUtilButton.setText("http 请求测试");
        utilPanel.add(httpUtilButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        encodeUtilButton = new JButton();
        encodeUtilButton.setText("编码工具");
        utilPanel.add(encodeUtilButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listenUtilButton = new JButton();
        listenUtilButton.setText("监听端口");
        utilPanel.add(listenUtilButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        bottomPanel.setBackground(new Color(-725535));
        SuperXray.add(bottomPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        authorLabel = new JLabel();
        authorLabel.setHorizontalAlignment(0);
        authorLabel.setHorizontalTextPosition(0);
        authorLabel.setText("<html> <p>Author: 4ra1n (https://github.com/4ra1n) from Chaitin Tech</p> </html>");
        bottomPanel.add(authorLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        outputPanel = new JScrollPane();
        outputPanel.setBackground(new Color(-725535));
        outputPanel.setForeground(new Color(-725535));
        SuperXray.add(outputPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 200), null, null, 0, false));
        outputPanel.setBorder(BorderFactory.createTitledBorder(null, "命令行输出结果：", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputTextArea = new JTextArea();
        outputTextArea.setBackground(new Color(-12828863));
        outputTextArea.setEditable(false);
        outputTextArea.setForeground(new Color(-16711895));
        outputTextArea.setLineWrap(true);
        outputPanel.setViewportView(outputTextArea);
        otherButton = new JPanel();
        otherButton.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        otherButton.setBackground(new Color(-725535));
        SuperXray.add(otherButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resetPanel = new JPanel();
        resetPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        resetPanel.setBackground(new Color(-725535));
        otherButton.add(resetPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resetConfigLabel = new JLabel();
        resetConfigLabel.setText("恢复默认配置：");
        resetPanel.add(resetConfigLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetConfigButton = new JButton();
        resetConfigButton.setText("点击按钮恢复");
        resetPanel.add(resetConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        openResultPanel = new JPanel();
        openResultPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        openResultPanel.setBackground(new Color(-725535));
        otherButton.add(openResultPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        openResultLabel = new JLabel();
        openResultLabel.setText(" 打开扫描结果：（如果存在）");
        openResultPanel.add(openResultLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openResultButton = new JButton();
        openResultButton.setText("点击打开扫描结果");
        openResultPanel.add(openResultButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopPanel = new JPanel();
        stopPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        stopPanel.setBackground(new Color(-725535));
        otherButton.add(stopPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stopLabel = new JLabel();
        stopLabel.setText("  如果意外地运行了危险的程序可以点击：");
        stopPanel.add(stopLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setText("强制停止");
        stopPanel.add(stopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(htmlRadioButton);
        buttonGroup.add(cliRadioButton);
        buttonGroup.add(jsonRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return SuperXray;
    }

}
