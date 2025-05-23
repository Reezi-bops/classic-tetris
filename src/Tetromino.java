import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tetromino {
    private final Point[] blocks;

    private Tetromino(Point[] blocks) {
        this.blocks = blocks;
    }

    private static final Point[][] SHAPES = {
            {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)},
            {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0)},
            {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(-1, 1)},
            {new Point(0, 0), new Point(-1, 0), new Point(0, 1), new Point(1, 1)},
            {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1)},
            {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(0, 1)},
            {new Point(0, 0), new Point(-1, 0), new Point(-2, 0), new Point(0, 1)}
    };

    public Point[] getBlocks() {
        return blocks;
    }

    private static List<Point[]> bag = new ArrayList<>();

    public Tetromino rotate() {
        Point[] rb = new Point[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            rb[i] = new Point(-blocks[i].y, blocks[i].x);
        }
        return new Tetromino(rb);
    }

    private static void refillBag() {
        bag.clear();
        for (Point[] shape : SHAPES) {
            bag.add(shape);
        }
        Collections.shuffle(bag);
    }

    public static Tetromino randomPiece() {
        if (bag.isEmpty()) {
            refillBag();
        }
        return new Tetromino(bag.remove(0));
    }
}
