package Inotia;

import javax.swing.*;

public class Main 
{
    public static void main(String[] args) 
    {
        // Create the game window
        JFrame window = new JFrame("Inotia");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        window.setUndecorated(true);

        // Screen Manager to switch between screens
        ScreenManager screenManager = new ScreenManager(window);
        screenManager.showMenu(); // Start with the main menu

        window.setVisible(true);
    }
}
