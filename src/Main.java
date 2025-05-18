import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
        private static void Loading() {
                JWindow loadingWindow = new JWindow();
                loadingWindow.setSize(500, 150);
                loadingWindow.setLocationRelativeTo(null);
                loadingWindow.setLayout(null);
                loadingWindow.getContentPane().setBackground(Color.BLACK);

                SoundPlayer loadingMusic = new SoundPlayer();
                loadingMusic.play("loading.wav", false, 1.0f);

                JLabel loadingText = new JLabel("LOADING CLASSIC TETRIS", SwingConstants.CENTER);
                loadingText.setBounds(0, 20, 500, 30);
                loadingText.setForeground(Color.GREEN);
                loadingText.setFont(new Font("Consolas", Font.BOLD, 22));

                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setBounds(50, 70, 400, 30);
                progressBar.setValue(0);
                progressBar.setForeground(Color.GREEN);
                progressBar.setBackground(Color.DARK_GRAY);
                progressBar.setBorderPainted(false);
                progressBar.setStringPainted(true);
                progressBar.setFont(new Font("Consolas", Font.PLAIN, 14));

                loadingWindow.add(loadingText);
                loadingWindow.add(progressBar);
                loadingWindow.setVisible(true);

                Timer textTimer = new Timer(500, null);
                textTimer.addActionListener(new ActionListener() {
                        int dotCount = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                                dotCount = (dotCount + 1) % 4;
                                String dots = ".".repeat(dotCount);
                                loadingText.setText("LOADING CLASSIC TETRIS" + dots);
                        }
                });
                textTimer.start();

                Timer barTimer = new Timer(40, null);
                barTimer.addActionListener(new ActionListener() {
                        int progress = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                                progress++;
                                progressBar.setValue(progress);
                                if (progress >= 100) {
                                        barTimer.stop();
                                        textTimer.stop();
                                        loadingWindow.dispose();
                                }
                        }
                });
                barTimer.start();

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
