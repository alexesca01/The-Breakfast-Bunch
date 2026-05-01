package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
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
    public List<Position> possibleMoves(Board board)
    {
        List<Position> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        int[][] dirs = 
        {
            {1,0}, {-1,0}, {0,1}, {0,-1},
            {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };

        for (int[] d : dirs)
        {
            int r = row;
            int c = col;

            while (true)
            {
                r += d[0];
                c += d[1];

                if (r < 0 || r > 7 || c < 0 || c > 7)
                    break;

                Position p = new Position(r, c);
                Piece target = board.getPiece(p);

                if (target == null)
                {
                    moves.add(p);
                }
                else
                {
                    if (!target.getColor().equalsIgnoreCase(color))
                        moves.add(p);

                    break;
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return color.equalsIgnoreCase("white") ? "wQ" : "bQ";
    }
}