package pieces;

import java.util.ArrayList;
import java.util.List;

import utils.Position;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends Piece {

    /**
     * Constructs a Pawn.
     *
     * @param color the color of the pawn
     * @param position the current position
     */
    public Pawn(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wp" : "bp";
    }
}