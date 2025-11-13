package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class window_of_game {
    // Declare a JFrame for the main game window
    private JFrame J_frame;

    // Constructor: takes a game_panel object and adds it to the window
    public window_of_game(game_panel Game_panel) {
        // Create the window
        J_frame = new JFrame();
        J_frame.add(Game_panel);
        // Prevent window resizing (to keep game layout consistent)
        J_frame.setResizable(false);
        // Adjust window size based on panel's preferred size
        J_frame.pack();
        // Center window on screen
        J_frame.setLocationRelativeTo(null);
        // Make window visible
        J_frame.setVisible(true);
        // Exit application when the window is closed
        J_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Allow panel to receive keyboard input
        Game_panel.setFocusable(true);
        Game_panel.requestFocusInWindow();

        J_frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                Game_panel.getGame1().windowFocusLost();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });
    }
}
