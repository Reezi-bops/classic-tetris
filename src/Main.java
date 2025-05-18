import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

        private static void Loading() {
                JWindow loadingWindow = new JWindow();
                loadingWindow.setSize(500, 120);
                loadingWindow.setLocationRelativeTo(null);
                loadingWindow.setLayout(null);
                loadingWindow.getContentPane().setBackground(Color.BLACK);

                JLabel title = new JLabel("LOADING CLASSIC TETRIS...", SwingConstants.CENTER);
                title.setBounds(0, 10, 500, 30);
                title.setForeground(Color.GREEN);
                title.setFont(new Font("Consolas", Font.BOLD, 22));

                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setBounds(50, 50, 400, 30);
                progressBar.setValue(0);
                progressBar.setForeground(Color.GREEN);
                progressBar.setBackground(Color.DARK_GRAY);
                progressBar.setBorderPainted(false);
                progressBar.setStringPainted(true);
                progressBar.setFont(new Font("Consolas", Font.PLAIN, 14));

                loadingWindow.add(title);
                loadingWindow.add(progressBar);
                loadingWindow.setVisible(true);

                Timer timer = new Timer(80, null);
                timer.addActionListener(new AbstractAction() {
                        int count = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                                count++;
                                progressBar.setValue(count);
                                if (count >= 100) {
                                        timer.stop();
                                        loadingWindow.dispose();
                                }
                        }
                });

                timer.start();

                while (progressBar.getValue() < 100) {
                        try {
                                Thread.sleep(10);
                        } catch (InterruptedException ignored) {}
                }
        }

        private static void Main() {
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

                frame.add(topPanel, BorderLayout.NORTH);
                frame.add(board);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        }

        public static void main(String[] args) {
                Loading();
                Main();
        }
}
