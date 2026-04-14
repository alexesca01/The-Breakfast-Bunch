package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Game;
import pieces.Piece;
import utils.Position;

public class ChessGUI extends JFrame 
{
    private Game game;

    private JButton[][] squares = new JButton[8][8];

    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessGUI(Game game)
    {
        this.game = game;

        setTitle("Chess Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initializeSquares();
        refreshBoard();

        setVisible(true);
    }

    private void initializeSquares() 
    {
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                JButton square = new JButton();
                square.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));

                if ((row + col) % 2 == 0) 
                {
                    square.setBackground(Color.WHITE);
                } 
                else
                {
                    square.setBackground(Color.GRAY);
                }
                int r = row;
                int c = col;

                square.addActionListener(e -> handleClick(r, c));

                squares[row][col] = square;
                add(square);
            }
        }   
    }

    // Updates the display of a single square after a move.
    private void updateSquare(int row, int col) 
    {
        Position pos = new Position(row, col);
        Piece piece = game.getBoard().getPiece(pos);

        JButton button = squares[row][col];

        button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
        button.setText(piece != null ? getUnicodePiece(piece) : "");
    }

    private void handleClick(int row, int col)
    {
        if (selectedRow == -1)
        {
            selectedRow = row;
            selectedCol = col;
            return;
        }

        Position from = new Position(selectedRow, selectedCol);
        Position to = new Position(row, col);
        
        Piece selectedPiece = game.getBoard().getPiece(from);
         
        boolean moved = game.move(from, to);

        if (moved)
        {
            String sideToTest = game.getCurrentTurn();

            if (game.isCheck(sideToTest)) 
            {
                if (game.isCheckmate(sideToTest)) 
                {
                    String winner = sideToTest.equalsIgnoreCase("white") ? "black" : "white";
                    JOptionPane.showMessageDialog(this, winner + " wins!");
                    System.exit(0);
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, sideToTest + " is in check!");
                }
            }
        }
        
        else
        {
            if (selectedPiece == null)
            {
                JOptionPane.showMessageDialog(this,"No piece at that square.");
            }
            else if (!selectedPiece.getColor().equalsIgnoreCase(game.getCurrentTurn()))
            {
                JOptionPane.showMessageDialog(this,"It's " + game.getCurrentTurn() + "'s turn!");
            }
            else
            {
                JOptionPane.showMessageDialog(this,"That move is not allowed!");
            }

        }
        int fromRow = selectedRow;
        int fromCol = selectedCol;

        selectedRow = -1;
        selectedCol = -1;

        updateSquare(fromRow, fromCol);
        updateSquare(row, col);
    }

    private void refreshBoard()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Position pos = new Position(row, col);
                Piece piece = game.getBoard().getPiece(pos);

                JButton button = squares[row][col];

                if ((row + col) % 2 == 0)
                {
                    button.setBackground(Color.WHITE);
                }
                else
                {
                    button.setBackground(Color.GRAY);
                }

                if (piece != null)
                {
                    button.setText(getUnicodePiece(piece));
                }
                else
                {
                    button.setText("");
                }
            }
        }
    }

    private String getUnicodePiece(Piece piece)
    {
        String color = piece.getColor();
        String symbol = piece.getSymbol().toLowerCase();

        switch (symbol)
        {
            case "wp": return "\u2659"; // White Pawn
            case "wr": return "\u2656"; // White Rook
            case "wn": return "\u2658"; // White Knight
            case "wb": return "\u2657"; // White Bishop
            case "wq": return "\u2655"; // White Queen
            case "wk": return "\u2654"; // White King
            case "bp": return "\u265F"; // Black Pawn
            case "br": return "\u265C"; // Black Rook
            case "bn": return "\u265E"; // Black Knight
            case "bb": return "\u265D"; // Black Bishop
            case "bq": return "\u265B"; // Black Queen
            case "bk": return "\u265A"; // Black King
        }
        return "";
    }
}