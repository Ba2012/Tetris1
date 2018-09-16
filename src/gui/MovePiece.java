/*
 * TCSS 305
 * Tetris B
 * Spring 2016
 */
package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Board;

/**
 * The KeyListener for Tetris.
 * @author Bryce Anderson
 * @version 05/06/16
 *
 */
public class MovePiece implements KeyListener {

    /**
     * The game board. 
     */
    private final Board myBoard;
    
    /**
     * Knows when the game is paused.
     */
    private boolean myPauseGame;
    
    /**
     * Knows when the game is over.
     */
    private boolean myGameOver;
    
    /**
     * The timer that updates the game.
     */
    private final Timer myGameTimer;
    
    /**
     * The frame that holds all the components.
     */
    private final JFrame myFrame;

    /**
     * Constructor for the keyListener.
     * @param theBoard myGameBoard
     * @param thePause if the game is paused.
     * @param theGame if the game is over.
     * @param theTimer the game timer.
     * @param theFrame myFrame.
     */
    public MovePiece(final Board theBoard, final boolean thePause, final boolean theGame,
                     final Timer theTimer, final JFrame theFrame) {

        super();
        myBoard = theBoard;
        myPauseGame = thePause;
        myGameOver = theGame;
        myGameTimer = theTimer;
        myFrame = theFrame;

    }

    @Override
    public void keyPressed(final KeyEvent theEvent) {



        if (myGameOver) {
            if (theEvent.getKeyCode() == KeyEvent.VK_LEFT) {

                myBoard.left();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_RIGHT) {

                myBoard.right();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_UP) {

                myBoard.rotateCW();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_DOWN) {

                myBoard.down();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_SPACE) {

                myBoard.drop();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_SHIFT) {

                myBoard.rotateCCW();
            }
        }
        
        if (theEvent.getKeyCode() == KeyEvent.VK_P) {

            if (myPauseGame) {
                myPauseGame = false;
                JOptionPane.showMessageDialog(myFrame, "Game resumed");
                myGameTimer.start();

            } else {

                myPauseGame = true;
                myGameTimer.stop();
                JOptionPane.showMessageDialog(myFrame, "Game paused");


            }
        }

    }

    @Override
    public void keyReleased(final KeyEvent theArgs) {
    }

    @Override
    public void keyTyped(final KeyEvent theArgs) {

    }

}
