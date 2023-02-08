package com.chaitin.xray.games.snake;

import java.util.ArrayList;

public class ThreadsController extends Thread {
    ArrayList<ArrayList<DataOfSquare>> Squares;
    Tuple headSnakePos;
    int sizeSnake = 3;
    long speed = 200;
    public static int directionSnake;
    ArrayList<Tuple> positions = new ArrayList<>();
    Tuple foodPosition;

    ThreadsController(Tuple positionDepart) {
        Squares = Window.Grid;
        headSnakePos = new Tuple(positionDepart.x, positionDepart.y);
        directionSnake = 1;
        Tuple headPos = new Tuple(headSnakePos.getX(), headSnakePos.getY());
        positions.add(headPos);
        foodPosition = new Tuple(Window.height - 1, Window.width - 1);
        spawnFood(foodPosition);
    }

    @SuppressWarnings("all")
    public void run() {
        while (true) {
            moveInternal(directionSnake);
            checkCollision();
            moveExternal();
            deleteTail();
            pause();
        }
    }

    private void pause() {
        try {
            sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkCollision() {
        Tuple posCritique = positions.get(positions.size() - 1);
        for (int i = 0; i <= positions.size() - 2; i++) {
            boolean biteItself = posCritique.getX() ==
                    positions.get(i).getX() && posCritique.getY() == positions.get(i).getY();
            if (biteItself) {
                stopTheGame();
            }
        }

        boolean eatingFood = posCritique.getX() ==
                foodPosition.y && posCritique.getY() == foodPosition.x;
        if (eatingFood) {
            sizeSnake = sizeSnake + 1;
            foodPosition = getValAreaNotInSnake();
            spawnFood(foodPosition);
        }
    }

    @SuppressWarnings("all")
    private void stopTheGame() {
        while (true) {
            pause();
        }
    }

    private void spawnFood(Tuple foodPositionIn) {
        Squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1);
    }

    private Tuple getValAreaNotInSnake() {
        Tuple p;
        int ranX = (int) (Math.random() * 19);
        int ranY = (int) (Math.random() * 19);
        p = new Tuple(ranX, ranY);
        for (int i = 0; i <= positions.size() - 1; i++) {
            if (p.getY() == positions.get(i).getX() && p.getX() == positions.get(i).getY()) {
                ranX = (int) (Math.random() * 19);
                ranY = (int) (Math.random() * 19);
                p = new Tuple(ranX, ranY);
                i = 0;
            }
        }
        return p;
    }

    private void moveInternal(int dir) {
        switch (dir) {
            case 4:
                headSnakePos.ChangeData(headSnakePos.x, (headSnakePos.y + 1) % 20);
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
            case 3:
                if (headSnakePos.y - 1 < 0) {
                    headSnakePos.ChangeData(headSnakePos.x, 19);
                } else {
                    headSnakePos.ChangeData(headSnakePos.x, Math.abs(headSnakePos.y - 1) % 20);
                }
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
            case 2:
                if (headSnakePos.x - 1 < 0) {
                    headSnakePos.ChangeData(19, headSnakePos.y);
                } else {
                    headSnakePos.ChangeData(Math.abs(headSnakePos.x - 1) % 20, headSnakePos.y);
                }
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));

                break;
            case 1:
                headSnakePos.ChangeData(Math.abs(headSnakePos.x + 1) % 20, headSnakePos.y);
                positions.add(new Tuple(headSnakePos.x, headSnakePos.y));
                break;
        }
    }

    private void moveExternal() {
        for (Tuple t : positions) {
            int y = t.getX();
            int x = t.getY();
            Squares.get(x).get(y).lightMeUp(0);

        }
    }

    private void deleteTail() {
        int cmp = sizeSnake;
        for (int i = positions.size() - 1; i >= 0; i--) {
            if (cmp == 0) {
                Tuple t = positions.get(i);
                Squares.get(t.y).get(t.x).lightMeUp(2);
            } else {
                cmp--;
            }
        }
        cmp = sizeSnake;
        for (int i = positions.size() - 1; i >= 0; i--) {
            if (cmp == 0) {
                positions.remove(i);
            } else {
                cmp--;
            }
        }
    }
}
