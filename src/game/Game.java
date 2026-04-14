package game;

import board.Board;
import pieces.King;
import pieces.Piece;
import utils.Position;

/**
 * Controls the flow of the chess game.
 */
public class Game 
{
    private final Board board;
    private String currentTurn;

    // Constructs a new game with an initialized board and sets the starting turn to white.
    public Game() 
    {
        this.board = new Board();
        this.board.initializeBoard();
        this.currentTurn = "white";
    }

    /**
     * Starts the game. In GUI mode, this initializes the game 
     * state and waits for user interaction.
     * Console loop from Phase 1 removed for GUI mode.
     */
    public void start() 
    {
        System.out.println("Game started in GUI mode.");
    }

    /**
     * Attempts to move a piece from one position to another.
     * Used by the GUI instead of console input.
     */
    public boolean move(Position from, Position to) 
    {
        if (from == null || to == null) {
            return false;
        }

        Piece piece = board.getPiece(from);

        if (piece == null) {
            return false;
        }

        if (!piece.getColor().equalsIgnoreCase(currentTurn)) {
            return false;
        }

        boolean isLegal = false;

        for (Position p : piece.possibleMoves(board)) 
        {
            if (p.getRow() == to.getRow() &&
                p.getColumn() == to.getColumn()) 
            {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) 
        {
            return false;
        }

        boolean moved = board.movePiece(from, to);

        if (moved) 
        {
            switchTurn();
        }

        return moved;
    }

    // Placeholder check detection for future phases.
    public boolean isCheck(String color)
    {
        Position kingPosition = findKing(color);
        if (kingPosition == null) 
        {
            return false;
        }

        String opponent = color.equalsIgnoreCase("white") ? "black" : "white";
        return isSquareAttacked(kingPosition, opponent);
    }

    // Placeholder checkmate detection for future phases.
    public boolean isCheckmate(String color)
    {
        if (!isCheck(color)) 
        {
            return false;
        }
        return !hasAnyLegalMove(color);
    }

    // Finds the position of the king for the given color.
    private Position findKing(String color)
    {
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece instanceof King && piece.getColor().equalsIgnoreCase(color)) 
                {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    // Checks if a given square is attacked by any piece of the specified color.
    private boolean isSquareAttacked(Position square, String byColor)
    {
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece == null || !piece.getColor().equalsIgnoreCase(byColor)) 
                {
                    continue;
                }

                for (Position move : piece.possibleMoves(board)) 
                {
                    if (move.getRow() == square.getRow() && move.getColumn() == square.getColumn()) 
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Checks if the given color has any legal moves available to escape check.
    private boolean hasAnyLegalMove(String color)
    {
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                Position from = new Position(row, col);
                Piece piece = board.getPiece(from);

                if (piece == null || !piece.getColor().equalsIgnoreCase(color)) 
                {
                    continue;
                }

                for (Position to : piece.possibleMoves(board)) 
                {
                    Piece destination = board.getPiece(to);

                    if (destination != null &&
                        destination.getColor().equalsIgnoreCase(color)) 
                    {
                        continue;
                    }

                    if (!wouldLeaveKingInCheck(from, to, color))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Simulates a move to check if it would leave the king in check, then reverts the move.
    private boolean wouldLeaveKingInCheck(Position from, Position to, String color)
    {
        Piece movingPiece = board.getPiece(from);
        Piece capturedPiece = board.getPiece(to);

        if (movingPiece == null) 
        {
            return true;
        }

        board.movePiece(from, to);
        boolean inCheck = isCheck(color);

        board.movePiece(to, from);
        if (capturedPiece != null) 
        {
            restoreCapturedPiece(to, capturedPiece);
        }

        return inCheck;
    }

    // Restores a captured piece back to the board after simulating a move.
    private void restoreCapturedPiece(Position position, Piece piece)
    {
        board.setPiece(position, piece);
    }

    // Switches the current turn to the other player.
    private void switchTurn() 
    {
        if (currentTurn.equals("white")) {
            currentTurn = "black";
        } else {
            currentTurn = "white";
        }
    }

    // Getters for board and current turn, used by the GUI.
    public Board getBoard() 
    {
        return board;
    }

    public String getCurrentTurn()
    {
        return currentTurn;
    }
}