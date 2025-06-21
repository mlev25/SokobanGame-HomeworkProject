package util;

public enum Moves {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    public final int dRow;
    public final int dCol;

    Moves(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }
}
