package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
    private final List<String> moveHistory;
    private final List<String> stateHistory;

    public Game() 
    {
        this.board = new Board();
        this.moveHistory = new ArrayList<>();
        this.stateHistory = new ArrayList<>();
        resetGame();
    }

    public void start() 
    {
        System.out.println("Game started in GUI mode.");
    }

    public void resetGame()
    {
        this.board.initializeBoard();
        this.currentTurn = "white";
        this.moveHistory.clear();
        this.stateHistory.clear();
        this.stateHistory.add(exportFullState());
    }

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

        String snapshotBeforeMove = exportFullState();
        Piece capturedPiece = board.getPiece(to);
        boolean moved = board.movePiece(from, to);

        if (!moved)
        {
            return false;
        }

        if (isCheck(piece.getColor()))
        {
            importFullState(snapshotBeforeMove);
            return false;
        }

        String moveText = buildMoveText(piece, from, to, capturedPiece);
        moveHistory.add(moveText);
        stateHistory.add(exportFullState());
        switchTurn();

        return true;
    }

    private String buildMoveText(Piece piece, Position from, Position to, Piece capturedPiece)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(piece.getColor()).append(" ")
               .append(piece.getClass().getSimpleName())
               .append(" ")
               .append(toCoordinate(from))
               .append(" -> ")
               .append(toCoordinate(to));

        if (capturedPiece != null)
        {
            builder.append(" (captured ")
                   .append(capturedPiece.getColor())
                   .append(" ")
                   .append(capturedPiece.getClass().getSimpleName())
                   .append(")");
        }
        return builder.toString();
    }

    private String toCoordinate(Position position)
    {
        char file = (char) ('A' + position.getColumn());
        int rank = 8 - position.getRow();
        return "" + file + rank;
    }

    // Undoes the last move made in the game.
    public boolean undoLastMove()
    {
        if (stateHistory.size() <= 1)
        {
            return false;
        }

        stateHistory.remove(stateHistory.size() - 1);
        String previous = stateHistory.get(stateHistory.size() - 1);
        importFullState(previous);

        if (!moveHistory.isEmpty())
        {
            moveHistory.remove(moveHistory.size() - 1);
        }

        return true;
    }

    public void saveGame(Path filePath) throws IOException
    {
        Files.writeString(filePath, exportFullState());
    }

    // Loads a game from a file.
    public void loadGame(Path filePath) throws IOException
    {
        String state = Files.readString(filePath);
        importFullState(state);
        stateHistory.clear();
        stateHistory.add(exportFullState());
    }

    private String exportFullState()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("TURN=").append(currentTurn).append('\n');
        for (String move : moveHistory)
        {
            builder.append("MOVE=").append(move).append('\n');
        }
        builder.append(board.exportBoardState());
        return builder.toString();
    }

    private void importFullState(String fullState)
    {
        String[] lines = fullState.split("\\R");
        currentTurn = "white";
        moveHistory.clear();

        StringBuilder boardState = new StringBuilder();
        for (String line : lines)
        {
            if (line.startsWith("TURN="))
            {
                currentTurn = line.substring("TURN=".length()).trim();
            }
            else if (line.startsWith("MOVE="))
            {
                moveHistory.add(line.substring("MOVE=".length()));
            }
            else
            {
                boardState.append(line).append('\n');
            }
        }
        board.importBoardState(boardState.toString());
    }

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

    public boolean isCheckmate(String color)
    {
        if (!hasKing(color))
        {
            return true;
        }
        if (!isCheck(color)) 
        {
            return false;
        }
        return !hasAnyLegalMove(color);
    }

    public boolean hasKing(String color)
    {
        return findKing(color) != null;
    }

    public String getWinnerIfAny()
    {
        if (!hasKing("white"))
        {
            return "black";
        }
        if (!hasKing("black"))
        {
            return "white";
        }
        return null;
    }

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

    // Checks if a square is attacked by a piece of the specified color.
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

                for (Position move : piece.attackSquares(board))
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

    private boolean wouldLeaveKingInCheck(Position from, Position to, String color)
    {
        String snapshot = exportFullState();
        boolean moved = board.movePiece(from, to);
        boolean inCheck = true;
        if (moved)
        {
            inCheck = isCheck(color);
        }
        importFullState(snapshot);
        return inCheck;
    }

    private void switchTurn() 
    {
        if (currentTurn.equals("white")) {
            currentTurn = "black";
        } else {
            currentTurn = "white";
        }
    }

    public Board getBoard() 
    {
        return board;
    }

    public String getCurrentTurn()
    {
        return currentTurn;
    }

    public List<String> getMoveHistory()
    {
        return new ArrayList<>(moveHistory);
    }

    public String getCapturedPiecesText()
    {
        return board.getCapturedPieces();
    }
}