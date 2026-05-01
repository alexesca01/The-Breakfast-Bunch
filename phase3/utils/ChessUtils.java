package utils;

/**
 * Utility methods for chess input and coordinate conversion.
 */
public final class ChessUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ChessUtils() {
    }

    /**
     * Checks whether the user's move input is in the required format.
     *
     * @param input the user input
     * @return true if valid, false otherwise
     */
    public static boolean isValidMoveFormat(String input) {
        return input != null && input.toUpperCase().matches("^[A-H][1-8]\\s+[A-H][1-8]$");
    }

    /**
     * Converts a board coordinate like E2 into a Position object.
     *
     * @param chessCoordinate the chess coordinate
     * @return the matching Position, or null if invalid
     */
    public static Position parsePosition(String chessCoordinate) {
        if (chessCoordinate == null || chessCoordinate.length() != 2) {
            return null;
        }

        chessCoordinate = chessCoordinate.toUpperCase();

        char file = chessCoordinate.charAt(0);
        char rank = chessCoordinate.charAt(1);

        if (file < 'A' || file > 'H' || rank < '1' || rank > '8') {
            return null;
        }

        int column = file - 'A';
        int row = 8 - Character.getNumericValue(rank);

        return new Position(row, column);
    }
}