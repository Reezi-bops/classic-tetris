import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
            JFrame frame = new JFrame("Classic Tetris");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Board board = new Board();

            JLabel scoreLabel = new JLabel("Score: 0");
            JLabel levelLabel = new JLabel("Level: 1");

            Font labelFont = new Font("Consolas", Font.BOLD, 18);
            scoreLabel.setFont(labelFont);
            scoreLabel.setForeground(new Color(57, 255, 20));
            levelLabel.setFont(labelFont);
            levelLabel.setForeground(new Color(255, 255, 0));

            JPanel topPanel = new JPanel();
            topPanel.setBackground(Color.BLACK);
            topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
            topPanel.add(scoreLabel);
            topPanel.add(levelLabel);

            board.setScoreLabel(scoreLabel);
            board.setLevelLabel(levelLabel);

            frame.add(topPanel, BorderLayout.SOUTH);
            frame.add(board);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
}