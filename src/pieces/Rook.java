package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a rook chess piece.
 */
public class Rook extends Piece {

    /**
     * Constructs a Rook.
     *
     * @param color the color of the rook
     * @param position the current position
     */
    public Rook(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wR" : "bR";
    }
}