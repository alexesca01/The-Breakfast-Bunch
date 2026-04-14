package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
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

    // The knight moves in an L shape: 2 squares in one direction and then 1 square perpendicular. It can jump over pieces.
    @Override
    public List<Position> possibleMoves(Board board)
    {
        List<Position> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        int[][] offsets = 
        {
            {2,1},{2,-1},{-2,1},{-2,-1},
            {1,2},{1,-2},{-1,2},{-1,-2}
        };

        for (int[] o : offsets)
        {
            int r = row + o[0];
            int c = col + o[1];

            if (r < 0 || r > 7 || c < 0 || c > 7)
                continue;

            Piece target = board.getPiece(new Position(r, c));

            if (target == null || !target.getColor().equalsIgnoreCase(color))
            {
                moves.add(new Position(r, c));
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() 
    {
        return color.equalsIgnoreCase("white") ? "wN" : "bN";
    }
}