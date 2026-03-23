package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a queen chess piece.
 */
public class Queen extends Piece {

    /**
     * Constructs a Queen.
     *
     * @param color the color of the queen
     * @param position the current position
     */
    public Queen(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wQ" : "bQ";
    }
}