/*
 * TCSS 305
 * Tetris B
 * Spring 2016
 */
package main;


import gui.GUIFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




/**
 * PowerPaint Driver.
 * 
 * @author Bryce Anderson
 * @version 05/02/16
 *
 */
public final class Main {
    
    /**
     * Empty constructor so there can be no children.
     */
    private Main() {
     
    }

    /**
     * The main method to start the GUI.
     * @param theArgs ignored.
     */
    public static void main(final String[] theArgs) {

        try {

            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.

        final GUIFrame runFrame = new GUIFrame();
        runFrame.start();

    }
}
