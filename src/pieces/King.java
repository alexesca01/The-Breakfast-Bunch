package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a king chess piece.
 */
public class King extends Piece {

    /**
     * Constructs a King.
     *
     * @param color the color of the king
     * @param position the current position
     */
    public King(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wK" : "bK";
    }
}