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
public class Board 
{

    private final Piece[][] grid;
    private final StringBuilder capturedPieces;

    /**
     * Constructs an empty 8x8 chessboard.
     */
    public Board() 
    {
        this.grid = new Piece[8][8];
        this.capturedPieces = new StringBuilder();
    }

    /**
     * Initializes the board with the standard starting positions.
     */
    public void initializeBoard() 
    {
        clearBoard();
        capturedPieces.setLength(0);
        initializeBlackPieces();
        initializeBlackPawns();
        initializeWhitePawns();
        initializeWhitePieces();
    }

    /**
     * Clears the full board.
     */
    public void clearBoard()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                grid[row][col] = null;
            }
        }
    }

    /**
     * Places black major pieces on the board.
     */
    private void initializeBlackPieces() 
    {
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
    private void initializeBlackPawns() 
    {
        for (int col = 0; col < 8; col++) 
        {
            grid[1][col] = new Pawn("black", new Position(1, col));
        }
    }

    /**
     * Places white pawns on the board.
     */
    private void initializeWhitePawns() 
    {
        for (int col = 0; col < 8; col++) 
        {
            grid[6][col] = new Pawn("white", new Position(6, col));
        }
    }

    /**
     * Places white major pieces on the board.
     */
    private void initializeWhitePieces() 
    {
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
    public Piece getPiece(Position position) 
    {
        if (position == null) 
        {
            return null;
        }
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 0 || row > 7 || col < 0 || col > 7)
        {
            return null;
        }
        return grid[row][col];
    }

    /**
     * Moves a piece from one square to another.
     *
     * @param from the source position
     * @param to the destination position
     * @return true if the move succeeded, false otherwise
     */
    public boolean movePiece(Position from, Position to) 
    {
        if (from == null || to == null) 
        {
            return false;
        }

        Piece movingPiece = getPiece(from);
        if (movingPiece == null) 
        {
            return false;
        }

        Piece destinationPiece = getPiece(to);

        if (destinationPiece != null) 
        {
            if (destinationPiece.getColor().equalsIgnoreCase(movingPiece.getColor())) 
            {
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

    public void removeLastCapturedSymbol(String symbol)
    {
        String token = symbol + " ";
        int lastIndex = capturedPieces.lastIndexOf(token);
        if (lastIndex >= 0)
        {
            capturedPieces.delete(lastIndex, lastIndex + token.length());
        }
    }

    public String getCapturedPieces()
    {
        return capturedPieces.toString().trim();
    }

    public void setCapturedPieces(String captured)
    {
        capturedPieces.setLength(0);
        if (captured != null && !captured.isBlank())
        {
            capturedPieces.append(captured.trim()).append(" ");
        }
    }

    public void setPiece(Position position, Piece piece)
    {
        if (position == null)
        {
            return;
        }
        grid[position.getRow()][position.getColumn()] = piece;
        if (piece != null)
        {
            piece.move(new Position(position.getRow(), position.getColumn()));
        }
    }

    /**
     * Serializes the board into a simple text format.
     */
    public String exportBoardState()
    {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Piece piece = grid[row][col];
                builder.append(piece == null ? "--" : piece.getSymbol());
                if (col < 7)
                {
                    builder.append(',');
                }
            }
            builder.append('\n');
        }
        builder.append("CAPTURED=").append(getCapturedPieces());
        return builder.toString();
    }

    /**
     * Restores the board from a saved text format.
     */
    public void importBoardState(String state)
    {
        clearBoard();
        capturedPieces.setLength(0);

        String[] lines = state.split("\\R");
        for (int row = 0; row < 8 && row < lines.length; row++)
        {
            String[] tokens = lines[row].split(",");
            for (int col = 0; col < 8 && col < tokens.length; col++)
            {
                String token = tokens[col].trim();
                if (!"--".equals(token))
                {
                    grid[row][col] = createPieceFromSymbol(token, row, col);
                }
            }
        }

        for (String line : lines)
        {
            if (line.startsWith("CAPTURED="))
            {
                setCapturedPieces(line.substring("CAPTURED=".length()));
                break;
            }
        }
    }

    private Piece createPieceFromSymbol(String symbol, int row, int col)
    {
        String color = Character.toLowerCase(symbol.charAt(0)) == 'w' ? "white" : "black";
        char type = Character.toLowerCase(symbol.charAt(1));
        Position position = new Position(row, col);

        switch (type)
        {
            case 'p': return new Pawn(color, position);
            case 'r': return new Rook(color, position);
            case 'n': return new Knight(color, position);
            case 'b': return new Bishop(color, position);
            case 'q': return new Queen(color, position);
            case 'k': return new King(color, position);
            default: return null;
        }
    }

    /**
     * Displays the board in the console.
     */
    public void display() 
    {
        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("  +---+---+---+---+---+---+---+---+");

        for (int row = 0; row < 8; row++) 
        {
            System.out.print((8 - row) + " |");

            for (int col = 0; col < 8; col++) 
            {
                String content;

                if (grid[row][col] == null) 
                {
                    content = ((row + col) % 2 == 0) ? "  " : "##";
                } 
                else 
                {
                    content = grid[row][col].getSymbol();
                }
                System.out.print(String.format(" %2s|", content));
            }

            System.out.println(" " + (8 - row));
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }

        System.out.println("    A   B   C   D   E   F   G   H");
        System.out.println("Captured pieces: " + capturedPieces.toString());
    }
}