package Main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        //properly close when user clicks "x" button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口大小不变
        window.setResizable(false);
        //game title
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        //此时需要规定位置 否则物体会出现在正中间
        window.setLocationRelativeTo(null);
        //visiable
        window.setVisible(true);

        gamePanel.setupGame();

        gamePanel.startGameThread();

    }

}
