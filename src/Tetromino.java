import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tetromino {
    private final Point[] blocks;

    private static final Point[][] SHAPES = {
            {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)}, // O
            {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0)}, // I
            {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(-1, 1)}, // Z
            {new Point(0, 0), new Point(-1, 0), new Point(0, 1), new Point(1, 1)}, // S
            {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1)}, // T
            {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(0, 1)}, // L
            {new Point(0, 0), new Point(-1, 0), new Point(-2, 0), new Point(0, 1)}  // mirrored L
    };

    private static List<Point[]> bag = new ArrayList<>();

    private Tetromino(Point[] blocks) {
        this.blocks = blocks;
    }

    public Point[] getBlocks() {
        return blocks;
    }

    public Tetromino rotate() {
        Point[] rb = new Point[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            rb[i] = new Point(-blocks[i].y, blocks[i].x);
        }
        return new Tetromino(rb);
    }

    public static Tetromino randomPiece() {
        if (bag.isEmpty()) refillBag();
        return new Tetromino(bag.remove(0));
    }

    private static void refillBag() {
        bag.clear();
        for (Point[] shape : SHAPES) {
            bag.add(shape);
        }
        Collections.shuffle(bag);
    }
}
