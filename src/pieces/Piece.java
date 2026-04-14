package pieces;

import java.util.List;

import board.Board;
import utils.Position;

public abstract class Piece {

    protected String color;
    protected Position position;

    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    public abstract List<Position> possibleMoves(Board board);

    public void move(Position newPosition) {
        this.position = newPosition;
    }

    public String getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public abstract String getSymbol();

    /**
     * Prevents moves going off the board.
     */
    protected void addIfValid(List<Position> moves, int row, int col)
    {
        if (row >= 0 && row < 8 && col >= 0 && col < 8)
        {
            moves.add(new Position(row, col));
        }
    }
}