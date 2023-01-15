package com.chaitin.xray.pinball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import static com.chaitin.xray.pinball.Constant.*;

public class PinballFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Random random;
    private int ySpeed, xSpeed;
    private int ballY, ballX;
    private int racketX;
    private boolean isLose;
    private final GameCanvas canvas;
    private Timer timer;
    public PinballFrame() {
        super("弹球");
        this.random = new Random();
        this.canvas = new GameCanvas(g -> {
            if (this.isLose) {
                g.setColor(new Color(255, 0, 0));
                g.setFont(new Font("Times", Font.BOLD, 30));
                int option = JOptionPane.showConfirmDialog(this,
                        "是否重新开始？", "游戏结束", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    this.startGame();
                }
            } else {
                // 设置颜色，并绘制小球
                g.setColor(new Color(255, 70, 0));
                g.fillOval(this.ballX, this.ballY, BALL_SIZE, BALL_SIZE);
                // 设置颜色，并绘制球拍
                g.setColor(new Color(25, 25, 110));
                g.fillRect(this.racketX, RACKET_Y, RACKET_WIDTH, RACKET_HEIGHT);
            }
        });
        this.init();
    }
    private void init() {
        this.canvas.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        this.add(this.canvas);
        KeyAdapter keyProcessor = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (PinballFrame.this.racketX > 0) {
                        PinballFrame.this.racketX -= 10;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (PinballFrame.this.racketX < TABLE_WIDTH - RACKET_WIDTH) {
                        PinballFrame.this.racketX += 10;
                    }
                }
            }
        };
        this.addKeyListener(keyProcessor);
        this.canvas.addKeyListener(keyProcessor);
        this.pack();
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(PinballFrame.this,
                        "确定退出游戏？", "退出游戏", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION && e.getWindow() == PinballFrame.this) {
                    PinballFrame.this.dispose();
                }
            }
        });
        this.setVisible(true);
        this.startGame();
    }

    private void startGame() {
        double xyRate = random.nextDouble() - 0.5;
        this.ySpeed = 12;
        this.xSpeed = (int) (ySpeed * xyRate * 2);
        this.ballY = random.nextInt(10) + 20;
        this.ballX = random.nextInt(200) + 20;
        this.racketX = random.nextInt(200);
        this.isLose = false;
        this.timer = new Timer(100, e -> {
            if (this.ballX <= 0 || this.ballX >= TABLE_WIDTH - BALL_SIZE) {
                this.xSpeed = -this.xSpeed;
            }
            if (this.ballY >= RACKET_Y - BALL_SIZE &&
                    (this.ballX < this.racketX || this.ballX > this.racketX + RACKET_WIDTH)) {
                this.timer.stop();
                this.isLose = true;
                this.canvas.repaint();
            } else if (this.ballY <= 0 || (this.ballY > RACKET_Y - BALL_SIZE &&
                    this.ballX > racketX && this.ballX <= racketX + RACKET_WIDTH)) {
                this.ySpeed = -this.ySpeed;
            }
            this.ballY += this.ySpeed;
            this.ballX += this.xSpeed;
            this.canvas.repaint();
        });
        this.timer.start();
    }

}
