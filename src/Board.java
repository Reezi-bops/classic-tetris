import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class Board extends JPanel implements ActionListener {
    private JLabel scoreLabel, levelLabel;
    private Tetromino tetris;
    private final Timer timer;
    private int moveX, moveY, score = 0,  level = 1;
    private List<Integer> flashRows = new ArrayList<>();
    private static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
    private boolean[][] boardGrid = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
    private boolean flashing = false;

    public Board() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (tetris == null) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> moveIfValid(moveX - 1, moveY, tetris);
                    case KeyEvent.VK_RIGHT -> moveIfValid(moveX + 1, moveY, tetris);
                    case KeyEvent.VK_DOWN -> {
                        if (!moveIfValid(moveX, moveY + 1, tetris)) lockPiece();
                    }
                    case KeyEvent.VK_UP -> {
                        Tetromino rotated = tetris.rotate();
                        if (isValid(moveX, moveY, rotated)) tetris = rotated;
                    }
                    case KeyEvent.VK_SPACE -> {
                        while (isValid(moveX, moveY + 1, tetris)) moveY++;
                        lockPiece();
                    }
                }
                repaint();
            }
        });

        timer = new Timer(500, this);
        timer.start();
        spawnPiece();
    }

    public void setScoreLabel(JLabel label) {
        this.scoreLabel = label;
    }

    public void setLevelLabel(JLabel label) {
        this.levelLabel = label;
    }

    private void updateLabels() {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
        if (levelLabel != null) {
            levelLabel.setText("Level: " + level);
        }
    }

    private void spawnPiece() {
        tetris = Tetromino.randomPiece();
        moveX = BOARD_WIDTH / 2 - 1;
        moveY = 0;
        if (!isValid(moveX, moveY, tetris)) {
            timer.stop();
            int option = JOptionPane.showOptionDialog(this, "Game Over!\nScore: " + score,
                    "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new String[]{"Restart", "Exit"}, "Restart");
            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void resetGame() {
        boardGrid = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
        score = 0;
        level = 1;
        updateLabels();
        timer.setDelay(500);
        spawnPiece();
        timer.start();
    }

    private boolean moveIfValid(int x, int y, Tetromino piece) {
        if (isValid(x, y, piece)) {
            moveX = x;
            moveY = y;
            return true;
        }
        return false;
    }

    private boolean isValid(int x, int y, Tetromino piece) {
        for (Point p : piece.getBlocks()) {
            int nx = x + p.x, ny = y + p.y;
            if (nx < 0 || nx >= BOARD_WIDTH || ny < 0 || ny >= BOARD_HEIGHT) {
                return false;
            }
            if (boardGrid[ny][nx]) {
                return false;
            }
        }
        return true;
    }

    private void lockPiece() {
        for (Point p : tetris.getBlocks()) {
            boardGrid[moveY + p.y][moveX + p.x] = true;
        }
        checkClearLines();
        spawnPiece();
    }

    private void checkClearLines() {
        int linesCleared = 0;
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (!boardGrid[row][col]) {
                    full = false;
                    break;
                }
            }
            if (full) {
                flashRows.add(row);
                linesCleared++;
            }
        }

        if (!flashRows.isEmpty()) {
            flashing = true;
            timer.stop();

            final int lines = linesCleared;

            Timer flash = new Timer(150, null);
            flash.addActionListener(e -> {
                for (int row : flashRows) {
                    for (int col = 0; col < BOARD_WIDTH; col++) {
                        boardGrid[row][col] = false;
                    }
                }
                flashRows.sort(Integer::compareTo);
                for (int clearedRow : flashRows) {
                    for (int row = clearedRow; row > 0; row--) {
                        boardGrid[row] = boardGrid[row - 1].clone();
                    }
                    boardGrid[0] = new boolean[BOARD_WIDTH];
                }

                score += lines * 100;
                level = score / 500 + 1;
                timer.setDelay(Math.max(100, 500 - (level - 1) * 30));
                updateLabels();
                flashRows.clear();
                flashing = false;
                timer.start();
                repaint();
                flash.stop();
            });
            flash.setRepeats(false);
            flash.start();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (boardGrid[y][x]) drawBlock(g, x, y);
            }
        }

        if (flashing) {
            g.setColor(Color.WHITE);
            for (int row : flashRows) {
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    drawBlock(g, x, row);
                }
            }
        }

        if (tetris != null && !flashing) {
            g.setColor(Color.MAGENTA);
            for (Point p : tetris.getBlocks()) {
                drawBlock(g, moveX + p.x, moveY + p.y);
            }
        }

        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            g.drawLine(x * BLOCK_SIZE, 0, x * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            g.drawLine(0, y * BLOCK_SIZE, BOARD_WIDTH * BLOCK_SIZE, y * BLOCK_SIZE);
        }
    }

    private void drawBlock(Graphics g, int x, int y) {
        g.fill3DRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!moveIfValid(moveX, moveY + 1, tetris)) {
            lockPiece();
        }
        repaint();
    }
}