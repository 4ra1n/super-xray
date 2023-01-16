package com.chaitin.xray.pocker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@SuppressWarnings("all")
public class Main extends JFrame implements ActionListener {
    public Container container = null;
    JMenuItem start, exit, about;
    JButton landlord[] = new JButton[2];
    JButton publishCard[] = new JButton[2];
    int dizhuFlag;
    int turn;
    JLabel dizhu;
    List<Card> currentList[] = new Vector[3];
    List<Card> playerList[] = new Vector[3];

    List<Card> lordList;

    Card card[] = new Card[56];
    JTextField time[] = new JTextField[3];
    Time t;
    boolean nextPlayer = false;

    public void SetMenu() {
        landlord[0] = new JButton("抢地主");
        landlord[1] = new JButton("不     抢");
        publishCard[0] = new JButton("出牌");
        publishCard[1] = new JButton("不要");
        for (int i = 0; i < 2; i++) {
            publishCard[i].setBounds(320 + i * 100, 400, 60, 20);
            landlord[i].setBounds(320 + i * 100, 400, 75, 20);
            container.add(landlord[i]);
            landlord[i].addActionListener(this);
            landlord[i].setVisible(false);
            container.add(publishCard[i]);
            publishCard[i].setVisible(false);
            publishCard[i].addActionListener(this);
        }
        for (int i = 0; i < 3; i++) {
            time[i] = new JTextField("倒计时:");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 60, 20);
        time[1].setBounds(374, 360, 60, 20);
        time[2].setBounds(620, 230, 60, 20);

        for (int i = 0; i < 3; i++) {
            currentList[i] = new Vector<Card>();
        }
    }

    public Main() {
        try {
            Init();
            SetMenu();
            this.setVisible(true);
            CardInit();
            getLord();
            time[1].setVisible(true);
            t = new Time(this, 10);
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void Init() {
        this.setTitle("斗地主");
        this.setSize(830, 620);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(0, 112, 26));
    }

    public void CardInit() throws IOException {

        int count = 1;

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 13; j++) {
                if ((i == 5) && (j > 2))
                    break;
                else {
                    card[count] = new Card(this, i + "-" + j, false);
                    card[count].setLocation(350, 50);
                    container.add(card[count]);
                    count++;
                }
            }
        }

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int a = random.nextInt(54) + 1;
            int b = random.nextInt(54) + 1;

            Card k = card[a];
            card[a] = card[b];
            card[b] = k;
        }


        for (int i = 0; i < 3; i++)
            playerList[i] = new Vector<Card>();

        lordList = new Vector<Card>();
        int t = 0;
        for (int i = 1; i <= 54; i++) {
            if (i >= 52) {
                Common.move(card[i], card[i].getLocation(), new Point(300 + (i - 52) * 80, 10));
                lordList.add(card[i]);
                continue;
            }

            switch ((t++) % 3) {
                case 0:

                    Common.move(card[i], card[i].getLocation(), new Point(50, 60 + i * 5));
                    playerList[0].add(card[i]);
                    break;
                case 1:

                    Common.move(card[i], card[i].getLocation(), new Point(180 + i * 7, 450));
                    playerList[1].add(card[i]);
                    card[i].turnFront();
                    break;
                case 2:

                    Common.move(card[i], card[i].getLocation(), new Point(700, 60 + i * 5));
                    playerList[2].add(card[i]);
                    break;
            }

            container.setComponentZOrder(card[i], 0);
        }

        for (int i = 0; i < 3; i++) {
            Common.order(playerList[i]);
            Common.rePosition(this, playerList[i], i);
        }
        dizhu = new JLabel(new ImageIcon(ImageIO.read(
                this.getClass().getClassLoader().getResourceAsStream("game/pocker/images/dizhu.gif"))));
        dizhu.setVisible(false);
        dizhu.setSize(40, 40);
        container.add(dizhu);
    }


    public void getLord() {
        for (int i = 0; i < 2; i++)
            landlord[i].setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == landlord[0]) {
            time[1].setText("抢地主");
            t.isRun = false;
        }
        if (e.getSource() == landlord[1]) {
            time[1].setText("不抢");
            t.isRun = false;
        }

        if (e.getSource() == publishCard[1]) {
            this.nextPlayer = true;
            currentList[1].clear();
            time[1].setText("不要");
        }

        if (e.getSource() == publishCard[0]) {
            List<Card> c = new Vector<Card>();

            for (int i = 0; i < playerList[1].size(); i++) {
                Card card = playerList[1].get(i);
                if (card.clicked) {
                    c.add(card);
                }
            }
            int flag = 0;


            if (time[0].getText().equals("不要") && time[2].getText().equals("不要")) {
                if (Common.jugdeType(c) != CardType.c0)
                    flag = 1;
            } else {
                flag = Common.checkCards(c, currentList, this);
            }


            if (flag == 1) {
                currentList[1] = c;
                playerList[1].removeAll(currentList[1]);

                Point point = new Point();
                point.x = (770 / 2) - (currentList[1].size() + 1) * 15 / 2;
                point.y = 300;
                for (int i = 0, len = currentList[1].size(); i < len; i++) {
                    Card card = currentList[1].get(i);
                    Common.move(card, card.getLocation(), point);
                    point.x += 15;
                }


                Common.rePosition(this, playerList[1], 1);
                time[1].setVisible(false);
                this.nextPlayer = true;
            }

        }
    }
}
