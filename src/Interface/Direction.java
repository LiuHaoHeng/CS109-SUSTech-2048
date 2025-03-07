package Interface;

public enum Direction {
    LEFT(0, -1), RIGHT(0, 1), UP(-1, 0), DOWN(0, 1);

    final int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
