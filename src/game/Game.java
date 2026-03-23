package game;

import java.util.Scanner;

import board.Board;
import utils.ChessUtils;
import utils.Position;

/**
 * Controls the flow of the chess game.
 */
public class Game {

    private final Board board;
    private String currentTurn;
    private final Scanner scanner;

    /**
     * Constructs a new Game object.
     */
    public Game() {
        this.board = new Board();
        this.currentTurn = "white";
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game by initializing the board and entering the main play loop.
     */
    public void start() {
        board.initializeBoard();
        play();
    }

    /**
     * Main loop for the Phase 1 version of the game.
     * Displays the board, asks for input, validates format,
     * and performs very basic movement on the board.
     */
    public void play() {
        boolean running = true;

        while (running) {
            board.display();
            System.out.println();
            System.out.println("Current turn: " + currentTurn);
            System.out.print("Enter move (example: E2 E4) or type EXIT to quit: ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("EXIT")) {
                running = false;
                end();
                continue;
            }

            if (!ChessUtils.isValidMoveFormat(input)) {
                System.out.println("Invalid format. Please use format like: E2 E4");
                System.out.println();
                continue;
            }

            String[] parts = input.toUpperCase().split("\\s+");
            Position from = ChessUtils.parsePosition(parts[0]);
            Position to = ChessUtils.parsePosition(parts[1]);

            if (from == null || to == null) {
                System.out.println("Invalid board position.");
                System.out.println();
                continue;
            }

            if (board.getPiece(from) == null) {
                System.out.println("There is no piece at " + parts[0] + ".");
                System.out.println();
                continue;
            }

            if (!board.getPiece(from).getColor().equalsIgnoreCase(currentTurn)) {
                System.out.println("That piece does not belong to " + currentTurn + ".");
                System.out.println();
                continue;
            }

            if (board.movePiece(from, to)) {
                switchTurn();
            } else {
                System.out.println("Move could not be completed.");
                System.out.println();
            }
        }
    }

    /**
     * Ends the game.
     */
    public void end() {
        System.out.println("Game ended.");
    }

    /**
     * Switches the current turn between white and black.
     */
    private void switchTurn() {
        if (currentTurn.equals("white")) {
            currentTurn = "black";
        } else {
            currentTurn = "white";
        }
    }
}