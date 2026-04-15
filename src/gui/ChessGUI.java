package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game.Game;
import pieces.Piece;
import utils.Position;

public class ChessGUI extends JFrame 
{
    private final Game game;
    private final JButton[][] squares = new JButton[8][8];
    private final JPanel boardPanel = new JPanel(new GridLayout(8, 8));
    private final JTextArea historyArea = new JTextArea();
    private final JLabel capturedLabel = new JLabel("Captured: ");
    private final JLabel turnLabel = new JLabel();

    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessGUI(Game game)
    {
        this.game = game;

        setTitle("Chess Game");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createMenuBar();
        initializeSquares();
        add(boardPanel, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);

        refreshBoard();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            game.resetGame();
            selectedRow = -1;
            selectedCol = -1;
            refreshBoard();
        });

        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener(e -> saveGame());

        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener(e -> loadGame());

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createSidePanel()
    {
        JPanel sidePanel = new JPanel(new BorderLayout(10, 10));
        sidePanel.setPreferredSize(new Dimension(250, 600));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JPanel topPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            if (game.undoLastMove())
            {
                selectedRow = -1;
                selectedCol = -1;
                refreshBoard();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "No moves to undo.");
            }
        });

        topPanel.add(turnLabel);
        topPanel.add(capturedLabel);
        topPanel.add(undoButton);

        sidePanel.add(topPanel, BorderLayout.NORTH);
        sidePanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        return sidePanel;
    }

    private void initializeSquares() 
    {
        for (int row = 0; row < 8; row++) 
        {
            for (int col = 0; col < 8; col++) 
            {
                JButton square = new JButton();
                square.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 32));
                square.setFocusPainted(false);
                square.setOpaque(true);
                square.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                int r = row;
                int c = col;
                square.addActionListener(e -> handleClick(r, c));

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }   
    }

    private void handleClick(int row, int col)
    {
        if (selectedRow == -1)
        {
            selectedRow = row;
            selectedCol = col;
            updateSelectionHighlight();
            return;
        }

        Position from = new Position(selectedRow, selectedCol);
        Position to = new Position(row, col);
        Piece selectedPiece = game.getBoard().getPiece(from);

        boolean moved = game.move(from, to);

        if (moved)
        {
            String winner = game.getWinnerIfAny();
            if (winner != null)
            {
                refreshBoard();
                JOptionPane.showMessageDialog(this, winner + " wins! The king was captured.");
                return;
            }

            String sideToTest = game.getCurrentTurn();
            if (game.isCheck(sideToTest)) 
            {
                if (game.isCheckmate(sideToTest)) 
                {
                    String actualWinner = sideToTest.equalsIgnoreCase("white") ? "black" : "white";
                    refreshBoard();
                    JOptionPane.showMessageDialog(this, actualWinner + " wins by checkmate!");
                    return;
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
                JOptionPane.showMessageDialog(this, "No piece at that square.");
            }
            else if (!selectedPiece.getColor().equalsIgnoreCase(game.getCurrentTurn()))
            {
                JOptionPane.showMessageDialog(this, "It's " + game.getCurrentTurn() + "'s turn!");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "That move is not allowed!");
            }
        }

        selectedRow = -1;
        selectedCol = -1;
        refreshBoard();
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
                button.setBackground(getSquareColor(row, col));
                button.setText(piece != null ? getUnicodePiece(piece) : "");
            }
        }

        updateSelectionHighlight();
        turnLabel.setText("Current turn: " + game.getCurrentTurn());
        capturedLabel.setText("Captured: " + game.getCapturedPiecesText());

        List<String> moves = game.getMoveHistory();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < moves.size(); i++)
        {
            builder.append(i + 1).append(". ").append(moves.get(i)).append('\n');
        }
        historyArea.setText(builder.toString());
    }

    private void updateSelectionHighlight()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                if (row == selectedRow && col == selectedCol)
                {
                    squares[row][col].setBackground(Color.YELLOW);
                }
                else
                {
                    squares[row][col].setBackground(getSquareColor(row, col));
                }
            }
        }
    }

    private Color getSquareColor(int row, int col)
    {
        return (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY;
    }

    private void saveGame()
    {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = chooser.getSelectedFile();
            try
            {
                game.saveGame(Path.of(selectedFile.getAbsolutePath()));
                JOptionPane.showMessageDialog(this, "Game saved successfully.");
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "Unable to save game: " + ex.getMessage());
            }
        }
    }

    private void loadGame()
    {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = chooser.getSelectedFile();
            try
            {
                game.loadGame(Path.of(selectedFile.getAbsolutePath()));
                selectedRow = -1;
                selectedCol = -1;
                refreshBoard();
                JOptionPane.showMessageDialog(this, "Game loaded successfully.");
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "Unable to load game: " + ex.getMessage());
            }
        }
    }

    private String getUnicodePiece(Piece piece)
    {
        String symbol = piece.getSymbol().toLowerCase();

        switch (symbol)
        {
            case "wp": return "\u2659";
            case "wr": return "\u2656";
            case "wn": return "\u2658";
            case "wb": return "\u2657";
            case "wq": return "\u2655";
            case "wk": return "\u2654";
            case "bp": return "\u265F";
            case "br": return "\u265C";
            case "bn": return "\u265E";
            case "bb": return "\u265D";
            case "bq": return "\u265B";
            case "bk": return "\u265A";
            default: return "";
        }
    }
}
