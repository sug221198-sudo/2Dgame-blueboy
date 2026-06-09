package Main;

import javax.swing.*;

public class Main {

    public static JFrame window;
    public static void main(String[] args) {
        window = new JFrame();

        //properly close when user clicks "x" button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // GAME TITLE
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();

        gamePanel.startGameThread();

    }

}
