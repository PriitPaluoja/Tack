package ee.ticktacktoeu;


import java.io.Serializable;

class Coordinate implements Serializable {
    private final int row;
    private final int col;

    Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
