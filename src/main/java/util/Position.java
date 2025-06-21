package util;

public record Position(int row, int column) {
    public Position move(Moves dir) {
        return switch (dir) {
            case UP -> new Position(row - 1, column);
            case DOWN -> new Position(row + 1, column);
            case LEFT -> new Position(row, column - 1);
            case RIGHT -> new Position(row, column + 1);
        };
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, column);
    }
}
