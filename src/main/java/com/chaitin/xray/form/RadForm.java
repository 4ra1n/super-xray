package com.chaitin.xray.form;

import com.chaitin.xray.model.RadCmd;
import com.chaitin.xray.utils.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RadForm {
    private static final Logger logger = LogManager.getLogger(RadForm.class);
    public JPanel radPanel;
    private JPanel radPane;
    private JScrollPane outputPanel;
    private JTextArea outputTextArea;
    private JTextField urlText;
    private JButton choseRadFileButton;
    private JTextField radFileText;
    private JButton startRadButton;
    private JPanel radConfigPanel;
    private JLabel targetLabel;
    private JLabel startLabel;
    private JTextField radCookieText;
    private JLabel radCookieLabel;
    private JButton confirmButton;
    private JButton copyCommandButton;
    private JTextField fileText;
    private JButton exportButton;
    private JButton choseButton;
    private JLabel fileLabel;

    private static RadCmd radCmd;
    public volatile boolean stop = false;

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
                InputStreamReader isr = new InputStreamReader(inputStream,
                        StandardCharsets.UTF_8);
                isReader = new BufferedReader(isr);
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

    private static boolean isRunning = false;

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            choseRadFileButton.setText("选择rad文件");
            startRadButton.setText("启动rad");
            startLabel.setText("  启动");
            targetLabel.setText("  目标url");
            radCookieLabel.setText("  设置Cookie");
            copyCommandButton.setText("复制命令");
            choseButton.setText("选择文件");
            exportButton.setText("导出命令表");
            fileLabel.setText("批量目标文件");
            outputPanel.setBorder(BorderFactory.createTitledBorder(
                    null, "控制台",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    null,
                    null));
        } else {
            choseRadFileButton.setText("Chose Rad File");
            startRadButton.setText("Start Rad");
            startLabel.setText("  Start");
            targetLabel.setText("  Target URL");
            radCookieLabel.setText("  Cookie");
            copyCommandButton.setText("Copy Command");
            choseButton.setText("Chose");
            exportButton.setText("Export");
            fileLabel.setText("Target File");
            outputPanel.setBorder(BorderFactory.createTitledBorder(
                    null, "Console",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    null,
                    null));
        }
    }

    @SuppressWarnings("unchecked")
    private void initCookie() {
        try {
            Path path = Paths.get("rad_config.yml");
            String data;
            if (!Files.exists(path)) {
                InputStream radIs = RadForm.class.getClassLoader()
                        .getResourceAsStream("rad_config.yml");
                data = IOUtil.readStringFromIs(radIs);
            } else {
                data = new String(Files.readAllBytes(path));
            }
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> obj = yaml.load(data);
            List<Object> objList = (List<Object>) obj.get("domain_headers");
            Map<String, Object> headers = (Map<String, Object>)
                    ((Map<String, Object>) objList.get(0)).get("headers");
            String cookie = (String) headers.get("Cookie");
            radCookieText.setText(cookie);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        confirmButton.addActionListener(e -> new Thread(() -> {
            try {
                Path path = Paths.get("rad_config.yml");
                String data;
                String co = radCookieText.getText();
                if (!Files.exists(path)) {
                    InputStream radIs = RadForm.class.getClassLoader()
                            .getResourceAsStream("rad_config.yml");
                    data = IOUtil.readStringFromIs(radIs);
                } else {
                    data = new String(Files.readAllBytes(path));
                }
                Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
                Map<String, Object> obj = yaml.load(data);
                List<Object> objList = (List<Object>) obj.get("domain_headers");
                Map<String, Object> headers = (Map<String, Object>)
                        ((Map<String, Object>) objList.get(0)).get("headers");
                headers.put("Cookie", co);
                String out = yaml.dump(obj);
                Files.write(path, out.getBytes());
                JOptionPane.showMessageDialog(radPane, "Success");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start());
    }

    private void initExport() {
        choseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                Path path = Paths.get(absPath);
                if (!Files.exists(path)) {
                    JOptionPane.showMessageDialog(radPane, "File Not Found");
                    return;
                }
                fileText.setText(absPath);
            }
        });

        exportButton.addActionListener(e -> {
            String file = fileText.getText();
            if (!StringUtil.notEmpty(file)) {
                JOptionPane.showMessageDialog(radPane, "Chose File First");
                return;
            }
            Path filePath = Paths.get(file);
            if (!Files.exists(filePath)) {
                JOptionPane.showMessageDialog(radPane, "File Not Found");
                return;
            }
            try {
                String data = new String(Files.readAllBytes(filePath));
                String[] split = data.split("\n");
                StringBuilder ss = new StringBuilder();
                for (String s : split) {
                    s = s.trim();
                    if (!StringUtil.notEmpty(s)) {
                        continue;
                    }
                    if (s.endsWith("\r")) {
                        s = s.substring(0, s.length() - 1);
                    }
                    radCmd.setTargetUrl(s);
                    String[] cmd = radCmd.buildCmd();
                    StringBuilder sb = new StringBuilder();
                    for (String a : cmd) {
                        sb.append(a);
                        sb.append(" ");
                    }
                    ss.append(sb.toString().trim());
                    ss.append("\n");
                }
                Files.write(Paths.get("rad-cmd.txt"), ss.toString().getBytes());
                radCmd.setTargetUrl(null);
                JOptionPane.showMessageDialog(radPane, "rad-cmd.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(radPane, "Error");
            }
        });
    }

    public RadForm(String inputPort) {
        initLang();

        initCookie();

        DropTarget dt = new DropTarget() {
            @SuppressWarnings("unchecked")
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
                    radCmd.setRad(absPath);
                    radFileText.setText(absPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        radPane.setDropTarget(dt);
        radPanel.setDropTarget(dt);

        radCmd = new RadCmd();
        radCmd.setTarget("-t");
        radCmd.setProxy("-http-proxy");
        radCmd.setProxyInfo(String.format("127.0.0.1:%s", inputPort));

        choseRadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();

                if (!CheckUtil.checkRadValid(absPath)) {
                    return;
                }

                radCmd.setRad(absPath);
                radFileText.setText(absPath);
            }
        });

        startRadButton.addActionListener(e -> {
            if (!isRunning) {
                radCmd.setTargetUrl(urlText.getText());
                outputTextArea.setText(null);
                stop = false;
                execAndFresh(radCmd.buildCmd());
                isRunning = true;
                startRadButton.setText("Stop Rad");
            } else {
                startRadButton.setText("Start Rad");
                stop = true;
                outputTextArea.setText(null);
                isRunning = false;
            }
        });

        copyCommandButton.addActionListener(e -> {
            radCmd.setTargetUrl(urlText.getText());
            String[] cmd = radCmd.buildCmd();
            StringBuilder sb = new StringBuilder();
            for (String s : cmd) {
                sb.append(s);
                sb.append(" ");
            }
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(sb.toString().trim()), null);
            JOptionPane.showMessageDialog(radPane, "Success");
            radCmd.setTargetUrl(null);
        });

        initExport();
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
        radPanel = new JPanel();
        radPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        radPane = new JPanel();
        radPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        radPane.setBackground(new Color(-12828863));
        radPanel.add(radPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(500, 300), null, null, 0, false));
        outputPanel = new JScrollPane();
        outputPanel.setBackground(new Color(-12828863));
        outputPanel.setForeground(new Color(-12828863));
        radPane.add(outputPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 200), null, null, 0, false));
        outputPanel.setBorder(BorderFactory.createTitledBorder(null, "Console", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputTextArea = new JTextArea();
        outputTextArea.setBackground(new Color(-12828863));
        outputTextArea.setEditable(false);
        Font outputTextAreaFont = this.$$$getFont$$$("Consolas", -1, -1, outputTextArea.getFont());
        if (outputTextAreaFont != null) outputTextArea.setFont(outputTextAreaFont);
        outputTextArea.setForeground(new Color(-16711895));
        outputTextArea.setLineWrap(true);
        outputPanel.setViewportView(outputTextArea);
        radConfigPanel = new JPanel();
        radConfigPanel.setLayout(new GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        radConfigPanel.setBackground(new Color(-12828863));
        radPane.add(radConfigPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        targetLabel = new JLabel();
        targetLabel.setText("  Target URL");
        radConfigPanel.add(targetLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlText = new JTextField();
        radConfigPanel.add(urlText, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        choseRadFileButton = new JButton();
        choseRadFileButton.setText("Chose Rad File");
        radConfigPanel.add(choseRadFileButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radFileText = new JTextField();
        radFileText.setEditable(false);
        radFileText.setEnabled(false);
        radConfigPanel.add(radFileText, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        startRadButton = new JButton();
        startRadButton.setText("Start Rad");
        radConfigPanel.add(startRadButton, new GridConstraints(4, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startLabel = new JLabel();
        startLabel.setText("  Start");
        radConfigPanel.add(startLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radCookieLabel = new JLabel();
        radCookieLabel.setText("  Cookie");
        radConfigPanel.add(radCookieLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        radCookieText = new JTextField();
        radConfigPanel.add(radCookieText, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        radConfigPanel.add(confirmButton, new GridConstraints(2, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyCommandButton = new JButton();
        copyCommandButton.setText("Copy Command");
        radConfigPanel.add(copyCommandButton, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileLabel = new JLabel();
        fileLabel.setText(" Target File");
        radConfigPanel.add(fileLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileText = new JTextField();
        radConfigPanel.add(fileText, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        exportButton = new JButton();
        exportButton.setText("Export");
        radConfigPanel.add(exportButton, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        choseButton = new JButton();
        choseButton.setText("Chose");
        radConfigPanel.add(choseButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return radPanel;
    }

}
