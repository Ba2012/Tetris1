/*
 * TCSS 305
 * Tetris B
 * Spring 2016
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import model.Board;

import model.Point;
import model.TetrisPiece;

/**
 * Makes the frame for the GUI.
 * 
 * @author Bryce Anderson
 * @version 05/20/16
 *
 */
public class GUIFrame implements Observer {

    /**
     * How long of delay for the timer before it starts.
     */
    private static final int MOVE_DELAY = 1000;

    /**
     * The size of the right panel.
     */
    private static final Dimension RIGHT_SIDE = new Dimension(160, 110);

    /**
     * The size of the score panel.
     */
    private static final Dimension SCORE_SIZE = new Dimension(120, 240);

    /**
     * The full frame size.
     */
    private static final Dimension FRAME_SIZE = new Dimension(500, 710);

    /**
     * The size of the game panel.
     */
    private static final Dimension GAME_SIZE = new Dimension(330, 450);
    /**
     * Size of the blocks.
     */
    private static final int BLOCK_SIZE = 30;

    /**
     * X size.
     */
    private static final int MY_WINDOW_X = 100;

    /**
     * Y size.
     */
    private static final int MY_WINDOW_Y = 100;
    /**
     * Size of the next piece window.
     */
    private static final Dimension WINDOW_SIZE = new Dimension(MY_WINDOW_X, MY_WINDOW_Y);
    /**
     * The Points of the next piece.
     */
    private Point[] myNextPiecePoints;

    /**
     * The playing board.
     */
    private final Board myBoard;

    /**
     * The tertis piece to use next.
     */
    private TetrisPiece myPiece;

    /**
     * the menu bar.
     */
    private final JMenuBar myMenuBar;

    /**
     * The string board observer.
     */

    private final MovePiece myKeyListener;

    /**
     * The score of the current game.
     */
    private int myScore;

    /**
     * The timer to keep the game moving.
     */
    private final Timer myMoveTimer;

    /**
     * The delay for the timer as levels increase.
     */
    private int myNewDelay = MOVE_DELAY;

    /**
     * An array to hold the string version of the board. 
     */
    private String[] myStringBoard;

    /**
     * Draws a grid for the game.
     */
    private boolean myGrid;

    /**
     * Adds crazy colors.
     */
    private boolean myCrazy;

    /**
     * Knows when the game is paused.
     */
    private boolean myPauseGame;

    /**
     * Knows when the game is over.
     */
    private boolean myGameOver;

    /**
     * The frame to hold all the components.
     */
    private final JFrame myFrame;

    /**
     * The text area to display the score.
     */
    private final JTextArea myDisplayScore = new JTextArea();
    
    /**
     * The starting level.
     */
    private int myLevel = 1;

    /**
     * Constructor for the GUIFrame.
     */
    public GUIFrame() {
        super();
        myFrame = new JFrame("Tetris");
        myMenuBar = new JMenuBar();
        myBoard = new Board();
        myStringBoard = new String[myBoard.getHeight()];
        myGameOver = true;
        myMoveTimer = new Timer(MOVE_DELAY, new MoveListener());

        buildFileMenu(myFrame);
        myFrame.setJMenuBar(myMenuBar);

        myKeyListener = new MovePiece(myBoard, myPauseGame, myGameOver, myMoveTimer, myFrame);
    }

    /**
     * Starts the GUI.
     */
    public void start() {

        myFrame.setPreferredSize(FRAME_SIZE);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.add(gamePanel(), BorderLayout.CENTER);
        myFrame.addKeyListener(myKeyListener);
        myFrame.add(rightSide(), BorderLayout.WEST);

        startGame();
        myFrame.pack();
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        myFrame.requestFocus();
    }

