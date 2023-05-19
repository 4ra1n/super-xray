package com.chaitin.xray.form;

import com.chaitin.xray.model.Const;
import com.chaitin.xray.model.DB;
import com.chaitin.xray.model.Poc;
import com.chaitin.xray.model.XrayCmd;
import com.chaitin.xray.utils.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.net.URI;
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
    private static final List<String> existOutputList = new ArrayList<>();
    private static DB db;

    public RadForm radInstance;
    private JButton choseDirButton;
    private JPanel SuperXray;
    private JPanel pathButtonPanel;
    private JTextField xrayPathTextField;
    private JPanel configPanel;
    private JPanel leftConfigPanel;
    private JPanel rightConfigPanel;
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
    private JPanel scanTargetPanel;
    private JPanel reverseConfigPanel;
    private JPanel startScanPanel;
    private JPanel otherPanel;
    private JPanel midConfigPanel;
    private JLabel pocNumLabel;
    private JTextField portText;
    private JLabel portLabel;
    private JPanel mitmPanel;
    public JTextField httpReverseText;
    private JPanel reverseUrlPanel;
    private JLabel httpReverseLabel;
    private JButton stopButton;
    private JButton resetConfigButton;
    private JButton openResultButton;
    private JLabel stopLabel;
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
    private JTextField tokenText;
    private JLabel tokenLabel;
    private JCheckBox autoDelCheckBox;
    private JButton cleanAreaButton;
    private JButton xrayUrlButton;
    private JRadioButton chineseLangButton;
    private JRadioButton englishLangButton;
    private JButton onlineButton;
    private JButton reverseServerButton;
    private JButton radButton;
    private JLabel radLabel;
    private JButton cleanPoCButton;
    private JRadioButton criticalRadioButton;
    private JRadioButton highRadioButton;
    private JRadioButton mediumRadioButton;
    private JButton levelButton;
    private JPanel levelPanel;
    public JCheckBox delLogCheckBox;
    private JButton radDownButton;
    private JButton subDomainButton;
    private JCheckBox xstreamCheckBox;
    private JScrollPane pocScroll;
    private JTextArea targetPocArea;
    private JSpinner parallelSet;
    private JLabel setParallelLabel;
    private JLabel tipParallelLabel;
    public JCheckBox delCaCheckBox;
    private JButton ajpScanButton;
    private JLabel ajpLabel;
    private JButton copyCmdButton;
    private JTextField mitmIpText;
    private JCheckBox lockCheckBox;
    private JLabel mitmIpLabel;
    private JPanel langParPanel;
    private JSpinner qpsSet;
    private JLabel qpsLabel;
    private JLabel qpsTip;
    private JSpinner cphSet;
    private JLabel cphLabel;
    private JLabel cphTip;
    private JPanel botPanel;
    private JRadioButton nullRadioButton;
    private JRadioButton debugRadioButton;
    private JRadioButton infoRadioButton;
    private JRadioButton warnRadioButton;
    private JRadioButton errorRadioButton;
    private JRadioButton fatalRadioButton;
    private JPanel logPanel;
    private JPanel mulPocPanel;
    private JLabel mulPocLabel;
    private SubdomainForm subdomainInstance;
    private AJPScanForm ajpInstance;

    @SuppressWarnings("unchecked")
    public void init() {
        checkBoxList = new ArrayList<>();
        xrayCmd = new XrayCmd();

        DropTarget dt = new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    Object obj = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    List<File> droppedFiles = (List<File>) obj;
                    if (droppedFiles.size() != 1) {
                        return;
                    }
                    String absPath = droppedFiles.get(0).getAbsolutePath();
                    if (!CheckUtil.checkValid(absPath)) {
                        return;
                    }
                    new Thread(() -> loadXray(absPath)).start();
                    JOptionPane.showMessageDialog(SuperXray, "Loading...");
                    DB data = new DB();
                    data.setLastXrayPath(absPath);
                    try {
                        Files.write(Paths.get("super-xray.db"), data.getDB().getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        SuperXray.setDropTarget(dt);

        try {
            Path dbPath = Paths.get("super-xray.db");
            if (Files.exists(dbPath)) {
                byte[] data = Files.readAllBytes(dbPath);
                db = DB.parseDB(data);
            } else {
                db = new DB();
                db.setLastXrayPath(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        reloadConfig(true, false);

        lookupConfigButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "查看配置文件";
            } else {
                t = "Lookup Config";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new LookupConfigForm().lookupConfigPanel);
            frame.pack();
            frame.setVisible(true);
        });

        lookupCmdButton.addActionListener(e -> {
            String[] cmd = xrayCmd.buildCmd();
            StringBuilder sb = new StringBuilder();
            for (String c : cmd) {
                if (c.length() < 50) {
                    if (StringUtil.notEmpty(c)) {
                        sb.append(c);
                        sb.append("\n");
                    }
                } else {
                    String t = c.substring(0, 44);
                    sb.append(t);
                    sb.append("......");
                    sb.append("\n");
                }
            }
            JOptionPane.showMessageDialog(this.SuperXray, sb.toString().trim());
        });
    }

    @SuppressWarnings("unchecked")
    public void reloadConfig(boolean init, boolean reset) {
        Path curConfig = Paths.get("config.yaml");
        if (init) {
            if (reset) {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
                configStr = IOUtil.readStringFromIs(is);
            } else {
                if (Files.exists(curConfig)) {
                    try {
                        configStr = new String(Files.readAllBytes(curConfig));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
                    configStr = IOUtil.readStringFromIs(is);
                }
            }
        } else {
            if (Files.exists(curConfig)) {
                try {
                    configStr = new String(Files.readAllBytes(curConfig));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        configTemplate = configStr;

        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        configObj = yaml.load(configStr);

        try {
            if (StringUtil.notEmpty(configPath)) {
                Files.write(Paths.get(configPath),
                        configStr.getBytes(StandardCharsets.UTF_8));
                Files.write(curConfig,
                        configStr.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        parallelSet.setValue(configObj.get("parallel"));

        Map<String, Object> httpConfig = (Map<String, Object>) configObj.get("http");
        qpsSet.setValue(httpConfig.get("max_qps"));
        cphSet.setValue(httpConfig.get("max_conns_per_host"));

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
                    if (plugin.getKey().equals("xstream")) {
                        Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                        xstreamCheckBox.setSelected((boolean) (items.get("enabled")));
                    }
                }
            }
        }
        for (Map.Entry<String, Object> entry : configObj.entrySet()) {
            if (entry.getKey().equals("reverse")) {
                Map<String, Object> reverse = (Map<String, Object>) entry.getValue();
                Map<String, Object> client = (Map<String, Object>) reverse.get("client");
                String token = (String) reverse.get("token");
                String httpUrl = (String) client.get("http_base_url");
                String dnsServer = (String) client.get("dns_server_ip");
                if (StringUtil.notEmpty(httpUrl) || StringUtil.notEmpty(dnsServer)) {
                    client.put("remote_server", true);
                }
                tokenText.setText(token);
                httpReverseText.setText(httpUrl);
            }
        }
    }

    private volatile boolean stop = false;

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

    public void loadXray(String absPath) {
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
        if (OSUtil.isMacOS() &&
                (!StringUtil.notEmpty(db.getLastXrayPath()) ||
                        db.getLastXrayPath().equals("null"))) {
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, Const.MacNeedAgree);
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, Const.MacNeedAgreeEn);
            }
        }

        try {
            Thread.sleep(1000);
            t.interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        XrayUtil.cpAllConfig(targetDir);

        try {
            Path configPathPath = Paths.get(targetDir + Const.ConfigYaml);
            configPath = configPathPath.toFile().getAbsolutePath();
            Files.write(configPathPath,
                    configStr.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        xrayCmd.setXray(absPath);

        stop = false;
        execAndFresh(cmd);
    }

    public void initLoadXray() {
        if (StringUtil.notEmpty(db.getLastXrayPath()) &&
                !db.getLastXrayPath().equals("null")) {
            if (CheckUtil.checkValid(db.getLastXrayPath())) {
                loadXray(db.getLastXrayPath());
            }
        }

        choseDirButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (OSUtil.isWindows()) {
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("EXE", "exe");
                fileChooser.addChoosableFileFilter(filter);
            }
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();

                if (CheckUtil.checkValid(absPath)) {
                    loadXray(absPath);
                }

                DB data = new DB();
                data.setLastXrayPath(absPath);
                try {
                    Files.write(Paths.get("super-xray.db"), data.getDB().getBytes());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                if (LANG == CHINESE) {
                    xrayPathTextField.setText("你取消了选择");
                } else {
                    xrayPathTextField.setText("Cancel");
                }
            }
        });
    }

    public void initPluginCheckBox() {
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
        checkBoxList.add(xstreamCheckBox);
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
                        if (plugin.getKey().equals("xstream")) {
                            Map<String, Object> items = (Map<String, Object>) plugin.getValue();
                            if (xstreamCheckBox.isSelected()) {
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
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置完成");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    public void refreshConfig() {
        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        StringWriter writer = new StringWriter();
        yaml.dump(configObj, writer);
        configStr = writer.toString();
        try {
            Files.write(Paths.get("config.yaml"),
                    configStr.getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get(configPath),
                    configStr.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initAdvanceConfig() {
        advanceButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "高级配置";
            } else {
                t = "Advance Config";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new AdvanceConfigForm().advanceConfigPanel);
            frame.setResizable(true);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initOutputConfig() {
        htmlRadioButton.setSelected(true);
        jsonRadioButton.setSelected(false);
        cliRadioButton.setSelected(false);
        outputConfigButton.addActionListener(e -> {
            refreshOutput();
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置输出成功");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    public void refreshOutput() {
        String uuid = UUID.randomUUID().toString();
        if (htmlRadioButton.isSelected()) {
            outputFilePath = Paths.get(String.format("./xray-%s.html", uuid)).toFile().getAbsolutePath();
            existOutputList.add(outputFilePath);
            xrayCmd.setOutputPrefix("--html-output");
            xrayCmd.setOutput(outputFilePath);
        }
        if (jsonRadioButton.isSelected()) {
            outputFilePath = Paths.get(String.format("./xray-%s.txt", uuid)).toFile().getAbsolutePath();
            existOutputList.add(outputFilePath);
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
            xrayCmd.setInputPrefix("--url");
            xrayCmd.setInput(url);
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置URL成功");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    public void initUrlFileConfig() {
        urlFileButton.addActionListener(e -> {
            urlField.setText(null);
            rawFileField.setText(null);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                urlFileField.setText(absPath);
                xrayCmd.setInputPrefix("--url-file");
                xrayCmd.setInput(String.format("%s", absPath));
            } else {
                if (LANG == CHINESE) {
                    urlFileField.setText("你取消了选择");
                } else {
                    urlFileField.setText("Cancel");
                }
            }
        });
    }

    public void initRawScanConfig() {
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
                if (LANG == CHINESE) {
                    rawFileField.setText("你取消了选择");
                } else {
                    rawFileField.setText("Cancel");
                }
            }
        });
    }

    private static boolean activeRunning = false;

    @SuppressWarnings("unchecked")
    public void initActiveScan() {
        copyCmdButton.addActionListener(e -> {
            XrayCmd temp = new XrayCmd();

            if (debugRadioButton.isSelected()) {
                temp.setLogLevel("debug");
            }
            if (infoRadioButton.isSelected()) {
                temp.setLogLevel("info");
            }
            if (warnRadioButton.isSelected()) {
                temp.setLogLevel("warn");
            }
            if (errorRadioButton.isSelected()) {
                temp.setLogLevel("error");
            }
            if (fatalRadioButton.isSelected()) {
                temp.setLogLevel("fatal");
            }
            if (nullRadioButton.isSelected()) {
                temp.setLogLevel(null);
            }

            temp.setXray(xrayCmd.getXray());
            temp.setModule("webscan");
            temp.setConfig(String.format("%s", configPath));
            temp.setOthers(null);

            String uuid = UUID.randomUUID().toString();
            if (htmlRadioButton.isSelected()) {
                temp.setOutputPrefix("--html-output");
                temp.setOutput(Paths.get(String.format("./xray-%s.html", uuid)).toFile().getAbsolutePath());
            }
            if (jsonRadioButton.isSelected()) {
                temp.setOutputPrefix("--json-output");
                temp.setOutput(Paths.get(String.format("./xray-%s.txt", uuid)).toFile().getAbsolutePath());
            }
            if (cliRadioButton.isSelected()) {
                temp.setOutput(null);
            }

            temp.setPoc(xrayCmd.getPoc());
            temp.setInput(xrayCmd.getInput());
            temp.setInputPrefix(xrayCmd.getInputPrefix());

            String[] cmdA = temp.buildCmd();
            StringBuilder sb = new StringBuilder();
            for (String cmd : cmdA) {
                sb.append(cmd);
                sb.append(" ");
            }

            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(sb.toString().trim()), null);

            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "已复制");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Copy");
            }
        });
        activeScanButton.addActionListener(e -> {
            if (!StringUtil.notEmpty(xrayCmd.getInput()) ||
                    !StringUtil.notEmpty(xrayCmd.getInputPrefix())) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "请输入扫描目标");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Need input target");
                }
                return;
            }
            try {
                if (!activeRunning) {

                    int value = (int) parallelSet.getValue();
                    int qps = (int) qpsSet.getValue();
                    int cph = (int) cphSet.getValue();

                    if (value > 100 || value < 1) {
                        if (LANG == CHINESE) {
                            JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                        } else {
                            JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                        }
                        parallelSet.setValue(30);
                        return;
                    }

                    if (qps > 1000 || qps < 1) {
                        if (LANG == CHINESE) {
                            JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                        } else {
                            JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                        }
                        qpsSet.setValue(500);
                        return;
                    }

                    if (cph > 200 || cph < 1) {
                        if (LANG == CHINESE) {
                            JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                        } else {
                            JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                        }
                        cphSet.setValue(50);
                        return;
                    }

                    configObj.put("parallel", value);
                    Map<String, Object> httpObj = (Map<String, Object>) configObj.get("http");
                    httpObj.put("max_qps", qps);
                    httpObj.put("max_conns_per_host", cph);
                    refreshConfig();

                    refreshOutput();

                    if (debugRadioButton.isSelected()) {
                        xrayCmd.setLogLevel("debug");
                    }
                    if (infoRadioButton.isSelected()) {
                        xrayCmd.setLogLevel("info");
                    }
                    if (warnRadioButton.isSelected()) {
                        xrayCmd.setLogLevel("warn");
                    }
                    if (errorRadioButton.isSelected()) {
                        xrayCmd.setLogLevel("error");
                    }
                    if (fatalRadioButton.isSelected()) {
                        xrayCmd.setLogLevel("fatal");
                    }
                    if (nullRadioButton.isSelected()) {
                        xrayCmd.setLogLevel(null);
                    }

                    xrayCmd.setModule("webscan");
                    xrayCmd.setConfig(String.format("%s", configPath));
                    xrayCmd.setOthers(null);
                    String[] finalCmd = xrayCmd.buildCmd();
                    outputTextArea.setText(null);
                    stop = false;
                    execAndFresh(finalCmd);
                    activeRunning = true;
                    if (LANG == CHINESE) {
                        activeScanButton.setText("停止主动扫描");
                    } else {
                        activeScanButton.setText("Stop Active Scan");
                    }
                } else {
                    if (LANG == CHINESE) {
                        activeScanButton.setText("开始主动扫描");
                    } else {
                        activeScanButton.setText("Start Active Scan");
                    }
                    stop = true;
                    outputTextArea.setText(null);
                    activeRunning = false;
                }
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
        if (LANG == CHINESE) {
            pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
        } else {
            pocNumLabel.setText(String.format("PoC Num: %s", Poc.getPocList().size()));
        }
        allPoCButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "查看所有PoC";
            } else {
                t = "All PoC";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new AllPoCForm().searchPoC);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
        cleanPoCButton.addActionListener(e -> {
            targetPocArea.setText(null);
            localPoCText.setText(null);
            xrayCmd.setPoc(null);
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "已清除PoC设置");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Clean PoC setting success");
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void onlyUsePhantasm(String poc, boolean flag) {
        if (flag) {
            xrayCmd.setPoc(String.format("--poc %s", poc));
        } else {
            xrayCmd.setPoc(String.format("--level %s", poc));
        }

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

    private void initPoCLevel() {
        criticalRadioButton.setSelected(true);
        levelButton.addActionListener(e -> {
            if (criticalRadioButton.isSelected()) {
                onlyUsePhantasm("critical", false);
            } else if (highRadioButton.isSelected()) {
                onlyUsePhantasm("high", false);
            } else if (mediumRadioButton.isSelected()) {
                onlyUsePhantasm("medium", false);
            } else {
                return;
            }
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置成功!");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success!");
            }
        });
    }

    public void initTargetPoC() {
        onlineButton.addActionListener(e -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                URI oURL = new URI("https://poc.xray.cool");
                desktop.browse(oURL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        localPoCButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter("YML", "yml");
            FileNameExtensionFilter filter2 = new FileNameExtensionFilter("YAML", "yaml");
            fileChooser.addChoosableFileFilter(filter1);
            fileChooser.addChoosableFileFilter(filter2);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                String[] temp = absPath.split("\\.");
                String suffix = temp[temp.length - 1].toLowerCase().trim();
                if (!suffix.equals("yml") && !suffix.equals("yaml")) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "你选择的不是合法YAML文件");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "Error File Type");
                    }
                    return;
                }
                localPoCText.setText(absPath);
                onlyUsePhantasm(absPath, true);
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "设置PoC成功");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Success");
                }
            }
        });

        updatePocButton.addActionListener(e -> {
            String[] cmd = new String[]{xrayCmd.getXray(), "ws", "--list"};
            InputStream is = Objects.requireNonNull(ExecUtil.exec(cmd)).getInputStream();
            stop = false;
            outputTextArea.setText(null);
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
                if (s.contains("test") && !s.contains("testrail")) {
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
            if (LANG == CHINESE) {
                outputTextArea.setText("同步PoC成功\n点击 查看所有PoC 即可搜索");
                pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
            } else {
                outputTextArea.setText("Success!\nClick Lookup All PoC to Search");
                pocNumLabel.setText(String.format("PoC Num: %s", Poc.getPocList().size()));
            }
        });

        pocButton.addActionListener(e -> {
            String poc = targetPocArea.getText();
            if (!StringUtil.notEmpty(poc)) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "输入为空");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Need Input");
                }
                return;
            }
            String[] pocs = poc.split("\n");
            StringBuilder sb = new StringBuilder();
            for (String p : pocs) {
                p = p.trim();
                if (p.endsWith("\r")) {
                    p = p.substring(0, p.length() - 1);
                }
                if (!Poc.getPocList().contains(p)) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "PoC不存在");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "PoC Not Exist");
                    }
                    return;
                }
                sb.append(p);
                sb.append(",");
            }
            String finalCmd = sb.toString();
            onlyUsePhantasm(finalCmd.substring(0, finalCmd.length() - 1), true);
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置PoC成功");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    private static boolean mitmRunning = false;

    @SuppressWarnings("unchecked")
    public void initMitmScan() {
        lockCheckBox.setSelected(true);
        lockCheckBox.addActionListener(e -> mitmIpText.setEditable(!lockCheckBox.isSelected()));
        mitmScanButton.addActionListener(e -> {
            if (!mitmRunning) {
                String ip = mitmIpText.getText();
                String port = portText.getText();

                if (!StringUtil.notEmpty(port)) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "请输入端口");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "Need port");
                    }
                    return;
                }

                int value = (int) parallelSet.getValue();
                int qps = (int) qpsSet.getValue();
                int cph = (int) cphSet.getValue();
                if (value > 100 || value < 1) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                    }
                    parallelSet.setValue(30);
                    return;
                }
                if (qps > 1000 || qps < 1) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                    }
                    qpsSet.setValue(500);
                    return;
                }
                if (cph > 200 || cph < 1) {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                    }
                    cphSet.setValue(50);
                    return;
                }

                configObj.put("parallel", value);
                Map<String, Object> httpObj = (Map<String, Object>) configObj.get("http");
                httpObj.put("max_qps", qps);
                httpObj.put("max_conns_per_host", cph);

                refreshConfig();

                if (debugRadioButton.isSelected()) {
                    xrayCmd.setLogLevel("debug");
                }
                if (infoRadioButton.isSelected()) {
                    xrayCmd.setLogLevel("info");
                }
                if (warnRadioButton.isSelected()) {
                    xrayCmd.setLogLevel("warn");
                }
                if (errorRadioButton.isSelected()) {
                    xrayCmd.setLogLevel("error");
                }
                if (fatalRadioButton.isSelected()) {
                    xrayCmd.setLogLevel("fatal");
                }
                if (nullRadioButton.isSelected()) {
                    xrayCmd.setLogLevel(null);
                }

                xrayCmd.setModule("webscan");
                xrayCmd.setConfig(String.format("%s", configPath));
                xrayCmd.setInput(null);

                refreshOutput();

                xrayCmd.setOthersPrefix("--listen");
                xrayCmd.setOthers(ip + ":" + port);
                String[] cmd = xrayCmd.buildCmd();
                stop = false;
                execAndFresh(cmd);
                if (LANG == CHINESE) {
                    mitmScanButton.setText("关闭被动监听");
                } else {
                    mitmScanButton.setText("Shutdown");
                }
                portText.setEnabled(false);
                mitmRunning = true;
            } else {
                stop = true;
                portText.setEnabled(true);
                if (LANG == CHINESE) {
                    mitmScanButton.setText("开启被动扫描");
                } else {
                    mitmScanButton.setText("Start MITM");
                }
                mitmRunning = false;
                outputTextArea.setText(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void initReverse() {
        for (Map.Entry<String, Object> entry : configObj.entrySet()) {
            if (entry.getKey().equals("reverse")) {
                Map<String, Object> reverse = (Map<String, Object>) entry.getValue();
                Map<String, Object> client = (Map<String, Object>) reverse.get("client");
                String token = (String) reverse.get("token");
                String httpUrl = (String) client.get("http_base_url");
                if (StringUtil.notEmpty(httpUrl)) {
                    client.put("remote_server", true);
                }
                tokenText.setText(token);
                httpReverseText.setText(httpUrl);
            }
        }

        reverseServerButton.addActionListener(e -> {
            String t = "Reverse Server Config";
            JFrame frame = new JFrame(t);
            frame.setContentPane(new ReverseForm().reversePanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });

        reverseConfigButton.addActionListener(e -> {
            String http = httpReverseText.getText();
            if (!http.startsWith("http")) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "应该以http开头");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Should start with http");
                }
                return;
            }
            String token = tokenText.getText();
            if (!StringUtil.notEmpty(http) || !StringUtil.notEmpty(token)) {
                httpReverseText.setText(null);
                tokenText.setText(null);
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "输入不可以为空");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Do not input null");
                }
                return;
            }
            for (Map.Entry<String, Object> entry : configObj.entrySet()) {
                if (entry.getKey().equals("reverse")) {
                    Map<String, Object> reverse = (Map<String, Object>) entry.getValue();
                    Map<String, Object> client = (Map<String, Object>) reverse.get("client");
                    reverse.put("token", token);
                    client.put("remote_server", true);
                    client.put("http_base_url", http);
                }
            }
            refreshConfig();
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "设置反连成功");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    public void initForcedStop() {
        stopButton.addActionListener(e -> {
            stop = true;
            outputTextArea.setText(null);
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "已强制停止");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Stop Success");
            }
        });
    }

    public void initOpenOutput() {
        autoDelCheckBox.setSelected(false);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (autoDelCheckBox.isSelected()) {
                for (String s : existOutputList) {
                    try {
                        Files.delete(Paths.get(s));
                    } catch (Exception ignored) {
                    }
                }
            }
        }));

        openResultButton.addActionListener(e -> {
            if (outputFilePath == null) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "目前没有输出文件");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "No output file");
                }
                return;
            }
            if (StringUtil.notEmpty(outputFilePath.trim())) {
                if (Files.exists(Paths.get(outputFilePath))) {
                    String tempOutput = outputFilePath.replace(".html",
                            "copy.html");
                    try {
                        // copy
                        Files.write(Paths.get(tempOutput),
                                Files.readAllBytes(Paths.get(outputFilePath)));
                    } catch (Exception ignored) {
                    }
                    existOutputList.add(tempOutput);
                    new Thread(() -> ExecUtil.execOpen(tempOutput)).start();
                } else {
                    if (LANG == CHINESE) {
                        JOptionPane.showMessageDialog(this.SuperXray, "目前没有输出文件");
                    } else {
                        JOptionPane.showMessageDialog(this.SuperXray, "No output file");
                    }
                }
            }
        });
    }

    public void initReset() {
        resetConfigButton.addActionListener(e -> {
            reloadConfig(true, true);
            if (LANG == CHINESE) {
                JOptionPane.showMessageDialog(this.SuperXray, "已恢复");
            } else {
                JOptionPane.showMessageDialog(this.SuperXray, "Success");
            }
        });
    }

    private void initHttpUtil() {
        httpUtilButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "Http工具";
            } else {
                t = "Repeater";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new HttpUtilForm().httpUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initListenUtil() {
        listenUtilButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "监听端口工具";
            } else {
                t = "Listener";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new ListenUtilForm().listenUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initEncodeUtil() {
        encodeUtilButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "编码工具";
            } else {
                t = "Decoder";
            }
            JFrame frame = new JFrame(t);
            frame.setContentPane(new EncodeUtilForm().encodeUtilPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    @SuppressWarnings("all")
    private void initOther() {
        cleanAreaButton.addActionListener(e -> outputTextArea.setText(null));
        delCaCheckBox.setSelected(true);
        parallelSet.addChangeListener(e -> {
            int value = (int) parallelSet.getValue();
            if (value > 100 || value < 1) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                }
                parallelSet.setValue(30);
                return;
            }
            configObj.put("parallel", value);
            refreshConfig();
        });
        qpsSet.addChangeListener(e -> {
            int qps = (int) qpsSet.getValue();
            if (qps > 1000 || qps < 1) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                }
                qpsSet.setValue(500);
                return;
            }
            Map<String, Object> httpObj = (Map<String, Object>) configObj.get("http");
            httpObj.put("max_qps", qps);
            refreshConfig();
        });
        cphSet.addChangeListener(e -> {
            int cph = (int) cphSet.getValue();
            if (cph > 200 || cph < 1) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "设置有误");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Error Config");
                }
                cphSet.setValue(50);
                return;
            }
            Map<String, Object> httpObj = (Map<String, Object>) configObj.get("http");
            httpObj.put("max_conns_per_host", cph);
            refreshConfig();
        });
        xrayUrlButton.addActionListener(e -> {
            JFrame frame = new JFrame("Xray Download");
            frame.setContentPane(new XrayDownForm().xrayDownPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
        radDownButton.addActionListener(e -> {
            JFrame frame = new JFrame("Rad Download");
            frame.setContentPane(new RadDownForm().radPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/4ra1n/super-xray/releases/latest")
                        .addHeader("Connection", "close")
                        .build();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // ignored
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            if (response.body() == null) {
                                return;
                            }
                            String body = response.body().string();
                            String ver = body.split("\"tag_name\":")[1].split(",")[0];
                            ver = ver.substring(1, ver.length() - 1);

                            if (!ver.equals(Const.CurVersion)) {
                                String output;
                                if (LANG == CHINESE) {
                                    output = String.format("新版本！\n%s: %s\n%s: %s\n%s",
                                            "您当前的版本", Const.CurVersion,
                                            "目前最新版本", ver,
                                            "https://github.com/4ra1n/super-xray/releases/latest");
                                    JOptionPane.showMessageDialog(instance.SuperXray, output);
                                } else {
                                    output = String.format("New Version!\n%s: %s\n%s: %s\n%s",
                                            "Your Current Version", Const.CurVersion,
                                            "Latest Version", ver,
                                            "https://github.com/4ra1n/super-xray/releases/latest");
                                    JOptionPane.showMessageDialog(instance.SuperXray, output);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        }.start();
    }

    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;
    public static int LANG;

    public void initLang() {
        chineseLangButton.setSelected(true);
        LANG = CHINESE;
        englishLangButton.setSelected(false);
        chineseLangButton.addActionListener(e -> {
            if (chineseLangButton.isSelected()) {
                LANG = CHINESE;
            }
            refreshLang();
            if (LANG == CHINESE) {
                pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
            } else {
                pocNumLabel.setText(String.format("PoC Num: %s", Poc.getPocList().size()));
            }
        });
        englishLangButton.addActionListener(e -> {
            if (englishLangButton.isSelected()) {
                LANG = ENGLISH;
            }
            refreshLang();
            if (LANG == CHINESE) {
                pocNumLabel.setText(String.format("当前共有: %s 个PoC", Poc.getPocList().size()));
            } else {
                pocNumLabel.setText(String.format("PoC Num: %s", Poc.getPocList().size()));
            }
        });
    }

    public void refreshLang() {
        if (LANG == ENGLISH) {
            leftConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Plugins Config", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            bruteForceCheckBox.setText("bruteforce");
            baselineCheckBox.setText("baseline");
            phantasmCheckBox.setText("phantasm");
            sqldetCheckBox.setText("sqldet");
            enableAllButton.setText("All select / Cancel");
            advanceButton.setText("Advance Config");
            pocPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "PoC Module", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            pocNumLabel.setText("Total PoC Num:");
            updatePocButton.setText("Update PoC DB");
            allPoCButton.setText("Lookup All PoC");
            pocButton.setText("Use PoC");
            localPoCButton.setText("Chose Local PoC");
            scanTargetPanel.setBorder(BorderFactory.createTitledBorder(null,
                    " Target Config", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            rawFileButton.setText("Use Request File");
            urlButton.setText("Target Url");
            urlFileButton.setText("Target Url List");
            outputConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Output Module", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            outputConfigButton.setText("Save Output Config");
            utilPanel.setBorder(BorderFactory.createTitledBorder(null, "Tools",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            httpUtilButton.setText("Repeater");
            listenUtilButton.setText("Listener");
            encodeUtilButton.setText("Decoder");
            reverseConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Reverse", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            tokenLabel.setText("Token");
            httpReverseLabel.setText("Input HTTP URL(IP)");
            reverseConfigButton.setText("Confirm");
            startScanPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Start", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            activeScanButton.setText("Start Active Scan");
            portLabel.setText("Listen Port");
            mitmScanButton.setText("Open MITM Scan");
            otherPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Others", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            lookupCmdButton.setText("Lookup Current Command");
            lookupConfigButton.setText("Lookup Current Config");
            xrayUrlButton.setText("Xray Download");
            cleanAreaButton.setText("Clear Console");
            outputPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "Console", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            stopLabel.setText("  Force Stop");
            resetConfigLabel.setText("Reset Default Config");
            stopButton.setText("Stop");
            resetConfigButton.setText("Reset");
            autoDelCheckBox.setText("Delete All Reports When Exit");
            openResultButton.setText("Open Scan Result");
            choseDirButton.setText("Chose File");
            confirmPluginButton.setText("Confirm");
            reverseServerButton.setText("Server Config");
            radLabel.setText("Open mitm scan and run rad");
            radButton.setText("Run with rad");
            onlineButton.setText("Generate Online");
            cleanPoCButton.setText("Clean PoC setting ");
            delLogCheckBox.setText("Delete All Logs When Exit");
            levelButton.setText("Set Level");
            radDownButton.setText("Rad Download");
            subDomainButton.setText("Subdomain Scan");
            setParallelLabel.setText("Set Parallel");
            tipParallelLabel.setText("Concurrency");
            delCaCheckBox.setText("Delete CA When Exit");
            ajpScanButton.setText("AJP Scan");
            copyCmdButton.setText("Copy Cmd");
            lockCheckBox.setText("Lock");
            mitmIpLabel.setText("Listen IP");
            qpsLabel.setText("QPS");
            qpsTip.setText("Request speed");
            cphLabel.setText("Max host conn");
            cphTip.setText("Host TCP conn");
            mulPocLabel.setText("   PoC Line");
        } else if (LANG == CHINESE) {
            leftConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "扫描插件配置", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            bruteForceCheckBox.setText("bruteforce");
            baselineCheckBox.setText("baseline");
            phantasmCheckBox.setText("phantasm");
            sqldetCheckBox.setText("sqldet");
            enableAllButton.setText("全选 / 取消全选");
            advanceButton.setText("高级配置");
            pocPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "PoC模块", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            pocNumLabel.setText("当前xray的PoC数量：");
            updatePocButton.setText("同步PoC数据库");
            allPoCButton.setText("查看所有PoC");
            pocButton.setText("指定PoC");
            localPoCButton.setText("选择本地PoC");
            scanTargetPanel.setBorder(BorderFactory.createTitledBorder(null,
                    " 扫描目标设置", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            rawFileButton.setText("指定request文件");
            urlButton.setText("指定url");
            urlFileButton.setText("指定url列表文件");
            outputConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "输出模块", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            outputConfigButton.setText("点击确认输出配置");
            utilPanel.setBorder(BorderFactory.createTitledBorder(null, "小工具",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            httpUtilButton.setText("http 请求测试");
            listenUtilButton.setText("监听端口");
            encodeUtilButton.setText("编码工具");
            reverseConfigPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "反连平台", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            tokenLabel.setText("Token");
            httpReverseLabel.setText("请输入HTTP URL（IP形式）");
            reverseConfigButton.setText("确认配置");
            startScanPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "启动", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            activeScanButton.setText("开启主动扫描");
            portLabel.setText("被动监听端口:");
            mitmScanButton.setText("开启被动扫描");
            otherPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "其他", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            lookupCmdButton.setText("查看当前命令");
            lookupConfigButton.setText("查看当前配置文件");
            xrayUrlButton.setText("xray下载面板");
            cleanAreaButton.setText("清空命令行输出");
            outputPanel.setBorder(BorderFactory.createTitledBorder(null,
                    "命令行输出结果：", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, null, null));
            stopLabel.setText("  如果意外地运行了危险的程序可以点击：");
            resetConfigLabel.setText("恢复默认配置：");
            stopButton.setText("强制停止");
            resetConfigButton.setText("确认");
            autoDelCheckBox.setText("关闭后删除报告");
            openResultButton.setText("点击打开扫描结果");
            choseDirButton.setText("点击按钮选择");
            confirmPluginButton.setText("确认插件");
            reverseServerButton.setText("生成服务端配置文件");
            radLabel.setText("开启被动扫描后与rad联动");
            radButton.setText("点击联动");
            onlineButton.setText("在线生成");
            cleanPoCButton.setText("清除PoC设置");
            delLogCheckBox.setText("关闭后删除日志");
            levelButton.setText("设置等级");
            radDownButton.setText("rad下载面板");
            subDomainButton.setText("子域名扫描");
            setParallelLabel.setText("设置PoC并发");
            tipParallelLabel.setText("同时运行的PoC数量");
            delCaCheckBox.setText("关闭后删除ca文件");
            ajpScanButton.setText("AJP服务扫描");
            copyCmdButton.setText("复制当前命令");
            lockCheckBox.setText("锁定");
            mitmIpLabel.setText("被动监听IP:");
            qpsLabel.setText("每秒最大请求");
            qpsTip.setText("参数越大发包越快");
            cphLabel.setText("目标主机最大连接");
            cphTip.setText("参数越大目标压力越大");
            mulPocLabel.setText("   每行一个POC");
        }
    }

    private void initExit() {
        delLogCheckBox.setSelected(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            instance.stop = true;
            if (radInstance != null) {
                radInstance.stop = true;
            }
            if (subdomainInstance != null) {
                subdomainInstance.stop = true;
            }
            if (ajpInstance != null) {
                ajpInstance.stop = true;
            }
            try {
                Thread.sleep(3000);
            } catch (Exception ignored) {
            }
        }));
    }

    private void initGetRad() {
        radButton.addActionListener(e -> {
            if (!StringUtil.notEmpty(portText.getText())) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "请先开启被动扫描");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Need open MITM");
                }
                return;
            }

            JFrame frame = new JFrame("Rad Command");
            radInstance = new RadForm(portText.getText());
            frame.setContentPane(radInstance.radPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    private void initSubdomain() {
        subDomainButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "子域名扫描";
            } else {
                t = "Subdomain Scan";
            }
            if (!StringUtil.notEmpty(xrayCmd.getXray())) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "请先加载xray");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Load xray first");
                }
                return;
            }
            JFrame frame = new JFrame(t);
            subdomainInstance = new SubdomainForm(xrayCmd);
            frame.setContentPane(subdomainInstance.subdomainPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    private void initAJP() {
        ajpScanButton.addActionListener(e -> {
            String t;
            if (LANG == CHINESE) {
                t = "Tomcat AJP 扫描";
            } else {
                t = "Tomcat AJP Scan";
            }
            if (!StringUtil.notEmpty(xrayCmd.getXray())) {
                if (LANG == CHINESE) {
                    JOptionPane.showMessageDialog(this.SuperXray, "请先加载xray");
                } else {
                    JOptionPane.showMessageDialog(this.SuperXray, "Load xray first");
                }
                return;
            }
            JFrame frame = new JFrame(t);
            ajpInstance = new AJPScanForm(xrayCmd);
            frame.setContentPane(ajpInstance.ajpPanel);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public void initLog() {
        nullRadioButton.setSelected(true);
    }

    public MainForm() {
        init();
        initLang();
        initLog();
        initLoadXray();
        initPluginCheckBox();
        initPluginSave();
        initAdvanceConfig();
        initOutputConfig();
        initRawScanConfig();
        initUrlFileConfig();
        initTargetUrlConfig();
        initAllPoC();
        initTargetPoC();
        initPoCLevel();
        initActiveScan();
        initMitmScan();
        initGetRad();
        initReverse();
        initForcedStop();
        initOpenOutput();
        initReset();
        initHttpUtil();
        initListenUtil();
        initEncodeUtil();
        initSubdomain();
        initOther();
        initAJP();
        initExit();
    }

    public static void startMainForm() {
        JFrame frame = new JFrame(Const.ApplicationName);
        instance = new MainForm();
        frame.setContentPane(instance.SuperXray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.setVisible(true);

        frame.setSize(1280, 960);
    }

    private static JMenu createAboutMenu() {
        try {
            JMenu aboutMenu = new JMenu("帮助");
            JMenuItem bugItem = new JMenuItem("报告bug");
            InputStream is = MainForm.class.getClassLoader().getResourceAsStream("issue.png");
            if (is == null) {
                return null;
            }

            ImageIcon imageIcon = new ImageIcon(ImageIO.read(is));
            bugItem.setIcon(imageIcon);
            aboutMenu.add(bugItem);
            bugItem.addActionListener(e -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI oURL = new URI("https://github.com/4ra1n/super-xray/issues/new/choose");
                    desktop.browse(oURL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            JMenuItem authorItem = new JMenuItem("项目地址");
            is = MainForm.class.getClassLoader().getResourceAsStream("address.png");
            if (is == null) {
                return null;
            }
            imageIcon = new ImageIcon(ImageIO.read(is));
            authorItem.setIcon(imageIcon);
            aboutMenu.add(authorItem);
            authorItem.addActionListener(e -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI oURL = new URI("https://github.com/4ra1n/super-xray");
                    desktop.browse(oURL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            JMenuItem normalItem = new JMenuItem("常见问题");
            is = MainForm.class.getClassLoader().getResourceAsStream("normal.png");
            if (is == null) {
                return null;
            }
            imageIcon = new ImageIcon(ImageIO.read(is));
            normalItem.setIcon(imageIcon);
            aboutMenu.add(normalItem);
            normalItem.addActionListener(e -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI oURL = new URI("https://github.com/4ra1n/super-xray/issues/98");
                    desktop.browse(oURL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            JMenuItem repairItem = new JMenuItem("自动修复");
            is = MainForm.class.getClassLoader().getResourceAsStream("fix.png");
            if (is == null) {
                return null;
            }
            imageIcon = new ImageIcon(ImageIO.read(is));
            repairItem.setIcon(imageIcon);
            aboutMenu.add(repairItem);

            repairItem.addActionListener(e -> {
                int i = JOptionPane.showConfirmDialog(instance.SuperXray,
                        "修复将会删除所有相关的数据，你确定吗？");
                if (i == 2 || i == 1) {
                    return;
                }

                instance.outputTextArea.setText(null);

                instance.outputTextArea.append("正在停止所有进程...\n");

                instance.stop = true;
                if (instance.radInstance != null) {
                    instance.radInstance.stop = true;
                }
                if (instance.subdomainInstance != null) {
                    instance.subdomainInstance.stop = true;
                }
                if (instance.ajpInstance != null) {
                    instance.ajpInstance.stop = true;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }

                String p = instance.xrayPathTextField.getText();
                if (StringUtil.notEmpty(p)) {
                    instance.outputTextArea.append("删除xray临时文件...\n");
                    String tp = Paths.get(p).toFile().getParent();
                    XrayUtil.rmAllConfig(tp);
                    try {
                        Files.delete(Paths.get(tp + Const.ConfigYaml));
                    } catch (Exception ignored) {
                    }
                }

                try {
                    instance.outputTextArea.append("删除xray配置文件...\n");
                    Files.delete(Paths.get(Const.ConfigYaml));
                } catch (Exception ignored) {
                }

                try {
                    instance.outputTextArea.append("删除保存的数据...\n");
                    Files.delete(Paths.get(Const.DBFile));
                } catch (Exception ignored) {
                }

                instance.outputTextArea.append("修复完毕请重启...\n");
            });

            return aboutMenu;
        } catch (Exception ex) {
            return null;
        }
    }

    private static JMenu createVersionMenu() {
        try {
            JMenu verMenu = new JMenu("版本");
            JMenuItem xrayItem = new JMenuItem(Const.MinXrayVersion);
            InputStream is = MainForm.class.getClassLoader().getResourceAsStream("ver.png");
            if (is == null) {
                return null;
            }
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(is));
            xrayItem.setIcon(imageIcon);

            JMenuItem superXrayItem = new JMenuItem(Const.SuperXrayVersion);
            superXrayItem.setIcon(imageIcon);


            JMenuItem downItem = new JMenuItem("验证最新版");
            downItem.setIcon(imageIcon);
            downItem.addActionListener(e -> {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/4ra1n/super-xray/releases/latest")
                        .addHeader("Connection", "close")
                        .build();

                JOptionPane.showMessageDialog(instance.SuperXray, Const.GithubTip);

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        JOptionPane.showMessageDialog(instance.SuperXray, e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            if (response.body() == null) {
                                if (LANG == CHINESE) {
                                    JOptionPane.showMessageDialog(instance.SuperXray, "网络错误");
                                } else {
                                    JOptionPane.showMessageDialog(instance.SuperXray, "network error");
                                }
                            }
                            String body = response.body().string();
                            String ver = body.split("\"tag_name\":")[1].split(",")[0];
                            ver = ver.substring(1, ver.length() - 1);

                            String output;
                            if (LANG == CHINESE) {
                                output = String.format("%s: %s\n%s: %s",
                                        "您当前的版本", Const.CurVersion,
                                        "目前最新版本", ver);
                                JOptionPane.showMessageDialog(instance.SuperXray, output);
                            } else {
                                output = String.format("%s: %s\n%s: %s",
                                        "Your Current Version", Const.CurVersion,
                                        "Latest Version", ver);
                                JOptionPane.showMessageDialog(instance.SuperXray, output);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(instance.SuperXray, ex.toString());
                        }
                    }
                });
            });

            verMenu.add(xrayItem);
            verMenu.add(superXrayItem);
            verMenu.add(downItem);
            return verMenu;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createAboutMenu());
        menuBar.add(createVersionMenu());
        return menuBar;
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
        SuperXray.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        SuperXray.setBackground(new Color(-12828863));
        loadXrayPanel = new JPanel();
        loadXrayPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        loadXrayPanel.setBackground(new Color(-12828863));
        SuperXray.add(loadXrayPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        loadXrayPanel.setBorder(BorderFactory.createTitledBorder(null, "Super Xray", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pathButtonPanel = new JPanel();
        pathButtonPanel.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        pathButtonPanel.setBackground(new Color(-12828863));
        loadXrayPanel.add(pathButtonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        xrayPathTextField = new JTextField();
        xrayPathTextField.setEditable(false);
        xrayPathTextField.setEnabled(false);
        pathButtonPanel.add(xrayPathTextField, new GridConstraints(0, 1, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(500, -1), null, 0, false));
        choseDirButton = new JButton();
        choseDirButton.setText("点击按钮选择");
        pathButtonPanel.add(choseDirButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        langParPanel = new JPanel();
        langParPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        pathButtonPanel.add(langParPanel, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        chineseLangButton = new JRadioButton();
        chineseLangButton.setBackground(new Color(-12828863));
        chineseLangButton.setText("简体中文");
        langParPanel.add(chineseLangButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        englishLangButton = new JRadioButton();
        englishLangButton.setBackground(new Color(-12828863));
        englishLangButton.setText("English");
        langParPanel.add(englishLangButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configPanel = new JPanel();
        configPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.setBackground(new Color(-12828863));
        SuperXray.add(configPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftConfigPanel = new JPanel();
        leftConfigPanel.setLayout(new GridLayoutManager(11, 3, new Insets(0, 0, 0, 0), -1, -1));
        leftConfigPanel.setBackground(new Color(-12828863));
        leftConfigPanel.setEnabled(true);
        leftConfigPanel.setForeground(new Color(-4540485));
        leftConfigPanel.setToolTipText("");
        configPanel.add(leftConfigPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "扫描插件配置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        bruteForceCheckBox = new JCheckBox();
        bruteForceCheckBox.setBackground(new Color(-12828863));
        bruteForceCheckBox.setText("bruteforce");
        leftConfigPanel.add(bruteForceCheckBox, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        baselineCheckBox = new JCheckBox();
        baselineCheckBox.setBackground(new Color(-12828863));
        baselineCheckBox.setText("baseline");
        leftConfigPanel.add(baselineCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmdInjectionCheckBox = new JCheckBox();
        cmdInjectionCheckBox.setBackground(new Color(-12828863));
        cmdInjectionCheckBox.setText("cmd-injection");
        leftConfigPanel.add(cmdInjectionCheckBox, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crlfInjectionCheckBox = new JCheckBox();
        crlfInjectionCheckBox.setBackground(new Color(-12828863));
        crlfInjectionCheckBox.setText("crlf-injection");
        leftConfigPanel.add(crlfInjectionCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dirscanCheckBox = new JCheckBox();
        dirscanCheckBox.setBackground(new Color(-12828863));
        dirscanCheckBox.setText("dirscan");
        leftConfigPanel.add(dirscanCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fastjsonCheckBox = new JCheckBox();
        fastjsonCheckBox.setBackground(new Color(-12828863));
        fastjsonCheckBox.setText("fastjson");
        leftConfigPanel.add(fastjsonCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jsonpCheckBox = new JCheckBox();
        jsonpCheckBox.setBackground(new Color(-12828863));
        jsonpCheckBox.setText("jsonp");
        leftConfigPanel.add(jsonpCheckBox, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pathTraversalCheckBox = new JCheckBox();
        pathTraversalCheckBox.setBackground(new Color(-12828863));
        pathTraversalCheckBox.setText("path-traversal");
        leftConfigPanel.add(pathTraversalCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phantasmCheckBox = new JCheckBox();
        phantasmCheckBox.setBackground(new Color(-12828863));
        phantasmCheckBox.setText("phantasm");
        leftConfigPanel.add(phantasmCheckBox, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        redirectCheckBox = new JCheckBox();
        redirectCheckBox.setBackground(new Color(-12828863));
        redirectCheckBox.setEnabled(true);
        redirectCheckBox.setText("redirect");
        leftConfigPanel.add(redirectCheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shiroCheckBox = new JCheckBox();
        shiroCheckBox.setBackground(new Color(-12828863));
        shiroCheckBox.setText("shiro");
        leftConfigPanel.add(shiroCheckBox, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqldetCheckBox = new JCheckBox();
        sqldetCheckBox.setBackground(new Color(-12828863));
        sqldetCheckBox.setText("sqldet");
        leftConfigPanel.add(sqldetCheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ssrfCheckBox = new JCheckBox();
        ssrfCheckBox.setBackground(new Color(-12828863));
        ssrfCheckBox.setText("ssrf");
        leftConfigPanel.add(ssrfCheckBox, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        strutsCheckBox = new JCheckBox();
        strutsCheckBox.setBackground(new Color(-12828863));
        strutsCheckBox.setText("struts");
        leftConfigPanel.add(strutsCheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thinkphpCheckBox = new JCheckBox();
        thinkphpCheckBox.setBackground(new Color(-12828863));
        thinkphpCheckBox.setText("thinkphp");
        leftConfigPanel.add(thinkphpCheckBox, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploadCheckBox = new JCheckBox();
        uploadCheckBox.setBackground(new Color(-12828863));
        uploadCheckBox.setText("upload");
        leftConfigPanel.add(uploadCheckBox, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xxeCheckBox = new JCheckBox();
        xxeCheckBox.setBackground(new Color(-12828863));
        xxeCheckBox.setText("xxe");
        leftConfigPanel.add(xxeCheckBox, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xssCheckBox = new JCheckBox();
        xssCheckBox.setBackground(new Color(-12828863));
        xssCheckBox.setText("xss");
        leftConfigPanel.add(xssCheckBox, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enableAllButton = new JButton();
        enableAllButton.setText("全选 / 取消全选");
        leftConfigPanel.add(enableAllButton, new GridConstraints(10, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        advanceButton = new JButton();
        advanceButton.setText("高级配置");
        leftConfigPanel.add(advanceButton, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xstreamCheckBox = new JCheckBox();
        xstreamCheckBox.setBackground(new Color(-12828863));
        xstreamCheckBox.setText("xstream");
        leftConfigPanel.add(xstreamCheckBox, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirmPluginButton = new JButton();
        confirmPluginButton.setText("确认插件");
        leftConfigPanel.add(confirmPluginButton, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightConfigPanel = new JPanel();
        rightConfigPanel.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightConfigPanel.setBackground(new Color(-12828863));
        rightConfigPanel.setEnabled(true);
        configPanel.add(rightConfigPanel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        reverseConfigPanel = new JPanel();
        reverseConfigPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        reverseConfigPanel.setBackground(new Color(-12828863));
        rightConfigPanel.add(reverseConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        reverseConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "反连平台", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        reverseUrlPanel = new JPanel();
        reverseUrlPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        reverseUrlPanel.setBackground(new Color(-12828863));
        reverseConfigPanel.add(reverseUrlPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        httpReverseLabel = new JLabel();
        httpReverseLabel.setText("请输入HTTP URL（IP形式）");
        reverseUrlPanel.add(httpReverseLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        httpReverseText = new JTextField();
        httpReverseText.setText("");
        reverseUrlPanel.add(httpReverseText, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tokenLabel = new JLabel();
        tokenLabel.setText("Token");
        reverseUrlPanel.add(tokenLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tokenText = new JTextField();
        tokenText.setText("");
        reverseUrlPanel.add(tokenText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reverseConfigButton = new JButton();
        reverseConfigButton.setText("确认配置");
        reverseConfigPanel.add(reverseConfigButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reverseServerButton = new JButton();
        reverseServerButton.setText("生成服务端配置文件");
        reverseConfigPanel.add(reverseServerButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startScanPanel = new JPanel();
        startScanPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        startScanPanel.setBackground(new Color(-12828863));
        rightConfigPanel.add(startScanPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        startScanPanel.setBorder(BorderFactory.createTitledBorder(null, "启动", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        activeScanButton = new JButton();
        activeScanButton.setText("开启主动扫描");
        startScanPanel.add(activeScanButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mitmPanel = new JPanel();
        mitmPanel.setLayout(new GridLayoutManager(6, 6, new Insets(0, 0, 0, 0), -1, -1));
        mitmPanel.setBackground(new Color(-12828863));
        startScanPanel.add(mitmPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mitmScanButton = new JButton();
        mitmScanButton.setText("开启被动扫描");
        mitmPanel.add(mitmScanButton, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portText = new JTextField();
        portText.setText("");
        mitmPanel.add(portText, new GridConstraints(4, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portLabel = new JLabel();
        portLabel.setText("被动监听端口:");
        mitmPanel.add(portLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radLabel = new JLabel();
        radLabel.setText("开启被动扫描后与rad联动");
        mitmPanel.add(radLabel, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setParallelLabel = new JLabel();
        setParallelLabel.setText("设置PoC并发");
        mitmPanel.add(setParallelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parallelSet = new JSpinner();
        parallelSet.setBackground(new Color(-12828863));
        mitmPanel.add(parallelSet, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipParallelLabel = new JLabel();
        tipParallelLabel.setText("同时运行的PoC数量");
        mitmPanel.add(tipParallelLabel, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radButton = new JButton();
        radButton.setText("点击联动");
        mitmPanel.add(radButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ajpScanButton = new JButton();
        ajpScanButton.setText("AJP服务扫描");
        mitmPanel.add(ajpScanButton, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ajpLabel = new JLabel();
        ajpLabel.setText("Tomcat AJP");
        mitmPanel.add(ajpLabel, new GridConstraints(5, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mitmIpLabel = new JLabel();
        mitmIpLabel.setText("被动监听IP:");
        mitmPanel.add(mitmIpLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mitmIpText = new JTextField();
        mitmIpText.setEditable(false);
        mitmIpText.setText("127.0.0.1");
        mitmPanel.add(mitmIpText, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockCheckBox = new JCheckBox();
        lockCheckBox.setBackground(new Color(-12828863));
        lockCheckBox.setText("锁定");
        mitmPanel.add(lockCheckBox, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qpsLabel = new JLabel();
        qpsLabel.setText("每秒最大请求");
        mitmPanel.add(qpsLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qpsSet = new JSpinner();
        qpsSet.setBackground(new Color(-12828863));
        mitmPanel.add(qpsSet, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qpsTip = new JLabel();
        qpsTip.setText("参数越大发包越快");
        mitmPanel.add(qpsTip, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cphLabel = new JLabel();
        cphLabel.setText("目标主机最大连接");
        mitmPanel.add(cphLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cphSet = new JSpinner();
        cphSet.setBackground(new Color(-12828863));
        mitmPanel.add(cphSet, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cphTip = new JLabel();
        cphTip.setText("参数越大目标压力越大");
        mitmPanel.add(cphTip, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyCmdButton = new JButton();
        copyCmdButton.setText("复制当前命令");
        startScanPanel.add(copyCmdButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        otherPanel = new JPanel();
        otherPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        otherPanel.setBackground(new Color(-12828863));
        rightConfigPanel.add(otherPanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        otherPanel.setBorder(BorderFactory.createTitledBorder(null, "其他", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        xrayUrlButton = new JButton();
        xrayUrlButton.setText("xray下载面板");
        otherPanel.add(xrayUrlButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lookupConfigButton = new JButton();
        lookupConfigButton.setText("查看当前配置文件");
        otherPanel.add(lookupConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lookupCmdButton = new JButton();
        lookupCmdButton.setText("查看当前命令");
        otherPanel.add(lookupCmdButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radDownButton = new JButton();
        radDownButton.setText("rad下载面板");
        otherPanel.add(radDownButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        subDomainButton = new JButton();
        subDomainButton.setText("子域名扫描");
        otherPanel.add(subDomainButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cleanAreaButton = new JButton();
        cleanAreaButton.setText("清空命令行输出");
        otherPanel.add(cleanAreaButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logPanel = new JPanel();
        logPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        rightConfigPanel.add(logPanel, new GridConstraints(1, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        logPanel.setBorder(BorderFactory.createTitledBorder(null, "Log Level", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        nullRadioButton = new JRadioButton();
        nullRadioButton.setText("null");
        logPanel.add(nullRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debugRadioButton = new JRadioButton();
        debugRadioButton.setText("debug");
        logPanel.add(debugRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infoRadioButton = new JRadioButton();
        infoRadioButton.setText("info");
        logPanel.add(infoRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warnRadioButton = new JRadioButton();
        warnRadioButton.setText("warn");
        logPanel.add(warnRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorRadioButton = new JRadioButton();
        errorRadioButton.setText("error");
        logPanel.add(errorRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fatalRadioButton = new JRadioButton();
        fatalRadioButton.setText("fatal");
        logPanel.add(fatalRadioButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        midConfigPanel = new JPanel();
        midConfigPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        midConfigPanel.setBackground(new Color(-12828863));
        configPanel.add(midConfigPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pocPanel = new JPanel();
        pocPanel.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        pocPanel.setBackground(new Color(-12828863));
        midConfigPanel.add(pocPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
        pocPanel.setBorder(BorderFactory.createTitledBorder(null, "PoC模块", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        allPoCButton = new JButton();
        allPoCButton.setText("查看所有PoC");
        pocPanel.add(allPoCButton, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pocNumLabel = new JLabel();
        pocNumLabel.setText("当前xray的PoC数量：");
        pocPanel.add(pocNumLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updatePocButton = new JButton();
        updatePocButton.setText("同步PoC数据库");
        pocPanel.add(updatePocButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        localPoCText = new JTextField();
        localPoCText.setEditable(false);
        localPoCText.setEnabled(false);
        pocPanel.add(localPoCText, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        localPoCButton = new JButton();
        localPoCButton.setText("选择本地PoC");
        pocPanel.add(localPoCButton, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cleanPoCButton = new JButton();
        cleanPoCButton.setText("清除poc设置");
        pocPanel.add(cleanPoCButton, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        onlineButton = new JButton();
        onlineButton.setText("在线生成");
        pocPanel.add(onlineButton, new GridConstraints(6, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        levelPanel.setBackground(new Color(-12828863));
        pocPanel.add(levelPanel, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        criticalRadioButton = new JRadioButton();
        criticalRadioButton.setBackground(new Color(-12828863));
        criticalRadioButton.setText("CRITICAL");
        levelPanel.add(criticalRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        highRadioButton = new JRadioButton();
        highRadioButton.setBackground(new Color(-12828863));
        highRadioButton.setText("HIGH");
        levelPanel.add(highRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mediumRadioButton = new JRadioButton();
        mediumRadioButton.setBackground(new Color(-12828863));
        mediumRadioButton.setText("MEDIUM");
        levelPanel.add(mediumRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        levelButton = new JButton();
        levelButton.setText("指定等级");
        pocPanel.add(levelButton, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pocScroll = new JScrollPane();
        pocPanel.add(pocScroll, new GridConstraints(1, 0, 3, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 100), null, null, 0, false));
        targetPocArea = new JTextArea();
        targetPocArea.setText("");
        pocScroll.setViewportView(targetPocArea);
        mulPocPanel = new JPanel();
        mulPocPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        pocPanel.add(mulPocPanel, new GridConstraints(1, 2, 3, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pocButton = new JButton();
        pocButton.setText("指定PoC");
        mulPocPanel.add(pocButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mulPocLabel = new JLabel();
        mulPocLabel.setText("   每行一个POC");
        mulPocPanel.add(mulPocLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scanTargetPanel = new JPanel();
        scanTargetPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        scanTargetPanel.setBackground(new Color(-12828863));
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
        outputConfigPanel.setBackground(new Color(-12828863));
        midConfigPanel.add(outputConfigPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        outputConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "输出模块", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputConfigButton = new JButton();
        outputConfigButton.setText("点击确认输出配置");
        outputConfigPanel.add(outputConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scanOutConfigPanel = new JPanel();
        scanOutConfigPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        scanOutConfigPanel.setBackground(new Color(-12828863));
        outputConfigPanel.add(scanOutConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        htmlRadioButton = new JRadioButton();
        htmlRadioButton.setBackground(new Color(-12828863));
        htmlRadioButton.setText("html");
        scanOutConfigPanel.add(htmlRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cliRadioButton = new JRadioButton();
        cliRadioButton.setBackground(new Color(-12828863));
        cliRadioButton.setText("cli");
        scanOutConfigPanel.add(cliRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jsonRadioButton = new JRadioButton();
        jsonRadioButton.setBackground(new Color(-12828863));
        jsonRadioButton.setText("json");
        scanOutConfigPanel.add(jsonRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        utilPanel = new JPanel();
        utilPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        utilPanel.setBackground(new Color(-12828863));
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
        outputPanel = new JScrollPane();
        outputPanel.setBackground(new Color(-12828863));
        outputPanel.setForeground(new Color(-12828863));
        SuperXray.add(outputPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 200), null, null, 0, false));
        outputPanel.setBorder(BorderFactory.createTitledBorder(null, "命令行输出结果：", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputTextArea = new JTextArea();
        outputTextArea.setBackground(new Color(-12828863));
        outputTextArea.setEditable(false);
        Font outputTextAreaFont = this.$$$getFont$$$("Consolas", -1, -1, outputTextArea.getFont());
        if (outputTextAreaFont != null) outputTextArea.setFont(outputTextAreaFont);
        outputTextArea.setForeground(new Color(-16711895));
        outputTextArea.setLineWrap(true);
        outputPanel.setViewportView(outputTextArea);
        otherButton = new JPanel();
        otherButton.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        otherButton.setBackground(new Color(-12828863));
        SuperXray.add(otherButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resetPanel = new JPanel();
        resetPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        resetPanel.setBackground(new Color(-12828863));
        otherButton.add(resetPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resetConfigLabel = new JLabel();
        resetConfigLabel.setText("恢复默认配置：");
        resetPanel.add(resetConfigLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetConfigButton = new JButton();
        resetConfigButton.setText("确认");
        resetPanel.add(resetConfigButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        openResultPanel = new JPanel();
        openResultPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        openResultPanel.setBackground(new Color(-12828863));
        otherButton.add(openResultPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        openResultButton = new JButton();
        openResultButton.setText("点击打开扫描结果");
        openResultPanel.add(openResultButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autoDelCheckBox = new JCheckBox();
        autoDelCheckBox.setBackground(new Color(-12828863));
        autoDelCheckBox.setText("关闭后删除报告");
        openResultPanel.add(autoDelCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delLogCheckBox = new JCheckBox();
        delLogCheckBox.setBackground(new Color(-12828863));
        delLogCheckBox.setText("关闭后删除日志");
        openResultPanel.add(delLogCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delCaCheckBox = new JCheckBox();
        delCaCheckBox.setBackground(new Color(-12828863));
        delCaCheckBox.setText("关闭后删除ca文件");
        openResultPanel.add(delCaCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopPanel = new JPanel();
        stopPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        stopPanel.setBackground(new Color(-12828863));
        otherButton.add(stopPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stopLabel = new JLabel();
        stopLabel.setText("  如果意外地运行了危险的程序可以点击：");
        stopPanel.add(stopLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setText("强制停止");
        stopPanel.add(stopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        botPanel = new JPanel();
        botPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        SuperXray.add(botPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(htmlRadioButton);
        buttonGroup.add(cliRadioButton);
        buttonGroup.add(jsonRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(chineseLangButton);
        buttonGroup.add(englishLangButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(criticalRadioButton);
        buttonGroup.add(highRadioButton);
        buttonGroup.add(mediumRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(nullRadioButton);
        buttonGroup.add(debugRadioButton);
        buttonGroup.add(infoRadioButton);
        buttonGroup.add(warnRadioButton);
        buttonGroup.add(errorRadioButton);
        buttonGroup.add(fatalRadioButton);
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
        return SuperXray;
    }

}
