package com.chaitin.xray.games.sweep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.chaitin.xray.games.sweep.Constant.*;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int rows;
    private final int cols;
    private final int bombCount;
    private final JLabel[][] labels;
    private final GameButton[][] buttons;

    public GamePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.bombCount = rows * cols / 10;
        this.labels = new JLabel[rows][cols];
        this.buttons = new GameButton[rows][cols];
        this.setLayout(null);
        startGame();
    }

    private void startGame() {
        this.initButtons();
        this.initLabels();
        this.initRandomBomb();
        this.initNumber();
    }

    private void initButtons() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                GameButton button = new GameButton();
                button.setBounds(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                this.add(button);
                this.buttons[i][j] = button;
                button.row = i;
                button.col = j;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            leftClick((GameButton) e.getSource());
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            rightClick((GameButton) e.getSource());
                        }
                    }
                });
                button.setForeground(Color.RED);
            }
        }
    }

    private void initLabels() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                JLabel label = new JLabel(BLANK_SPACE, JLabel.CENTER);
                label.setBounds(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                this.add(label);
                this.labels[i][j] = label;
            }
        }
    }

    private void initRandomBomb() {
        for (int i = 0; i < this.bombCount; i++) {
            int randomRow = (int) (Math.random() * this.rows);
            int randomCol = (int) (Math.random() * this.cols);
            this.labels[randomRow][randomCol].setText(BOMB_EMOJI);
            this.labels[randomRow][randomCol].setBackground(Color.LIGHT_GRAY);
            this.labels[randomRow][randomCol].setForeground(Color.RED);
        }
    }

    private void initNumber() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                String text = this.labels[i][j].getText();
                if (!BOMB_EMOJI.equals(text)) {
                    int bombCount = 0;
                    for (int[] offset : OFFSETS) {
                        int row = i + offset[0];
                        int col = j + offset[1];
                        if (checkOutOfBound(row, col) && BOMB_EMOJI.equals(this.labels[row][col].getText())) {
                            bombCount++;
                        }
                    }
                    if (bombCount > 0) {
                        this.labels[i][j].setText(String.valueOf(bombCount));
                    }
                }
            }
        }
    }

    private void leftClick(GameButton actionButton) {
        String buttonText = this.buttons[actionButton.row][actionButton.col].getText();
        if (BLANK_SPACE.equals(buttonText)) {
            String labelText = this.labels[actionButton.row][actionButton.col].getText();
            actionButton.setVisible(false);
            if (BLANK_SPACE.equals(labelText)) {
                for (int[] offset : OFFSETS) {
                    int newRow = actionButton.row + offset[0];
                    int newCol = actionButton.col + offset[1];
                    if (checkOutOfBound(newRow, newCol)) {
                        GameButton button = this.buttons[newRow][newCol];
                        if (button.isVisible()) {
                            this.leftClick(button);
                        }
                    }
                }
            } else if (BOMB_EMOJI.equals(labelText)) {
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        this.buttons[i][j].setVisible(false);
                    }
                }
                this.doGameOver();
            }
        }
    }

    private void rightClick(GameButton actionButton) {
        String buttonText = this.buttons[actionButton.row][actionButton.col].getText();
        if (BLANK_SPACE.equals(buttonText)) {
            actionButton.setText(FLAG_EMOJI);
        } else if (FLAG_EMOJI.equals(buttonText)) {
            actionButton.setText(QUESTION_MARK);
        } else if (QUESTION_MARK.equals(buttonText)) {
            actionButton.setText(BLANK_SPACE);
        }
    }

    private boolean checkOutOfBound(int row, int col) {
        return row >= 0 && row < this.rows && col >= 0 && col < this.cols;
    }

    private void doGameOver() {
        int option = JOptionPane.showConfirmDialog(this,
                "是否重新开始？", "踩雷啦", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.removeAll();
            this.repaint();
            this.startGame();
            this.revalidate();
        }
    }
}
