package pieces;

import java.util.List;

import utils.Position;

/**
 * Abstract parent class for all chess pieces.
 */
public abstract class Piece {

    protected String color;
    protected Position position;

    /**
     * Constructs a chess piece.
     *
     * @param color the color of the piece
     * @param position the current position of the piece
     */
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns a list of possible moves for this piece.
     * Phase 1 uses placeholder logic.
     *
     * @return a list of possible positions
     */
    public abstract List<Position> possibleMoves();

    /**
     * Moves this piece to a new position.
     *
     * @param newPosition the new position
     */
    public void move(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Returns the color of the piece.
     *
     * @return the piece color
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the current position of the piece.
     *
     * @return the current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the two-character display symbol for the piece.
     *
     * @return the symbol shown on the board
     */
    public abstract String getSymbol();
}