package utils;

/**
 * Represents a position on the chessboard.
 */
public class Position {

    private int row;
    private int column;

    /**
     * Constructs a Position.
     *
     * @param row the row index
     * @param column the column index
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column.
     *
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the row.
     *
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column.
     *
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Position[row=" + row + ", column=" + column + "]";
    }
}