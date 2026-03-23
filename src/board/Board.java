package board;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import utils.Position;

/**
 * Represents the chessboard and stores all pieces.
 */
public class Board {

    private final Piece[][] grid;
    private final StringBuilder capturedPieces;

    /**
     * Constructs an empty 8x8 chessboard.
     */
    public Board() {
        this.grid = new Piece[8][8];
        this.capturedPieces = new StringBuilder();
    }

    /**
     * Initializes the board with the standard starting positions.
     */
    public void initializeBoard() {
        initializeBlackPieces();
        initializeBlackPawns();
        initializeWhitePawns();
        initializeWhitePieces();
    }

    /**
     * Places black major pieces on the board.
     */
    private void initializeBlackPieces() {
        grid[0][0] = new Rook("black", new Position(0, 0));
        grid[0][1] = new Knight("black", new Position(0, 1));
        grid[0][2] = new Bishop("black", new Position(0, 2));
        grid[0][3] = new Queen("black", new Position(0, 3));
        grid[0][4] = new King("black", new Position(0, 4));
        grid[0][5] = new Bishop("black", new Position(0, 5));
        grid[0][6] = new Knight("black", new Position(0, 6));
        grid[0][7] = new Rook("black", new Position(0, 7));
    }

    /**
     * Places black pawns on the board.
     */
    private void initializeBlackPawns() {
        for (int col = 0; col < 8; col++) {
            grid[1][col] = new Pawn("black", new Position(1, col));
        }
    }

    /**
     * Places white pawns on the board.
     */
    private void initializeWhitePawns() {
        for (int col = 0; col < 8; col++) {
            grid[6][col] = new Pawn("white", new Position(6, col));
        }
    }

    /**
     * Places white major pieces on the board.
     */
    private void initializeWhitePieces() {
        grid[7][0] = new Rook("white", new Position(7, 0));
        grid[7][1] = new Knight("white", new Position(7, 1));
        grid[7][2] = new Bishop("white", new Position(7, 2));
        grid[7][3] = new Queen("white", new Position(7, 3));
        grid[7][4] = new King("white", new Position(7, 4));
        grid[7][5] = new Bishop("white", new Position(7, 5));
        grid[7][6] = new Knight("white", new Position(7, 6));
        grid[7][7] = new Rook("white", new Position(7, 7));
    }

    /**
     * Returns the piece at the given position.
     *
     * @param position the requested board position
     * @return the piece at that position, or null if empty
     */
    public Piece getPiece(Position position) {
        if (position == null) {
            return null;
        }
        return grid[position.getRow()][position.getColumn()];
    }

    /**
     * Moves a piece from one square to another.
     * This Phase 1 version performs only basic movement and capture handling.
     *
     * @param from the source position
     * @param to the destination position
     * @return true if the move succeeded, false otherwise
     */
    public boolean movePiece(Position from, Position to) {
        if (from == null || to == null) {
            return false;
        }

        Piece movingPiece = getPiece(from);
        if (movingPiece == null) {
            return false;
        }

        Piece destinationPiece = getPiece(to);

        if (destinationPiece != null) {
            if (destinationPiece.getColor().equalsIgnoreCase(movingPiece.getColor())) {
                System.out.println("You cannot move onto your own piece.");
                return false;
            }
            capturedPieces.append(destinationPiece.getSymbol()).append(" ");
        }

        grid[to.getRow()][to.getColumn()] = movingPiece;
        grid[from.getRow()][from.getColumn()] = null;
        movingPiece.move(to);

        return true;
    }

    /**
     * Placeholder check detection for future phases.
     *
     * @param color the color to check
     * @return false in this Phase 1 implementation
     */
    public boolean isCheck(String color) {
        return false;
    }

    /**
     * Placeholder checkmate detection for future phases.
     *
     * @param color the color to check
     * @return false in this Phase 1 implementation
     */
    public boolean isCheckmate(String color) {
        return false;
    }

    /**
     * Displays the board in the console.
     */
    public void display() {
        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("  +---+---+---+---+---+---+---+---+");

        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " |");
            for (int col = 0; col < 8; col++) {
                if (grid[row][col] == null) {
                    System.out.print("## |");
                } else {
                    System.out.print(grid[row][col].getSymbol() + "|");
                }
            }
            System.out.println(" " + (8 - row));
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }

        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("Captured pieces: " + capturedPieces.toString());
    }
}