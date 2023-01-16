package com.chaitin.xray.games.sweep;

public interface Constant {
    String TITLE = "扫雷";
    int BLOCK_WIDTH = 50;
    int BLOCK_HEIGHT = 50;
    int PANEL_ROW_NUMBER = 18;
    int PANEL_COL_NUMBER = 30;
    String BOMB_EMOJI = "\uD83D\uDCA3";
    String FLAG_EMOJI = "\uD83D\uDEA9";
    String QUESTION_MARK = "❓";
    String BLANK_SPACE = "";
    int[][] OFFSETS = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
}
