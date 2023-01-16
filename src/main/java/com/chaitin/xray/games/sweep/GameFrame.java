package com.chaitin.xray.games.sweep;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.chaitin.xray.games.sweep.Constant.*;

public class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public GameFrame() {
        GamePanel mainPanel = new GamePanel(PANEL_ROW_NUMBER, PANEL_COL_NUMBER);
        int frameWidth = PANEL_COL_NUMBER * BLOCK_WIDTH + 20;
        int frameHeight = PANEL_ROW_NUMBER * BLOCK_HEIGHT + 40;
        this.setSize(frameWidth, frameHeight);
        this.setTitle(TITLE);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(GameFrame.this,
                        "确定退出游戏？", "退出游戏", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION && e.getWindow() == GameFrame.this) {
                    GameFrame.this.dispose();
                }
            }
        });
        this.add(mainPanel);
    }

}
