package ee.ticktacktoeu;


public class Coordinate {
    private final int row;
    private final int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (row != that.row) return false;
        return col == that.col;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int[] getCoordinates() {
        return new int[]{row, col};
    }
}
