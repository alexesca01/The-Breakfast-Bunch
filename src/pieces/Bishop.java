package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a bishop chess piece.
 */
public class Bishop extends Piece {

    /**
     * Constructs a Bishop.
     *
     * @param color the color of the bishop
     * @param position the current position
     */
    public Bishop(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wB" : "bB";
    }
}