package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
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

    // The king can move one square in any direction, as long as it's not occupied by a piece of the same color.
    @Override
    public List<Position> possibleMoves(Board board)
    {
        List<Position> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        int[][] dirs = 
        {
            {1,0},{-1,0},{0,1},{0,-1},
            {1,1},{1,-1},{-1,1},{-1,-1}
        };

        for (int[] d : dirs) 
        {
            int r = row + d[0];
            int c = col + d[1];

            if (r < 0 || r > 7 || c < 0 || c > 7) continue;

            Piece p = board.getPiece(new Position(r, c));

            if (p == null || !p.getColor().equalsIgnoreCase(color)) 
            {
                moves.add(new Position(r, c));
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() 
    {
        return color.equalsIgnoreCase("white") ? "wK" : "bK";
    }
}