package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
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

    // The pawn moves forward 1 square, or 2 squares from starting position. It captures diagonally.
    @Override
    public List<Position> possibleMoves(Board board)
    {
        List<Position> moves = new ArrayList<>();

        int dir = color.equalsIgnoreCase("white") ? -1 : 1;

        int row = position.getRow();
        int col = position.getColumn();

        int r1 = row + dir;
        if (board.getPiece(new Position(r1, col)) == null) 
        {
            moves.add(new Position(r1, col));
        }

        int r2 = row + 2 * dir;
        if ((color.equalsIgnoreCase("white") && row == 6 || color.equalsIgnoreCase("black") && row == 1)
            && board.getPiece(new Position(r1, col)) == null && board.getPiece(new Position(r2, col)) == null)
        {
            moves.add(new Position(r2, col));
        }

        int[] dc = {-1, 1};
        for (int d : dc)
        {
            int c = col + d;
            if (c >= 0 && c < 8) 
            {
                Position p = new Position(r1, c);
                Piece target = board.getPiece(p);
                if (target != null && !target.getColor().equalsIgnoreCase(color)) 
                {
                    moves.add(p);
                }
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() 
    {
        return color.equalsIgnoreCase("white") ? "wp" : "bp";
    }
}