    /**
     * Builds the File Menu. The JFrame is being passed so when quit is called
     * it can close the frame.
     * 
     * @param theFrame the JFrame being used.
     */
    private void buildFileMenu(final JFrame theFrame) {
        final JMenuItem endGame = new JMenuItem("End Game");
        final JMenuItem newGame = new JMenuItem("New Game");
        final JCheckBox grid = new JCheckBox("Grid");
        final JCheckBox crazy = new JCheckBox("Colors");

        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myGrid) {
                    myGrid = false;
                } else {
                    myGrid = true;
                }
            }
        });

        crazy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myCrazy) {
                    myCrazy = false;
                }  else {
                    myCrazy = true;
                }
            }
        });

        endGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        myMenuBar.add(fileMenu);

        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGame.setEnabled(true);
        if (myGameOver) {

            newGame.setEnabled(true);
            endGame.setEnabled(false);

        }
        if (!myGameOver) {
            newGame.setEnabled(false);
            endGame.setEnabled(true);
            myGameOver = true;
        }
        newGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {

                reset();
                startGame();
                myGameOver = false;
                endGame.setEnabled(true);
                newGame.setEnabled(false);

            }

        });
        fileMenu.add(newGame);
        fileMenu.addSeparator();

        endGame.setEnabled(false);
        endGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {

                myGameOver = true;
                endGame.setEnabled(false);
                newGame.setEnabled(true);

                update(myBoard, true);

            }

        });

        fileMenu.add(endGame);
        final JMenuItem quit = new JMenuItem("Quit", KeyEvent.VK_Q);
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theFrame.dispose();
            }

        });

        fileMenu.add(quit);
        fileMenu.addSeparator();
        fileMenu.add(grid);
        fileMenu.addSeparator();
        fileMenu.add(crazy);
    }

    /**
     * Builds the next piece panel.
     * @return JPanel
     */
    private JPanel nextPiecePanel() {

        final JPanel nextPanel = new JPanel();
        final TitledBorder outSide = new TitledBorder("");
        nextPanel.setBorder(outSide);
        nextPanel.setBackground(Color.BLACK);
        myFrame.setBackground(Color.BLACK);

        final JPanel inside = new JPanel();
        inside.setPreferredSize(WINDOW_SIZE);
        inside.setBackground(Color.BLACK);
        final TitledBorder nextPiece = new TitledBorder("The next piece");
        inside.setBorder(nextPiece);
        inside.setOpaque(false);

        nextPiece.setTitleColor(Color.RED);
        nextPiece.setTitlePosition(TitledBorder.ABOVE_TOP);
        inside.setBorder(nextPiece);
        nextPanel.add(inside);
        nextPanel.setOpaque(false);
        // nextPanel.se

        return nextPanel;
    }

    /**
     * Builds the whole right side of the GUI.
     * @return JPanel
     */
    private JPanel rightSide() {

        final JPanel rightSide = new JPanel();

        rightSide.setPreferredSize(RIGHT_SIDE);
        rightSide.setLayout(new BorderLayout());
        rightSide.add(directions(), BorderLayout.SOUTH);
        rightSide.add(nextPiecePanel(), BorderLayout.NORTH);
        rightSide.add(scoreBoard(), BorderLayout.CENTER);
        rightSide.setBackground(Color.BLACK);
        rightSide.setOpaque(true);

        return rightSide;
    }

    /**
     * Builds the score board.
     * @return JPanel
     */
    private JPanel scoreBoard() {
        final JPanel scoreBoard = new JPanel();
        scoreBoard.setPreferredSize(SCORE_SIZE);
        final TitledBorder score = new TitledBorder("Score");
        score.setTitlePosition(TitledBorder.ABOVE_TOP);
        score.setTitleFont(Font.getFont(Font.MONOSPACED));
        score.setTitleColor(Color.RED);
        scoreBoard.setBorder(score);


        myDisplayScore.setText(" Score: " + myScore + " \nLines before next level: 5 \n"
                               + " Current Level : " + myLevel + "\n \n  \n"
                                               + " Total lines cleared: " + myScore);
        myDisplayScore.setBackground(Color.BLACK);
        myDisplayScore.setFont(Font.getFont(Font.SANS_SERIF));
        myDisplayScore.setForeground(Color.RED);
        scoreBoard.add(myDisplayScore, BorderLayout.CENTER);
        scoreBoard.setBackground(Color.BLACK);

        return scoreBoard;
    }

    /**
     * Builds the JPanel for the directions.
     * @return JPanel
     */
    private JPanel directions() {

        final JPanel directionPanel = new JPanel();

        directionPanel.setPreferredSize(SCORE_SIZE);
        directionPanel.setBackground(Color.BLACK);
        final TitledBorder directionTitle = new TitledBorder("Directions");
        directionTitle.setTitlePosition(TitledBorder.ABOVE_TOP);
        directionTitle.setTitleFont(Font.getFont(Font.MONOSPACED));
        directionTitle.setTitleColor(Color.RED);
        directionPanel.setBorder(directionTitle);
        final JTextArea directions = new JTextArea();
        directions.setText(directionText());
        directions.setBackground(Color.BLACK);
        directions.setFont(Font.getFont(Font.SANS_SERIF));
        directions.setForeground(Color.RED);
        directionPanel.add(directions, BorderLayout.CENTER);
        directions.setEditable(false);
        return directionPanel;
    }

    /**
     * Builds the string for the directions.
     * @return String directions
     */
    private String directionText() {
        final StringBuilder builder = new StringBuilder(256);
        builder.append("Left arrow: left\n" + "Right arrow: right\n" + "Down arrow: down one\n"
                       + "Space bar: quick drop\nShift: rotate CCW\n"
                       + "Up arrrow: rotate CW\nPause: P\n"
                       + "CTR E: End game\nCTR N: New game\n\n"
                       + "          SCORING:\nEach line clear = 1 point");
        return builder.toString();
    }

    /**
     * Starts the game.
     */
    private void startGame() {

        myBoard.addObserver(this);
        myBoard.newGame();

    }

    /**
     * Creates the game panel.
     * @return a Panel to draw the game in.
     */
    private JPanel gamePanel() {
        final GamePanel gamePanel = new GamePanel();

        gamePanel.setPreferredSize(GAME_SIZE);
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        gamePanel.requestFocus();
        return gamePanel;

    }

    /**
     * Draws the String representation of the board.
     * @param theGraphics the 2D graphics
     */
    private void drawMap(final Graphics2D theGraphics) {

        final int startY = 5;
        int topY = 0;
        for (int y = startY; y < myStringBoard.length; y++) {

            int leftX = 0;
            for (int x = 1; x < myStringBoard[x].length(); x++) {
                /*
                 * draws lines around the blocks. may use later.
                 */

                if (myGrid) {
                    theGraphics.setPaint(Color.GREEN);
                    theGraphics.draw3DRect(leftX, topY, BLOCK_SIZE, BLOCK_SIZE, true);
                }

                final char letter = myStringBoard[y].charAt(x);

                switch (letter) {
                    case 'J':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.CYAN);

                        }
                        theGraphics.fill3DRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE, true);

                        break;

                    case 'L':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.ORANGE);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;

                    case 'T':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.YELLOW);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;

                    case 'S':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.PINK);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;

                    case '0':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.DARK_GRAY);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;

                    case 'I':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.GREEN);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;

                    case 'Z':
                        theGraphics.setPaint(Color.WHITE);
                        if (myCrazy) {
                            theGraphics.setPaint(Color.MAGENTA);
                        }
                        theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);

                        break;
                    case ' ':
                        if (myCrazy) {
                            theGraphics.setPaint(Color.RED);
                            theGraphics.fillRect(leftX, topY + 1, BLOCK_SIZE, BLOCK_SIZE);
                        }
                        break;

                    default:
                        break;

                }
                leftX += BLOCK_SIZE + 2;
            }
            topY += BLOCK_SIZE + 2;
        }

    }

    @Override
    public void update(final Observable theObservable, final Object theObject) {
        
        if (theObject instanceof String) {
            myStringBoard = theObject.toString().split("\n");
            myFrame.requestFocus();
        }

        if (theObject instanceof TetrisPiece) {

            myPiece = (TetrisPiece) theObject;
            myNextPiecePoints = myPiece.getPoints();
            myFrame.requestFocus();

        }

        if (theObject instanceof Boolean) {
            myGameOver = (boolean) theObject;
            myBoard.deleteObserver(this);
            JOptionPane.showMessageDialog(myFrame, "GAME OVER");

            myGameOver = false;
            reset();


        }

        if (theObject instanceof Integer[]) {

            calcScore((Integer[]) theObject);
            final int nextLevel = 5;


            calcLevel(nextLevel);

            myDisplayScore.setText("Score: " + myScore + "\nLines before next level: 5\n"
                            + "Current Level : " + myLevel + "\n\n\n"
                                            + "Total lines cleared: " + myScore);

        }


    }

    /**
     * Calculates if the user reached the next level.
     * If five lines have been cleared then the game timer delay 
     * decreases by 100 milliseconds.
     * @param theNextLevel  how many lines to clear before the next level.
     */
    private void calcLevel(final int theNextLevel) {


        if (myScore % theNextLevel == 0) {
            // I am ignoring this check style warning because this is the only
            // time
            // I will use 100.
            // 100 is the number of milliseconds being subtracted from the
            // timer's delay
            myNewDelay = myNewDelay - 100;
            myLevel += 1;
            myMoveTimer.setDelay(myNewDelay);
            myMoveTimer.restart();

        }

    }

    /**
     * Calculates the score of the game.
     * This comes from the Board and gives me the number of lines cleared. 
     * @param theObject the number of lines cleared.
     */
    private void calcScore(final Integer[] theObject) {
        myScore += theObject.length;
    }

    /**
     * Resets the board so a new game can be played.
     */
    private void reset() {

        myBoard.deleteObserver(this);


        myMoveTimer.restart();
        myFrame.requestFocus();

    }

    /**
     * A class that listens for timer events and moves the shape, checking for
     * the window boundaries and changing direction as appropriate.
     */
    private class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();
        }
    }

    /*
     * This is intended to draw the next piece but I could
     * not get it to draw in the right panel.
     */                    
