package snake;

import javax.swing.*;

public class GameFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();

        // Set the frame to fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen mode
        frame.setUndecorated(true);                    // Hide title bar
        frame.add(gamePanel);
        frame.pack();

        frame.setLocationRelativeTo(null);             // Center the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
