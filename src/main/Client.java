package main;

import javax.swing.SwingUtilities;
import client.ChatGUI;

/**
 * GUI chat client runner.
 */
public class Client {
    /**
     * Creates an instance of the GUI
     */
    public static void main(String[] args) {        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatGUI main = new ChatGUI();
                main.setVisible(true);
            }
        });
    }
}
