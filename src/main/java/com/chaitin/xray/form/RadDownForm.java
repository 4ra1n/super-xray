package com.chaitin.xray.form;

import com.chaitin.xray.utils.ExecUtil;
import com.chaitin.xray.utils.OSUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import okhttp3.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RadDownForm {
    public JPanel radPanel;
    private JTextField curVerText;
    private JProgressBar progressBar;
    private JButton downButton;
    private JButton unzipButton;
    private JRadioButton windowsAmd64RadioButton;
    private JRadioButton windows386RadioButton;
    private JRadioButton linuxArm64RadioButton;
    private JRadioButton darwinAmd64RadioButton;
    private JRadioButton linuxAmd64RadioButton;
    private JRadioButton linux386RadioButton;
    private JPanel innerPanel;
    private JLabel osTypeLabel;
    private JLabel curVerLabel;
    private JPanel osTypePanel;
    private JLabel downLabel;
    private JLabel opLabel;
    private JPanel opPanel;
    private JLabel saveLabel;
    private JTextField savePathText;
    private JButton saveButton;
    private static boolean finish = false;
    public static final String xrayDownBase = "https://download.xray.cool/rad";
    private static String savePath = ".";
    private static final String xrayVersion = "0.4";
    private static String osType = "rad_windows_amd64.exe.zip";

    private static String outPath;

    private void initLang() {
        if (MainForm.LANG == MainForm.CHINESE) {
            curVerLabel.setText("下载版本：");
            osTypeLabel.setText("选择你需要：");
            downLabel.setText("下载进度：");
            saveLabel.setText("选择保存路径：");
            saveButton.setText("选择");
            opLabel.setText("操作：");
            downButton.setText("下载");
            unzipButton.setText("解压");
        } else {
            curVerLabel.setText("Download Version:");
            osTypeLabel.setText("Chose:");
            downLabel.setText("Progress:");
            saveLabel.setText("Chose Save Path:");
            saveButton.setText("Chose");
            opLabel.setText("Operate:");
            downButton.setText("Download");
            unzipButton.setText("Unzip");
        }
    }

    private void initSavePath() {
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(radPanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                savePath = file.getAbsolutePath();
                savePathText.setText(savePath);
            }
        });
    }

    private void initOSType() {
        windowsAmd64RadioButton.setSelected(true);
    }

    private void initDownload() {
        downButton.addActionListener(e -> new Thread(() -> {
            if (windowsAmd64RadioButton.isSelected()) {
                osType = "rad_windows_amd64.exe.zip";
            } else if (windows386RadioButton.isSelected()) {
                osType = "rad_windows_386.exe.zip";
            } else if (linux386RadioButton.isSelected()) {
                osType = "rad_linux_386.zip";
            } else if (linuxAmd64RadioButton.isSelected()) {
                osType = "rad_linux_amd64.zip";
            } else if (darwinAmd64RadioButton.isSelected()) {
                osType = "rad_darwin_amd64.zip";
            } else if (linuxArm64RadioButton.isSelected()) {
                osType = "rad_linux_arm64.zip";
            } else {
                return;
            }
            progressBar.setValue(1);
            OkHttpClient okHttpClient = new OkHttpClient();
            String finalUrl = String.format("%s/%s/%s", xrayDownBase, xrayVersion, osType);
            progressBar.setValue(2);
            Request request = new Request.Builder()
                    .url(finalUrl)
                    .addHeader("Connection", "close")
                    .build();
            progressBar.setValue(3);
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    try {
                        if (response.body() == null) {
                            return;
                        }
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(savePath, finalUrl.substring(
                                finalUrl.lastIndexOf("/") + 1));
                        fos = new FileOutputStream(file);
                        progressBar.setValue(4);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            if (progress < 4) {
                                progress = 4;
                            }
                            progressBar.setValue(progress);
                        }
                        fos.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException ignored) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException ignored) {
                        }
                        finish = true;
                    }
                }
            });
        }).start());
    }

    private void initUnzip() {
        unzipButton.addActionListener(e -> {
            if (!finish) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(radPanel, "请先下载");
                } else {
                    JOptionPane.showMessageDialog(radPanel,
                            "Please download first");
                }
                return;
            }
            Path path = Paths.get(savePath + File.separator + osType);
            if (!Files.exists(path)) {
                if (MainForm.LANG == MainForm.CHINESE) {
                    JOptionPane.showMessageDialog(radPanel, "文件不存在");
                } else {
                    JOptionPane.showMessageDialog(radPanel,
                            "File not exist");
                }
                return;
            }
            try {
                ZipFile zip = new ZipFile(path.toFile());
                for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    InputStream in = zip.getInputStream(entry);
                    String curEntryName = entry.getName();
                    outPath = savePath + File.separator + curEntryName;
                    OutputStream out = Files.newOutputStream(Paths.get(outPath));
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    if(!OSUtil.isWindows()){
                        String absOutPath = Paths.get(outPath).toAbsolutePath().toString();
                        ExecUtil.chmod(absOutPath);
                    }
                    if (MainForm.LANG == MainForm.CHINESE) {
                        JOptionPane.showMessageDialog(radPanel, "文件位置：" + outPath);
                    } else {
                        JOptionPane.showMessageDialog(radPanel,
                                "File:" + outPath);
                    }
                    in.close();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public RadDownForm() {
        initLang();
        initSavePath();
        initOSType();
        initDownload();
        initUnzip();
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
        innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        innerPanel.setBackground(new Color(-528927));
        radPanel.add(innerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(600, 200), null, null, 1, false));
        curVerText = new JTextField();
        curVerText.setEditable(false);
        curVerText.setText("0.4");
        innerPanel.add(curVerText, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        osTypeLabel = new JLabel();
        osTypeLabel.setText("选择你需要：");
        innerPanel.add(osTypeLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        curVerLabel = new JLabel();
        curVerLabel.setText("下载版本");
        innerPanel.add(curVerLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        osTypePanel = new JPanel();
        osTypePanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        osTypePanel.setBackground(new Color(-528927));
        innerPanel.add(osTypePanel, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        windowsAmd64RadioButton = new JRadioButton();
        windowsAmd64RadioButton.setBackground(new Color(-528927));
        windowsAmd64RadioButton.setText("windows-amd64");
        osTypePanel.add(windowsAmd64RadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        windows386RadioButton = new JRadioButton();
        windows386RadioButton.setBackground(new Color(-528927));
        windows386RadioButton.setText("windows-386");
        osTypePanel.add(windows386RadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        linuxArm64RadioButton = new JRadioButton();
        linuxArm64RadioButton.setBackground(new Color(-528927));
        linuxArm64RadioButton.setText("linux-arm64");
        osTypePanel.add(linuxArm64RadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        darwinAmd64RadioButton = new JRadioButton();
        darwinAmd64RadioButton.setBackground(new Color(-528927));
        darwinAmd64RadioButton.setEnabled(true);
        darwinAmd64RadioButton.setText("darwin-amd64");
        osTypePanel.add(darwinAmd64RadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        linuxAmd64RadioButton = new JRadioButton();
        linuxAmd64RadioButton.setBackground(new Color(-528927));
        linuxAmd64RadioButton.setText("linux-amd64");
        osTypePanel.add(linuxAmd64RadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        linux386RadioButton = new JRadioButton();
        linux386RadioButton.setBackground(new Color(-528927));
        linux386RadioButton.setText("linux-386");
        osTypePanel.add(linux386RadioButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        downLabel = new JLabel();
        downLabel.setText("下载进度：");
        innerPanel.add(downLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        innerPanel.add(progressBar, new GridConstraints(2, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        opLabel = new JLabel();
        opLabel.setText("操作");
        innerPanel.add(opLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        opPanel = new JPanel();
        opPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        opPanel.setBackground(new Color(-528927));
        innerPanel.add(opPanel, new GridConstraints(4, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downButton = new JButton();
        downButton.setText("下载");
        opPanel.add(downButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unzipButton = new JButton();
        unzipButton.setText("解压");
        opPanel.add(unzipButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveLabel = new JLabel();
        saveLabel.setText("选择保存路径：");
        innerPanel.add(saveLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        savePathText = new JTextField();
        savePathText.setEditable(false);
        innerPanel.add(savePathText, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        saveButton = new JButton();
        saveButton.setText("选择");
        innerPanel.add(saveButton, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return radPanel;
    }

}
