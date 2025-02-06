package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int SIZE = 20;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point apple;
    private char direction = 'R'; // R: Right, L: Left, U: Up, D: Down
    private boolean running = false;
    private boolean paused = false; // Flag to check if the game is paused
    private Timer timer;
    private int score = 0; // Score variable
    private int gameSpeed; // Variable for storing the speed

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        // Get the difficulty from the user
        String difficulty = getDifficultyLevel();
        setGameSpeed(difficulty);

        startGame();
    }

    public void startGame() {
        running = true;
        paused = false; // Ensure game is not paused on start
        score = 0; // Reset score
        snake.clear();
        snake.add(new Point(5, 5));
        placeApple();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(gameSpeed, this); // Set timer with the chosen speed
        timer.start();
    }

    public String getDifficultyLevel() {
        String[] options = {"Easy", "Medium", "Hard"};
        int response = JOptionPane.showOptionDialog(
                this,
                "Select Difficulty Level:",
                "Snake Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );
        return options[response];
    }

    public void setGameSpeed(String difficulty) {
        switch (difficulty) {
            case "Easy":
                gameSpeed = 150; // Slow speed for easy
                break;
            case "Medium":
                gameSpeed = 100; // Normal speed for medium
                break;
            case "Hard":
                gameSpeed = 50;  // Fast speed for hard
                break;
            default:
                gameSpeed = 100; // Default to medium
                break;
        }
    }

    public void placeApple() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / SIZE);
        int y = rand.nextInt(HEIGHT / SIZE);
        apple = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            if (paused) {
                showPauseMenu(g); // Display pause menu when game is paused
            } else {
                draw(g);
            }
        } else {
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.yellow);
        g.fillRect(apple.x * SIZE, apple.y * SIZE, SIZE, SIZE);

        g.setColor(Color.red);
        for (Point point : snake) {
            g.fillRect(point.x * SIZE, point.y * SIZE, SIZE, SIZE);
        }

        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, 10, 20);
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.y--;
                break;
            case 'D':
                newHead.y++;
                break;
            case 'L':
                newHead.x--;
                break;
            case 'R':
                newHead.x++;
                break;
        }

        snake.add(0, newHead);
        if (newHead.equals(apple)) {
            score++;
            placeApple();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollisions() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH / SIZE || head.y < 0 || head.y >= HEIGHT / SIZE || snake.subList(1, snake.size()).contains(head)) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        String message = "Game Over! Press R to Restart";
        g.setColor(Color.WHITE);
        g.drawString(message, WIDTH / 2 - g.getFontMetrics().stringWidth(message) / 2, HEIGHT / 2);
        g.drawString("Final Score: " + score, WIDTH / 2 - g.getFontMetrics().stringWidth("Final Score: " + score) / 2, HEIGHT / 2 + 20);
        
        if (score < 10) {
            String noobMessage = "Noob!";
            g.drawString(noobMessage, WIDTH / 2 - g.getFontMetrics().stringWidth(noobMessage) / 2, HEIGHT / 2 + 40);
        }
    }

    public void showPauseMenu(Graphics g) {
        String pauseMessage = "Game Paused: Press P to Resume";
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, 30); // Draw the pause menu bar
        g.setColor(Color.WHITE);
        g.drawString(pauseMessage, WIDTH / 2 - g.getFontMetrics().stringWidth(pauseMessage) / 2, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) { // Only move if not paused
            move();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_R:
                    if (!running) {
                        startGame();
                    }
                    break;
                case KeyEvent.VK_P:
                    paused = !paused; // Toggle pause
                    break;
            }
        }
    }
}
