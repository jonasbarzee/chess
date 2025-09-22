package chess.piecemoves;

public enum Direction {
    NORTH(1, 0),
    NORTHEAST(1, 1),
    NORTHWEST(1, -1),
    SOUTH(-1, 0),
    SOUTHEAST(-1, 1),
    SOUTHWEST(-1, -1),
    EAST(0, 1),
    WEST(0, -1);


    private final int rowChange;
    private final int colChange;

    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }
    public int getRowChange() {
        return rowChange;
    }
    public int getColChange() {
        return colChange;
    }
}
