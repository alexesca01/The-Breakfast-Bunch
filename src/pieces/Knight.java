package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a knight chess piece.
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight.
     *
     * @param color the color of the knight
     * @param position the current position
     */
    public Knight(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wN" : "bN";
    }
}