//    private void drawCurrent(final Graphics theGraphics) {
//        final Graphics2D g2d = (Graphics2D) theGraphics;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                             RenderingHints.VALUE_ANTIALIAS_ON);
//
//        if (myPiece != null && myNextPiecePoints != null) {
//            for (final Point p : myNextPiecePoints) {
//
//                final Point startPoint = new Point(0, 0);
//                final int x = p.x() + startPoint.x();
//                final int y = p.y() + startPoint.y();
//                g2d.setPaint(Color.BLUE);
//
//                g2d.fill3DRect((int) x, (int) y, BLOCK_SIZE / 2, BLOCK_SIZE / 2, false);
//
//            }
//
//        }
//
//    }

    /**
     * The JPanel for my paintComponent.
     * 
     * @author Bryce Anderson
     * @version 06/05/16
     *
     */
    private class GamePanel extends JPanel {

        /**
         * 
         */
        private static final long serialVersionUID = -1633631687731572663L;

        /**
         * Paints the next piece that will fall onto the board.
         * 
         * @param theGraphics what will be drawn.
         */
        @Override
        public void paintComponent(final Graphics theGraphics) {
            super.paintComponent(theGraphics);
            final Graphics2D g2d = (Graphics2D) theGraphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            drawMap(g2d);
            // drawCurrent(g2d);
            repaint();

        }
    }

}
