package main;

import game.Game;
import gui.ChessGUI;

public class Main 
{
    public static void main(String[] args) 
    {
        Game game = new Game();

        // Start the GUI and the game
        new ChessGUI(game);
    }
}